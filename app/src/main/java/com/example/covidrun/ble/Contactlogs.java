package com.example.covidrun.ble;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;

import com.example.covidrun.BuildConfig;
import com.example.covidrun.R;
import com.example.covidrun.sql.DBHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.heraldprox.herald.sensor.Sensor;
import io.heraldprox.herald.sensor.SensorDelegate;
import io.heraldprox.herald.sensor.analysis.SocialDistance;
import io.heraldprox.herald.sensor.datatype.ImmediateSendData;
import io.heraldprox.herald.sensor.datatype.Locations;
import io.heraldprox.herald.sensor.datatype.PayloadData;
import io.heraldprox.herald.sensor.datatype.Proximity;
import io.heraldprox.herald.sensor.datatype.SensorState;
import io.heraldprox.herald.sensor.datatype.SensorType;
import io.heraldprox.herald.sensor.datatype.TargetIdentifier;
import io.heraldprox.herald.sensor.datatype.TimeInterval;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Contactlogs extends Fragment implements SensorDelegate, OnMapReadyCallback {
    private ImageButton btn_share;
    private DBHandler dbHandler;
    private View view;
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat dateFormatterTime = new SimpleDateFormat("HH:mm:ss");
    private SupportMapFragment mapFrag;
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;


    private final static String tag = Contactlogs.class.getName();

    /// Test UI specific data, not required for production solution.
    //private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    private boolean foreground = false;

    // MARK:- Events
    private long didDetect = 0, didRead = 0, didMeasure = 0, didShare = 0, didReceive = 0;

    // MARK:- Detected payloads
    private final Map<TargetIdentifier, PayloadData> targetIdentifiers = new ConcurrentHashMap<>();
    private final Map<PayloadData,Target> payloads = new ConcurrentHashMap<>();
    private final List<Target> targets = new ArrayList<>();

    // MARK:- Social mixing
    private final SocialDistance socialMixingScore = new SocialDistance();
    private TimeInterval socialMixingScoreUnit = new TimeInterval(60);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate((R.layout.contactlogs), container, false);



        // Set the map ready callback to receive the GoogleMap object
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFrag = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFrag.getMapAsync(this);
        dbHandler = new DBHandler(getActivity());

        //share button event
        btn_share =(ImageButton)view.findViewById(R.id.btn_sharelogs);
        btn_share.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {


                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "Android/media/com.example.covidrun.sensor/Sensor/detection.csv");

                Uri path = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID+".provider",file);

                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    fileIntent.setType("text/*");
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    Toast.makeText(getActivity()," "+path,Toast.LENGTH_LONG).show();
                    startActivity(Intent.createChooser(fileIntent, "Send"));


            }
        });

        // Test UI specific process to gather data from sensor for presentation
        final Sensor sensor = AppDelegate.getAppDelegate().sensor();
        sensor.add(this);
        sensor.add(socialMixingScore);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private synchronized void updateTargets() {
        // De-duplicate targets based on short name and last updated at time stamp
        final Map<String,Target> shortNames = new HashMap<>(payloads.size());
        for (Map.Entry<PayloadData,Target> entry : payloads.entrySet()) {
            final String shortName = entry.getKey().shortName();
            final Target target = entry.getValue();
            final Target duplicate = shortNames.get(shortName);
            if(dbHandler.comparePayload(target.payloadData().shortName()) == false) {
                dbHandler.addNewPayload(target.payloadData().shortName(), dateFormatterTime.format(target.didRead()),
                        dateFormatter.format(target.lastUpdatedAt()));
            }
            if (duplicate == null || duplicate.lastUpdatedAt().getTime() < target.lastUpdatedAt().getTime()) {
                shortNames.put(shortName, target);
                dbHandler.updatePayload(target.payloadData().shortName(), target.payloadData().shortName(),
                        dateFormatterTime.format(target.didRead()), dateFormatter.format((target.lastUpdatedAt())));
            }

        }
        // Get target list in alphabetical order
        final List<Target> targetList = new ArrayList<>(shortNames.values());
        Collections.sort(targetList, new Comparator<Target>() {
            @Override
            public int compare(Target t0, Target t1) {
                return t0.payloadData().shortName().compareTo(t1.payloadData().shortName());
            }
        });
        // Update UI
        ((TextView) view.findViewById(R.id.detection)).setText("DETECTION (" + targetList.size() + ")");
    }

    // Update social distance score
    private void updateSocialDistance(TimeInterval unit) {
        final long millisecondsPerUnit = unit.value * 1000;

        final long epoch = (new Date().getTime() / millisecondsPerUnit) - 11;
        for (int i=0; i<=11; i++) {
            // Compute score for time slot
            final Date start = new Date((epoch + i) * millisecondsPerUnit);
            final Date end = new Date((epoch + i + 1) * millisecondsPerUnit);
            final double score = socialMixingScore.scoreByProximity(start, end, -25, -70);


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        foreground = true;
        Log.d(tag, "app (state=foreground)");
        updateTargets();

    }

    @Override
    public void onPause() {
        foreground = false;
        Log.d(tag, "app (state=background)");
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void sensor(SensorType sensor, TargetIdentifier didDetect) {
        this.didDetect++;
        if (foreground) {
            final String text = Long.toString(this.didDetect);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, PayloadData didRead, TargetIdentifier fromTarget) {
        this.didRead++;
        targetIdentifiers.put(fromTarget, didRead);
        Target target = payloads.get(didRead);
        if (target != null) {
            target.didRead(new Date());
        } else {
            payloads.put(didRead, new Target(fromTarget, didRead));
        }
        if (foreground) {
            final String text = Long.toString(this.didRead);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, List<PayloadData> didShare, TargetIdentifier fromTarget) {

        final Date now = new Date();
        for (PayloadData didRead : didShare) {
            targetIdentifiers.put(fromTarget, didRead);
            Target target = payloads.get(didRead);
            if (target != null) {
                target.didRead(new Date());
            } else {
                payloads.put(didRead, new Target(fromTarget, didRead));
            }
        }
        if (foreground) {
            final String text = Long.toString(this.didShare);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, Proximity didMeasure, TargetIdentifier fromTarget) {
        this.didMeasure++;
        final PayloadData didRead = targetIdentifiers.get(fromTarget);
        if (didRead != null) {
            final Target target = payloads.get(didRead);
            if (target != null) {
                target.targetIdentifier(fromTarget);
                target.proximity(didMeasure);
            }
        }
        if (foreground) {
            final String text = Long.toString(this.didMeasure);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                    updateSocialDistance(socialMixingScoreUnit);
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, ImmediateSendData didReceive, TargetIdentifier fromTarget) {
        this.didReceive++;
        final PayloadData didRead = new PayloadData(didReceive.data.value);
        if (didRead != null) {
            final Target target = payloads.get(didRead);
            if (target != null) {
                targetIdentifiers.put(fromTarget, didRead);
                target.targetIdentifier(fromTarget);
                target.received(didReceive);
            }
        }
        if (foreground) {
            final String text = Long.toString(this.didReceive);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
    }

    @Override
    public void sensor(SensorType sensor, Locations didVisit) {
        if (foreground) {
            Object loc = didVisit.value;
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            Log.e(null,"Loction Result  "+loc);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    updateTargets();
                }
            });
        }
        // Not used
    }

    @Override
    public void sensor(SensorType sensor, Proximity didMeasure, TargetIdentifier fromTarget, PayloadData withPayload) {
        // High level integration API is not used as the test app is using the low level API to present all the detection events.
    }

    @Override
    public void sensor(SensorType sensor, SensorState didUpdateState) {
        // Sensor state is already presented by the operating system, so not duplicating in the test app.
    }

}

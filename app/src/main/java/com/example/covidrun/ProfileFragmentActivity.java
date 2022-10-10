package com.example.covidrun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.covidrun.ble.Contactlogs;
import com.example.covidrun.ui.cases.CaseFragment;
import com.example.covidrun.ui.faq.FaqFragment;
import com.example.covidrun.ui.profile.ProfileFragment;
import com.example.covidrun.ui.qr.QRFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ProfileFragment()).addToBackStack(null).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_case:
                            selectedFragment = new CaseFragment();
                            break;
                        case R.id.navigation_faq:
                            selectedFragment = new FaqFragment();
                            break;
                        case R.id.navigation_qr:
                            selectedFragment = new QRFragment();
                            break;
                        case R.id.navigation_tracing:
                            selectedFragment = new Contactlogs();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).addToBackStack(null).commit();

                    return true;
                }
            };
}
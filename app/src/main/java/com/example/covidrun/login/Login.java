package com.example.covidrun.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidrun.MainActivity;
import com.example.covidrun.R;
import com.example.covidrun.register.Register;
import com.example.covidrun.sql.DBHandler;

public class Login extends AppCompatActivity {
    EditText phoneNum, password;
    Button btnLogin;
    TextView createAcc;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);
        phoneNum = findViewById(R.id.login_phoneNum);
        password = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_bt);

        dbHandler = new DBHandler(Login.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumCheck = phoneNum.getText().toString();
                String passCheck = password.getText().toString();
                Cursor cursor = dbHandler.getUser();
                if(cursor.getCount() == 0){
                    Toast.makeText(Login.this, "No entries exists", Toast.LENGTH_SHORT).show();
                }
                if(loginCheck(cursor, phoneNumCheck, passCheck)){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtra("phoneNum", phoneNumCheck);
                    phoneNum.setText("");
                    password.setText("");
                    startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setCancelable(true);
                    builder.setTitle("Invalid Login!");
                    builder.setMessage("Wrong Phone Number or Password!");
                    builder.show();
                }
                dbHandler.close();;
            }
        });
        createAcc = findViewById(R.id.register_user);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public static boolean loginCheck(Cursor cursor, String phoneNumCheck, String passCheck){
        while(cursor.moveToNext()){
            if (cursor.getString(3).equals(phoneNumCheck)){
                if(cursor.getString(2).equals(passCheck)){
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}

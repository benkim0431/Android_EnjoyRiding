package com.skh.enjoyriding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.skh.enjoyriding.model.Bike;
import com.skh.enjoyriding.model.Hours;
import com.skh.enjoyriding.model.User;
import com.skh.enjoyriding.persistence.DBHelper;
import com.skh.enjoyriding.utils.Constant;
import com.skh.enjoyriding.utils.CustomDialog;

public class LoginActivity extends AppCompatActivity {

    LinearLayout lnLogin;
    EditText edtUsername, edtPassword;
    CheckBox chkRememberMe;
    Button btnLogin;
    TextView textViewSignUp;
    DBHelper dbh;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean ischecked = sharedPreferences.getBoolean(Constant.REMEMBER_ME, false);
                if (ischecked) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (edtUsername.getText().toString().trim() != "") {
                        editor.putString(Constant.REMEMBER_USER_ID, edtUsername.getText().toString().trim());
                    }
                    editor.commit();
                }
            }
        });

        chkRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constant.REMEMBER_ME, isChecked);
                editor.commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                // Todo test
                if (username.equals("1") && password.equals("1")) {
                    insertBikesOnDB();
                    insertHoursOnDB();
                    launchMapWithBikeActivity();
                    return;
                }

                User user = dbh.getUserByUsername(username);

                if(user == null ){
//                    Snackbar.make(lnLogin, "Username or Password doesn't match.", Snackbar.LENGTH_LONG).show();
                    CustomDialog cd = new CustomDialog("Check userID.", "Login Failed", "OK", null);
                    cd.show(getSupportFragmentManager(), "Custom");
                }else{
                    if(user.getUsername().equals(username) &&
                       user.getPassword().equals(password)){
                        insertBikesOnDB();
                        insertHoursOnDB();
                        Snackbar.make(lnLogin, "Logged in.", Snackbar.LENGTH_LONG).show();

                        // Store the current user
                        SharedPreferences sp = getSharedPreferences(Constant.USER_ID, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(Constant.USER_ID, user.getUserId());
                        editor.putString(Constant.USER_NAME, user.getUsername());
                        editor.commit();

                        launchMapWithBikeActivity();
                    }else{
//                        Snackbar.make(lnLogin, "Username or Password doesn't match.", Snackbar.LENGTH_LONG).show();
                        CustomDialog cd = new CustomDialog("Username or Password doesn't match.", "Login Failed", "OK", null);
                        cd.show(getSupportFragmentManager(), "Custom");
                    }
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init(){
        sharedPreferences = getSharedPreferences(Constant.REMEMBER_ME, Context.MODE_PRIVATE);
        lnLogin = (LinearLayout) findViewById(R.id.lnLogin);
        edtUsername = (EditText) findViewById(R.id.edtLoginUsername);
        edtPassword = (EditText) findViewById(R.id.edtLoginPassword);
        chkRememberMe = (CheckBox) findViewById(R.id.chkLoginRememberMe);
        boolean ischecked = sharedPreferences.getBoolean(Constant.REMEMBER_ME, false);
        if (ischecked) {
            String username = sharedPreferences.getString(Constant.REMEMBER_USER_ID, "");
            if(!"".equals(username)) {
                edtUsername.setText(username);
            }
            chkRememberMe.setChecked(true);
        }
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        //Instantiates the database h
        dbh = new DBHelper(getBaseContext());
    }

    private void insertBikesOnDB(){
        dbh.resetBikeTable();

        Bike bike1 = new Bike(43.4840959617, -80.5178117752, "Alan", "Urban Cycling", R.drawable.alan);
        Bike bike2 = new Bike(43.4738507274, -80.5275535583, "Brompton", "Fixed Gear Bike", R.drawable.brompton);
        Bike bike3 = new Bike(43.4752832963, -80.520215035, "Alex Singer", "Custom Bike", R.drawable.alex);
        Bike bike4 = new Bike(43.4739441569, -80.511460343, "Basso", "Modern type", R.drawable.basso);
        Bike bike5 = new Bike(43.4852480526, -80.52226612091, "Carrera Bicycles", "Italian bicycle", R.drawable.carrera);

        String message = null;

        message = dbh.insertOrUpdateBike(bike1);
        message = dbh.insertOrUpdateBike(bike2);
        message = dbh.insertOrUpdateBike(bike3);
        message = dbh.insertOrUpdateBike(bike4);
        message = dbh.insertOrUpdateBike(bike5);
    }

    private void insertHoursOnDB(){
        dbh.resetHourTable();

        String message = null;

        message = dbh.insertHour(new Hours(0));
        message = dbh.insertHour(new Hours(1));
        message = dbh.insertHour(new Hours(2));
        message = dbh.insertHour(new Hours(3));
        message = dbh.insertHour(new Hours(4));
        message = dbh.insertHour(new Hours(5));
        message = dbh.insertHour(new Hours(6));
        message = dbh.insertHour(new Hours(7));
        message = dbh.insertHour(new Hours(8));
        message = dbh.insertHour(new Hours(9));
        message = dbh.insertHour(new Hours(10));
        message = dbh.insertHour(new Hours(11));
        message = dbh.insertHour(new Hours(12));
        message = dbh.insertHour(new Hours(13));
        message = dbh.insertHour(new Hours(14));
        message = dbh.insertHour(new Hours(15));
        message = dbh.insertHour(new Hours(16));
        message = dbh.insertHour(new Hours(17));
        message = dbh.insertHour(new Hours(18));
        message = dbh.insertHour(new Hours(19));
        message = dbh.insertHour(new Hours(20));
        message = dbh.insertHour(new Hours(21));
        message = dbh.insertHour(new Hours(22));
        message = dbh.insertHour(new Hours(23));
    }

    private void launchMapWithBikeActivity() {
//        Intent intent = new Intent(LoginActivity.this, MapWithBikesActivity.class);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check permission
        boolean permission = checkPermission();
        if (permission == false) {
            Toast.makeText(getApplicationContext(), "Permission denied, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission() {
        boolean permissionGranded = true;
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            permissionGranded = false;
        }
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            permissionGranded = false;
        }
        return permissionGranded;
    }
}
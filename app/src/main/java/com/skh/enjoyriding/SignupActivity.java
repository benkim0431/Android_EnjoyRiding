package com.skh.enjoyriding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.skh.enjoyriding.model.User;
import com.skh.enjoyriding.persistence.DBHelper;
import com.skh.enjoyriding.utils.CustomDialog;

public class SignupActivity extends AppCompatActivity {

    LinearLayout lnSignUp;
    EditText edtUsername, edtPassword, edtConfirmPassword;
    Button btnSignup;
    TextView textViewLogin;

    Intent intent1;
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();

                boolean isValid = validate(username, password, confirmPassword);

                if (isValid) {
                    User user = new User(username, password);
                    String message = dbh.insertOrUpdateUser(user);

                    if (message == null) {
                        Snackbar.make(lnSignUp, "User registered successfully.", Snackbar.LENGTH_LONG).show();
                        //move to the login page
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        CustomDialog cd = new CustomDialog(message, "Sign Up Failed", "OK", null);
                        cd.show(getSupportFragmentManager(), "SignUp");
                    }
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1 = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void init(){
        lnSignUp = (LinearLayout) findViewById(R.id.lnSignUp);
        edtUsername = (EditText) findViewById(R.id.edtSignupUsername);
        edtPassword = (EditText) findViewById(R.id.edtSignupPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtSignupConfirmPassword);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        //Instantiates the database helper
        dbh = new DBHelper(getBaseContext());
    }

    private boolean validate(String username, String password, String confirmPassword){
        if(username.isEmpty() || username == null){
            CustomDialog cd = new CustomDialog("Username is mandatory.", "Sign Up Failed", "OK", null);
            cd.show(getSupportFragmentManager(), "SignUp");
            return false;
        }

        if(password.isEmpty() || password == null){
            CustomDialog cd = new CustomDialog("Password is mandatory.", "Sign Up Failed", "OK", null);
            cd.show(getSupportFragmentManager(), "SignUp");
            return false;
        }

        if(confirmPassword.isEmpty() || confirmPassword == null){
            CustomDialog cd = new CustomDialog("Password confirmation is mandatory.", "Sign Up Failed", "OK", null);
            cd.show(getSupportFragmentManager(), "SignUp");
            return false;
        }

        if(!password.equals(confirmPassword)){
            CustomDialog cd = new CustomDialog("Passwords don't match.", "Sign Up Failed", "OK", null);
            cd.show(getSupportFragmentManager(), "SignUp");
            return false;
        }
        return true;
    }
}
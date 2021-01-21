package com.padunime.animemanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.security.MessageDigest;


public class MainActivity extends AppCompatActivity {
    String userEmail;
    String password;
    private FirebaseAuth mAuth;
    private AdView mAdView;
    DatabaseHelper mDatabaseHelper;


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);

        }

    }

    private void RegisterUser(){
        final String name = userEmail;
        final String pass = password;

        mAuth.createUserWithEmailAndPassword(name, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //store fields in database
                            Users user = new Users(
                                    name,
                                    pass
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        FirebaseUser user = auth.getCurrentUser();
                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.d("Email", "Email sent.");
                                                    Toast.makeText(MainActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {

    }

    public void startDB(){
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.getWritableDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        final Button bLogin = findViewById(R.id.button_login);
        final Button bRegister = findViewById(R.id.button_register);
        final EditText email = findViewById(R.id.edit_text_username);
        final EditText pass = findViewById(R.id.edit_text_password);
        final TextView forgotpass = findViewById(R.id.password_forgot);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
            Intent i = new Intent(MainActivity.this, AnimeViewActivity.class);
            startActivity(i);
            finish();
        }


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        try{
            startDB();
        }catch (Error error){

        }

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = email.getText().toString().trim();
                if (userEmail.isEmpty()) {
                    email.setError("E-mail required");
                    email.requestFocus();
                    return;
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("PassReset", "Email sent.");
                                Toast.makeText(MainActivity.this, "Reset password e-mail sent!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vLogin) {
                userEmail = email.getText().toString().trim();
                password = pass.getText().toString().trim();
                if (userEmail.isEmpty()){
                    email.setError("E-mail required");
                    email.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    pass.setError("Password required");
                    pass.requestFocus();
                    return;
                }
                else{
                    mAuth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Log.d("Login", "signInWithEmail:succes");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Intent i = new Intent(MainActivity.this, AnimeViewActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Log.w("123", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentification failed!", Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }
                    });
                }

            }
        });



        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = email.getText().toString();
                password = pass.getText().toString();
                if (userEmail.isEmpty()){
                    email.setError("E-mail required");
                    email.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    pass.setError("Password required");
                    pass.requestFocus();
                    return;
                }
                RegisterUser();
            }
        });
    }

}

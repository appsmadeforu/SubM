package com.example.sofiy.subm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText pass;
    Button LOGIN, REGISTER;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailid);
        pass = (EditText) findViewById(R.id.password);
        LOGIN = (Button) findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();
        REGISTER= (Button) findViewById(R.id.registerbutton);

        REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        LOGIN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                String p = pass.getText().toString();
                String admin ="sofiadmin@gmail.com";
                String adminpass="12215562";
                if (e.equals(admin) && p.equals(adminpass)) {
                    final ProgressDialog progressDialog = ProgressDialog.show
                            (Login.this, "Please wait...", "Processing...", true);
                    firebaseAuth.signInWithEmailAndPassword(admin, adminpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(Login.this, Home.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else if(e.isEmpty() && p.isEmpty()){

                    Toast.makeText(Login.this,"Invalid Input", Toast.LENGTH_LONG).show();

                }
                else
                    {
                        final ProgressDialog progressDialog = ProgressDialog.show
                                (Login.this, "Please wait...", "Processing...", true);
                        firebaseAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(Login.this, Home.class);
                                    //i.putExtra("email",firebaseAuth.getCurrentUser().getEmail());
                                    startActivity(i);
                                }
                                else
                                    
                                    {
                                    Log.e("ERROR", task.getException().toString());
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                            }

                        });
                    }
        }
    });
    }
}

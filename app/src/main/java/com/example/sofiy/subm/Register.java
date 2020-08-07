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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText username, email, phoneNo, pass;
    FirebaseAuth firebaseAuth;
    Button Register;
    ProgressDialog mProgress;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        phoneNo = (EditText) findViewById(R.id.phoneNo);
        pass = (EditText) findViewById(R.id.pass);
        Register = (Button) findViewById(R.id.Register);

        firebaseAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
    }

    public void Register_click(View v) {
        mProgress = ProgressDialog.show(Register.this, "Please wait...", "Processing...", true);
        String p = pass.getText().toString();
        final String e = email.getText().toString();
        String n = phoneNo.getText().toString();
        String u = username.getText().toString();
        if (p.isEmpty() || e.isEmpty() || n.isEmpty() || u.isEmpty()) {
            mProgress.dismiss();
            Toast.makeText(Register.this, "Enter all details", Toast.LENGTH_LONG).show();

        }
        else
            {
            firebaseAuth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                    if (task.isSuccessful()) {
                        String s = firebaseAuth.getCurrentUser().getUid();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference user_details = mDatabase.child(s);
                        user_details.child("Username").setValue(username.getText().toString());
                        user_details.child("Contact").setValue(phoneNo.getText().toString());
                        user_details.child("Email").setValue(email.getText().toString());

                        mProgress.dismiss();

                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Register.this, Home.class);
                        startActivity(i);
                    } else {
                        Log.e("ERROR", task.getException().toString());
                        Toast.makeText(Register.this, "Error Signing up", Toast.LENGTH_LONG).show();
                        mProgress.dismiss();
                    }

                }
            });

            }
    }

}

package com.example.sofiy.subm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity
{
    private ImageView SubmLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SubmLogo= (ImageView) findViewById(R.id.logo);
        Animation newanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        SubmLogo.startAnimation(newanim);
        final Intent i= new Intent(this, Login.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();


    }

}

package com.example.icalvin.historymapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Nick on 8-3-2017.
 */

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Animation anSplash = AnimationUtils.loadAnimation(getBaseContext(), R.anim.splash_animation);

        iv.startAnimation(anSplash);

        anSplash.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation){

            }
            @Override
            public void onAnimationEnd(Animation animation){
                iv.setImageAlpha(0);
                finish();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);

            }
            @Override
            public void onAnimationRepeat(Animation animation){

            }

        });
    }
}

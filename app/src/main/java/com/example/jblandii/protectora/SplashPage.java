package com.example.jblandii.protectora;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashPage extends AppCompatActivity {

    private ImageView imageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);

        imageLogo = findViewById(R.id.ivLogo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.transition);
        imageLogo.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashPage.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}

package com.example.activity3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.lang.annotation.Annotation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void zoom(View view){
        ImageView image=(ImageView)findViewById(R.id.imageView);
        Animation animation1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        image.startAnimation(animation1);
    }
    public void clockwise(View view){
        ImageView image=(ImageView)findViewById(R.id.imageView);
        Animation animation2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.clockwise);
        image.startAnimation(animation2);
    }
    public void fade(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        image.startAnimation(animation3);
    }
    public void blink(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation4 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        image.startAnimation(animation4);
    }
    public void move(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation5 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        image.startAnimation(animation5);
    }
    public void slide(View view) {
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation6 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
        image.startAnimation(animation6);
    }
}
package com.example.group9_age;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText f1;
    private EditText f2;
    private EditText age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        f1 = (EditText )findViewById(R.id.f1) ;
        f2 = (EditText )findViewById(R.id.f2) ;
        age = (EditText )findViewById(R.id.age) ;
    }

    public void submit(View view) {
        String fname = f1.getText().toString() ;
        String lname = f2.getText().toString() ;
        String fullname = fname + " " + lname;
        int edad = Integer.parseInt(age.getText() .toString() ) ;

        if(edad >=18){
            Toast.makeText(MainActivity.this,fullname +" Can vote !",Toast.LENGTH_LONG  ).show() ;
        }else{
            Toast.makeText(MainActivity.this,fullname +" Can't vote !",Toast.LENGTH_LONG  ).show();
        }

    }
}
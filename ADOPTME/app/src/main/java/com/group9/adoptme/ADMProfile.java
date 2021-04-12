package com.group9.adoptme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class ADMProfile extends AppCompatActivity {
    private TextView fullname,
            email,
            username,
            gender,
            joindate;
    SessionManager sessionManager;
    CircleImageView user_photo;
    private Button button_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_profile);
        sessionManager = new SessionManager( this);

        fullname = findViewById(R.id.profile_fullname);
        email = findViewById(R.id.profile_email);
        username = findViewById(R.id.profile_username);
        gender = findViewById(R.id.profile_gender);
        joindate = findViewById(R.id.profile_join);
        button_update = findViewById(R.id.button_edit);
        user_photo = findViewById(R.id.picture);


    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Go back to Dashboard?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent b= new Intent(ADMProfile.this, AdMDashboard.class );
                startActivity(b);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
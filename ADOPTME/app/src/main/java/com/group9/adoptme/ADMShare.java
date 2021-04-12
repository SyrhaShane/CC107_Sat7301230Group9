package com.group9.adoptme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ADMShare extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = ADMShare.class.getSimpleName();
    private TextView fullname,
            username;
    private CircleImageView user_photo;

    private Bitmap bitmap;
    private ImageView image_meal;
    SessionManager sessionManager;
    private EditText data_mealname,
            data_description;
    Spinner spin_category;
    Button btn_selectmeal, btn_sharemeal;
    private static String URL_ADDMEAL ="https://groupadoptme.000webhostapp.com/AdoptMe/addPet.php";
    String [] meal_categories = {"Morning", "Noon", "Evening"};
    String getID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_share);
        data_mealname = findViewById(R.id.meal_title);
        data_description = findViewById(R.id.meal_description);
        image_meal = findViewById(R.id.meal_photo);
        btn_selectmeal = findViewById(R.id.upload_meal);
        btn_sharemeal = findViewById(R.id.button_share);
        spin_category  = findViewById(R.id.spinner_category);
        spin_category.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, meal_categories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_category.setAdapter(aa);

        sessionManager = new SessionManager( this);
        fullname = findViewById(R.id.label_fullname);
        username = findViewById(R.id.label_username);
        user_photo = findViewById(R.id.s_picture);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mUsername = user.get(sessionManager.USERNAME);
        String mPhoto = user.get(sessionManager.PHOTO);
        getID = user.get(sessionManager.USERID);

        if (mPhoto != null){
            Glide.with(this)
                    .load(mPhoto).error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(user_photo);
        }
        else if(mPhoto == ""){
            String default_photo = "https://groupadoptme.000webhostapp.com/AdoptMe/photo/adm_user.png";
            Glide.with(this)
                    .load(default_photo).error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(user_photo);
        }
        fullname.setText(mName);
        username.setText("@"+mUsername);



        btn_sharemeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ADMShare.this);
                alertDialogBuilder.setTitle("Share your Pet");
                alertDialogBuilder.setMessage("Continue sharing your pet?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AddMeal();
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

        });


        btn_selectmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ADMShare.this)
                        .setMessage("Select Photo")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chooseFile();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pet photo"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image_meal.setImageBitmap(bitmap);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }



    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte []imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodeImage;

    }





    private void AddMeal() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting...");
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final String petName = this.data_mealname.getText().toString();
        final String petCategory = this.spin_category.getSelectedItem().toString();
        final String petProcedure = this.data_description.getText().toString();
        final String userID = getID;
        final String petPhoto = getStringImage(bitmap);
        final String petDate = sdf.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDMEAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(ADMShare.this, "Sharing Successful", Toast.LENGTH_SHORT).show();
                                clearFields();
                                progressDialog.dismiss();
                                Intent adm_timeline = new Intent(ADMShare.this, ADMTimeline.class);
                                startActivity(adm_timeline);
                                finish();

                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ADMShare.this, "Sharing Failed!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMShare.this, "Sharing Failed!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("petName", petName);
                params.put("petCategory", petCategory);
                params.put("petPhoto", petPhoto);
                params.put("petProcedure", petProcedure);
                params.put("petDate", petDate);
                params.put("userID", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }

    public void onItemSelected(AdapterView<?>arg0, View arg1, int posistion, long id){


    }

    public void onNothingSelected(AdapterView<?>arg0){
    }

    public void clearFields(){
        image_meal.setImageResource(R.drawable.clifford);
        data_mealname.setText("");
        data_description.setText("");
    }




    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Go back to Dashboard?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent b = new Intent(ADMShare.this, AdMDashboard.class );
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
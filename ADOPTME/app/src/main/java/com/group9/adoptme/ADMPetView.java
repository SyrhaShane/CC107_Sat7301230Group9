package com.group9.adoptme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ADMPetView extends AppCompatActivity {
    TextView petName, petDate, petDescription, fullname, petCategory, add_comment;
    ImageView petPhoto, userphoto;
    SessionManager sessionManager;
    String getID;
    String data_petName, data_petDate, data_petDescription, data_fullname, data_petCategory,
            data_petPhoto, data_userphoto;

    int data_petID, data_userID;
    String set_pet;
    EditText combox;

    List<PetComments> petCommentsList;
    private RecyclerView commentRecycler;
    AdapterComment adapter;
    private static final String GETCOMMENT= "\"https://groupadoptme.000webhostapp.com/AdoptMe/getComment.php?setPet=";
    private static String URL_ADDCOMMENT ="\"https://groupadoptme.000webhostapp.com/AdoptMe/addComment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adoptme_petview);
        sessionManager = new SessionManager( this);


        fullname = findViewById(R.id.mv_textuser);
        petName = findViewById(R.id.mv_textTitle);
        petDate = findViewById(R.id.mv_textdate);
        petDescription = findViewById(R.id.mv_textDescription);
        petCategory = findViewById(R.id.mv_categ);
        petPhoto = findViewById(R.id.mv_mealImage);
        userphoto = findViewById(R.id.mv_picture);

        combox = findViewById(R.id.mv_commentbox);
        add_comment = findViewById(R.id.mv_comment);

        petCommentsList = new ArrayList<>();
        commentRecycler = findViewById(R.id.comment_recycler);
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        data_petID = intent.getIntExtra("petID", 0);
        data_petName = intent.getStringExtra("petName");
        data_petCategory = intent.getStringExtra("petCategory");
        data_petPhoto = intent.getStringExtra("petPhoto");
        data_petDescription = intent.getStringExtra("petProcedure");
        data_petDate = intent.getStringExtra("petDate");
        data_userID = intent.getIntExtra("userID",0);
        data_userphoto = intent.getStringExtra("userphoto");
        data_fullname = intent.getStringExtra("fullname");

        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(sessionManager.USERID);

        fullname.setText(data_fullname);
        petName.setText(data_petName);
        petCategory.setText(data_petCategory);
        petDescription.setText(data_petDescription);
        petDate.setText(data_petDate);

        Glide.with(this)
                .load(data_petPhoto).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(petPhoto);

        Glide.with(this)
                .load(data_userphoto).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(userphoto);

        getComment();
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddComment();
            }
        });
    }

    private void AddComment() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Comment...");
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final String comments = this.combox.getText().toString();
        final String userID = getID;
        final int petID = data_petID;
        final String commentDate = sdf.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDCOMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                petCommentsList.clear();
                                getComment();
                                combox.setText("");
                                progressDialog.dismiss();
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ADMPetView.this, "Failed to comment!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMPetView.this, "Failed to comment!!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("comments", comments);
                params.put("petID", String.valueOf(petID));
                params.put("commentDate", commentDate);
                params.put("userID", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getComment() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETCOMMENT+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            for(int i=0; i<dashPet.length();i++){
                                JSONObject petObject = dashPet.getJSONObject(i);
                                String commentID = petObject.getString("commentID");
                                String comments = petObject.getString("comments");
                                String commentDate = petObject.getString("commentDate");
                                String petID = petObject.getString("petID");
                                String userphoto = petObject.getString("userphoto");
                                String fullname = petObject.getString("fullname");
                                PetComments petComments = new PetComments(commentID, comments, commentDate, petID, userphoto, fullname);
                                petCommentsList.add(petComments);
                            }
                            adapter = new AdapterComment(ADMPetView.this, petCommentsList);
                            adapter.notifyDataSetChanged();
                            commentRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMPetView.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }

                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
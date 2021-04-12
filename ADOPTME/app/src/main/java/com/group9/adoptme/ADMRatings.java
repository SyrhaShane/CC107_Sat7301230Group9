package com.group9.adoptme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ADMRatings extends AppCompatActivity {
    String data_petName, data_petDate, data_petDescription, data_fullname, data_petCategory,
            data_petPhoto, data_userphoto;
    String getID, set_pet;
    double sumRate;
    int data_petID, data_userID;
    int star1, star2, star3, star4, star5, starSum, prog1, prog2, prog3, prog4, prog5;
    SessionManager sessionManager;
    final Handler handler = new Handler();

    private static String URL_ADDRATE ="https://groupadoptme.000webhostapp.com/AdoptMe/addRateReview.php";
    private static final String GETREVIEW = "https://groupadoptme.000webhostapp.com/AdoptMe/getRateReview.php?setMeal=";
    private static String GETTOTALRATER ="https://groupadoptme.000webhostapp.com/AdoptMe/countTotalRater.php?setMeal=";
    private static String GETTOTALNUM1 ="https://groupadoptme.000webhostapp.com/AdoptMe/count1Rate.php?setMeal=";
    private static String GETTOTALNUM2 ="https://groupadoptme.000webhostapp.com/AdoptMe/count2Rate.php?setMeal=";
    private static String GETTOTALNUM3 ="https://groupadoptme.000webhostapp.com/AdoptMe/count3Rate.php?setMeal=";
    private static String GETTOTALNUM4 ="https://groupadoptme.000webhostapp.com/AdoptMe/count4Rate.php?setMeal=";
    private static String GETTOTALNUM5 ="https://groupadoptme.000webhostapp.com/AdoptMe/count5Rate.php?setMeal=";
    private static String GETSUMRATE ="https://groupadoptme.000webhostapp.com/AdoptMe/countSumRate.php?setMeal=";

    ////////
    TextView petName, userFullName, totalPetRate, totalRater, totalRates,
            total5, total4, total3, total2, total1 ;
    ImageView petPhoto, userphoto;
    ProgressBar progress01, progress02, progress03, progress04, progress05;
    RatingBar totalRate, setRate;
    EditText rateReviewbox;
    Button submittRate;
    List<RatingReviews> ratingReviewsList;
    private RecyclerView rateRecycler;
    AdapterRatings adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adoptme_ratings);
        sessionManager = new SessionManager( this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(sessionManager.USERID);

        ///////INITIALIZE////////////
        petName = findViewById(R.id.rate_mealname);
        userFullName = findViewById(R.id.rate_mealUser);
        totalPetRate = findViewById(R.id.totalRate);
        totalRater = findViewById(R.id.rater);
        totalRates = findViewById(R.id.total_rater);
        petPhoto = findViewById(R.id.rate_mealimage);
        userphoto = findViewById(R.id.rate_userphoto);
        progress01 = findViewById(R.id.ratebar1);
        progress02 = findViewById(R.id.ratebar2);
        progress03 = findViewById(R.id.ratebar3);
        progress04 = findViewById(R.id.ratebar4);
        progress05 = findViewById(R.id.ratebar5);
        totalRate = findViewById(R.id.totalRate_bar);

        total1 = findViewById(R.id.total_num1);
        total2 = findViewById(R.id.total_num2);
        total3 = findViewById(R.id.total_num3);
        total4 = findViewById(R.id.total_num4);
        total5 = findViewById(R.id.total_num5);

        setRate = findViewById(R.id.meal_ratebar);
        submittRate = findViewById(R.id.button_rate);
        rateReviewbox= findViewById(R.id.review_box);

        ratingReviewsList = new ArrayList<>();
        rateRecycler = findViewById(R.id.rate_recycler);
        rateRecycler.setHasFixedSize(true);
        rateRecycler.setLayoutManager(new LinearLayoutManager(this));

        ////INTENT DATA////////////////////////////////////
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

        ///////////SET INTENT DATA////////////////////////////////

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
        userFullName.setText("by "+data_fullname);
        petName.setText(data_petName);

        generateRatings();
        /////////////////////////ADDING RATE
        submittRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float mealRate = setRate.getRating();
                String rateReview = rateReviewbox.getText().toString();
                if(mealRate==0){
                    Toast.makeText(ADMRatings.this, "Your rate has not been set!", Toast.LENGTH_SHORT).show();
                }
                else if(rateReview.isEmpty()){
                    Toast.makeText(ADMRatings.this, "Please write your review!", Toast.LENGTH_SHORT).show();
                }
                else {
                    addRateReview();
                }
            }
        });
    }

    public void generateRatings(){
        getTotal1();
        getTotal2();
        getTotal3();
        getTotal4();
        getTotal5();
        getTotalRater();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ratingReviewsList.clear();
                getRateReview();
                getSumRates();

            }
        }, 4000);
    }

    private void addRateReview()  {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding your Review");
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final float petRate = setRate.getRating();
        final String rateReview = this.rateReviewbox.getText().toString();
        final String userID = getID;
        final int petID = data_petID;
        final String rateDate = sdf.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDRATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                ratingReviewsList.clear();
                                getRateReview();
                                rateReviewbox.setText("");
                                generateRatings();
                                progressDialog.dismiss();
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ADMRatings.this, "You have already rated this pet!\n", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Failed to add review!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rateReview", rateReview);
                params.put("petID", String.valueOf(petID));
                params.put("petRate", String.valueOf(petRate));
                params.put("rateDate", rateDate);
                params.put("userID", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getRateReview() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETREVIEW+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            for(int i=0; i<dashPet.length();i++){
                                JSONObject petObject = dashPet.getJSONObject(i);
                                int rateID = petObject.getInt("rateID");
                                int userID = petObject.getInt("userID");
                                int petID = petObject.getInt("petID");
                                String petRate = petObject.getString("petRate");
                                String rateReview = petObject.getString("rateReview");
                                String rateDate = petObject.getString("rateDate");
                                String fullname = petObject.getString("fullname");
                                String userphoto = petObject.getString("userphoto");
                                RatingReviews ratingReviews = new RatingReviews (rateID, userID, petID, petRate, rateReview, rateDate, fullname, userphoto);
                                ratingReviewsList.add(ratingReviews);
                            }
                            adapter = new AdapterRatings(ADMRatings.this, ratingReviewsList);
                            adapter.notifyDataSetChanged();
                            rateRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotalRater() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALRATER+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            JSONObject petObject = dashPet.getJSONObject(0);
                            int petRater = petObject.getInt("petRater");
                            starSum = petRater;
                            totalRater.setText(String.valueOf(petRater)+ " ratings");
                            totalRates.setText(String.valueOf(petRater));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal1() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM1+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            JSONObject petObject = dashPet.getJSONObject(0);
                            int pet1Rate = petObject.getInt("pet1Rate");
                            star1 = pet1Rate;
                            total1.setText(String.valueOf(pet1Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal2() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM2+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet= new JSONArray(response);
                            JSONObject petObject = dashPet.getJSONObject(0);
                            int pet2Rate = petObject.getInt("pet2Rate");
                            star2 = pet2Rate;
                            total2.setText(String.valueOf(pet2Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal3() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM3+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            JSONObject petObject = dashPet.getJSONObject(0);
                            int pet3Rate = petObject.getInt("pet3Rate");
                            star3 = pet3Rate;
                            total3.setText(String.valueOf(pet3Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal4() {
        set_pet= String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM4+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            JSONObject petObject = dashPet.getJSONObject(0);
                            int pet4Rate = petObject.getInt("pet4Rate");
                            star4 = pet4Rate;
                            total4.setText(String.valueOf(pet4Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal5() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM5+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            JSONObject petObject = dashPet.getJSONObject(0);
                            int pet5Rate = petObject.getInt("pet5Rate");
                            star5 = pet5Rate;
                            total5.setText(String.valueOf(pet5Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getSumRates() {
        set_pet = String.valueOf(data_petID);
        ContentValues values = new ContentValues();
        values.put("1", set_pet);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETSUMRATE+set_pet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashPet = new JSONArray(response);
                            JSONObject petObject = dashPet.getJSONObject(0);
                            double data_sumRate = Double.valueOf(petObject.getDouble("sumRate"));
                            sumRate = data_sumRate;

                            prog1 = (100/starSum)*star1;
                            prog2 = (100/starSum)*star2;
                            prog3 = (100/starSum)*star3;
                            prog4 = (100/starSum)*star4;
                            prog5 = (100/starSum)*star5;

                            progress01.setProgress(prog1);
                            progress02.setProgress(prog2);
                            progress03.setProgress(prog3);
                            progress04.setProgress(prog4);
                            progress05.setProgress(prog5);

                            NumberFormat df = new DecimalFormat("#0.0");
                            double averageRate = sumRate/starSum;
                            Double ave = Double.valueOf(averageRate);
                            totalRate.setRating(ave.floatValue());
                            totalPetRate.setText(String.valueOf(df.format(averageRate)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ADMRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

}
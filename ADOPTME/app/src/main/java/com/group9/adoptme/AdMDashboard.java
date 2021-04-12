package com.group9.adoptme;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

public class AdMDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView text_fullname,
            text_email,
            text_welcome;
    private CircleImageView user_photo;
    SessionManager sessionManager;
    private static final String GETPET = "https://groupadoptme.000webhostapp.com/AdoptMe/getPetDash.php";

    private String pet_category;
    private RecyclerView dashRecycler;
    AdapterDashboard adapter;
    private onClickInterface onclickInterface;
    List<DashboardPet> dashboardPetList;
    String getID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_m_dashboard);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >=0 && timeOfDay <9){
            pet_category = "Pet";
        }
        else if (timeOfDay >=9 && timeOfDay <14){
            pet_category = "Pet";
        }
        else if (timeOfDay >=14 && timeOfDay <21){
            pet_category = "Pet";
        }
        else if (timeOfDay >=21 && timeOfDay <24){
            pet_category = "Pet";
        }
        text_welcome = findViewById(R.id.meal_categ);
        text_welcome.setText("Here are our recomended "+pet_category+ " for you!");
        sessionManager = new SessionManager( this);
        drawerLayout = findViewById(R.id.nav_drawer);
        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {

            }
        };
        dashboardPetList = new ArrayList<>();
        dashRecycler = findViewById(R.id.dash_recycler);
        dashRecycler.setHasFixedSize(true);
        dashRecycler.setLayoutManager(new LinearLayoutManager(this));

        getDashboardPet();
        ////////////////////////

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView mNavigationView = findViewById(R.id.nav_drawer);
        text_fullname = mNavigationView.getHeaderView(0).findViewById(R.id.user_fullname);
        text_email = mNavigationView.getHeaderView(0).findViewById(R.id.user_email);
        user_photo = mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);

        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);
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

        else if(!mPhoto.isEmpty()){
            String default_photo = "https://groupadoptme.000webhostapp.com/AdoptMe/photo/adm_user.png";
            Glide.with(this)
                    .load(default_photo).error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(user_photo);
        }
        text_fullname.setText(mName);
        text_email.setText(mEmail);
        if (mNavigationView != null){
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void getDashboardPet() {
        String GETPET_URL = null;
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >=0 && timeOfDay <9){
            GETPET_URL = "https://groupadoptme.000webhostapp.com/AdoptMe/getPetDashMorning.php";
        }
        else if (timeOfDay >=9 && timeOfDay <14){
            GETPET_URL = "https://groupadoptme.000webhostapp.com/AdoptMe/getPetDashNoon.php";
        }
        else if (timeOfDay >=14 && timeOfDay <21){
            GETPET_URL = "https://groupadoptme.000webhostapp.com/AdoptMe/getPetDashEvening.php";
        }
        else if (timeOfDay >=21 && timeOfDay <24){
            GETPET_URL = "https://groupadoptme.000webhostapp.com/AdoptMe/getPetDashEvening.php";
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETPET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashpet = new JSONArray(response);
                            for(int i=0; i<dashpet.length();i++){
                                JSONObject petObject = dashpet.getJSONObject(i);
                                int petID = petObject.getInt("petID");
                                String petName = petObject.getString("petName");
                                String petCategory = petObject.getString("petCategory");
                                String petProcedure = petObject.getString("petProcedure");
                                int userID = petObject.getInt("userID");
                                String petDate = petObject.getString("petDate");
                                String petPhoto = petObject.getString("petPhoto");
                                String userphoto = petObject.getString("userphoto");
                                String fullname = petObject.getString("fullname");
                                //int likeValue = mealObject.getInt("likeValue");
                                //int favValue = mealObject.getInt("favValue");
                                DashboardPet dashboardPet = new DashboardPet(petID, petName, petCategory, petPhoto, petProcedure, petDate, userID, userphoto, fullname);//, likeValue, favValue);
                                dashboardPetList.add(dashboardPet);
                            }
                            adapter = new AdapterDashboard(AdMDashboard.this, dashboardPetList, onclickInterface);
                            dashRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdMDashboard.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
       /* {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("r_userID", r_userID);
                params.put("r_category", r_category);
                return params;
            }
        }

        */
                ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.nav_logout){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to Log out?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    sessionManager.logout();
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
        else if(id == R.id.nav_profile){
            Intent mh_profile = new Intent(AdMDashboard.this, ADMProfile.class);
            startActivity(mh_profile);
        }
        else if(id == R.id.nav_sharepet){
            Intent mh_share = new Intent(AdMDashboard.this, ADMShare.class);
            startActivity(mh_share);
        }

        else if(id == R.id.nav_timeline){
            Intent mh_timeline = new Intent(AdMDashboard.this, ADMTimeline.class);
            startActivity(mh_timeline);
        }

        else if(id == R.id.nav_favorites){
            Intent mh_fav = new Intent(AdMDashboard.this, ADMFavorite.class);
            startActivity(mh_fav);
        }

        else if(id == R.id.nav_about){
            Intent mh_about = new Intent(AdMDashboard.this, ADMAbout.class);
            startActivity(mh_about);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return  true;
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to Log out?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                sessionManager.logout();

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
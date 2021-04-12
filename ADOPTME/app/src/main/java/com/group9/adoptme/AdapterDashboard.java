package com.group9.adoptme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.DashBoardPetHolder> {
    private Context mCtx;
    private List<DashboardPet> dashboardPetList;
    SessionManager sessionManager;
    String getID;
    onClickInterface onClickInterface;
    private static String URL_ADDLIKE ="https://jdrael.000webhostapp.com/MealHub/addLike.php";
    private static String URL_ADDFAV ="https://jdrael.000webhostapp.com/MealHub/addfav.php";


    public AdapterDashboard(Context mCtx, List<DashboardPet> dashboardPetList, onClickInterface onClickInterface) {
        this.mCtx = mCtx;
        this.dashboardPetList = dashboardPetList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public DashBoardPetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_dashboard, parent, false);
        return new DashBoardPetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashBoardPetHolder holder, final int position) {
        DashboardPet dashboardPet = dashboardPetList.get(position);
        holder.petName.setText(dashboardPet.getPetName());
        holder.petDate.setText(dashboardPet.getPetDate());
        holder.petDescription.setText(dashboardPet.getPetProcedure());
        holder.fullname.setText(dashboardPet.getFullname());

       /* int likeValue = dashboardMeal.getLikeValue();
        if(likeValue==1)
            holder.addlike.setImageResource(R.drawable.ic_like_on);
        else{
            holder.addlike.setImageResource(R.drawable.ic_like_off);
        }

        int favValue = dashboardMeal.getFavValue();
        if(favValue==1)
            holder.addfav.setImageResource(R.drawable.ic_fav_on);
        else{
            holder.addfav.setImageResource(R.drawable.ic_fav_off);
        }

        */

        Glide.with(mCtx)
                .load(dashboardPet.getPetPhoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.petPhoto);

        Glide.with(mCtx)
                .load(dashboardPet.getuserPhoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);

        holder.commentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                Intent intent = new Intent(mCtx, ADMPetView.class);
                intent.putExtra("petID", dashboardPetList.get(position).getPetID());
                intent.putExtra("petName", dashboardPetList.get(position).getPetName());
                intent.putExtra("petCategory", dashboardPetList.get(position).getPetCategory());
                intent.putExtra("petPhoto", dashboardPetList.get(position).getPetPhoto());
                intent.putExtra("petProcedure", dashboardPetList.get(position).getPetProcedure());
                intent.putExtra("petDate", dashboardPetList.get(position).getPetDate());
                intent.putExtra("userID", dashboardPetList.get(position).getUserID());
                intent.putExtra("userphoto", dashboardPetList.get(position).getuserPhoto());
                intent.putExtra("fullname", dashboardPetList.get(position).getFullname());
                mCtx.startActivity(intent);
            }
        });


        holder.dashrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                Intent intent = new Intent(mCtx, ADMRatings.class);
                intent.putExtra("petID", dashboardPetList.get(position).getPetID());
                intent.putExtra("petName", dashboardPetList.get(position).getPetName());
                intent.putExtra("petCategory", dashboardPetList.get(position).getPetCategory());
                intent.putExtra("petPhoto", dashboardPetList.get(position).getPetPhoto());
                intent.putExtra("petProcedure", dashboardPetList.get(position).getPetProcedure());
                intent.putExtra("petDate", dashboardPetList.get(position).getPetDate());
                intent.putExtra("userID", dashboardPetList.get(position).getUserID());
                intent.putExtra("userphoto", dashboardPetList.get(position).getuserPhoto());
                intent.putExtra("fullname", dashboardPetList.get(position).getFullname());
                mCtx.startActivity(intent);
            }
        });

        holder.addlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                sessionManager = new SessionManager( mCtx);
                sessionManager.checkLogin();
                HashMap<String, String> user = sessionManager.getUserDetail();
                getID = user.get(sessionManager.USERID);

                final int userID = Integer.parseInt(getID);
                final int petID = dashboardPetList.get(position).getPetID();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDLIKE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    String message = jsonObject.getString("message");
                                    if(success.equals("1")){
                                        Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                                    }
                                    if(message.equals("You liked this pet."))
                                        holder.addlike.setImageResource(R.drawable.like);
                                    else{
                                        holder.addlike.setImageResource(R.drawable.like_off);
                                    }
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(mCtx, "Failed to like this pet!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCtx, "Failed to like this pet!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("petID", String.valueOf(petID));
                        params.put("userID", String.valueOf(userID));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                requestQueue.add(stringRequest);
            }
        });

        holder.addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                sessionManager = new SessionManager( mCtx);
                sessionManager.checkLogin();
                HashMap<String, String> user = sessionManager.getUserDetail();
                getID = user.get(sessionManager.USERID);

                final int userID = Integer.parseInt(getID);
                final int petID = dashboardPetList.get(position).getPetID();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDFAV,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    String message = jsonObject.getString("message");
                                    if(success.equals("1")){
                                        Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                                    }
                                    if(message.equals("Pet added to favorite."))
                                        holder.addfav.setImageResource(R.drawable.ic_favorite_icon);
                                    else{
                                        holder.addfav.setImageResource(R.drawable.ic_favorite_off);
                                    }

                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(mCtx, "Failed to add favorite on this pet!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCtx, "Failed to add favorite on this pet!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("petID", String.valueOf(petID));
                        params.put("userID", String.valueOf(userID));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                requestQueue.add(stringRequest);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dashboardPetList.size();
    }

    class DashBoardPetHolder extends RecyclerView.ViewHolder {
        TextView petName, petDate, petDescription, fullname, dashrate;
        ImageView petPhoto, userphoto, commentview, addlike, addfav;
        LinearLayout linearLayout;

        public DashBoardPetHolder(View itemView) {
            super(itemView);
            petPhoto = itemView.findViewById(R.id.listImage);
            petName = itemView.findViewById(R.id.textTitle);
            petDate = itemView.findViewById(R.id.textdate);
            petDescription = itemView.findViewById(R.id.textDescription);
            userphoto = itemView.findViewById(R.id.dash_picture);
            fullname = itemView.findViewById(R.id.textuser);
            dashrate = itemView.findViewById(R.id.textrate);
            commentview = itemView.findViewById(R.id.m_comment);
            linearLayout = itemView.findViewById(R.id.linear_dash);
            addlike = itemView.findViewById(R.id.m_like);
            addfav = itemView.findViewById(R.id.m_favorite);
        }
    }

}

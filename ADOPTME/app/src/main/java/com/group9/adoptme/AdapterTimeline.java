package com.group9.adoptme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class AdapterTimeline extends RecyclerView.Adapter<AdapterTimeline.TimelineHolder> {
    private Context mCtx;
    private List<UsersTimeline> usersTimelineList;
    onClickInterface onClickInterface;
    SessionManager sessionManager;
    String getID;
    private static String URL_ADDFAV = "https://jdrael.000webhostapp.com/MealHub/addfav.php";
    private static String DLTMEAL = "https://jdrael.000webhostapp.com/MealHub/deleteMeal.php";

    public AdapterTimeline(Context mCtx, List<UsersTimeline> usersTimelineList, onClickInterface onClickInterface) {
        this.mCtx = mCtx;
        this.usersTimelineList = usersTimelineList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public TimelineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_timeline, parent, false);
        return new TimelineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimelineHolder holder, final int position) {
        UsersTimeline usersTimeline = usersTimelineList.get(position);
        holder.petName.setText(usersTimeline.getPetName());
        holder.petDate.setText(usersTimeline.getPetDate());
        holder.fullname.setText(usersTimeline.getFullname());
        String category = usersTimeline.getPetCategory();
        holder.petCategory.setText("Pet Category: " + category);
        Glide.with(mCtx)
                .load(usersTimeline.getPetPhoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.petPhoto);

        Glide.with(mCtx)
                .load(usersTimeline.getUserphoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);


        holder.editpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                Intent intent = new Intent(mCtx, ADMUpdate.class);
                intent.putExtra("petID", usersTimelineList.get(position).getPetID());
                intent.putExtra("petName", usersTimelineList.get(position).getPetName());
                intent.putExtra("petCategory", usersTimelineList.get(position).getPetCategory());
                intent.putExtra("petPhoto", usersTimelineList.get(position).getPetPhoto());
                intent.putExtra("petProcedure", usersTimelineList.get(position).getPetProcedure());
                mCtx.startActivity(intent);
            }
        });

        holder.deletepet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                new AlertDialog.Builder(mCtx).setMessage("Delete this Pet?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog progressDialog = new ProgressDialog(mCtx);
                                progressDialog.setMessage("Updating...");
                                progressDialog.show();
                                final String petID = usersTimelineList.get(position).getPetID();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, DLTMEAL,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String success = jsonObject.getString("success");
                                                    if (success.equals("1")) {
                                                        Toast.makeText(mCtx, "Pet deleted.", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        Intent refreshT = new Intent(mCtx, ADMTimeline.class);
                                                        mCtx.startActivity(refreshT);
                                                        ((Activity) mCtx).finish();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(mCtx, "Failed to delete!\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }

                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(mCtx, "Failed to delete!\n" + error.toString(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("petID", petID);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                                requestQueue.add(stringRequest);

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return usersTimelineList.size();
    }

    class TimelineHolder extends RecyclerView.ViewHolder {
        TextView petName, petDate, fullname, petCategory;
        ImageView petPhoto, userphoto, deletepet, editpet;
        LinearLayout linearLayout;

        public TimelineHolder(View itemView) {
            super(itemView);
            petPhoto = itemView.findViewById(R.id.timeline_listImage);
            petName = itemView.findViewById(R.id.timeline_textTitle);
            petDate = itemView.findViewById(R.id.timeline_textdate);
            userphoto = itemView.findViewById(R.id.timeline_picture);
            fullname = itemView.findViewById(R.id.timeline_textuser);
            deletepet = itemView.findViewById(R.id.timeline_delete);
            editpet = itemView.findViewById(R.id.timeline_edit);
            petCategory = itemView.findViewById(R.id.timeline_category);
        }
    }
}
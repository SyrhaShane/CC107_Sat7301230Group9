package com.group9.adoptme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;

public class AdMLogin extends AppCompatActivity {
    private EditText data_username,
            data_password;
    private Button button_login;
    TextView text_signup;

    //private ProgressBar progressBar;
    private static String URL ="https://groupadoptme.000webhostapp.com/AdoptMe/login1.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_m_login);
        sessionManager = new SessionManager( this);

        text_signup = findViewById(R.id.textView_signup);
        data_username = findViewById(R.id.username);
        data_password = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);



        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(AdMLogin.this, AdMRegister.class);
                startActivity(signup);
            }
        });
    }

    public void login(View view) {
        String username = data_username.getText().toString().trim();
        String password = data_password.getText().toString().trim();
        if(!username.equals("") && !password.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) {
                        Intent intent = new Intent(AdMLogin.this, ADMProfile.class);
                        startActivity(intent);
                    } else if (response.equals("failure")) {
                        Toast.makeText(AdMLogin.this, "Invalid Login ID/Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AdMLogin.this,error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("username",username);
                    data.put("password",password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Close this Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

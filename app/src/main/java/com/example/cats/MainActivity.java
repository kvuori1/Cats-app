package com.example.cats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private String startingCat = "https://cdn2.thecatapi.com/images/121.jpg";
    private String defaultCatUrl = "https://api.thecatapi.com/v1/images/search";
    private String catBreedUrl = "https://api.thecatapi.com/v1/images/search?breed_ids=";
    private String lastCatUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);

        //katsotaan onko nettiä
        if (checkConnection()) {
            //katsotaan onko bundlea olemassa
            if (savedInstanceState != null) {
                loadImage(savedInstanceState.getString("lastCatUrl"));
            }
            //katsotaan onko valittu tiettyä rotua asetuksista
            else if (getIntent().getStringExtra("breedID") == null) {
                loadImage(startingCat);
            }
            //jos ei ole bundlea tai intentissä tietoa pistetään oletus kissa luonnissa
            else {
                View view = findViewById(android.R.id.content).getRootView();
                getNewCat(view);
            }
        }
    }

    //ladataan kuva imageviewiin käyttän picassoa
    private void loadImage(String url) {
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.get()
                .load(url)

                .into(imageView);
        //tallenetaan viimeinen kuva muistiin
        lastCatUrl = url;
    }



    public void getNewCat(View view) {
        //katsotaan onko nettiä
        if(checkConnection()) {
            //jos on netti niin katsotaan onko tiettyä rotua valittu
            if (getIntent().getStringExtra("breedID") == null) {
                getCatURL(view, defaultCatUrl);
            }
            //jos ei niin haetaan kissa kaikilla roduilla
            else {
                String breedUrl = catBreedUrl + getIntent().getStringExtra("breedID");
                getCatURL(view, breedUrl);
            }
        }
    }



    //haetaan JSON netistä
    public void getCatURL(View view, String mUrl) {
        //haetaan JSON
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, mUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseJsonAndUpdateUI(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //lisätään json jonooon
        mQueue.add(jsonObjectRequest);
    }

    private void parseJsonAndUpdateUI (JSONArray catObject) {
        //haetaan kissan osoite Json:ista
        try {
            String catUrl = catObject.getJSONObject(0).getString("url");
            loadImage(catUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    //tuodaan dropdownin tila takaisin asetuksisiin
    public void toSettings(View view) {
        int dropDownPos = getIntent().getIntExtra("dropDownPos", 0);
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("dropDownPos", dropDownPos);
        startActivity(intent);
    }












    private boolean checkConnection() {
        //katsotaan onko netti ja jos ei ole niin kerrotaan käyttäjälle
        if(connectedToInternet()) {
            Resources res = getResources();
            TextView textViewIntert = findViewById(R.id.textViewInternetState);
            textViewIntert.setText(res.getString(R.string.yes_internet));
            return true;
        }
        else {
            Resources res = getResources();
            TextView textViewIntert = findViewById(R.id.textViewInternetState);
            textViewIntert.setText(res.getString(R.string.no_internet));
            return false;
        }
    }

    //katsoo onko netti
    private boolean connectedToInternet() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        if(networkInfo != null) {
            if(networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }


    //tallentaa viimeisen kissan bundleen
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("lastCatUrl", lastCatUrl);
        super.onSaveInstanceState(savedInstanceState);
    }
}
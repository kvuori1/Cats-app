package com.example.cats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    //API:n kissojen rotujen ID:eet
    String[] breedIDs = {"abys", "aege", "abob", "acur", "asho", "awir", "amau", "amis", "bali", "bamb", "beng", "birm", "bomb", "bslo", "bsho", "bure",
            "buri", "cspa", "ctif", "char", "chau", "chee", "csho", "crex", "cymr", "cypr", "drex", "dons", "lihu", "emau", "ebur", "esho", "hbro", "hima",
            "jbob", "java",  "khao", "kora", "lape", "mcoo", "manx", "munc", "nebe", "norw", "ocic", "orie", "pers", "pixi", "raga", "ragd",
            "rblu", "sava", "sfol", "srex", "siam", "sibe", "sing", "snow", "soma", "sphy", "tonk", "toyg", "tang", "tvan", "ycho"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //haetaan rodut resursseista
        Resources res = getResources();
        String[] items = res.getStringArray(R.array.breeds);
        //haetaan spinneri
        Spinner spinner = findViewById(R.id.spinnerDropDown);
        //luodaan adapteri millä kerrotaan miten asiat näytetään spinnerissä
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //vaihdetaan spinnerin adapteri
        spinner.setAdapter(adapter);

        if(savedInstanceState != null) {
            spinner.setSelection(savedInstanceState.getInt("dropDownPos"));
        }
        else {
            spinner.setSelection(getIntent().getIntExtra("dropDownPos", 0));
        }
    }

    //mennään takaisin mainiin ja tallenetaan rotu ID ja dropdownin valinnan kohta intentiin
    public void returnToMain(View view) {
        Spinner spinner = findViewById(R.id.spinnerDropDown);
        Intent intent = new Intent(this, MainActivity.class);
        if(spinner.getSelectedItemPosition() != 0) {
            intent.putExtra("breedID", breedIDs[spinner.getSelectedItemPosition() - 1]);
            intent.putExtra("dropDownPos", spinner.getSelectedItemPosition());
        }
        startActivity(intent);
    }

    //tallenetaan dropdownin tila bundleen
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Spinner spinner = findViewById(R.id.spinnerDropDown);
        savedInstanceState.putInt("dropDownPos", spinner.getSelectedItemPosition());
        super.onSaveInstanceState(savedInstanceState);
    }
}
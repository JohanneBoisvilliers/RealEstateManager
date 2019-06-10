package com.openclassrooms.realestatemanager.controllers;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.utils.MyApp.getContext;

public class AddARealEstateActivity extends AppCompatActivity {

    @BindView(R.id.spinner_realestate_type) Spinner test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_areal_estate);
        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.type_list,R.layout.custom_item_spinner);


        test.setAdapter(adapter);
    }
}

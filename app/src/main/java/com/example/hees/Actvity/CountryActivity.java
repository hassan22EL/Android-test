package com.example.hees.Actvity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hees.Fragment.CountryFragment;
import com.example.hees.R;

public class CountryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CountryFragment.newInstance())
                    .commitNow();
        }
    }
}

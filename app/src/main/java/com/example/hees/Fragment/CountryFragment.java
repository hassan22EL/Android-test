package com.example.hees.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hees.Adapter.CountryAdapter;
import com.example.hees.Model.Country;
import com.example.hees.R;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CountryFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private EditText search_edittext;
    private TextView search_text;
    private SparseArray<ArrayList<Country>> map = new SparseArray<>();
    private CountryAdapter mAdapter;
    private List<Country> mCountries;
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();

    public static CountryFragment newInstance() {
        return new CountryFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_fragment, container, false);
        search_edittext = (EditText) view.findViewById(R.id.search_contries);
        search_text = (TextView) view.findViewById(R.id.search_txt);

        recyclerView = (RecyclerView) view.findViewById(R.id.countries);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCountries = new ArrayList<>();
        ListCounites();
        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_text.setVisibility(View.GONE);
                search_edittext.setVisibility(View.VISIBLE);
            }
        });
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void ListCounites() {
        int Position;
        Context mContext = getActivity();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));
            // do reading, usually loop until end of file reading
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                //process line
                Country c = new Country(mContext, line, i);
                mCountries.add(c);
                i++;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        mAdapter = new CountryAdapter(mContext, mCountries);
        recyclerView.setAdapter(mAdapter);
    }


}

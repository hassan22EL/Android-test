package com.example.hees.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hees.Actvity.LoginActivity;
import com.example.hees.Model.Country;
import com.example.hees.R;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<Country> mCountries;
    private List<Country> SearchCountries;

    public CountryAdapter(Context mContext, List<Country> mCountries) {
        this.mContext = mContext;
        this.mCountries = mCountries;
        this.SearchCountries = mCountries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.country_item, parent, false);
        return new CountryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Country country = mCountries.get(position);
        holder.CountryName.setText(country.getCountryName());
        holder.CountryCode.setText(country.getCountryCode());
        holder.CountryRes.setImageResource(country.getResId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("Countryname", country.getCountryName());
                intent.putExtra("CountryCode", country.getCountryCode());
                intent.putExtra("CountryRes", country.getResId());
                boolean b = true;
                intent.putExtra("action", b);
                mContext.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return SearchCountries.size();
    }


    //serach
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String serach = charSequence.toString();
                if (serach.isEmpty()) {
                    SearchCountries = mCountries;
                } else {
                    List<Country> list = new ArrayList<>();
                    for (Country c : mCountries) {
                        if (c.getCountryName().toLowerCase().contains(serach.toLowerCase())) {
                            list.add(c);
                        }
                    }
                    SearchCountries = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = SearchCountries;
                filterResults.count = SearchCountries.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                SearchCountries = (ArrayList<Country>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView CountryRes;
        private TextView CountryName;
        private TextView CountryCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CountryName = (TextView) itemView.findViewById(R.id.country_name);
            CountryCode = (TextView) itemView.findViewById(R.id.country_code);
            CountryRes = (ImageView) itemView.findViewById(R.id.country_image);
        }
    }

}

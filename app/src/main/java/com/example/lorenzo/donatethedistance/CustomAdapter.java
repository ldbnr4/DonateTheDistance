package com.example.lorenzo.donatethedistance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Lorenzo on 4/23/2016.
 *
 */
public class CustomAdapter extends ArrayAdapter {

    private final Context context;
    private final ArrayList<String> dates;
    private final ArrayList<String> types;
    private final ArrayList<String> charities;
    private final ArrayList<Float> donations;
    private final ArrayList<Float> calsBurned;

    public CustomAdapter(Context context, ArrayList<String> dates, ArrayList<String> types, ArrayList<String> charities, ArrayList<Float> donations, ArrayList<Float> calsBurned) {
        super(context, R.layout.workout_history, dates);
        this.context = context;
        this.dates = dates;
        this.types = types;
        this.charities = charities;
        this.donations = donations;
        this.calsBurned = calsBurned;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.workout_history, null);
        }

        final ViewHolder holder = new ViewHolder();
        holder.date = (TextView) convertView.findViewById(R.id.lbl_histDate);
        holder.type = (TextView) convertView.findViewById(R.id.lbl_histType);
        holder.charity = (TextView) convertView.findViewById(R.id.lbl_histCharity);
        holder.donation = (TextView) convertView.findViewById(R.id.lbl_histDonation);
        holder.calsBurned = (TextView) convertView.findViewById(R.id.lbl_histCalories);

        holder.date.setText(dates.get(position));
        holder.type.setText(types.get(position));
        holder.charity.setText(charities.get(position));
        holder.donation.setText(NumberFormat.getCurrencyInstance(Locale.US).format(donations.get(position)));
        holder.calsBurned.setText(String.valueOf((int) calsBurned.get(position).floatValue()));

        return convertView;
    }

    public class ViewHolder {
        TextView date, type, charity, donation, calsBurned;
    }
}

package com.example.jakub.osobistytrenerbiegacza.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jakub.osobistytrenerbiegacza.model.Training;

import java.util.List;

/**
 * Created by Jakub on 2016-03-06.
 */
public class HistoryArrayAdapter extends ArrayAdapter <Training> {
    public HistoryArrayAdapter(Context context, List<Training> trainingHistory) {
        super(context,android.R.layout.simple_list_item_1, android.R.id.text1, trainingHistory);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        Training training = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        TextView text1; // view lookup cache stored in tag
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        text1 = (TextView) convertView.findViewById(android.R.id.text1);
        // Populate the data into the template view using the data object

        String text = DateFormatter.toString(training.getSummary().getDate(), "dd.MM.yyyy");
        text1.setText(text);
//        text1.setText(training.getSummary().getDate().toString());
        // Return the completed view to render on screen
        return convertView;
    }
}

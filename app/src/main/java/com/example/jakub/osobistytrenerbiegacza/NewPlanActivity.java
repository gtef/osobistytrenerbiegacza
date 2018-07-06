package com.example.jakub.osobistytrenerbiegacza;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakub.osobistytrenerbiegacza.utils.DateFormatter;
import com.example.jakub.osobistytrenerbiegacza.utils.FileOperations;
import com.example.jakub.osobistytrenerbiegacza.utils.PlanGeneration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewPlanActivity extends ActionBarActivity {

    private Spinner distanceSpinner;

    private EditText timeET;

    private EditText etStartDate;
    private TextView tvEndDate;

    //name of selected spinner item
    private String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        setUpEditText();
        setInitialSelectedItem();
        setUpSpinner();
    }

    @Override
    protected void onStart( ) {
        String result = getRecommendedResult(Singleton.getInstance().getStaminaLevel(), selectedItem);
        timeET.setText(result);
        super.onStart();
    }

    private String getRecommendedResult(int staminaLevel, String selectedItem) {
        switch(selectedItem){
            case "Maraton": return getMarathonRecommendedResult(staminaLevel);
            case "Półmaraton": return getHalfMarathonRecommendedResult(staminaLevel);
            case "10 km": return getTenKmRecommendedResult(staminaLevel);
        }
        return null;
    }

    //TODO proper values
    private String getMarathonRecommendedResult(int staminaLevel) {
        switch(staminaLevel) {
            case 0: return "";
            case 1: return "06:00";
            case 2: return "05:00";
            case 3: return "04:00";
            case 4: return "03:30";
            case 5: return "03:00";
        }
        return null;
    }

    //TODO proper values
    private String getHalfMarathonRecommendedResult(int staminaLevel) {
        switch(staminaLevel) {
            case 0: return "";
            case 1: return "03:00";
            case 2: return "02:30";
            case 3: return "02:00";
            case 4: return "01:45";
            case 5: return "01:30";
        }
        return null;
    }

    //TODO proper values
    private String getTenKmRecommendedResult(int staminaLevel) {
        switch(staminaLevel) {
            case 0: return "";
            case 1: return "01:00";
            case 2: return "00:55";
            case 3: return "00:50";
            case 4: return "00:40";
            case 5: return "00:35";
        }
        return null;
    }

    @Override
    protected void onResume( ) {
        super.onResume();
    }
    @Override
    protected void onPause( ) {
        super.onPause();
    }
    @Override
    protected void onStop( ) {
        super.onStop();
    }

    private void setUpEditText() {
        timeET = (EditText) findViewById(R.id.et_time);
        etStartDate = (EditText) findViewById(R.id.activity_new_plan_et_start_date);
        tvEndDate = (TextView) findViewById(R.id.activity_new_plan_tv_end_date);
        Date d = new Date();
        etStartDate.setText(DateFormatter.toString(d, DateFormatter.STANDARD_FORMAT));
        addListener();
    }

    private void addListener() {
//        etStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    setEndDateText();
//                }
//            }
//        });
        etStartDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    setEndDateText();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    private void setEndDateText() {
        int days = PlanGeneration.getPlanDuration(selectedItem);

        Date dob_var = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            dob_var = sdf.parse(etStartDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date endDate = DateFormatter.addDays(dob_var, days);
        String textA = "Długość planu: " + days+"dni";
        String textB = "\nData zakończenia: "+ DateFormatter.toString(endDate, DateFormatter.STANDARD_FORMAT);
        tvEndDate.setText(textA + textB);
    }


    private void setInitialSelectedItem() {
        String[] distances = getResources().getStringArray(R.array.distance_array);
        selectedItem = distances[0];
    }

    public void setUpSpinner() {
        distanceSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.distance_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(adapter);
        distanceSpinner.setOnItemSelectedListener(getItemSelectedListener());
    }

    private AdapterView.OnItemSelectedListener getItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                String result = getRecommendedResult(Singleton.getInstance().getStaminaLevel(), selectedItem);
                timeET.setText(result);
                setEndDateText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
    }

    public void makeTest(View v) {
        startActivity(new Intent(this, CooperActivity.class));
    }

    public void generate(View v) {
        int properRange = inProperRange(String.valueOf(timeET.getText()), selectedItem);
//        System.out.println(properRange+" "+ selectedItem);
//        if (isTimeValid(String.valueOf(timeET.getText())) && properRange) {
        if (properRange == 0) {
            Date dob_var = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            try {
                dob_var = sdf.parse(etStartDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PlanGeneration.generate(selectedItem, getMinutesFromString(timeET.getText().toString()),dob_var);
            FileOperations.writeObject(Singleton.getInstance(),this);
            startActivity(new Intent(this, MainActivity.class));
        } else {
            if (properRange == 1) {
                timeET.setText("");
                Toast.makeText(getApplicationContext(), "Zły format czasu", Toast.LENGTH_SHORT).show();
            } else if (properRange == 2) {
                timeET.setText("");
                Toast.makeText(getApplicationContext(), "Poza zakresem dozwolonego czasu", Toast.LENGTH_SHORT).show();
            } else {
                timeET.setText("");
                Toast.makeText(getApplicationContext(), "Błąd", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private int getMinutesFromString(String time) {
        String[] arr = time.split(":");

        String hours = arr[0];
        String minutes = arr[1];

        return Integer.parseInt(hours)*60 + Integer.parseInt(minutes);
    }

    private int inProperRange(String time, String distance) {
        String[] arr = time.split(":");
        if(arr.length == 2) {
            String hours = arr[0];
            String minutes = arr[1];
            int intHours;
            int intMinutes;
            try {
                intHours = Integer.parseInt(hours);
                intMinutes = Integer.parseInt(minutes);
            } catch (NumberFormatException ex){
                return 1;
            }
            System.out.println(hours + " " + intHours + " " + minutes + " " + intMinutes);
            if(
                intHours < 0 ||
                intHours > 99 ||
                intMinutes < 0 ||
                intMinutes > 59
            ){
                return 1;
            } else {
                switch (distance) {
                    case "Półmaraton":
                        System.out.println("półmaraton");
                        if ((intHours < 1) || (intHours == 1 && intMinutes < 20) || (intHours == 2 && intMinutes > 25) || (intHours > 3)) {
                            return 2;
                        }
                        break;
                    case "Maraton":
                        System.out.println("maraton");
                        if ((intHours < 3) || (intHours == 3 && intMinutes < 30) || (intHours >= 6)) {
                            return 2;
                        }
                        break;
                    case "10 km":
                        System.out.println("10km");
                        if ( (intHours > 0 && intMinutes > 20) || (intMinutes < 33) ) {
                            return 2;
                        }
                        break;
                }
            }

        } else {
            return 1;
        }
        return 0;
    }

    private boolean isTimeValid(String time) {
        String[] arr = time.split(":");
        if(arr.length == 2) {
            String hours = arr[0];
            String minutes = arr[1];
            int intHours;
            int intMinutes;
            try {
                intHours = Integer.parseInt(hours);
                intMinutes = Integer.parseInt(minutes);
            } catch (NumberFormatException ex){
                return false;
            }
            if(
                intHours < 0 ||
                intHours > 99 ||
                intMinutes < 0 ||
                intMinutes > 59
            ){
                return false;
            }

        }else{
            return false;
        }
        return true;
    }
}
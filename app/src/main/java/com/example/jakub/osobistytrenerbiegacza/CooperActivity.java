package com.example.jakub.osobistytrenerbiegacza;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.jakub.osobistytrenerbiegacza.model.User;
import com.example.jakub.osobistytrenerbiegacza.utils.Cooper;
import com.example.jakub.osobistytrenerbiegacza.utils.FileOperations;
import com.example.jakub.osobistytrenerbiegacza.utils.Toasts;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class CooperActivity extends ActionBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    //layout
    private TextView tvTime;
    private TextView tvDistance;
    private Button btnStart;
    private Button btnStop;

    //time
    private long startTime;
    private Handler timerHandler;
    private Runnable timerRunnable;

    //sound
    private MediaPlayer soundPlayer;

    //location
    private double totalDistance;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private Location lastLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean requestingLocationUpdates;

    private boolean connected;

    /*--------------------- Lifecycle methods ---------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooper);

        createInformationDialog();
        if(Singleton.getInstance().getAge() == 0){
            createAgeDialog();
        }
        initVariables();
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        // Within {@code onPause()}, we pause location updates, but leave the
//        // connection to GoogleApiClient intact.  Here, we resume receiving
//        // location updates if the user has requested them.
//
//        if (googleApiClient.isConnected() && requestingLocationUpdates) {
//            startLocationUpdates();
//        }
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
//        if (googleApiClient.isConnected()) {
//            stopLocationUpdates();
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /*--------------------- onClick methods ---------------------*/
    public void startTest(View v) {
        toggleButtonVisibility();

        soundPlayer.start();
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        if(connected) {
            if (!requestingLocationUpdates) {
                requestingLocationUpdates = true;
                startLocationUpdates();
            }
        }
    }

    public void stopTest(View v) {
        toggleButtonVisibility();

        timerHandler.removeCallbacks(timerRunnable);
        tvTime.setText("00:00");
        totalDistance = 0.0;
        if(connected) {
            if (requestingLocationUpdates) {
                requestingLocationUpdates = false;
                stopLocationUpdates();
            }
        }
    }

    /*--------------------- interfaces methods ---------------------*/
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        connected = true;

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (currentLocation == null) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            updateDistanceTextView();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // requestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of requestingLocationUpdates and if it is true, we start location updates.
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toasts.localizationsServicesDoesntWork(this);
        connected = false;
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = currentLocation;
        currentLocation = location;
        totalDistance += lastLocation.distanceTo(currentLocation);
        updateDistanceTextView();
    }

    /*--------------------- other methods ---------------------*/
    private Runnable getRunnable() {
        return new Runnable() {

            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                tvTime.setText(String.format("%02d:%02d", minutes, seconds));

                if(seconds < 3){
//                if(minutes < 12){
                    timerHandler.postDelayed(this, 500);
                }else {
                    soundPlayer.start();
                    int staminaLevel = Cooper.getStaminaLevel(totalDistance, Singleton.getInstance().getAge());
                    Singleton.getInstance().setStaminaLevel(staminaLevel);
                    createCooperResultDialog(totalDistance, staminaLevel);
                }
            }
        };
    }

    private void createInformationDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_cooper_message)
                .setTitle(R.string.dialog_cooper_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    private int sex = 0;
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_M:
                if (checked) {
                    sex = 1;
                }
                break;
            case R.id.radio_K:
                if (checked) {
                    sex = 2;
                }
                break;
        }

        if (sex != 0 && age > 0 && age <= 150) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }
    AlertDialog dialog = null;
    private int age;
    private void createAgeDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_cooper, null);
        final Context context = getApplicationContext();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setView(view);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_cooper_et_age);

        dialogBuilder.setTitle(R.string.dialog_age_title);
        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Singleton singleton = Singleton.getInstance();
                        singleton.setAge(age);
                        singleton.setSex(sex);
                        FileOperations.writeObject(singleton, getApplicationContext());
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        if (editText != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    age = 0;
                    String text = editText.getText().toString();
                    if(!text.equals("")) {
                        age = Integer.parseInt(editText.getText().toString());
                    } else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                    if (sex == 0 || age <= 0 || age > 150) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    } else {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void createCooperResultDialog(double distance, int staminaLevel) {
        new AlertDialog.Builder(this)
                .setMessage("Przebiegłeś "+distance+ " m.\n" +
                        "Jest to "+ Cooper.getText(staminaLevel)+ " wynik.\n" +
                        "W widoku 'nowy plan' zostały ustawione zalecane czasy.")
                .setTitle(R.string.dialog_cooper_result_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of requestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                requestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                setButtonsEnabledState();
            }

            // Update the value of currentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that currentLocation
                // is not null.
                currentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }


            updateDistanceTextView();
        }
    }


    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void setButtonsEnabledState() {
//        if (requestingLocationUpdates) {
//            mStartUpdatesButton.setEnabled(false);
//            mStopUpdatesButton.setEnabled(true);
//        } else {
//            mStartUpdatesButton.setEnabled(true);
//            mStopUpdatesButton.setEnabled(false);
//        }
    }

    private void updateDistanceTextView() {
        tvDistance.setText(String.format("%.0f m", totalDistance));
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, currentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initVariables() {
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvDistance = (TextView) findViewById(R.id.tv_distance);

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);

        totalDistance = 0.0;
        soundPlayer = MediaPlayer.create(getApplicationContext(), R.raw.stop);

        timerRunnable = getRunnable();
        timerHandler = new Handler();

        requestingLocationUpdates = false;
    }

    private void toggleButtonVisibility() {
        if (btnStart.getVisibility() == View.VISIBLE) {
            btnStart.setVisibility(View.GONE);
            btnStop.setVisibility(View.VISIBLE);
        } else {
            btnStart.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
        }
    }
}
//http://www.online-convert.com/result/7b6cea03490de03758f2b01bf8e5cb39
//http://onlinetonegenerator.com/
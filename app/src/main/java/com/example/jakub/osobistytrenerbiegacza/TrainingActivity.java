package com.example.jakub.osobistytrenerbiegacza;

import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakub.osobistytrenerbiegacza.model.GpxPoint;
import com.example.jakub.osobistytrenerbiegacza.model.GpxSegment;
import com.example.jakub.osobistytrenerbiegacza.model.GpxTrack;
import com.example.jakub.osobistytrenerbiegacza.model.Plan;
import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.model.TrainingType;
import com.example.jakub.osobistytrenerbiegacza.utils.DateFormatter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class TrainingActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //view
    private TextView tvTime;
    private TextView tvDistance;
    private TextView tvPace;
    private TextView tvCurrentLine;
    private Button btnStart;
    private Button btnPause;

    //training
    private int actualPartNumber;
    private TrainingType actualPart;
    private List<TrainingType> parts;
    private int numberOfRep;//numer powtórzenia w serii

    //time
    private long totalTrainingTime;
    private long startTime;
    private long lastTime;//used to compute pace
    private Handler timerHandler;
    private Runnable timerRunnable;

    private long pauseTime;
    private long breakTime = 0L;

    //sound
    private MediaPlayer soundPlayer;

    //velocity
    private double avgTrainingVelocity;

    //location
    private double totalDistance;
    private double totalTrainingDistance;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 2000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private List<GpxPoint> gpxPoints;
    private List<GpxSegment> gpxSegments;
    private List<GpxTrack> gpxTracks;

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
    private boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("aaa", "0 onCreate");
        setContentView(R.layout.activity_training);
        setTrainingData();
        initVariables();
        updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("aaa", "1 onStart");
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("aaa", "2 onStop");
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private void setTrainingData() {
        Log.i("aaa", "3 setTrainingData");
        totalTrainingDistance = 0;
        totalTrainingTime = 0;
        avgTrainingVelocity = 0;
        Plan plan = Singleton.getInstance().getUser().getPlan();
        parts = plan.getTraining(plan.getActualTrainingNumber()).getParts();

        TextView trainingText = (TextView) findViewById(R.id.tv_trainingText);
        tvCurrentLine = (TextView) findViewById(R.id.tv_currentLine);

        String text = "";
        for(TrainingType tt: parts) {
            text += tt.getDescription()+"\n";
        }
        trainingText.setText(text);

        actualPartNumber = 0;
        actualPart = parts.get(actualPartNumber);
        tvCurrentLine.setText(actualPart.getDescription());
    }

    private void initVariables() {
        Log.i("aaa", "4 initVariables");
        initViews();

        totalDistance = 0.0;
        soundPlayer = MediaPlayer.create(getApplicationContext(), R.raw.stop);

        timerRunnable = getRunnable();
        timerHandler = new Handler();

        requestingLocationUpdates = false;

        numberOfRep = 1;

        gpxPoints = new LinkedList<>();
        gpxTracks = new LinkedList<>();
        gpxSegments = new LinkedList<>();
    }

    private void initViews() {
        Log.i("aaa", "5 initViews");
        tvTime = (TextView) findViewById(R.id.activity_training_tv_time);
        tvDistance = (TextView) findViewById(R.id.activity_training_tv_distance);
        tvPace = (TextView) findViewById(R.id.activity_training_tv_pace);

        btnStart = (Button) findViewById(R.id.activity_training_btn_startTraining);
        btnPause = (Button) findViewById(R.id.activity_training_btn_pauseTraining);
        btnStart.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.GONE);
    }
    //-------------------------------------
    //------buttons onClick methods--------
    //-------------------------------------
    public void finishTraining(View view) {
        Log.i("aaa", "6 finishTraining");
        //TODO czy na pewno chcesz skończyć trening?
        timerHandler.removeCallbacks(timerRunnable);
        if(connected) {
            if (requestingLocationUpdates) {
                requestingLocationUpdates = false;
                stopLocationUpdates();
            }
        }
        totalTrainingTime /= 2;
        avgTrainingVelocity = totalTrainingTime != 0 ? totalTrainingDistance/(double)totalTrainingTime : 0;
        System.out.println("ttv "+ avgTrainingVelocity);
        setGpxTrackToTraining();
        Intent intent = new Intent(this, TrainingSummaryActivity.class);
        intent.putExtra("totalTrainingDistance", totalTrainingDistance);
        intent.putExtra("averageTrainingVelocity", avgTrainingVelocity);
        intent.putExtra("totalTrainingTime", totalTrainingTime);

        startActivity(intent);
    }

    public void startTraining(View view) {
        Log.i("aaa", "7 startTraining");
        toggleButtonVisibility();

        soundPlayer.start();
        if (!paused) {//beggining of training
            startTime = System.currentTimeMillis();
            lastTime = 0L;
            timerRunnable = getRunnable();
        } else {//start after pause
            breakTime += System.currentTimeMillis() - pauseTime;
            paused = false;
        }

        timerHandler.postDelayed(timerRunnable, 0);
        if(connected) {
            if (!requestingLocationUpdates) {
                requestingLocationUpdates = true;
                startLocationUpdates();
            }
        }
    }

    public void pauseTraining(View view) {
        Log.i("aaa", "8 pauseTraining");
        paused = true;
        pauseTime = System.currentTimeMillis();
        toggleButtonVisibility();

        timerHandler.removeCallbacks(timerRunnable);
//        tvTime.setText("00:00");
//        totalDistance = 0.0;
        if(connected) {
            if (requestingLocationUpdates) {
                requestingLocationUpdates = false;
                stopLocationUpdates();
                addGpxSegment();
            }
        }
    }
/* --------------------- interfaces methods ---------------------*/
    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("aaa", "9 onConnected");
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
//            updatePaceTextView(distanceDifference, timeDifference);
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
        Log.i("aaa", "10 inConnectionSuspend");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("aaa", "11 onConnectionFailed");
        Toast.makeText(this, "Usługi lokalizacyjne nie działają", Toast.LENGTH_SHORT).show();
        connected = false;
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.i("aaa", "12 onConnectionChanged");
        lastLocation = currentLocation;
        currentLocation = location;
        double distanceDifference = lastLocation.distanceTo(currentLocation);
        totalDistance += distanceDifference;
        totalTrainingDistance += distanceDifference;

        long currentTime = System.currentTimeMillis() - startTime;
        double v;

        double t_s = (currentTime - lastTime)/1000.0;
        if (t_s !=0) {
            v = distanceDifference / t_s;
        } else {
            v = 0.0;
        }
        lastTime = currentTime;

        addPoint(location);

        updateDistanceTextView();
        updatePaceTextView(v);
//        updatePaceTextView(location);
    }

    public void onLocationChangedTest() {
        double distanceDifference = 3.5/2.0;
        totalDistance += distanceDifference;
        totalTrainingDistance += distanceDifference;

        long currentTime = System.currentTimeMillis() - startTime;
        double v;

        double t_s=(currentTime-lastTime)/1000.0;
        if (t_s !=0) {
            v = distanceDifference / t_s;
        } else {
            v = 0.0;
        }
        lastTime = currentTime;

        updateDistanceTextView();
        updatePaceTextView(v);

        addTestPoint();
//        updatePaceTextView(location);
    }
    private void addTestPoint(){
        Random r = new Random();
        GpxPoint point = new GpxPoint(r.nextDouble(),r.nextDouble());
        gpxPoints.add(point);
    }
    //-----------------------------------------------------------
    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i("aaa", "13 updateInstanceState");
        if (savedInstanceState != null) {
            // Update the value of requestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                requestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
//                setButtonsEnabledState();
            }

            // Update the value of currentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that currentLocation
                // is not null.
                currentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            updateDistanceTextView();
//            updatePaceTextView(distanceDifference, timeDifference);
        }
    }

    private void updateDistanceTextView() {
        Log.i("aaa", "14 updateDistanceTextView");
        tvDistance.setText(String.format("%.0f m", totalDistance));
    }

    private LinkedList<Double> veloLL = new LinkedList<>();
    private double getVelo(Location location) {
        double velocity = location.getSpeed();
        veloLL.add(velocity);
        if (veloLL.size() == 11) {
            veloLL.pollFirst();
        }
        double sum = 0.0;
        for(double v: veloLL) {
            sum += v;
        }

        return sum/(double)veloLL.size();
    }

    private void updatePaceTextView(Location location) {//v=s/t
        Log.i("aaa", "updatePaceTextView");
        if(location.hasSpeed()) {

            double velocity = getVelo(location);

            velocity = 1.0 / velocity * 1000.0;

            int minutes = (int) (velocity / 60);
            int seconds = (int) (velocity % 60);
            tvPace.setText(String.format("%d'%d\"/km", minutes, seconds));
        }
    }

    private double getVelo(double v1) {//in meters/seconds
        double velocity;
        if (v1 < 0.1) {
            velocity = 0;
        } else if (v1 < 0.83) {//0.83 it is about 3 km/h
            velocity = 0.83;
        } else if (v1 > 11.11) { // 11.11 it is about 100m in 9s
            velocity = 11.11;
        } else {
            velocity = v1;
        }
        int valuesToAvg = 7;
        veloLL.addLast(velocity);
        if (veloLL.size() == valuesToAvg) {
            veloLL.removeFirst();
        }

        //odrzuć dwie skrajne wartości nie licząc najnowszej
        LinkedList<Double> copyVeloLL = new LinkedList<>();//skopiuj wartości
        for(double d: veloLL) {
            copyVeloLL.add(d);
        }
        double min=12.0, max=0.0;
        int indexMin=-1, indexMax=-1, i = 0;
        for(double d: copyVeloLL) {//znajdź min i max
            if (i < copyVeloLL.size() - 1) {
                if (d < min) {
                    min = d;
                    indexMin = i;
                }
                if (d > max) {
                    max = d;
                    indexMax = i;
                }
                ++i;
            } else {
                break;
            }
        }
        //usuń min i max
        if (copyVeloLL.size() >= valuesToAvg) {
            System.out.println(indexMin + " " + indexMax);
            copyVeloLL.remove(indexMin);
            copyVeloLL.remove(indexMax);
        }

        double sum = 0.0;
        double weights = 0;
        double actualWeight = 1;
        for(double v: copyVeloLL) {//suma ważona. najnowsze są ważniejsze
            sum += v * actualWeight;
            weights += actualWeight;
            actualWeight += 0.1;
        }

        System.out.println("v1 "+v1);
        System.out.println("veloLL size "+veloLL.size());
        System.out.println("suma wazona "+sum/weights);
        return sum/weights;
    }

    private void updatePaceTextView(double v) {//v=s/t [m/s]
        Log.i("aaa", "updatePaceTextView");
        double velocity = getVelo(v);
        tvPace.setText(DateFormatter.minPerKm(velocity));
    }

    private synchronized void buildGoogleApiClient() {
        Log.i("aaa", "15 buildGoogleApiClient");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private void createLocationRequest() {
        Log.i("aaa", "16 createLocationRequest");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        Log.i("aaa", "17 startLocationUpdates");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    private void stopLocationUpdates() {
        Log.i("aaa", "18 stopLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    private void addPoint(Location location) {
        Log.i("aaa", "19 addPoint");
        GpxPoint point = new GpxPoint(location.getLatitude(), location.getLongitude());
        gpxPoints.add(point);
    }
    /*--------------------- other methods ---------------------*/
    private void toggleButtonVisibility() {
        Log.i("aaa", "20 toogleButtonVisibility");
        if (btnStart.getVisibility() == View.VISIBLE) {
            btnStart.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
        } else {
            btnStart.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);
        }
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

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("aaa", "21 onSaveInstanceState");
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, currentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    private Runnable getRunnable() {
        Log.i("aaa", "22 getRunnable");
        isBreakTime = false;
//        return new Runnable();
        switch (actualPart.getType()) {
            case 1:
                numberOfRep = 1;
                return new Runnable() {
                    @Override
                    public void run() {
                        trainingTypeOne(this);
                    }
                };
            case 2:
                numberOfRep = 1;
                actualTrainingPartText = actualPart.getDescription();
                tvCurrentLine.setText(actualTrainingPartText + "\npowtórzenie nr " + numberOfRep + "-bieg");
                return new Runnable() {
                    @Override
                    public void run() {
                        trainingTypeTwo(this);
                    }
                };
            case 3:
                numberOfRep = 1;
                actualTrainingPartText = actualPart.getDescription();
////                totalTrainingDistance += totalDistance;
////                totalDistance = 0.0;
                tvCurrentLine.setText(actualTrainingPartText+"\npowtórzenie nr "+numberOfRep+"-bieg");
                return new Runnable() {
                    @Override
                    public void run() {
                        trainingTypeThree(this);
                    }
                };
            case 4:
                numberOfRep = 1;
                return new Runnable() {
                    @Override
                    public void run() {
                        trainingTypeFour(this);
                    }
                };
            case 5:
                actualTrainingPartText = actualPart.getDescription();
//                totalTrainingDistance += totalDistance;
                totalDistance = 0.0;
                tvCurrentLine.setText(actualTrainingPartText+"\npowtórzenie nr "+numberOfRep);
                return new Runnable() {
                    @Override
                    public void run() {
                        trainingTypeFive(this);
                    }
                };
        }
        return null;
    }

    /**
     * WB X km, Y min
     * @param runnable
     */
    private void trainingTypeOne(Runnable runnable) {
        onLocationChangedTest();
//        Log.i("aaa", "23 trainingTypeOne");
        long millis = getMillis();
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        tvTime.setText(String.format("%02d:%02d", minutes, seconds));

        if (minutes*60 + seconds >= actualPart.getTime()) {
            ++actualPartNumber;
            addGpxSegment();
            addGpxTrack();
            if(actualPartNumber < parts.size()) { // kolejna część treningu
                removeCallback();
                veloLL = new LinkedList<Double>();
                setNextTrainingData(minutes, seconds);
                updateDistanceTextView();
                toggleButtonVisibility();
                tvTime.setText(String.format("%02d:%02d", 0, 0));
            } else { //koniec treningu
//                totalTrainingDistance += totalDistance;
//                totalTrainingTime += minutes*60 + seconds;
                breakTime = 0L;
                totalDistance = 0;
                setEndOfTraining();
            }
        } else {
            totalTrainingTime += 1;
            timerHandler.postDelayed(runnable, 500);
        }
    }

    private boolean isBreakTime;
    private String actualTrainingPartText;
    /**
     * Z.B. X razy, Y min, Z przerwa
     * @param runnable
     */
    private void trainingTypeTwo(Runnable runnable) {
        onLocationChangedTest();
        Log.i("aaa", "24 TrainingTypeTwo");
        String t;
        long millis = getMillis();
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        if (numberOfRep <= actualPart.getTimes()) {
            long time = isBreakTime ? actualPart.getBreakTime() : actualPart.getTime();
            if (minutes * 60 + seconds >= time) {
                addGpxSegment();
                tvTime.setText(String.format("%02d:%02d", 0, 0));
                isBreakTime = !isBreakTime;//isBreakTime odnosi się do części treningu zwanej "przerwą" czyli lekkiego biegu
                breakTime = 0L;//breakTime jest to czas jaki upłynął po wciśnięciu przycisku pause
                soundPlayer.start();
                if(isBreakTime){
                    t = "-przerwa";
                } else {
                    ++numberOfRep;
                    t = "-bieg";
                }
//                totalTrainingDistance += totalDistance;
//                totalTrainingTime += minutes*60 + seconds;
                totalDistance = 0.0;
                updateDistanceTextView();

                tvCurrentLine.setText(actualTrainingPartText + "\npowtórzenie nr " + numberOfRep + t);

                startTime = System.currentTimeMillis();
            } else {
                tvTime.setText(String.format("%02d:%02d", minutes, seconds));
            }
            totalTrainingTime += 1;
            timerHandler.postDelayed(runnable, 500);
        } else {//koniec treningu
            tvTime.setText(String.format("%02d:%02d", 0, 0));
            ++actualPartNumber;
            addGpxTrack();
            if (actualPartNumber < parts.size()) { // kolejna część treningu
                removeCallback();
                veloLL = new LinkedList<>();
                setNextTrainingData(minutes, seconds);
                updateDistanceTextView();
                toggleButtonVisibility();
            } else {
//                totalTrainingDistance += totalDistance;
//                totalTrainingTime += minutes*60 + seconds;
                totalDistance = 0.0;
                setEndOfTraining();
            }
        }
    }

    /**
     * odcinki X razy, Y metrów, Z przerwa
     * @param runnable
     */
    private void trainingTypeThree(Runnable runnable) {
        onLocationChangedTest();
        Log.i("aaa", "25 trainingTypeThree");
        //do testów
//        totalDistance += 0.27;
        updateDistanceTextView();
        //-----------------------
        String t;
        long millis = getMillis();
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        tvTime.setText(String.format("%02d:%02d", minutes, seconds));

        if (numberOfRep <= actualPart.getTimes()) {
            if (!isBreakTime) {//bieg odcinek
                if (totalDistance >= actualPart.getDistance()) {
                    addGpxSegment();
                    tvTime.setText(String.format("%02d:%02d", 0, 0));

                    isBreakTime = !isBreakTime;
                    breakTime = 0L;//breakTime jest to czas jaki upłynął po wciśnięciu przycisku pause
                    soundPlayer.start();
//                    totalTrainingDistance += totalDistance;
//                    totalTrainingTime += minutes*60 + seconds;
                    totalDistance = 0.0;
                    updateDistanceTextView();
                    tvCurrentLine.setText(actualTrainingPartText+"\npowtórzenie nr "+numberOfRep+"-przerwa");
                    startTime = System.currentTimeMillis();
                }
            } else {
                if (minutes * 60 + seconds >= actualPart.getBreakTime()) {//trucht przerwa
                    addGpxSegment();
                    ++numberOfRep;
                    tvTime.setText(String.format("%02d:%02d", 0, 0));
                    isBreakTime = !isBreakTime;
                    breakTime = 0L;//breakTime jest to czas jaki upłynął po wciśnięciu przycisku pause
                    soundPlayer.start();
//                    totalTrainingDistance += totalDistance;
//                    totalTrainingTime += minutes*60 + seconds;
                    totalDistance = 0.0;
                    updateDistanceTextView();
                    tvCurrentLine.setText(actualTrainingPartText+"\npowtórzenie nr "+numberOfRep+"-bieg");
                    startTime = System.currentTimeMillis();
                }
            }
            totalTrainingTime += 1;
            timerHandler.postDelayed(runnable, 500);
        } else {//koniec treningu
            tvTime.setText(String.format("%02d:%02d", 0, 0));
            ++actualPartNumber;
            addGpxTrack();
            if (actualPartNumber < parts.size()) { // kolejna część treningu
                removeCallback();
                veloLL = new LinkedList<>();
                /**zamiast setNextTrainingData*/
//                setNextTrainingData(minutes, seconds);
                Log.i("aaa", "29 setNextTrainingData");
                actualPart = parts.get(actualPartNumber);
                tvCurrentLine.setText(actualPart.getDescription());
                soundPlayer.start();
                breakTime = 0L;
                /***************************/

                updateDistanceTextView();
                toggleButtonVisibility();
            } else {
//                totalTrainingDistance += totalDistance;
//                totalTrainingTime += minutes*60 + seconds;
//                totalDistance = 0.0;
                setEndOfTraining();
            }
        }
    }

    /**
     * ćw X czasu
     * @param runnable
     */
    private void trainingTypeFour(Runnable runnable) {
        onLocationChangedTest();
        Log.i("aaa", "26 TrainingTypeFour");
        long millis = getMillis();
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        tvTime.setText(String.format("%02d:%02d", minutes, seconds));

        if (minutes*60 + seconds >= actualPart.getTime()) {
            ++actualPartNumber;
            addGpxSegment();
            addGpxTrack();
            if(actualPartNumber < parts.size()) { // kolejna część treningu
                removeCallback();
                veloLL = new LinkedList<>();
                setNextTrainingData(minutes, seconds);
                updateDistanceTextView();
                toggleButtonVisibility();
                tvTime.setText(String.format("%02d:%02d", 0, 0));
            } else { //koniec treningu
                tvTime.setText(String.format("%02d:%02d", 0, 0));
//                totalTrainingDistance += totalDistance;
//                totalTrainingTime += minutes*60 + seconds;
                breakTime = 0L;
                totalDistance = 0;
                setEndOfTraining();
            }
        } else {
            totalTrainingTime += 1;
            timerHandler.postDelayed(runnable, 500);
        }
    }

    /**
     * skipy X razy, Y metrów
     * @param runnable
     */
    private void trainingTypeFive(Runnable runnable) {
        onLocationChangedTest();
        Log.i("aaa", "27 TrainingTypeFive");
        updateDistanceTextView();
        //-----------------------
        String t;
        long millis = getMillis();
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;

        tvTime.setText(String.format("%02d:%02d", minutes, seconds));
        if (totalDistance >= actualPart.getDistance()) {
            addGpxSegment();
            if (numberOfRep < actualPart.getTimes()) {
                tvTime.setText(String.format("%02d:%02d", 0, 0));
                breakTime = 0L;//breakTime jest to czas jaki upłynął po wciśnięciu przycisku pause
                soundPlayer.start();
                ++numberOfRep;

//                totalTrainingDistance += totalDistance;
//                totalTrainingTime += minutes*60 + seconds;
                totalDistance = 0.0;
                updateDistanceTextView();
                tvCurrentLine.setText(actualTrainingPartText + "\npowtórzenie nr " + numberOfRep);
                startTime = System.currentTimeMillis();
                toggleButtonVisibility();
            } else {//koniec treningu
                soundPlayer.start();
                tvTime.setText(String.format("%02d:%02d", 0, 0));
                ++actualPartNumber;
                breakTime = 0L;//breakTime jest to czas jaki upłynął po wciśnięciu przycisku pause
                addGpxTrack();
                if (actualPartNumber < parts.size()) { // kolejna część treningu
                    numberOfRep = 1;
                    removeCallback();
                    veloLL = new LinkedList<>();
                    setNextTrainingData(minutes, seconds);
                    updateDistanceTextView();
                    toggleButtonVisibility();
                } else {
//                    totalTrainingDistance += totalDistance;
//                    totalTrainingTime += minutes*60 + seconds;
                    totalDistance = 0.0;
                    setEndOfTraining();
                }
            }
        } else {
            totalTrainingTime += 1;
            timerHandler.postDelayed(runnable, 500);
        }
    }

    private void setEndOfTraining() {
        removeCallback();
        Log.i("aaa", "28 setEndOfTraining");
        tvCurrentLine.setText("Koniec treningu");
        btnPause.setVisibility(View.GONE);
        soundPlayer.start();
    }

    private void setNextTrainingData(int minutes, int seconds) {
        Log.i("aaa", "29 setNextTrainingData");
        actualPart = parts.get(actualPartNumber);
        tvCurrentLine.setText(actualPart.getDescription());
        soundPlayer.start();
//        totalTrainingDistance += totalDistance;
//        totalTrainingTime += minutes*60 + seconds;
        breakTime = 0L;
        totalDistance = 0;
    }

    private void addGpxTrack() {
        Log.i("aaa", "30 addGpxTrack");
        List<GpxSegment> cSegments = new LinkedList<>();
        for(GpxSegment gs: gpxSegments) {
            cSegments.add(gs.clone());
        }
        GpxTrack gpxTrack = new GpxTrack(parts.get(actualPartNumber-1).getDescription(), cSegments);
        gpxTracks.add(gpxTrack);
        gpxSegments = new LinkedList<>();
    }

    private void addGpxSegment() {
        List<GpxPoint> cPoints = new LinkedList<>();
        for(GpxPoint gp: gpxPoints) {
            cPoints.add(gp.clone());
        }
        GpxSegment gpxSegment = new GpxSegment(cPoints);
        gpxSegments.add(gpxSegment);
        gpxPoints = new LinkedList<>();
    }

    private void setGpxTrackToTraining() {
        Log.i("aaa", "31 setGpxTrackToTraining");
        Plan plan = Singleton.getInstance().getUser().getPlan();
        Training training = plan.getTraining(plan.getActualTrainingNumber());
        training.setTracks(gpxTracks);
    }

    private long getMillis() {
//        Log.i("aaa", "32 getMillis");
        return System.currentTimeMillis() - startTime - breakTime;
    }

    private void removeCallback() {
        timerHandler.removeCallbacks(timerRunnable);
        if(connected) {
            if (requestingLocationUpdates) {
                requestingLocationUpdates = false;
                stopLocationUpdates();
            }
        }
    }
}
package com.example.jakub.osobistytrenerbiegacza;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jakub.osobistytrenerbiegacza.model.Plan;
import com.example.jakub.osobistytrenerbiegacza.model.Summary;
import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.model.User;
import com.example.jakub.osobistytrenerbiegacza.utils.DateFormatter;
import com.example.jakub.osobistytrenerbiegacza.utils.FileOperations;
import com.example.jakub.osobistytrenerbiegacza.utils.Strings;
import com.example.jakub.osobistytrenerbiegacza.utils.Toasts;

import java.util.ArrayList;
import java.util.Date;


public class TrainingSummaryActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 1;
    private TextView tvTime;
    private TextView tvDistance;
    private TextView tvPace;

    private long totalTrainingTime;
    private double avgTrainingVelocity;
    private double totalTrainingDistance;
    private Date trainingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_summary);

        getTrainingData();
        getTextViews();
        updateTextViews();
    }

    /**
     * OnCLick method of activity_training_summary_btn_done
     * @param view
     */
    public void done(View view) {
        saveData();
        Intent intent = new Intent(this, MainActivity.class)
            .putExtra("caller", "TrainingSummaryActivity");
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //---------------speech recognizer--------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public void recognizeSpeech(View view) {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_PROMPT, Strings.SAY_TO_DENOTE);

        try {
            startActivityForResult(recognizerIntent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toasts.recognizeSpeechNotSuported(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            setNotesEditText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
        }
    }

    //----------------------------------------------------------------------------------------------
    //---------------private methods----------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    private void getTextViews() {
        tvDistance = (TextView) findViewById(R.id.activity_training_summary_tv_distance);
        tvPace = (TextView) findViewById(R.id.activity_training_summary_tv_velocity);
        tvTime = (TextView) findViewById(R.id.activity_training_summary_tv_time);
    }

    private void getTrainingData() {
        Intent intent = getIntent();
        totalTrainingDistance = intent.getDoubleExtra("totalTrainingDistance", 0);
        totalTrainingTime = intent.getLongExtra("totalTrainingTime", 0L);
        avgTrainingVelocity = intent.getDoubleExtra("averageTrainingVelocity",0);
        trainingDate = new Date();
    }

    private void updateTextViews() {
        tvDistance.setText(String.format("%.0f m", totalTrainingDistance));
//        tvPace.setText(String.format("%d'%d\"/km", 0, 0));
        tvPace.setText(DateFormatter.minPerKm(avgTrainingVelocity));
        long minutes = totalTrainingTime / 60;
        long seconds = totalTrainingTime % 60;
        tvTime.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void saveData() {
        EditText notes = (EditText) findViewById(R.id.activity_training_summary_et_notes);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.activity_training_summary_ratingBar);
        Summary summary = new Summary(trainingDate,
                notes.getText().toString(),
                (int)ratingBar.getRating(),
                totalTrainingDistance,
                (int)totalTrainingTime,
                avgTrainingVelocity);

        User user = Singleton.getInstance().getUser();
        Plan plan = user.getPlan();
        Training t = plan.getTraining(plan.getActualTrainingNumber());
        t.setSummary(summary);
        Training tClone = t.clone();
        user.addToHistory(tClone);

        FileOperations.writeObject(Singleton.getInstance(), this);
    }

    private void setNotesEditText(ArrayList<String> results) {
        EditText notes = (EditText) findViewById(R.id.activity_training_summary_et_notes);
        notes.setText(results.get(0));
    }
}
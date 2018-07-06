package com.example.jakub.osobistytrenerbiegacza;

import java.io.File;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jakub.osobistytrenerbiegacza.model.Plan;
import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.model.TrainingType;
import com.example.jakub.osobistytrenerbiegacza.utils.FileOperations;
import com.example.jakub.osobistytrenerbiegacza.utils.GpxWriter;
import com.example.jakub.osobistytrenerbiegacza.utils.Toasts;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, PlanFragment.OnFragmentInteractionListener {
    private static final int MAIL_REQUEST_CODE = 1;
    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    TodayFragment todayFragment;

    private static boolean firstOpened = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("Main onCreatae!! "+firstOpened);
        if (firstOpened) {
            Singleton s = FileOperations.readObject(this);
            setSingleton(s);
            firstOpened = false;
        }


        Intent intent = getIntent();
        if (intent != null && "TrainingSummaryActivity".equals(intent.getStringExtra("caller"))) {
            updateHistory();
            updatePlan();
        }

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        addTabs(actionBar);

        todayFragment = TodayFragment.newInstance(2);
    }

    private void setSingleton(Singleton s) {
        Singleton singleton = Singleton.getInstance();
        if (s != null) {
            singleton.setUser(s.getUser());
            singleton.setAge(s.getAge());
            singleton.setSex(s.getSex());
            singleton.setStaminaLevel(s.getStaminaLevel());
        } else {

        }
    }

    private void updateHistory() {

    }

    private void updatePlan() {
//        Plan plan = Singleton.getInstance().getUser().getPlan();
//        int actualTrainingNumber = plan.getActualTrainingNumber();
//        if(actualTrainingNumber < plan.getNumberOfTrainings()) {
//            plan.setActualTrainingNumber(plan.getActualTrainingNumber() + 1);
//            //TODO akutalizacja listy z planem treningu
//        }
    }

    /**
     * Tworzy i dodaje zakładki do aktywności.
     * @param actionBar
     */
    private void addTabs(ActionBar actionBar) {
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Historia".toUpperCase(Locale.getDefault()))
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Dziś".toUpperCase(Locale.getDefault()))
                        .setTabListener(this));

        actionBar.addTab(
                actionBar.newTab()
                        .setText("Plan".toUpperCase(Locale.getDefault()))
                        .setTabListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_new_plan:
                Intent intent = new Intent(this, NewPlanActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_clear_data:
                createConfirmationDialog();
                return true;
            case R.id.action_export_data:
                exportTrainingHistory();
                return true;
            case R.id.action_export_plan:
                exportPlan();
                return true;
            case R.id.action_help:
                Intent intentHelp = new Intent(this, HelpActivity.class);
                startActivity(intentHelp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void createConfirmationDialog() {
        Log.i("menu", "createConfirmationDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_delete_message)
                .setTitle(R.string.dialog_delete_title);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                clearUserData();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i("menu", "Nie");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearUserData() {
        Singleton.getInstance().clearData();
        FileOperations.writeObject(Singleton.getInstance(),this);
        this.recreate();
//        int visibility = Singleton.getInstance().getUser().getHistory().size() != 0 ? View.GONE : View.VISIBLE;
//        TextView tvNoHistory = (TextView) view.findViewById(R.id.fragment_history_list_tv_no_history);
//        tvNoHistory.setVisibility(visibility);
    }

    private void exportTrainingHistory(){
        List<Training> history = Singleton.getInstance().getUser().getHistory();
        if (history.size() > 0) {
            GpxWriter.exportToZip(this, history);
            sendEmail();
        } else {
            Toasts.noHistory(this);
        }

    }

    private String filenameee;
    private void exportPlan() {
//        http://stackoverflow.com/questions/18548255/android-send-xml-file-doesnt-send-attachment/18548685#18548685
        Plan p = Singleton.getInstance().getUser().getPlan();
        if (p.getNumberOfTrainings() > 0) {
            FileOperations.exportPlan(this, p);
            filenameee = "plan" + p.getTargetDistance() + "km";
//        File f = FileOperations.copyFileToExternal(this,filenameee);
            sendEmail2();
        } else {
            Toasts.noPlan(this);
        }
    }


    //------------------------------------------
//    private void sendEmail3(String email) {
//
//        File file = new File(Environment.getExternalStorageState()+"/folderName/" + filenameee+ ".txt");
//        Uri path = Uri.fromFile(file);
//
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "plan");
//        String to[] = { email };
//        intent.putExtra(Intent.EXTRA_EMAIL, to);
//        intent.putExtra(Intent.EXTRA_TEXT, "Wygenerowano plan treningowy");
//        intent.putExtra(Intent.EXTRA_STREAM, path);
//        startActivityForResult(Intent.createChooser(intent, "Send mail..."),1222);
//    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1222) {
//            File file = new File(Environment.getExternalStorageState()+"/folderName/" + filenameee+ ".txt");
//            file.delete();
//        }
//    }
    //------------------------------------------

    private void sendEmail2(){
        File file = getExternalCacheDir();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String to[] = {"jakub@chalupczak.com"};
        sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Wygenerowano plan treningowy");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "plan treningów");
        Uri uri = null;
        if(file.isDirectory()) {
            File[] files = file.listFiles();

            for(File f:files) {
//                f.delete();
                uri = Uri.fromFile(f);
            }
        }

        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "tytul wiadomosci"));
    }

    private void sendEmail(){
        File file = getExternalCacheDir();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String to[] = {"jakub@chalupczak.com"};
        sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Wygenerowano historię treningów");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "historia treningów");
        Uri uri = null;
        if(file.isDirectory()) {
            File[] files = file.listFiles();

            for(File f:files) {
//                f.delete();
                uri = Uri.fromFile(f);
            }
        }

        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.setType("application/zip");
        startActivity(Intent.createChooser(sendIntent, "tytul wiadomosci"));
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void startTraining(View view) {
        Plan plan = Singleton.getInstance().getUser().getPlan();
        List<TrainingType> tt = plan.getTraining(plan.getActualTrainingNumber()).getParts();
        if (tt.get(0).getType() != 6) {
//            TextView todayTrainingText = (TextView) findViewById(R.id.todayTrainingText);
//            if (!todayTrainingText.equals(getResources().getString(R.string.fragment_today_training_text))) {
                Intent intent = new Intent(this, TrainingActivity.class);
                startActivity(intent);
//            } else {
//                Toast.makeText(this, "Brak ustawionego treningu", Toast.LENGTH_SHORT).show();
//            }
        } else {
            Toast.makeText(this, "Dzisiaj przerwa w treningu", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Czy to jest naprawdę potrzebne? Może lepiej to zrobić w PlanFragment?
     * @param position training number in plan (begin from 0)
     */
    @Override
    public void onFragmentInteraction(int position) {
        Plan plan = Singleton.getInstance().getUser().getPlan();
        int id = plan.getTrainings().get(position).getTrainingNumber();
        plan.setActualTrainingNumber(id);
//        View viewById = todayFragment.getActivity().findViewById(android.R.id.todayTrainingText);
        TextView todayTrainingText = (TextView) findViewById(R.id.todayTrainingText);

        String text = "";
        for(TrainingType tt: plan.getTraining(plan.getActualTrainingNumber()).getParts()) {
            text += tt.getDescription()+"\n";
        }
        todayTrainingText.setText(text);

        Button btn = (Button) findViewById(R.id.todayStartTrainingButton);
        btn.setEnabled(true);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return HistoryFragment.newInstance(position + 1);
            } else if (position == 1) {
                return todayFragment;
            } else if (position == 2) {
                return PlanFragment.newInstance(position + 1);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
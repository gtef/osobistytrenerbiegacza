package com.example.jakub.osobistytrenerbiegacza;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HelpActivity extends ActionBarActivity {

    TextView help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        help = (TextView) findViewById(R.id.activity_help_tv_help);
        help.setText(getText());
    }
    private String getText() {
        return "Lekki bieg - luźny, swobodny bieg nieprzekraczający 80% maksymalnego tętna\n"+
                "\nZabawa biegowa - Naprzemienny wolny i szybki bieg. Szybki bieg w tempie sprintu, wolny bieg truchtem";
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_help, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

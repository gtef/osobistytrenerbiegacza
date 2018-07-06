package com.example.jakub.osobistytrenerbiegacza.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.example.jakub.osobistytrenerbiegacza.MainActivity;
import com.example.jakub.osobistytrenerbiegacza.TrainingSummaryActivity;

/**
 * Created by Jakub on 2016-03-03.
 */
public class Toasts {
    public static void recognizeSpeechNotSuported(Context context) {
        Toast.makeText(context, "Usługa rozpoznawania mowy nie jest wspierana", Toast.LENGTH_SHORT).show();
    }

    public static void fileNotFound(Context context) {
        Toast.makeText(context,"Nie znaleziono pliku",Toast.LENGTH_SHORT).show();
    }

    public static void errorWhileSavingToZip(Context context) {
        Toast.makeText(context,"Błąd zapisu do ZIP",Toast.LENGTH_SHORT).show();
    }

    public static void localizationsServicesDoesntWork(Context context) {
        Toast.makeText(context, "Usługi lokalizacyjne nie działają",Toast.LENGTH_SHORT).show();
    }

    public static void wrongNumberFormat(Context context) {
        Toast.makeText(context, "Podany ciąg znaków nie jest liczbą",Toast.LENGTH_SHORT).show();
    }

    public static void ageBeyondRange(Context context) {
        Toast.makeText(context, "Wiek poza zakresem 1-150 lat",Toast.LENGTH_SHORT).show();
    }

    public static void noSexIsChosen(Context context) {
        Toast.makeText(context, "Nie wybrano płci",Toast.LENGTH_SHORT).show();
    }

    public static void noHistory(Context context) {
        Toast.makeText(context, "Brak historii",Toast.LENGTH_SHORT).show();
    }

    public static void noPlan(Context context) {
        Toast.makeText(context, "Brak ustawionego planu",Toast.LENGTH_SHORT).show();
    }
}

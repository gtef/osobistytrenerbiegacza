package com.example.jakub.osobistytrenerbiegacza.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.example.jakub.osobistytrenerbiegacza.Singleton;
import com.example.jakub.osobistytrenerbiegacza.model.Plan;
import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.model.TrainingType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jakub on 2015-11-08.
 */
public class FileOperations {

//    public static <T> void save(String filename, T object) throws IOException {
//        FileOutputStream fos = new FileOutputStream(filename);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(object);
//    }
//
//    public static <T> T read(String filename) {
//        ObjectInputStream objectinputstream = null;
//            streamIn = new FileInputStream(filename);
//            objectinputstream = new ObjectInputStream(streamIn);
//            MyClass readCase = null;
//            do{
//                readCase = (MyClass) objectinputstream.readObject();
//
//        return new T();
//    }

    public static void deleteFiles(Context context) {
        File[] files = context.getExternalCacheDir().listFiles();
        for(File f: files) {
            f.delete();
        }
    }

    private static void saveToFile(String filename, String text, Context context) {
        FileOutputStream outputStream;
        try {
            File fi = context.getExternalCacheDir();
            File fil =  File.createTempFile(filename,".txt",fi);
            outputStream = new FileOutputStream(fil);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (IOException e) {
            Toast.makeText(context, "Błąd zapisu", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private static String createString(Plan plan) {
        Date startDate = plan.getStartDate();
        StringBuilder text = new StringBuilder();
        int i = 0;
        for(Training t: plan.getTrainings()) {
            Date date = DateFormatter.addDays(startDate, i);
            SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy");
            text.append(dt1.format(date)+"\n");
            for(TrainingType tt: t.getParts()) {
                text.append(tt.getDescription()+"\n");
            }
            text.append("\n");
            i++;
        }
        return new String(text);
    }

    public static void exportPlan(Context context, Plan plan) {
        deleteFiles(context);
        String t = createString(plan);
        saveToFile("plan" + plan.getTargetDistance() + "km", t, context);

    }

    public static void writeObject(Singleton singleton, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput("user_data.data" , Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(singleton);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Singleton readObject(Context context) {
        Singleton singleton = null;
        try {
            FileInputStream fis = context.openFileInput("user_data.data");
            ObjectInputStream ois = new ObjectInputStream(fis);
            singleton = (Singleton) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return singleton;
    }
}

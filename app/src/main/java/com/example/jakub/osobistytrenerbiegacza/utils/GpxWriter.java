package com.example.jakub.osobistytrenerbiegacza.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.jakub.osobistytrenerbiegacza.model.GpxPoint;
import com.example.jakub.osobistytrenerbiegacza.model.GpxSegment;
import com.example.jakub.osobistytrenerbiegacza.model.GpxTrack;
import com.example.jakub.osobistytrenerbiegacza.model.Summary;
import com.example.jakub.osobistytrenerbiegacza.model.Training;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Jakub on 2016-03-07.
 * GPX documentation http://www.topografix.com/gpx.asp
 * http://utrack.crempa.net/
 */
public class GpxWriter {
    private static final String XML_TAG = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final String GPX_START_TAG =    "<gpx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
            "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n" +
            "    version=\"1.1\" \n" +
            "    creator=\"Osobisty trener biegacza\" \n" +
            "    xmlns=\"http://www.topografix.com/GPX/1/1\">";
    private static final String GPX_END_TAG = "</gpx>";
    private static final String METADATA_START_TAG = "<metadata>";
    private static final String METADATA_END_TAG = "</metadata>";
    private static final String TRK_START_TAG = "<trk>";
    private static final String TRK_END_TAG = "</trk>";
    private static final String NAME_START_TAG = "<name>";
    private static final String NAME_END_TAG = "</name>";
    private static final String TRKSEG_START_TAG = "<trkseg>";
    private static final String TRKSEG_END_TAG = "</trkseg>";
    private static final String TRKPT_END_TAG = "</trkpt>";
    private static final int MAIL_REQUEST = 1;

    private static String getTrkStartTag(double latitude, double longitude) {
        return "<trkpt lat=\""+latitude+"\" lon=\""+longitude+"\">";
    }

    private static void write(String filename, Training training, Context context){
        Summary summary = training.getSummary();
        List<GpxTrack> tracks = training.getTracks();
        StringBuilder gpxText = new StringBuilder();
        gpxText.append(XML_TAG+GPX_START_TAG);
        //add metadata
        gpxText.append(METADATA_START_TAG+NAME_START_TAG);
        gpxText.append(DateFormatter.toString(summary.getDate(), ""));
        gpxText.append(NAME_END_TAG+METADATA_END_TAG);
        //add trk - training part
        for(GpxTrack track: tracks) {
            gpxText.append(TRK_START_TAG);
            gpxText.append(NAME_START_TAG);
            gpxText.append(track.getName());
            gpxText.append(NAME_END_TAG);
            for(GpxSegment segment: track.getSegments()) {
                gpxText.append(TRKSEG_START_TAG);
                for(GpxPoint point: segment.getPoints()){
                    gpxText.append(getTrkStartTag(point.getLatitude(), point.getLongitude()));
                    gpxText.append(TRKPT_END_TAG);
                }
                gpxText.append(TRKSEG_END_TAG);
            }
            gpxText.append(TRK_END_TAG);
        }
        gpxText.append(GPX_END_TAG);
        saveToFile(filename+"---", gpxText, context);
    }

    public static void deleteFiles(Context context) {
        File[] files = context.getExternalCacheDir().listFiles();
        for(File f: files) {
            f.delete();
        }
    }

    private static void saveToFile(String filename, StringBuilder gpxText, Context context) {
        FileOutputStream outputStream;
        try {
            File fi = context.getExternalCacheDir();
            File fil =  File.createTempFile(filename,".gpx",fi);
            outputStream = new FileOutputStream(fil);
            outputStream.write(gpxText.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            Toast.makeText(context,"Błąd zapisu",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void exportToZip(Context context, List<Training> history) {
        deleteFiles(context);
        saveFiles(context, history);
        saveToZip(context);
    }

    private static void saveToZip(Context context) {
        try {
            File fi = context.getExternalCacheDir();
            File fil =  File.createTempFile("historiaTreningow",".zip",fi);
            FileOutputStream outputStream = new FileOutputStream(fil);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(outputStream));
            try {
                File[] files = context.getExternalCacheDir().listFiles();
                for (int i = 0; i < files.length-1; ++i) {
                    File f = files[i];
                    FileInputStream inputStream = new FileInputStream(f);
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    ZipEntry entry = new ZipEntry(f.getName().replaceAll("---.*",".gpx"));
                    zos.putNextEntry(entry);
                    int b;
                    while ((b = bis.read()) != -1) {
                        zos.write(b);
                    }
                    zos.closeEntry();
                }
            } finally {
                zos.close();
            }
        } catch (FileNotFoundException e) {
            Toasts.fileNotFound(context);
            e.printStackTrace();
        } catch (IOException e) {
            Toasts.errorWhileSavingToZip(context);
            e.printStackTrace();
        }
    }

    private static void saveFiles(Context context, List<Training> history) {
        int i = 1;
        StringBuilder summaryText = new StringBuilder();
        for(Training t: history) {
            summaryText.append(createString(t.getSummary(), t.getTrainingDescription()));
        }
        saveToFile("Podsumowania_treningów", summaryText.toString(), context);
        for(Training t: history) {
            write("trening" + i++ + "_" + DateFormatter.toString(t.getSummary().getDate(), "dd-MM-yyyy"), t, context);
        }
    }

    /**
     * Zapis do pliku. Używany aby zapisać podsumowania treningów
     * @param filename
     * @param text
     * @param context
     */
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

    private static String createString(Summary summary, String trainingDescription) {
        Date date = summary.getDate();
        StringBuilder text = new StringBuilder();
        SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy");
        text.append(dt1.format(date)+"\n");
        text.append("Trening:\n"+trainingDescription+"\n");
        text.append("Czas treningu: "+DateFormatter.time(summary.getTrainingTime())+"\n");
        text.append("Całkowity dystans: "+getDistance(summary.getDistanceCovered())+"\n");
        text.append("Średnia prędkość: "+DateFormatter.minPerKm(summary.getAverageVelocity())+"\n");
        text.append("Notatki: "+summary.getNotes()+"\n");
        text.append("Ocena treningu: "+summary.getComfort()+"\n");
        text.append("\n");
        return new String(text);
    }

    private static String getDistance(double distance){
        String ret;
        if (distance < 1000) {
            ret = (int)distance + " m";
        } else {
            int km = (int)distance / 1000;
            int m  = (int)distance % 1000;
            ret = km + " km " + m + " m";
        }
        return ret;
    }

    private static final String testString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<gpx xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" version=\"1.1\" creator=\"Osobisty trener biegacza\" xmlns=\"http://www.topografix.com/GPX/1/1\">\n" +
            "\t<metadata>\n" +
            "\t\t<name>aaa</name>\n" +
            "\t</metadata>\n" +
            "\t<trk>\n" +
            "\t\t<name>Lekki bieg 5s.</name>\n" +
            "\t\t<trkseg>\n" +
            "\t\t\t<trkpt lat=\"51.1148121\" lon=\"16.9518037\"></trkpt>\n" +
            "\t\t\t<trkpt lat=\"51.2148309\" lon=\"16.8517803\"></trkpt>\n" +
            "\t\t\t<trkpt lat=\"51.2448218\" lon=\"16.7517505\"></trkpt>\n" +
            "\t\t\t<trkpt lat=\"51.1348218\" lon=\"16.5317505\"></trkpt>\n" +
            "\t\t</trkseg>\n" +
            "\t</trk>\n" +
            "\t<trk><name>Lekki bieg 8s.</name>\n" +
            "\t\t<trkseg>\n" +
            "\t\t\t<trkpt lat=\"51.1148309\" lon=\"16.9517803\"></trkpt>\n" +
            "\t\t\t<trkpt lat=\"51.1148218\" lon=\"16.9517505\"></trkpt>\n" +
            "\t\t\t<trkpt lat=\"51.1148121\" lon=\"16.9518037\"></trkpt>\n" +
            "\t\t</trkseg>\n" +
            "\t</trk>\n" +
            "\t<trk>\n" +
            "\t\t<name>Lekki bieg 10s.</name>\n" +
            "\t\t<trkseg>\n" +
            "\t\t\t<trkpt lat=\"51.1148218\" lon=\"16.9517505\"></trkpt>\n" +
            "\t\t\t<trkpt lat=\"51.1148309\" lon=\"16.9517803\"></trkpt>\n" +
            "\t\t\t<trkpt lat=\"51.1148121\" lon=\"16.9518037\"></trkpt>\n" +
            "\t\t</trkseg>\n" +
            "\t</trk>\n" +
            "</gpx>\n";
}
package com.newradiolite.jawhrafmlite;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;


public class Content extends AsyncTask<Void, Void, Void> {
    Context c;
    String url="https://www.jawharafm.net/fr/live/56";
    private Bitmap bitmap;
    private String title;
    private String type;
    private String date;
    private String desc;

    public Content(Context c) {
        this.c = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //Connect to the website
            Document document = Jsoup.connect(url).get();

            //Get the logo source of the website
            Element el0 = document.select("img").get(18);
            title= document.getElementsByClass("titr_proglive").get(0).text();
            date= document.getElementsByClass("dat_proglive").get(0).text();
            desc= document.getElementsByClass("parag_proglive").get(0).text();
            type= document.getElementsByClass("etiquet_proglive").get(0).text();
            // Locate the src attribute
            String imgSrc = el0.absUrl("src");
            // Download image from URL
            InputStream input = new java.net.URL(imgSrc).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);

            //Get the title of the website
            String title = document.title();
            Log.i("TestJsoupAB ", " "+title+" " +type+" " +date+" " +desc+" ");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.setProgramNow( bitmap, title,desc,type,date);
    }
}

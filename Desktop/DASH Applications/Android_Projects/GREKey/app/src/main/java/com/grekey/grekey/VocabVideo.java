package com.grekey.grekey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import Adapters.VideoList;
import Adapters.VideoListAdapter;

public class VocabVideo extends ActionBarActivity {

    ArrayList<VideoList> als;
    ListView lv1;
    AlertDialog ad;
    AlertDialog.Builder builder;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__list);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#663399")));
        als=new ArrayList<>();
        lv1=(ListView)findViewById(R.id.list_quiz);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(als.get(position).getLink())));
            }
        });

        String query="https://spreadsheets.google.com/tq?key=1oGrlVFy-4SbcfFcAQGaHCrpSfx9pSzunr30wphE36GQ";
        builder=new AlertDialog.Builder(this);
        builder.setTitle("Network Error");
        builder.setMessage("Please Connect To Internet");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                finish();
            }
        });
        ad = builder.create();
        pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        if(isNetworkConnected())
        {
            pd.show();
            downloadData(query);
        }
        else
        {
            ad.show();
        }
    }

    private void downloadData(String query)
    {

        if (!query.isEmpty())
        {
            new DownloadWebpageTask(new AsyncResult()
            {
                @Override
                public void onResult(JSONObject object)
                {
                    System.out.println("Processing JSON Contacts");
                    processJson(object);
                }
            }).execute(query);
        }
    }
    private void processJson(JSONObject object) {

        try
        {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r)
            {
                System.out.println("In JSON Process "+rows.length());
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String name = columns.getJSONObject(0).getString("v");
                String link = columns.getJSONObject(1).getString("v");
                als.add(new VideoList(name,link));
            }
            Collections.reverse(als);
            VideoListAdapter ad=new VideoListAdapter(als);
            lv1.setAdapter(ad);
            pd.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz__list, menu);
        return true;
    }

    private boolean isNetworkConnected()
    {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}

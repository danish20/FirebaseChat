package com.grekey.grekey;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.widget.LikeView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends ActionBarActivity {


    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    AlertDialog.Builder builder;
    GridView lv;
    String root = Environment.getExternalStorageDirectory().toString();
//    File myDir = new File(root + "/ABC_Images");
    File myDir;
    String fname="Slide";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myDir= new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ABC_Images");
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        lv=(GridView)findViewById(R.id.lv);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        Scrolladapter adpt=new Scrolladapter();
        builder=new AlertDialog.Builder(this);
        lv.setAdapter(adpt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item="Contact Us";
                System.out.println(id+" Danish");
                if(id==12)
                {
                    IntentIntegrator integrator = new IntentIntegrator(Home.this);
                    integrator.initiateScan();
                }
                else if(id==22)
                {
                    startActivity(new Intent(getApplicationContext(),TimeTable.class));
                }
                else if(id==42)
                {
                    startActivity(new Intent(getApplicationContext(),VocabVideo.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(),AboutUs.class));
                    finish();
                }
            }
        });
       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(102,51,153)));

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        pageSwitcher(8);
    }

    @Override
    public void onBackPressed()
    {
        builder.setTitle("Exit Application");
        builder.setMessage("Are you sure you want to quit Application?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                System.exit(0);
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog backDialog=builder.create();
        backDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.call:
                Intent in = new Intent(Intent.ACTION_CALL);
                in.setData(Uri.parse("tel:" + "09888870560"));
                startActivity(in);
                return true;
            case R.id.email:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"rishabh.arora.90@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Enquiry From User");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult!=null)
        {
            String re = scanResult.getContents();
            if(re!=null)
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://www." + re));
                startActivity(i);
            }
            else
            {
                Toast.makeText(this,"Looks like you already know the solution. Happy Preparations.",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this,"Looks like you already know the solution. Happy Preparations.",Toast.LENGTH_LONG).show();
        }
    }
    class CustomPagerAdapter extends PagerAdapter
    {

        Context mContext;
        LayoutInflater mLayoutInflater;
        protected void unbindDrawables(View view)
        {
            if (view.getBackground() != null)
            {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup)
            {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
                {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        }
        public CustomPagerAdapter(Context context)
        {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            Bitmap bmp=BitmapFactory.decodeFile(myDir+"/"+fname+(position+1)+".jpg");
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageBitmap(bmp);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
            unbindDrawables((View) object);
            object = null;
        }

    }
    Timer timer;
    int page = 0;

    public void pageSwitcher(int seconds)
    {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run()
                {
                    if(page==11)
                    {
                        page=0;
                    }

                        mViewPager.setCurrentItem(page++);


                }

            });

        }
    }
}

package com.dashapplications.arunproject;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class SetWallpaper extends Activity {

    ImageView iv;
    WallpaperManager wm;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);
        iv=(ImageView)findViewById(R.id.img);
        iv.setImageResource(R.drawable.img1);
        wm=WallpaperManager.getInstance(this);

    }
    public void setWallpaper(View v)
    {
        System.out.println("HELLO WORLD");
        try {
            wm.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.img1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

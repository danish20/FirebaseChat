package com.grekey.grekey;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class Scrolladapter extends BaseAdapter{
    ImageView iv;
    TextView tv1,tv2;

    ArrayList<Customlist> list=new ArrayList<>();
    Scrolladapter()
    {

        list.add(new Customlist(R.drawable.qr,"Video Solutions"));
        list.add(new Customlist(R.drawable.timetable,"Time Table"));
        list.add(new Customlist(R.drawable.knowus,"About GREKEY"));
        list.add(new Customlist(R.drawable.video,"Vocab Videos"));

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 12+10*position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        convertView=inflater.inflate(R.layout.template_scroll, parent, false);
        iv=(ImageView)(convertView.findViewById(R.id.iv));
        tv1=(TextView)(convertView.findViewById(R.id.tv1));
        Customlist ap=list.get(position);
        iv.setImageResource(ap.icon);
        tv1.setText(ap.name);
        return convertView;
    }
}

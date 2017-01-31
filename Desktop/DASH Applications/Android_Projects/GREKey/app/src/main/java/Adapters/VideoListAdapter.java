package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grekey.grekey.R;

import java.util.ArrayList;

/**
 * Created by Danish on 1/28/16.
 */
public class VideoListAdapter extends BaseAdapter
{
    ArrayList<VideoList> als;
    public VideoListAdapter(ArrayList<VideoList> als)
    {
        this.als=als;
    }
    @Override
    public int getCount() {
        return als.size();
    }

    @Override
    public Object getItem(int position) {
        return als.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position*89;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        convertView=inflater.inflate(R.layout.video_list, parent, false);
        TextView tv;
        tv=(TextView)(convertView.findViewById(R.id.tv));
        tv.setText(als.get(position).name);
        return convertView;
    }
}

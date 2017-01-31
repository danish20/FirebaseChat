package com.grekey.grekey;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.LikeView;


public class AboutUs extends ActionBarActivity implements  FragmentManager.OnBackStackChangedListener {

    private Handler mHandler = new Handler();
    private boolean mShowingBack = false;
    public static Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        font= Typeface.createFromAsset(this.getAssets(), "font/two.ttc");
        if (savedInstanceState == null)
        {
            getFragmentManager().beginTransaction().add(R.id.container, new CardFrontFragment()).commit();
        }
        else
        {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        getFragmentManager().addOnBackStackChangedListener(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(102, 51, 153)));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuItem item=menu.add(Menu.NONE,R.id.action_flip,Menu.NONE,mShowingBack
                ? R.string.action_photo
                : R.string.action_info);
        item.setIcon(mShowingBack
                ? R.drawable.ic_action_about
                : R.drawable.ic_action_about);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
            case R.id.action_flip:
                flipCard();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }


        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out, R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out).replace(R.id.container,
                new CardBackFragment()).addToBackStack(null).commit();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }
    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        // When the back stack changes, invalidate the options menu (action bar).
        invalidateOptionsMenu();
    }

    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment {

        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View root=inflater.inflate(R.layout.fragment_card_front, container, false);

            FacebookSdk.sdkInitialize(root.getContext());
             LikeView likeView = (LikeView) root.findViewById(R.id.like_view);
            likeView.setObjectIdAndType("https://www.facebook.com/grekey", LikeView.ObjectType.PAGE);
            likeView.setLikeViewStyle(LikeView.Style.BOX_COUNT);
            Button bt=(Button)root.findViewById(R.id.rishabh_bt);
            TextView tv=(TextView)root.findViewById(R.id.address);
            TextView tv1=(TextView)root.findViewById(R.id.about);
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.grekey.com/p/about-us_27.html")));
                }
            });
            tv.setTypeface(font);
            bt.setTypeface(font);
            return root;
        }
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragment extends Fragment {
        TextView tv;
        public CardBackFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView= inflater.inflate(R.layout.fragment_card_back, container, false);
            tv=(TextView)rootView.findViewById(R.id.bhoru);
            tv.setTypeface(font);
            return rootView;
        }
    }
    public void flip_it(View v)
    {
        System.out.println("Flip Testing");
        flipCard();
    }
}

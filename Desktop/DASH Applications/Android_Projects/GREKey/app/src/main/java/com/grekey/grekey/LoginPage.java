package com.grekey.grekey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.LikeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginPage extends ActionBarActivity
{
    EditText name,email,phone,college;
    LoginButton loginButton;
    private CallbackManager callbackmanager;
    AlertDialog.Builder builder;
    AlertDialog fb_ad,ad;
    String device_id;
    TextView link;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_page);
        link=(TextView)findViewById(R.id.link);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.grekey.com/p/blog-page_15.html"));

          startActivity(intent);
            }
        });
        //link.setText(Html.fromHtml("By Logging In you agree to accept our <a href=\"https://www.grekey.com/p/blog-post_15.html\">Terms and Conditions</a>"));
        device_id= Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        callbackmanager = CallbackManager.Factory.create();
        Typeface font = Typeface.createFromAsset(this.getAssets(), "font/two.ttc");
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        college = (EditText) findViewById(R.id.college);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        name.setTypeface(font);
        email.setTypeface(font);
        phone.setTypeface(font);
        college.setTypeface(font);
        loginButton.setTypeface(font);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onFblogin();
            }
        });
        builder=new AlertDialog.Builder(this);
        builder.setTitle("Invalid Data Entered");
        builder.setMessage("Either E-Mail or Phone Number is incorrect. Please re-enter valid data");
        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                email.requestFocus();
            }
        });
        ad=builder.create();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onBackPressed()
    {

    }
    private void onFblogin()
    {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        AccessToken accessToken=loginResult.getAccessToken();
                        System.out.println(accessToken.getUserId()+" Danish");
                        System.out.println(loginResult.getRecentlyGrantedPermissions());
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.d("Response",response.getJSONObject().toString());
                                        if (response.getError() != null) {
                                            // handle error
                                        } else {
                                            final String email = object.optString("email");
                                            final String name = object.optString("name");
                                            final String gender = object.optString("gender");
                                            final String birthday=object.optString("birthday");
                                            Thread t=new Thread(new Runnable() {
                                                @Override
                                                public void run()
                                                {
                                                    postData(name,email,gender,birthday);
                                                }
                                            });
                                            t.start();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel()
                    {
                        Log.d("Danish","On cancel");
                        builder.setMessage("Please try to login with E-Mail and Phone Number");
                        builder.setTitle("Login Failed");
                        fb_ad=builder.create();
                        fb_ad.show();
                    }

                    @Override
                    public void onError(FacebookException error)
                    {
                        Log.d("Danish",error.toString());
                        builder.setMessage("Please try to login with E-Mail and Phone Number");
                        builder.setTitle("Login Failed");
                        fb_ad=builder.create();
                        fb_ad.show();
                    }
                });
        }
    public void postData(String s1, String s2, String s3, String s4)
    {
    String fullurl="https://docs.google.com/forms/d/13pbnRLLbSjGIDn8DJOomRYm47k2RTFu9TpKWkp-JMBU/formResponse";
    HttpRequest request=new HttpRequest();
        String col1=s1;
        String col2=s2;
        String col3=s3;
        String col4=s4;
        String col5=device_id;
        String data= "entry_1085198369="+ URLEncoder.encode(col1) +"&" + "entry_1178206981="+ URLEncoder.encode(col2)+"&" + "entry_454355010="+ URLEncoder.encode(col3)+"&" + "entry_1984070759="+ URLEncoder.encode(col4)+"&" + "entry_2065997949="+ URLEncoder.encode(col5);
        String response=request.sendPost(fullurl,data);
        startActivity(new Intent(this,Home.class));
        finish();
    }
    public static boolean isEmailValid(String email)
    {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            isValid = true;
        }
        return isValid;
    }
    public static boolean isPhoneNumberValid(String phoneNumber)
    {
        boolean isValid = false;
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches()){
            isValid = true;
        }
        return isValid;
    }
    public void go(View v)
    {
    final String s1=name.getText().toString();
    final String s2=email.getText().toString();
        final String s3=phone.getText().toString();
        final String s4=college.getText().toString();
    Thread t=new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            postData(s1,s2,s3,s4);
        }
    });
    if(isEmailValid(s2) && isPhoneNumberValid(s3) && s1.length()>3 && s4.length()>3)
    {
        t.start();
    }
    else
    {
        if(s1.length()<3 || s4.length()<3)
        {
            builder.setMessage("Invalid Name or College");
            ad=builder.create();
        }
        else
        {
            builder.setMessage("Invalid Email or Phone Number");
            ad=builder.create();
        }
        ad.show();
    }
}
}

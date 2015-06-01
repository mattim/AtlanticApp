package com.example.mtimmons.atlanticapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.lang.reflect.Method;


public class LinkActivity extends MainActivity{
    private WebView mWebView;
    private LinearLayout container;
    private Button nextButton, closeButton;
    private EditText findBox;
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_link, null, false);
        preferenceSettings = getPreferences(PREFERENCE_MODE_PRIVATE);
        username = preferenceSettings.getString("username", "");//defaults to ""
        password = preferenceSettings.getString("password","");//defaults to ""
        mDrawerLayout.addView(contentView, 0);
        mWebView = (WebView)findViewById(R.id.web_view);
        // WebSettings webSettings = mWebView.getSettings();
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        String url = "http://ftp.atlanticbcs.com/atlantic/atlanticwebapplication/atlanticwebapplication/index.html";
        mWebView.loadUrl(url);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });
    }
    private static final int SEARCH_MENU_ID = Menu.FIRST;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_link, menu);
        menu.add(0, SEARCH_MENU_ID, 0, "Search");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_login) {
            Intent i = new Intent(LinkActivity.this, SignInActivity.class);
            startActivity(i);
            return true;
        }
        if (id == SEARCH_MENU_ID) {
            search();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void search(){
        container = (LinearLayout)findViewById(R.id.layoutId);

        nextButton = new Button(this);
        nextButton.setText("Next");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.findNext(true);
            }
        });
        container.addView(nextButton);

        closeButton = new Button(this);
        closeButton.setText("Close");
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeAllViews();
                mWebView.reload();

            }
        });
        container.addView(closeButton);

        findBox = new EditText(this);
        findBox.setMinEms(30);
        findBox.setSingleLine(true);
        findBox.setHint("Search");

        findBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) ) { //waits for key to be released
                    mWebView.findAllAsync(findBox.getText().toString());

                    try {
                        Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                        m.invoke(mWebView, true);
                    } catch (Exception ignored) {
                    }
                }
                return false;
            }
        });
        container.addView(findBox);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return true;
        }
        @Override
          public void onReceivedHttpAuthRequest(WebView view,HttpAuthHandler handler, String host, String realm){
            handler.proceed(username,password);
        }
    }
}

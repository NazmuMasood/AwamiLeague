package com.example.awamileague;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar; ImageView progressBarIV;
    WebView webView;
    SwipeRefreshLayout swipeLayout;
    //String BANGLA_FONT_SOLAIMAN_LIPI = "fonts/SolaimanLipi_22-02-2012.ttf";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CircleFillView circleFill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the layout variables
        progressBar = findViewById(R.id.progressBar);
        progressBarIV = findViewById(R.id.progressBarIV);
        webView = findViewById(R.id.webView);
        swipeLayout = findViewById(R.id.swipeLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        circleFill = (CircleFillView) findViewById(R.id.circleFillView);

        progressBar.setProgress(100);

        //Loading the web-page url
        webView.loadUrl("https://www.albd.com.bd/");
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                circleFill.setVisibility(View.VISIBLE);
                progressBarIV.setVisibility(View.VISIBLE);
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                circleFill.setVisibility(View.GONE);
                progressBarIV.setVisibility(View.GONE);
                swipeLayout.setRefreshing(false);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                circleFill.setValue(newProgress);
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

        //Setting up bangla font
        //Typeface tf = Typeface.createFromAsset(getAssets(),BANGLA_FONT_SOLAIMAN_LIPI);

        //Setting up custom toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up the navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Setting up the navigation view i.e. nav_menu
        navigationView.setCheckedItem(R.id.nav_home_url);

        //Handling the navigation view inputs
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home_url:
                        webView.loadUrl("https://www.albd.com.bd/");
                        break;
                    case R.id.nav_sign_up:
                        webView.loadUrl("https://albd.com.bd/members/registration");
                        break;
                    case R.id.nav_login:
                        webView.loadUrl("https://albd.com.bd/members/login");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(null);
            dialog.setMessage(R.string.close_alert_question);
            dialog.setPositiveButton(R.string.close_alert_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.setNegativeButton(R.string.close_alert_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }
}

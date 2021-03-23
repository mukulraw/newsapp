package com.app.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import im.delight.android.webview.AdvancedWebView;

public class Web extends AppCompatActivity implements AdvancedWebView.Listener{

    AdvancedWebView web;
    String url;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        url = getIntent().getStringExtra("url");

        web = findViewById(R.id.web);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle(url);

        web.setListener(this, this);
        web.setMixedContentAllowed(false);
        web.loadUrl(url);

    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        web.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        web.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        web.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!web.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

}
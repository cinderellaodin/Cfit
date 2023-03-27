package com.odin.cfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WeightSimulatorActivity extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_simulator);

        webView = (WebView) findViewById(R.id.webView_simulator);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://modelmydiet.com");
    }
}
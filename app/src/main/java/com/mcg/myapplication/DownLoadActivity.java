package com.mcg.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import base.MyProgressDialog;

/**
 * Created by 成刚 on 2016/4/20.
 */
public class DownLoadActivity extends AppCompatActivity {
    private RelativeLayout rl_back;

    private WebView webView;
    private MyProgressDialog myProgressDialog = new MyProgressDialog();
    private static String murl = null;

    public static void actionStart(Context context,String url){
        Intent intent = new Intent(context,DownLoadActivity.class);
        intent.putExtra("url",url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        murl = url;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        rl_back = (RelativeLayout)findViewById(R.id.rl_download_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView)findViewById(R.id.web_view_download);
        webView.getSettings().setJavaScriptEnabled(true);//设置支持脚本
        webView.getSettings().setBuiltInZoomControls(true);//设置支持缩放
        //直接传入
        webView.loadUrl(murl);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                myProgressDialog.dismiss();
                finish();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                myProgressDialog.show(DownLoadActivity.this,"正在下载...");
            }
        });
    }
}

package com.smartalquran.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

public class MainActivity extends Activity {
  private WebView web;
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    getWindow().setStatusBarColor(0xff064536);
    getWindow().setNavigationBarColor(0xff064536);
    web = new WebView(this);
    web.getSettings().setJavaScriptEnabled(true);
    web.getSettings().setDomStorageEnabled(true);
    web.getSettings().setAllowFileAccess(false);
    web.getSettings().setAllowContentAccess(false);
    web.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) { return !"appassets.androidplatform.net".equals(request.getUrl().getHost()); }
      @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
        if (!"https".equals(req.getUrl().getScheme()) || !"appassets.androidplatform.net".equals(req.getUrl().getHost())) return empty();
        String path = req.getUrl().getPath();
        if (path != null && path.startsWith("/api/")) {
          String endpoint = path.substring(5);
          if (!endpoint.matches("(surah|juz)/[0-9]{1,3}/[a-z0-9.-]+|surah")) return empty();
          try {
            HttpURLConnection c = (HttpURLConnection) new URL("https://api.alquran.cloud/v1/" + endpoint).openConnection();
            c.setConnectTimeout(12000); c.setReadTimeout(15000); c.setInstanceFollowRedirects(false);
            int status = c.getResponseCode();
            if (status != 200) { c.disconnect(); return empty(); }
            return new WebResourceResponse("application/json", "UTF-8", c.getInputStream());
          } catch (Exception e) { return empty(); }
        }
        String file = path == null || path.equals("/") ? "index.html" : path.substring(1);
        if (!file.matches("[a-zA-Z0-9._-]+")) return empty();
        try {
          InputStream stream = getAssets().open(file);
          String mime = file.endsWith(".css") ? "text/css" : file.endsWith(".js") ? "text/javascript" : "text/html";
          return new WebResourceResponse(mime, "UTF-8", stream);
        } catch (Exception e) { return empty(); }
      }
      private WebResourceResponse empty() { return new WebResourceResponse("text/plain", "UTF-8", 404, "Not Found", Collections.emptyMap(), new java.io.ByteArrayInputStream(new byte[0])); }
    });
    setContentView(web);
    web.loadUrl("https://appassets.androidplatform.net/index.html");
  }
  @Override public void onBackPressed() {
    if (web.canGoBack()) web.goBack(); else super.onBackPressed();
  }
}

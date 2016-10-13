package com.example.sky.testproject;

import android.content.ContentUris;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testUri();
    }

    private void testUri() {
        URI u;
        ContentUris c;
        String uriPath = "http://www.java2s.com:8080/yourpath/fileName.htm?stove=10&path=32&id=4#harvic";
        Uri uri = Uri.parse(uriPath);
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();
        String encodePath = uri.getEncodedPath();
        String query = uri.getQuery();
        String fragment = uri.getFragment();
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        Log.i(TAG, "scheme:" + scheme + ", authority:" + authority + ", host:" + host
                + ", port:" + port + ", path:" + path + ", encodePath:" + encodePath + ", query:"
                + query + ", fragment:" + fragment + ", schemeSpecificPart:" + schemeSpecificPart);
        String lastPathSegment = uri.getLastPathSegment();
        List<String> paths = uri.getPathSegments();
        for (int i = 0; i < paths.size(); i++) {
            Log.i(TAG, "path" + i +":" + paths.get(i));
        }
        Set<String> queryParameters = uri.getQueryParameterNames();
        Iterator<String> iterator = queryParameters.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = uri.getQueryParameter(key);
            Log.i(TAG, "query key:" + key + ", value:" + value);
        }
        Log.i(TAG, "lastPathSegment:" + lastPathSegment);
        String baseUriPath = "http://www.java2s.com:8080/";
        Uri baseUri = Uri.parse(baseUriPath);
        Uri realUri = baseUri.buildUpon()
                             .appendEncodedPath("yourpath/fileName.htm")
                             .appendQueryParameter("stove", "10")
                             .appendQueryParameter("path", "32")
                             .appendQueryParameter("id", "4")
                             .encodedFragment("harvic")
                             .build();
        Log.i(TAG, "real uri path:" + realUri.toString());
    }
}

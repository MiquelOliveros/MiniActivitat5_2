package com.example.miniactivitat5_2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final int TIMEOUT = 3000;
    private Button btDownloadWeb;
    private Button btDownloadImage;
    private TextView tvWebContent;
    private ImageView ivContent;

    ConnectivityManager connectivityManager;
    NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        this.connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkManager = new NetworkManager(this);
        this.networkManager.execute(this.connectivityManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.networkManager.cancel(true);
    }

    private void findViewsById() {
        tvWebContent = findViewById(R.id.tvWebContent);
        ivContent = findViewById(R.id.ivContent);
        btDownloadImage = findViewById(R.id.btDownloadImage);
        btDownloadWeb = findViewById(R.id.btDownloadWeb);
    }

    //Download String WebPage

    public void downloadWebPage(View view) throws MalformedURLException {
        downloadWeb(new URL("https://www.google.com/"));
    }

    private void downloadWeb(final URL url) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                InputStream stream = null;
                HttpsURLConnection connection = null;

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                try {
                    connection = (HttpsURLConnection) url.openConnection();

                    connection.setReadTimeout(TIMEOUT);
                    connection.setConnectTimeout(TIMEOUT);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + responseCode);
                    }

                    // Retrieve the response body as an InputStream.
                    stream = connection.getInputStream();
                    if (stream != null) {
                        // Converts Stream to String with max length of 500.
                        tvWebContent.setText(readStream(stream));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Close Stream and disconnect HTTPS connection.
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        };
        handler.post(runnable);
    }

    public String readStream(InputStream stream) throws IOException {

        Reader reader = null;
        reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader, 1000);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            sb.append(str);
        }

        return sb.toString();
    }

    //Download Image

    public void downloadImage(View view) throws MalformedURLException {
        downloadImageWeb(new URL("https://s.inyourpocket.com/gallery/113383.jpg"));
    }

    private void downloadImageWeb(final URL url) {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                InputStream stream = null;
                HttpsURLConnection connection = null;

                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                try {
                    connection = (HttpsURLConnection) url.openConnection();

                    connection.setReadTimeout(TIMEOUT);
                    connection.setConnectTimeout(TIMEOUT);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    int responseCode = connection.getResponseCode();

                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code: " + responseCode);
                    }

                    // Retrieve the response body as an InputStream.
                    stream = connection.getInputStream();
                    if (stream != null) {
                        // Converts Stream to String with max length of 500.
                        ivContent.setImageBitmap(BitmapFactory.decodeStream(stream));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Close Stream and disconnect HTTPS connection.
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        };
        handler.post(runnable);
    }
}

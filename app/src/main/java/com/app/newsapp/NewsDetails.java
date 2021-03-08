package com.app.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.newsapp.newsDetailsPOJO.Data;
import com.app.newsapp.newsDetailsPOJO.newsDetailsBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewsDetails extends AppCompatActivity {

    Toolbar toolbar;
    String id, tit;
    RoundedImageView image;
    ImageView youtubeimage;
    TextView date, title, content;
    ProgressBar progress;
    CardView youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        id = getIntent().getStringExtra("id");
        tit = getIntent().getStringExtra("title");

        toolbar = findViewById(R.id.toolbar);
        image = findViewById(R.id.imageView5);
        date = findViewById(R.id.textView6);
        title = findViewById(R.id.textView3);
        content = findViewById(R.id.textView7);
        progress = findViewById(R.id.progressBar3);
        youtubeimage = findViewById(R.id.youtubeimage);
        youtube = findViewById(R.id.textView8);


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
        toolbar.setTitle(tit);

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<newsDetailsBean> call = cr.getNewsById(id);

        call.enqueue(new Callback<newsDetailsBean>() {
            @Override
            public void onResponse(Call<newsDetailsBean> call, Response<newsDetailsBean> response) {

                if (response.body().getStatus().equals("1")) {
                    Data item = response.body().getData();


                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getThumbnail(), image, options);
                    loader.displayImage(item.getYoutubeThumbnail(), youtubeimage, options);

                    title.setText(item.getTitle());
                    date.setText(item.getCreated());
                    content.setText(Html.fromHtml(item.getContent()));

                    final String iidd = getYouTubeId(item.getYoutubeLink());

                    youtube.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + iidd));
                            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" + iidd));
                            try {
                                startActivity(appIntent);
                            } catch (ActivityNotFoundException ex) {
                                startActivity(webIntent);
                            }

                        }
                    });

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<newsDetailsBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

}
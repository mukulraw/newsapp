package com.app.newsapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.app.newsapp.newsDetailsPOJO.Data;
import com.app.newsapp.newsDetailsPOJO.newsDetailsBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

public class verticalNews2 extends Fragment {

    RoundedImageView image;
    TextView title, content, date, read, subtitle;
    ProgressBar progress;
    NewsActivity mainActivity;
    String id;
    CardView youtube;
    ImageView youtubeimage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_news, container, false);
        mainActivity = (NewsActivity) getActivity();

        id = getArguments().getString("id");

        image = view.findViewById(R.id.image);
        title = view.findViewById(R.id.textView12);
        content = view.findViewById(R.id.textView13);
        progress = view.findViewById(R.id.progressBar3);
        date = view.findViewById(R.id.textView14);
        youtubeimage = view.findViewById(R.id.youtubeimage);
        youtube = view.findViewById(R.id.textView8);
        read = view.findViewById(R.id.textView19);
        subtitle = view.findViewById(R.id.textView123);

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) mainActivity.getApplicationContext();

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

                    read.setText("और पढ़ें " + item.getNews_source() + " पर >");
                    subtitle.setText(item.getSub_title());

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getThumbnail(), image, options);
                    if (item.getYoutubeLink().length() > 0)
                    {
                        loader.displayImage(item.getYoutubeThumbnail(), youtubeimage, options);
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
                        youtube.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        youtube.setVisibility(View.GONE);
                    }

                    title.setText(item.getTitle());
                    date.setText(item.getCreated());
                    content.setText(Html.fromHtml(item.getContent()));

                    read.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(mainActivity, Web.class);
                            intent.putExtra("url", item.getPage_link());
                            startActivity(intent);

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

        return view;
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

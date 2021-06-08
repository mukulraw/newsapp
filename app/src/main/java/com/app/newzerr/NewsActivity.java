package com.app.newzerr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.app.newzerr.newsPOJO.Datum;
import com.app.newzerr.newsPOJO.newsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import me.kaelaela.verticalviewpager.VerticalViewPager;
import me.kaelaela.verticalviewpager.transforms.ZoomOutTransformer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewsActivity extends AppCompatActivity {

    Toolbar toolbar;
    String id, title;
    VerticalViewPager pager;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        toolbar = findViewById(R.id.toolbar2);
        pager = findViewById(R.id.pager);
        progress = findViewById(R.id.progressBar4);
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
        toolbar.setTitle(title);

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

        Call<newsBean> call = cr.getNews(id);
        call.enqueue(new Callback<newsBean>() {
            @Override
            public void onResponse(Call<newsBean> call, Response<newsBean> response) {


                if (response.body().getStatus().equals("1")) {
                    PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, response.body().getData());
                    pager.setAdapter(adapter);
                    pager.setPageTransformer(false, new ZoomOutTransformer());
                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<newsBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Datum> list = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fm, int behavior, List<Datum> list) {
            super(fm, behavior);
            this.list = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            verticalNews2 frag = new verticalNews2();
            Bundle b = new Bundle();
            b.putString("id", list.get(position).getId());
            frag.setArguments(b);
            return frag;
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }


}
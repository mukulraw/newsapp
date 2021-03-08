package com.app.newsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.newsapp.cityPOJO.Datum;
import com.app.newsapp.cityPOJO.cityBean;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class News extends Fragment {

    ProgressBar progress;
    TabLayout tabs;
    ViewPager pager;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, container, false);
        mainActivity = (MainActivity) getActivity();


        tabs = view.findViewById(R.id.tabs);
        progress = view.findViewById(R.id.progressBar2);
        pager = view.findViewById(R.id.pager);

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

        Call<cityBean> call = cr.getCities();
        call.enqueue(new Callback<cityBean>() {
            @Override
            public void onResponse(Call<cityBean> call, Response<cityBean> response) {


                if (response.body().getStatus().equals("1")) {

                    PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, response.body().getData());
                    pager.setAdapter(adapter);
                    tabs.setupWithViewPager(pager);

                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<cityBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Datum> list = new ArrayList<>();

        public PagerAdapter(@NonNull FragmentManager fm, int behavior, List<Datum> list) {
            super(fm, behavior);
            this.list = list;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getCity();
        }


        @Override
        public Fragment getItem(int position) {
            newsList frag = new newsList();
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

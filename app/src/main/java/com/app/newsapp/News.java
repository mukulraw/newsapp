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

import com.google.android.material.tabs.TabLayout;

public class News extends Fragment {

    ProgressBar progress;
    //String id, title;
    TabLayout tabs;
    ViewPager pager;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, container, false);
        mainActivity = (MainActivity) getActivity();
/*
        id = getArguments().getString("id");
        title = getArguments().getString("title");
*/


        tabs = view.findViewById(R.id.tabs);
        progress = view.findViewById(R.id.progressBar2);
        pager = view.findViewById(R.id.pager);


        //PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

       /* PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);*/

        /*progress.setVisibility(View.VISIBLE);

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

        Call<subCat1Bean> call = cr.getSubCat2(id);
        call.enqueue(new Callback<subCat1Bean>() {
            @Override
            public void onResponse(Call<subCat1Bean> call, Response<subCat1Bean> response) {


                if (response.body().getStatus().equals("1"))
                {

                    PagerAdapter adapter = new PagerAdapter(getChildFragmentManager() , response.body().getData());
                    pager.setAdapter(adapter);
                    tabs.setupWithViewPager(pager);

                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<subCat1Bean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });*/


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    /*class PagerAdapter extends FragmentStatePagerAdapter {

        String[] titles = {
                "All",
                "Pending",
                "Rescheduled",
                "Packed",
                "Shipped",
                "Hold"
        };

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        //List<Datum> list = new ArrayList<>();

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


        @Override
        public Fragment getItem(int position) {
            orderlist frag = new orderlist();
            *//*Bundle b = new Bundle();
            b.putString("id" , list.get(position).getId());
            frag.setArguments(b);
            *//*
            return frag;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }*/

}

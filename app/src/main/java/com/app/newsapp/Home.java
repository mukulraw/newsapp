package com.app.newsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Home extends Fragment {

    RecyclerView grid;
    ProgressBar progress;

    //String id;

    //List<Datum> list;
    BestAdapter adapter;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        mainActivity = (MainActivity) getActivity();
        //list = new ArrayList<>();

        //id = getArguments().getString("id");

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar);

        adapter = new BestAdapter(getActivity());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        return view;
    }


    /*@Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

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

        Call<productsBean> call = cr.getProducts(id, SharePreferenceUtils.getInstance().getString("location"));
        call.enqueue(new Callback<productsBean>() {
            @Override
            public void onResponse(Call<productsBean> call, Response<productsBean> response) {


                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getData());
                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<productsBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }*/

    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {

        Context context;
        //List<Datum> list = new ArrayList<>();

        public BestAdapter(Context context) {
            this.context = context;
            //this.list = list;
        }

        /*public void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }*/

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.news_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

//            final Datum item = list.get(position);

/*
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);
*/


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title, selling, mrp;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                /*image = itemView.findViewById(R.id.imageView5);
                title = itemView.findViewById(R.id.textView2);
                selling = itemView.findViewById(R.id.textView4);
                mrp = itemView.findViewById(R.id.textView6);
                add = itemView.findViewById(R.id.button3);*/

            }
        }
    }

}

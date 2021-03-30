package com.app.newsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.newsapp.breakingPOJO.breakingBean;
import com.app.newsapp.catPOJO.Datum;
import com.app.newsapp.catPOJO.catBean;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class categories extends Fragment {

    RecyclerView grid, grid2;
    ProgressBar progress;
    MainActivity2 mainActivity;
    BestAdapter adapter;
    BestAdapter2 adapter2;
    List<Datum> list;
    List<com.app.newsapp.breakingPOJO.Datum> list2;
    CircleImageView profile;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 12;
    private static final String TAG = "Login";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoey, container, false);
        mainActivity = (MainActivity2) getActivity();

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mainActivity, gso);

        list = new ArrayList<>();
        list2 = new ArrayList<>();

        grid = view.findViewById(R.id.grid);
        grid2 = view.findViewById(R.id.grid2);
        progress = view.findViewById(R.id.progressBar6);
        profile = view.findViewById(R.id.imageView3);

        adapter = new BestAdapter(mainActivity, list);
        adapter2 = new BestAdapter2(mainActivity, list2);
        GridLayoutManager manager = new GridLayoutManager(mainActivity, 3);
        GridLayoutManager manager2 = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter2);
        grid.setLayoutManager(manager2);

        grid2.setAdapter(adapter);
        grid2.setLayoutManager(manager);

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

        Call<catBean> call = cr.getCat();

        call.enqueue(new Callback<catBean>() {
            @Override
            public void onResponse(Call<catBean> call, Response<catBean> response) {

                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getData());
                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<catBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
        progress.setVisibility(View.VISIBLE);
        Call<breakingBean> call1 = cr.getBreaking();

        call1.enqueue(new Callback<breakingBean>() {
            @Override
            public void onResponse(Call<breakingBean> call, Response<breakingBean> response) {
                if (response.body().getStatus().equals("1")) {
                    adapter2.setData(response.body().getData());
                }

                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<breakingBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = SharePreferenceUtils.getInstance().getString("userId");

                if (userid.length() > 0) {
                    final Dialog dialog = new Dialog(mainActivity, R.style.MyDialogTheme);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.signin);
                    dialog.show();

                    CircleImageView image = dialog.findViewById(R.id.imageView3);
                    TextView name = dialog.findViewById(R.id.textView10);
                    TextView email = dialog.findViewById(R.id.textView11);
                    Button logout = dialog.findViewById(R.id.button);

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(SharePreferenceUtils.getInstance().getString("image"), image, options);

                    name.setText(SharePreferenceUtils.getInstance().getString("name"));
                    email.setText(SharePreferenceUtils.getInstance().getString("email"));

                    logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(mainActivity, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            SharePreferenceUtils.getInstance().deletePref();

                                            dialog.dismiss();

                                            Intent intent = new Intent(mainActivity, Splash.class);
                                            startActivity(intent);
                                            mainActivity.finishAffinity();
                                        }
                                    });

                            //FirebaseAuth.getInstance().signOut();

                        }
                    });

                } else {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }

            }
        });

        return view;
    }

    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        public BestAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cat_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final Datum item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, NewsActivity.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getName());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView7);
                title = itemView.findViewById(R.id.textView16);

            }
        }
    }


    class BestAdapter2 extends RecyclerView.Adapter<BestAdapter2.ViewHolder> {

        Context context;
        List<com.app.newsapp.breakingPOJO.Datum> list = new ArrayList<>();

        public BestAdapter2(Context context, List<com.app.newsapp.breakingPOJO.Datum> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<com.app.newsapp.breakingPOJO.Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.breaking_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final com.app.newsapp.breakingPOJO.Datum item = list.get(position);

            holder.title.setText(item.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mainActivity, Web.class);
                    intent.putExtra("url", item.getLink());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.textView21);

            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, user.getDisplayName());
                            Log.d(TAG, user.getEmail());

                            String email = user.getEmail();
                            String password = user.getUid();
                            String name = user.getDisplayName();
                            String image = user.getPhotoUrl().toString();

                            //progress.setVisibility(View.VISIBLE);

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

                            Call<loginBean> call = cr.login(email, password, name, SharePreferenceUtils.getInstance().getString("token"));

                            call.enqueue(new Callback<loginBean>() {
                                @Override
                                public void onResponse(@NotNull Call<loginBean> call, @NotNull Response<loginBean> response) {

                                    assert response.body() != null;
                                    if (response.body().getStatus().equals("1")) {

                                        SharePreferenceUtils.getInstance().saveString("userId", response.body().getUserId());
                                        SharePreferenceUtils.getInstance().saveString("phone", response.body().getPhone());
                                        SharePreferenceUtils.getInstance().saveString("email", response.body().getEmail());
                                        SharePreferenceUtils.getInstance().saveString("name", response.body().getName());
                                        SharePreferenceUtils.getInstance().saveString("image", image);
                                        Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                                        ImageLoader loader = ImageLoader.getInstance();
                                        loader.displayImage(SharePreferenceUtils.getInstance().getString("image"), profile, options);

                                    } else {
                                        Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    //progress.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(@NotNull Call<loginBean> call, @NotNull Throwable t) {
                                    //progress.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        String userid = SharePreferenceUtils.getInstance().getString("userId");

        if (userid.length() == 0) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(SharePreferenceUtils.getInstance().getString("image"), profile, options);
        }
    }
}

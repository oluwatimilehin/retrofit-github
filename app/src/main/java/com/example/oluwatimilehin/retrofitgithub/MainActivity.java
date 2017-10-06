package com.example.oluwatimilehin.retrofitgithub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.oluwatimilehin.retrofitgithub.models.CredentialDialog;
import com.example.oluwatimilehin.retrofitgithub.models.GithubRepo;
import com.example.oluwatimilehin.retrofitgithub.models.GithubRepoDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CredentialDialog.CredentialsDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.reposSpinner)
    Spinner reposSpinner;
    @BindView(R.id.issuesSpinner)
    Spinner issuesSpinner;
    @BindView(R.id.commentField)
    EditText commentField;
    @BindView(R.id.loadReposButton)
    Button loadReposButton;
    @BindView(R.id.postCommentButton)
    Button postCommentButton;

    String userName;
    String password;
    GithubService githubService;
    CompositeDisposable compositeDisposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        reposSpinner.setEnabled(false);
        issuesSpinner.setEnabled(false);

        createGithubApi();
    }

    private void createGithubApi() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(GithubRepo.class, new GithubRepoDeserializer())
                .create();

        Interceptor interceptor = chain -> {
            Request request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", Credentials.basic(userName, password))
                    .build();
            return chain.proceed(request);
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        githubService = retrofit.create(GithubService.class);

    }

    @Override
    public void onDialogPositiveClick(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.credentials:
                showCredentialsDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCredentialsDialog() {
        CredentialDialog dialog = new CredentialDialog();
        Bundle bundle = new Bundle();
        bundle.putString("username", userName);
        bundle.putString("password", password);
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), dialog.getClass().getSimpleName());
        loadReposButton.setEnabled(true);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadReposButton:
                compositeDisposables.add(githubService.getRepos()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (List<GithubRepo> value) -> {
                                    if (!value.isEmpty()) {
                                        ArrayAdapter<GithubRepo> adapter = new ArrayAdapter<GithubRepo>
                                                (MainActivity.this,
                                                        android.R.layout.simple_spinner_dropdown_item, value);
                                        reposSpinner.setAdapter(adapter);
                                        reposSpinner.setEnabled(true);
                                    } else {
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                                MainActivity.this, android.R.layout
                                                .simple_spinner_dropdown_item, new String[]{"No " +
                                                "data"});
                                        reposSpinner.setAdapter(adapter);
                                        reposSpinner.setEnabled(false);
                                    }
                                },
                                (Throwable e) -> e.printStackTrace()));
                break;
        }
    }

    private DisposableSingleObserver<List<GithubRepo>> getReposObserver() {


        return new DisposableSingleObserver<List<GithubRepo>>() {
            @Override
            public void onSuccess(List<GithubRepo> value) {

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposables.dispose();
    }
}

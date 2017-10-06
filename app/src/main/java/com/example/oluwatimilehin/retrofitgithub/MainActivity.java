package com.example.oluwatimilehin.retrofitgithub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.oluwatimilehin.retrofitgithub.models.CredentialDialog;
import com.example.oluwatimilehin.retrofitgithub.models.GithubRepo;
import com.example.oluwatimilehin.retrofitgithub.models.GithubRepoDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
}

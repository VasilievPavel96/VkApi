package com.example.hp_.vkapitest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    public static final String AUTH_URL = "oauth.vk.com";
    public static final String BASE_URL = "https://api.vk.com";
    public static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";
    public static final String CLIENT_ID = "5915435";

    private RecyclerView mRecyclerView;
    private UsersAdapter mUsersAdapter;
    private List<User> friends;

    @Override
    protected void onNewIntent(Intent intent) {
        Uri uri = intent.getData();
        Uri normalizedUri = Uri.parse(uri.toString().replaceFirst("#", "?"));
        String token = normalizedUri.getQueryParameter("access_token");
        Type type = new TypeToken<List<User>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type, new VkFriendsDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        VkApi vkService = retrofit.create(VkApi.class);
        Call<List<User>> call = vkService.getFriends(token);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                friends = response.body();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(LoginActivity.this, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setHasFixedSize(true);
                mUsersAdapter = new UsersAdapter(friends, LoginActivity.this);
                mRecyclerView.setAdapter(mUsersAdapter);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(AUTH_URL)
                .appendPath("authorize")
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("display", "mobile")
                .appendQueryParameter("redirect_uri", REDIRECT_URL)
                .appendQueryParameter("scope", "friends")
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("v", "5.62")
                .appendQueryParameter("state", "12345");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(builder.toString()));
        startActivity(intent);
    }
}

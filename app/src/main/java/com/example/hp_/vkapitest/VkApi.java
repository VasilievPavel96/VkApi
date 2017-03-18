package com.example.hp_.vkapitest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HP- on 10.03.2017.
 */

public interface VkApi {
    @GET("/method/friends.get?v=5.62&fields=photo_50")
    Call<List<User>> getFriends(@Query("access_token") String token);
}

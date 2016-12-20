package com.example.awadeshkumar.twitter;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Awadesh Kumar on 12/7/2016.
 */

public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide FollowersCustomService with defined endpoints
     */
    public FollowersCustomService getFollowersCustomService() {
        return getService(FollowersCustomService.class);
    }

    public FollowingCustomService getFollowingCustomService() {
        return getService(FollowingCustomService.class);
    }
}


//cursored collection of user objects for users following the specified user.
interface FollowersCustomService {
    @GET("/1.1/followers/list.json")
    Call<Followers> show(@Query("user_id") Long userId, @Query("cursor") long cursor, @Query("screen_name") String
            var, @Query("skip_status") Boolean var1, @Query("include_user_entities") Boolean var2, @Query("count") Integer var3);
}

// user objects for every user the specified user is following
interface FollowingCustomService {
    @GET("/1.1/friends/list.json")
    Call<Followers> show(@Query("user_id") Long userId, @Query("screen_name") String
            var, @Query("skip_status") Boolean var1, @Query("include_user_entities") Boolean var2, @Query("count") Integer var3);
}


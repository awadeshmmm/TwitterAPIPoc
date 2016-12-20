package com.example.awadeshkumar.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "";
    private static final String TWITTER_SECRET = "";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";

    //Twitter Login Button
    TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLogin);

        //Adding callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                // Toast.makeText(this,"success",Toast.LENGTH_LONG).show();
                //login(result);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Your message", Toast.LENGTH_LONG).show();
                    }
                });
                final TwitterSession session = Twitter.getSessionManager()
                        .getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String userName = session.getUserName();
                final long userId = session.getUserId();
                String token = authToken.token;
                String secret = authToken.secret;
                Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
                userResult.enqueue(new Callback<User>() {

                    @Override
                    public void failure(TwitterException e) {

                    }

                    @Override
                    public void success(Result<User> userResult) {

                        User user = userResult.data;
                        String twitterImage = user.profileImageUrl;

                        try {
                            Log.d("imageurl", user.profileImageUrl);
                            Log.d("name", user.name);
                            Log.d("email", user.email);
                            Log.d("des", user.description);
                            Log.d("followers ", String.valueOf(user.followersCount));
                            Log.d("createdAt", user.createdAt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                });

                /**
                 * Custom twitter API client
                 */

                    new MyTwitterApiClient(session).getFollowingCustomService().show(session.getId(), null, true, true, 100).enqueue(new Callback<Followers>() {
                        @Override
                        public void success(Result<Followers> result) {
                            int i = result.data.users.size();
                            Log.i("Get success", "" + result.data.users.size());
                            List<User> usersList = result.data.users;
                            // process your followers list
                            for (User user : usersList) {
                             findSpecificUsersFollowers(session, usersList.get(0).getId());
                             long specificUserId = user.getId();
                             }
                        }

                        @Override
                        public void failure(TwitterException exception) {
                           exception.printStackTrace();
                        }
                    });


//                new MyTwitterApiClient(session).getCustomService().show(session.getId(), -1, null, true, true, 100).enqueue(new Callback<Followers>() {
//                    @Override
//                    public void success(Result<Followers> result) {
//                        int i = result.data.users.size();
//                        Log.i("Get success", "" + result.data.users.size());
//                        List<User> usersList = result.data.users;
//
//                        //for (User user : usersList) {
//                        // findSpecificUsersFollowers(session, usersList.get(0).getId());
//                        // long specificUserId = user.getId();
//                        // }
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//
//                    }
//                });
//

            }


            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Adding the login result back to the button
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void login(Result<TwitterSession> result) {

        //Creating a twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();

        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
//        Twitter.getApiClient(session).getAccountService()
//                .verifyCredentials(true, true, new Callback<User>() {
//
//                    @Override
//                    public void success(Result<User> result) {
//
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//
//                    }
//                });
    }

    public void findSpecificUsersFollowers(TwitterSession session, long userID) {
        new MyTwitterApiClient(session).getFollowingCustomService().show(userID, null, true, true, 100).enqueue(new Callback<Followers>() {
            @Override
            public void success(Result<Followers> result) {
                int i = result.data.users.size();
                Log.i("Get success", "" + result.data.users.size());
                List<User> usersList = result.data.users;
                //   Twitter.getInstance().logOut();

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }
}

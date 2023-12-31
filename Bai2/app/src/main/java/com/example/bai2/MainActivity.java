package com.example.bai2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private Fragment mContent;
    public final static String FRAGMENT_TAG = "MyFragment";
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //get firebase auth instance
        fAuth = FirebaseAuth.getInstance();
        //firebase messaging instance
        FirebaseMessaging fMsg = FirebaseMessaging.getInstance();
        //firebase instance to subscribe topics
        fMsg.subscribeToTopic("todos");
        //firebase analytics instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Sets whether analytics collection is enabled for this app on thisdevice.mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        //Sets the minimum engagement time required before starting asession. The default value is 10000 (10 seconds). Let's make it 20 secondsjust for the funmFirebaseAnalytics.setMinimumSessionDuration(20000);
        //Sets the duration of inactivity that terminates the currentsession. The default value is 1800000 (30 minutes).mFirebaseAnalytics.setSessionTimeoutDuration(600000);
        if (savedInstanceState == null) {
            // To get the currently signed-in user by calling getCurrentUser
            // If no currently user signed in, we'll redirect the user tologin screen else home screen
            Bundle bundle = new Bundle();
            if (fAuth.getCurrentUser() != null) {
                mContent = HomeFragment.newInstance();
                bundle.putString(FirebaseAnalytics.Param.DESTINATION, "Home Fragment");
            } else {
                mContent = LoginSignUpFragment.newInstance();
                bundle.putString(FirebaseAnalytics.Param.DESTINATION, "Login Fragment");
            }
            FragmentManager fm = getSupportFragmentManager();
            // This Log event logs whether the user has logged in before ornot
            bundle.putString(FirebaseAnalytics.Param.ORIGIN, "Main Activity");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.content, mContent, FRAGMENT_TAG);
            ft.commit();
        } else {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mContent = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (mContent != null)
            getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mContent);
    }

}
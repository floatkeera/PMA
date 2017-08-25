package com.teephopk.pma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends YouTubeBaseActivity implements AuthenticationCallback, YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.PlaybackEventListener {


    LoginButton loginButton;
    YouTubePlayerView youTubeView;
    CustomTitleTextView mPromotionText;
    CallbackManager callbackManager;
    CustomButton mLoginButton;
    CustomButton mRegisterButton;
    CustomButton mSkipButton;
    YouTubePlayer mPlayer;
    private FirebaseAuth mAuth;

    int myMILLIS;

    public static String PUBLIC_KEY = "AIzaSyAIzCQYt-52mbDgP_NYvu8fEGyz64hq6CU";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        mPromotionText = (CustomTitleTextView) findViewById(R.id.txtWelcomePromo);
        mLoginButton = (CustomButton) findViewById(R.id.btnLogin);
        mRegisterButton = (CustomButton) findViewById(R.id.btnRegister);
        mSkipButton = (CustomButton) findViewById(R.id.btnSkip);







        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            youTubeView.initialize(PUBLIC_KEY, this);
        }



        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.fblogin_button);


        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                intent.putExtra("FACEBOOK_TOKEN", loginResult.getAccessToken());
                startActivity(intent);
                animateIntent();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                animateIntent();

            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent);
                animateIntent();
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mProgressDialog = new ProgressDialog(WelcomeActivity.this);
                mProgressDialog.setTitle("Please wait.");
                mProgressDialog.setMessage("Connecting to the store...");
                CubeGrid cubeGrid = new CubeGrid();
                cubeGrid.setColor(getColor(R.color.colorAccent));

                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminateDrawable(cubeGrid);

                mProgressDialog.show();





                mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
                        startActivity(intent);
                        animateIntent();
                    }
                });


            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {


            Intent intent = new Intent(WelcomeActivity.this, MenuActivity.class);
            startActivity(intent);


        }


    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPlayer != null) {
            myMILLIS = mPlayer.getCurrentTimeMillis();
        }

    }

    @Override
    public void onResume() {

        super.onResume();


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        if (mPlayer != null) {
            mPlayer.loadVideo("Hm5c8N1CwHs", myMILLIS);
//            mPlayer.seekToMillis(myMILLIS);
//            mPlayer.play();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onSuccessCallback() {

    }

    @Override
    public void onFailureCallback() {

    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mPlayer = youTubePlayer;
        mPlayer.setPlaybackEventListener(this);
        mPlayer.setPlayerStateChangeListener(this);
        youTubePlayer.loadVideo("Hm5c8N1CwHs");
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {
        mPlayer.play();


    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {
        Log.d("Pause", "");
    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {
        Log.d("", b ? "ty" : "no");
    }

    @Override
    public void onSeekTo(int i) {

    }

    void animateIntent(){
        this.overridePendingTransition(R.anim.enter_left, R.anim.exit_left);
    }
}

package automate.com.automate.activityfragment;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import automate.com.automate.R;
import automate.com.automate.util.BitmapWorkerTask;

public class LoginActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    // Google SignIn
    private GoogleApiClient googleApiClient;
    private ConnectionResult googleConnectionResult;
    private boolean googleSignInProgress;
    private boolean googleSignInButtonClicked;
    private final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ImageView loginImageView = (ImageView) findViewById(R.id.login_image);
        int REQ_WIDTH = 100;
        int REQ_HEIGHT = 100;
        new BitmapWorkerTask(this, loginImageView, REQ_WIDTH, REQ_HEIGHT).execute(R.id.login_image);

        SignInButton googleSignInButton = (SignInButton) findViewById(R.id.signin_button);
        setGooglePlusButtonText(googleSignInButton, getResources().getString(R.string.signin_string));
        googleSignInButton.setOnClickListener(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, new Plus.PlusOptions.Builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

        // Before displaying the activity check if sign in is already done
        if(googleApiClient.isConnected()) {
            Toast.makeText(this,
                    "Signed In!!",
                    Toast.LENGTH_LONG).show();
            this.moveToNavigationActivity();
        }
    }

    private void moveToNavigationActivity() {
        // Navigate to TabbedActionBar Activity
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    private void setGooglePlusButtonText(SignInButton signInButton,
                                         String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                int SIGN_IN_TEXT_SIZE = 20;
                tv.setTextSize(SIGN_IN_TEXT_SIZE);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void resolveGoogleSignInError() {
        if (googleConnectionResult.hasResolution()) {
            try {
                googleSignInProgress = true;
                googleConnectionResult.startResolutionForResult(this, REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                googleSignInProgress = false;
                googleApiClient.connect();
            }
        }
    }

    private void logUserInfo() {
        try{
            if(Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                String personName = person.getDisplayName();
                String personPhotoURL = person.getImage().getUrl();
                String personProfileURL = person.getUrl();
                String email = Plus.AccountApi.getAccountName(googleApiClient);

                Log.d("GooglePlusLogin", String.format(
                        "Name:: %s, GooglePlus Profile:: %s, Photo URL:: %s, Email:: %s",
                        personName, personProfileURL, personPhotoURL, email
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_button:
                if(googleApiClient.isConnected()) {
                    Toast.makeText(this,
                            "Signed In!!",
                            Toast.LENGTH_LONG).show();
                } else if(!googleApiClient.isConnecting()) {
                    googleSignInButtonClicked = true;
                    resolveGoogleSignInError();
                }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        googleSignInButtonClicked = false;
        Toast.makeText(this,
                "SignIn Success!",
                Toast.LENGTH_LONG).show();

        logUserInfo();

        this.moveToNavigationActivity();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(
                    connectionResult.getErrorCode(),
                    this,
                    0).show();
            return;
        }

        if(!googleSignInProgress) {
            googleConnectionResult = connectionResult;

            if(googleSignInButtonClicked) {
                resolveGoogleSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int respCode, Intent intent) {
        if(reqCode == REQUEST_CODE) {
            if(respCode != RESULT_OK) {
                googleSignInButtonClicked = false;
            }

            googleSignInProgress = false;

            if(googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }
}



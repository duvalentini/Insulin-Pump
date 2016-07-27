package com.example.gestalt.insulinpumpulator;


import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobile.user.IdentityProvider;
import com.amazonaws.mobile.user.signin.SignInManager;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.AmazonS3;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnConnectionFailedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Message name from tutorial that I was following, used in sending the app to the main page.
     */
    public final static String EXTRA_MESSAGE = "com.insulinpumpulator.tutorialMessage.";


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    ////////////////////
    private GoogleLoginTask mGoogleAuthTask = null;
    private final static String LOG_TAG = LoginActivity.class.getSimpleName();
    private SignInManager signInManager;

    // For dynamoDB
    private IdentityManager identityManager;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private DynamoDBMapper mapper;
    public static User mUser;
    private DBTask mDBTask = null;
    private GoogleSignInAccount acct;
    private String identityId;

    //s3 connection
    private AmazonS3 s3;
    public static TransferUtility s3TransferUtility;

    /**
     * Permission Request Code (Must be < 256).
     */
    private static final int GET_ACCOUNTS_PERMISSION_REQUEST_CODE = 93;

    /**
     * The Google OnClick listener, since we must override it to get permissions on Marshmallow and above.
     */
    private View.OnClickListener googleOnClickListener;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1;
    private String TAG = "LoginActivity";

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            //showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void logIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("REQUEST CODE = " + requestCode);
        System.out.println("RC_SIGN_IN = " + RC_SIGN_IN);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            System.out.println("RESULT = " + result);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        System.out.println("RESULT2 = " + result);
        System.out.println("RESULT STATUS = " + result.getStatus());
        System.out.println("RESULT ACCOUNT = " + result.getSignInAccount());
        int statusCode = result.getStatus().getStatusCode();
        System.out.println("STATUS CODE = " + statusCode);

        if (result.isSuccess()) {
            Toast.makeText(getApplicationContext(), "You are now logged in!", Toast.LENGTH_LONG).show();

            ////////////////////// CALL NEW TASK HERE
            mGoogleAuthTask = new GoogleLoginTask(result);
            mGoogleAuthTask.execute((Void) null);

//            Intent myIntent = new Intent(this, MainPageActivity.class);
//            this.startActivity(myIntent);

            /////// TOOK OUT CODE HERE /////////////////



            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(), "USER WAS NOT SUCCESSFULLY SIGNED IN :(", Toast.LENGTH_LONG).show();
            //updateUI(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
        setContentView(R.layout.activity_login);
        // Set up the login form.

        signInManager = SignInManager.getInstance(this);

        signInManager.setResultsHandler(this, new SignInResultsHandler());

        // START ///////////////////////
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                //.requestIdToken(getString(R.string.client_id))
                //.requestIdToken("581753661381-u3929pfgi7k6joscs713djtiiou76im9.apps.googleusercontent.com")
                .requestIdToken("581753661381-5b3ge2pr10avvdbl7lfhf5k64h6cl9ht.apps.googleusercontent.com")
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ImageButton signInButton = (ImageButton) findViewById(R.id.g_login_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

/*
        // Initialize sign-in buttons.
        googleOnClickListener =
                signInManager.initializeSignInButton(GoogleSignInProvider.class, findViewById(R.id.g_login_button));

        if (googleOnClickListener != null) {
            // if the onClick listener was null, initializeSignInButton will have removed the view.
            this.findViewById(R.id.g_login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final Activity thisActivity = LoginActivity.this;
                    if (ContextCompat.checkSelfPermission(thisActivity,
                            Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{Manifest.permission.GET_ACCOUNTS},
                                GET_ACCOUNTS_PERMISSION_REQUEST_CODE);
                        return;
                    }

                    // call the Google onClick listener.
                    googleOnClickListener.onClick(view);
                }
            });
        }
*/

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        if (requestCode == GET_ACCOUNTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.findViewById(R.id.g_login_button).callOnClick();
            } else {
                Log.i(LOG_TAG, "Permissions not granted for Google sign-in. :(");
            }
        }
    }
    /**
     * SignInResultsHandler handles the final result from sign in. Making it static is a best
     * practice since it may outlive the SplashActivity's life span.
     */
    private class SignInResultsHandler implements IdentityManager.SignInResultsHandler {
        /**
         * Receives the successful sign-in result and starts the main activity.
         * @param provider the identity provider used for sign-in.
         */
        @Override
        public void onSuccess(final IdentityProvider provider) {
            Log.d(LOG_TAG, String.format("User sign-in with %s succeeded",
                    provider.getDisplayName()));

            // The sign-in manager is no longer needed once signed in.
            SignInManager.dispose();

            Toast.makeText(LoginActivity.this, String.format("Sign-in with %s succeeded.",
                    provider.getDisplayName()), Toast.LENGTH_LONG).show();

            // Load user name and image.
            AWSMobileClient.defaultMobileClient()
                    .getIdentityManager().loadUserInfoAndImage(provider, new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Launching Main Activity...");
                    startActivity(new Intent(LoginActivity.this, MainPageActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    // finish should always be called on the main thread.
                    finish();
                }
            });
        }

        /**
         * Recieves the sign-in result indicating the user canceled and shows a toast.
         * @param provider the identity provider with which the user attempted sign-in.
         */
        @Override
        public void onCancel(final IdentityProvider provider) {
            Log.d(LOG_TAG, String.format("User sign-in with %s canceled.",
                    provider.getDisplayName()));

            Toast.makeText(LoginActivity.this, String.format("Sign-in with %s canceled.",
                    provider.getDisplayName()), Toast.LENGTH_LONG).show();
        }

        /**
         * Receives the sign-in result that an error occurred signing in and shows a toast.
         * @param provider the identity provider with which the user attempted sign-in.
         * @param ex the exception that occurred.
         */
        @Override
        public void onError(final IdentityProvider provider, final Exception ex) {
            Log.e(LOG_TAG, String.format("User Sign-in failed for %s : %s",
                    provider.getDisplayName(), ex.getMessage()), ex);

            final AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            errorDialogBuilder.setTitle("Sign-In Error");
            errorDialogBuilder.setMessage(
                    String.format("Sign-in with %s failed.\n%s", provider.getDisplayName(), ex.getMessage()));
            errorDialogBuilder.setNeutralButton("Ok", null);
            errorDialogBuilder.show();
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
        Sends you to the main page activity.
     */
    public void sendToMain(View view){
        Intent intent = new Intent(this, MainPageActivity.class);
        EditText editText = (EditText) findViewById(R.id.password);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }

    /////////////////////////////////////////////////////////////////////////
    public class GoogleLoginTask extends AsyncTask<Void, Void, Boolean> {

        //private final String mEmail;
        //private final String mPassword;
        private final GoogleSignInResult mResult;

        GoogleLoginTask(GoogleSignInResult result) {
            //mEmail = email;
            //mPassword = password;
            mResult = result;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            //////////////////////////////////////////////

            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = mResult.getSignInAccount();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
//            Toast.makeText(getApplicationContext(), "USER WAS SUCCESSFULLY SIGNED IN :)", Toast.LENGTH_LONG).show();

            // Initialize the Amazon Cognito credentials provider
            final CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:d4ea7b2f-a140-47a4-b2cc-2b5698e4e9ad", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            credentialsProvider.clear();
            credentialsProvider.clearCredentials();


            GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            GooglePlayServicesUtil.getRemoteContext(getApplicationContext());
            //AccountManager am = AccountManager.get(this);
            AccountManager am = AccountManager.get(getApplicationContext());
            //com.google.android.gms.plus.Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            android.accounts.Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            try {
                //String token = GoogleAuthUtil.getToken(getApplicationContext(), accounts[0],
                //        "audience:server:client_id:581753661381-u3929pfgi7k6joscs713djtiiou76im9.apps.googleusercontent.com");

                //String token = GoogleAuthUtil.getToken(getApplicationContext(), accounts[0],
                //        "audience:server:client_id:581753661381-323vpsrvnijvnbsku6882g0sjf89jnr1.apps.googleusercontent.com");

                //String token = GoogleAuthUtil.getToken(getApplicationContext(), accounts[0],
                //        "audience:server:client_id:960858442806248e063b18bd87fd4d8e8d8a4434");

                String xxxtoken = GoogleAuthUtil.getToken(getApplicationContext(), accounts[0],
                        "audience:server:client_id:581753661381-5b3ge2pr10avvdbl7lfhf5k64h6cl9ht.apps.googleusercontent.com");


                System.out.println("ACCOUNTS[0] = " + accounts[0]);
                System.out.println("CONTEXT = " + getApplicationContext());

                //String xxxtoken = GoogleAuthUtil.getToken(getApplicationContext(), accounts[0],
                //        "audience:server:client_id:581753661381-u3v67daj80ktje5nhvsd6nuqc36363i7.apps.googleusercontent.com");

                acct = mResult.getSignInAccount();
                String token = acct.getIdToken();
                System.out.println("ID Token: " + token);

                System.out.println("JUST GOT THE TOKEN = " + xxxtoken);

                System.out.println("NAME = " + acct.getDisplayName());



                Map<String, String> logins = new HashMap<String, String>();
                logins.put("accounts.google.com", token);
                //credentialsProvider.refresh();
                credentialsProvider.setLogins(logins);
                System.out.println("CREDENTIALS PROVIDER = " + credentialsProvider);
                System.out.println("LOGINS = " + logins);

                // seems right
                String cachedID = credentialsProvider.getCachedIdentityId();
                System.out.println("CACHED_IDENTITY_ID = " + cachedID);
                identityId = credentialsProvider.getIdentityId();
                System.out.println("IDENTITY_ID = " + identityId);


                ///////////////////////////////////////////////////////////////////
                //// COGNITO


                // setup AWS service configuration. Choosing default configuration
                ClientConfiguration clientConfiguration = new ClientConfiguration();

                // Create a CognitoUserPool object to refer to your user pool
                CognitoUserPool userPool = new CognitoUserPool(getApplicationContext(), "us-east-1_Vvz5YVUdO", "345b9s65qbq8lfs6p9785abc0r", "124psqofkdimiehhijlabjttfqg0t01ni7vatt3v8qleqdnml86e", clientConfiguration);
                // (context, poolID, clientID, clientsecret, clientconfig)

                // Create a CognitoUserAttributes object and add user attributes
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                // Add the user attributes. Attributes are added as key-value pairs
                // Adding user's given name.
                // Note that the key is "given_name" which is the OIDC claim for given name
                userAttributes.addAttribute("name", acct.getDisplayName());

                // Adding user's email address
                userAttributes.addAttribute("email", acct.getEmail());

                // Adding user's identityID
                userAttributes.addAttribute("custom:IdentityID", identityId);

                SignUpHandler signupCallback = new SignUpHandler() {

                    @Override
                    public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                        // Sign-up was successful
                        System.out.println("User sign up success!");

                        // Check if this user (cognitoUser) has to be confirmed
                        if(!userConfirmed) {
                            // This user has to be confirmed and a confirmation code was sent to the user
                            // cognitoUserCodeDeliveryDetails will indicate where the confirmation code was sent
                            // Get the confirmation code from user
                            System.out.println("User is not confirmed");
                        }
                        else {
                            // The user has already been confirmed
                            System.out.println("User is confirmed");
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        // Sign-up failed, check exception for the cause
                        System.out.println("User sign up failed!");
                        exception.printStackTrace();
                    }
                };

                /////////////////////////////
                final String password = "abcabc";

                System.out.println("Signing up user in background...");

                String email = acct.getEmail();
                String id = email.substring(0, email.indexOf('@'));
                System.out.println("COGNITOOOOOOOO ID = " + id);


                // Sign up this user
                userPool.signUpInBackground(id, password, userAttributes, null, signupCallback);

                // Implement callback handler for get details call
                GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
                    @Override
                    public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                        // The user detail are in cognitoUserDetails
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        // Fetch user details failed, check exception for the cause
                    }
                };

                //CognitoUser user = userPool.getUser(userId);
                CognitoUser cognitoUser = userPool.getCurrentUser();

                // Fetch the user details
                cognitoUser.getDetailsInBackground(getDetailsHandler);



                // Callback handler for the sign-in process
                AuthenticationHandler authenticationHandler = new AuthenticationHandler() {

                    @Override
                    public void onSuccess(CognitoUserSession cognitoUserSession) {
                        // Sign-in was successful, cognitoUserSession will contain tokens for the user
                        // Get id token from CognitoUserSession.
                        String idToken = cognitoUserSession.getIdToken().getJWTToken();

                        // Create a credentials provider, or use the existing provider.
                        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), "us-east-1:d4ea7b2f-a140-47a4-b2cc-2b5698e4e9ad", Regions.US_EAST_1);

                        // Set up as a credentials provider.
                        Map<String, String> logins = new HashMap<String, String>();
                        logins.put("cognito-idp.us-east-1.amazonaws.com/us-east-1_Vvz5YVUdO", cognitoUserSession.getIdToken().getJWTToken());
                        credentialsProvider.setLogins(logins);
                    }

                    @Override
                    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                        // The API needs user sign-in credentials to continue
                        AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, password, null);

                        // Pass the user sign-in credentials to the continuation
                        authenticationContinuation.setAuthenticationDetails(authenticationDetails);

                        // Allow the sign-in to continue
                        authenticationContinuation.continueTask();
                    }

                    @Override
                    public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
                        // Multi-factor authentication is required, get the verification code from user
//                        multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
                        // Allow the sign-in process to continue
                        multiFactorAuthenticationContinuation.continueTask();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        // Sign-in failed, check exception for the cause
                    }
                };

                // Sign-in the user
                cognitoUser.getSessionInBackground(authenticationHandler);



//                cognitoUser.getSessionInBackground(new AuthenticationHandler() {
//                    @Override
//                    public void onSuccess(CognitoUserSession session) {
//                        String idToken = session.getIdToken().getJWTToken();
//
//                        Map<String, String> logins = new HashMap<String, String>();
//                        logins.put("cognito-idp.us-east-1.amazonaws.com/us-east-1_MdlbQcbJE", session.getIdToken().getJWTToken());
//                        credentialsProvider.setLogins(logins);
//                    }

                    // Other unimplemented methods

//                });


                // TODO: At this point you should save this identifier so you won’t
                // have to make this call the next time a user connects

                System.out.println("XXXXXXXXXXXXXXXX");
            }
            catch (UserRecoverableAuthException e) {
                System.out.println("AAAAAAAAAAAAAAAAAAAA");
//                startActivityForResult(e.getIntent(), 888);
                e.printStackTrace();
            } catch (GoogleAuthException | IOException e) {
                System.out.println("BBBBBBBBBBBBBBBBBBBB");
                e.printStackTrace();
            }

            // TODO: register the new account here.

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }

            // Save user info to the database
            System.out.println("CALLING DB TASK NOW");
            mDBTask = new DBTask();
            mDBTask.execute((Void) null);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }

    public class DBTask extends AsyncTask<Void, Void, Boolean> {

        DBTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //for dynamoDB, will put somewhere else later
            // setup AWS service configuration. Choosing default configuration

            final CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:d4ea7b2f-a140-47a4-b2cc-2b5698e4e9ad", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            //ClientConfiguration clientConfiguration = new ClientConfiguration();
            //identityManager = new IdentityManager(this, clientConfiguration);
            //credentialsProvider = identityManager.getCredentialsProvider();
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
            mapper = new DynamoDBMapper(ddbClient);
            // TODO: Set the mapper above to only update if attribute doesn't exist


            try {
                // Pull the pump settings if they exist
                mUser = mapper.load(User.class, identityId);
                System.out.println("THE USER WAS LOADED FROM DB SUCCESSFULLY");
            }
            catch (AmazonServiceException ase) {
                System.err.println("Could not complete operation");
                System.err.println("Error Message:  " + ase.getMessage());
                System.err.println("HTTP Status:    " + ase.getStatusCode());
                System.err.println("AWS Error Code: " + ase.getErrorCode());
                System.err.println("Error Type:     " + ase.getErrorType());
                System.err.println("Request ID:     " + ase.getRequestId());
                mUser = new User();

            } catch (AmazonClientException ace) {
                System.err.println("Internal error occured communicating with DynamoDB");
                System.out.println("Error Message:  " + ace.getMessage());
                mUser = new User();
            }

            if (mUser == null) {
                mUser = new User();
            }

            // Set the user info
            mUser.setIdentityID(identityId);
            mUser.setEmail(acct.getEmail());
            mUser.setName(acct.getDisplayName());
            String email = acct.getEmail();
            String username = email.substring(0, email.indexOf('@'));
            mUser.setUsername(username);

            try {
                // Save the user info to the db
                mapper.batchSave(Arrays.asList(mUser));
            }
            catch (AmazonServiceException ase) {
                System.err.println("Could not complete operation");
                System.err.println("Error Message:  " + ase.getMessage());
                System.err.println("HTTP Status:    " + ase.getStatusCode());
                System.err.println("AWS Error Code: " + ase.getErrorCode());
                System.err.println("Error Type:     " + ase.getErrorType());
                System.err.println("Request ID:     " + ase.getRequestId());

            } catch (AmazonClientException ace) {
                System.err.println("Internal error occured communicating with DynamoDB");
                System.out.println("Error Message:  " + ace.getMessage());
            }


            //creating s3 setup
            //  s3 = new AmazonS3Client(credentialsProvider);
            /* don't think need this */
            //   s3.setRegion(Region.getRegion(Regions.US_WEST_2));

            //setting up transfer utility for uploading to s3
            //   s3TransferUtility = new TransferUtility(s3, getApplicationContext());

            //uploading file to s3
         //   File fileUp = new File("/Users/aloverfield/Documents/AndroidApp/InsulinPumpulator/app/src/main/assets/test_file.txt");
         //   File fileDown = new File("/InsulinPumpulator/app/src/main/assets/down_test_file.txt");
         //   TransferObserver observer = s3TransferUtility.download("user-audio-files", "s3textdownload.txt", fileDown);


          //  transferObserverListener(observer);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                finish();
            } else {
            }

            System.out.println("CALLING MAINPAGEACTIVITY INTENT NOW");
            Intent myIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            startActivity(myIntent);
        }

        @Override
        protected void onCancelled() {

        }


    }

}


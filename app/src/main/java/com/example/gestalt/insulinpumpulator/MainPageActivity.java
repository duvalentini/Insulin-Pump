package com.example.gestalt.insulinpumpulator;

import android.os.AsyncTask;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.*;

import com.amazonaws.services.dynamodbv2.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;




public class MainPageActivity extends FragmentActivity implements MainPageNavButtons.OnFragmentInteractionListener {


    private String msg = "Android : ";

    //for dynamoDB, will move later
    private IdentityManager identityManager;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private DynamoDBMapper mapper;
    private Book book;
    private DBTask mDBTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_section) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            MainPageNavButtons firstFragment = new MainPageNavButtons();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_section, firstFragment).commit();
        }
    }


    public void swapFragment(Fragment fragment) { //add int containerID to specify which fragment container
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back

        //dynamoDB test, will remove later
//        if (fragment.getId() == R.id.bConnections) {
//            //      mapper.save(book);
//        }
        transaction.replace(R.id.fragment_section, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
        }




    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }

    public class DBTask extends AsyncTask<Void, Void, Boolean> {

        DBTask() {}

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

            AmazonDynamoDB dynamoDB;

            Map<String, AttributeValue> map = new HashMap<>();

            AttributeValue attributeValue = new AttributeValue("6666666");

            map.put("ISBN", attributeValue);

            ddbClient.putItem("Books", map);


//        DynamoDBTable table = dynamoDB
//                getTable("Movies");

            book = new Book();
            book.setIsbn("666666666");

            //dynamoDB test, will remove later
//            mapper.save(book);
            mapper.batchSave(Arrays.asList(book));

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            if (success) {
                //finish();
            } else {

            }
        }

        @Override
        protected void onCancelled() {

        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

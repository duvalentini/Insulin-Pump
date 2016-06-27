package com.example.gestalt.insulinpumpulator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt Carney on 6/18/16.
 */

public class PumpSettingsFragment extends Fragment{

    private LinearLayout mLayout;
    private int basalCount;
    private int layoutChildCounter;
    private DynamoDBMapper mapper;
    private DBTask mDBTask = null;
    private TextInputEditText mPumpType;
    private String pumpType;
    private TextInputEditText mTargetBloodSugar;
    private float targetBloodSugar;
    private TextInputEditText mInsulinCarbRatio;
    private float insulinCarbRatio;
    private TextInputEditText mActiveInsulinTime;
    private float activeInsulinTime;
    private TextInputEditText mSensitivity;
    private float sensitivity;
    private List<TextInputEditText> mBasalRates = new ArrayList<>();
    private List<Float> basalRates = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pump_settings, container, false);


        Button addBasalRate = (Button) view.findViewById(R.id.add_basal_rate);
        addBasalRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Add Basal Rate Pressed");

                // Add a new basal rate input
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.basal_rate, mLayout);
                mBasalRates.add(mBasalRates.size(), (TextInputEditText) mLayout.getChildAt(layoutChildCounter).findViewById(R.id.basal_rate_new));
                layoutChildCounter++;
                basalCount++;
        }
        });

        Button update = (Button) view.findViewById(R.id.update_pump_settings_button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Update Pump Settings Pressed");

                // Get the values from the editTexts
                pumpType = mPumpType.getText().toString();
                targetBloodSugar = Float.valueOf(mTargetBloodSugar.getText().toString());
                insulinCarbRatio = Float.valueOf(mInsulinCarbRatio.getText().toString());
                activeInsulinTime = Float.valueOf(mActiveInsulinTime.getText().toString());
                sensitivity = Float.valueOf(mSensitivity.getText().toString());

                int size = mBasalRates.size();
                for (int i=0; i < size; i++) {
                    if (!mBasalRates.get(i).getText().toString().equals("") && !basalRates.contains(Float.valueOf(mBasalRates.get(i).getText().toString()))) {
                        basalRates.add(i, Float.valueOf(mBasalRates.get(i).getText().toString()));
                    } else if (mBasalRates.get(i).getText().toString().equals("")) {
                        basalRates.remove(i);
                        size--;
                    }
                }

                // Run the DB AsyncTask to save the values to the database
                mDBTask = new DBTask();
                mDBTask.execute((Void) null);

            }
        });

        // Set the layout that contains the new basal rates
        mLayout = (LinearLayout) view.findViewById(R.id.editTextGroupLayout);

        // Get references to editTexts
        mPumpType = (TextInputEditText) view.findViewById(R.id.pump_type);
        mTargetBloodSugar = (TextInputEditText) view.findViewById(R.id.target_blood_sugar);
        mInsulinCarbRatio = (TextInputEditText) view.findViewById(R.id.insulin_carb_ratio);
        mActiveInsulinTime = (TextInputEditText) view.findViewById(R.id.active_insulin_time);
        mSensitivity = (TextInputEditText) view.findViewById(R.id.sensitivity);

        basalCount = 1;
        layoutChildCounter = 0;
        if (LoginActivity.mUser.getBasalRates() != null) {
            basalRates = LoginActivity.mUser.getBasalRates();
            basalRates.removeAll(Collections.singleton(null));
            basalCount = basalRates.size();
        }

        // Add initial basal rate box to list of buttons
        mBasalRates.add(0, (TextInputEditText) view.findViewById(R.id.basal_rate));

        if (basalRates != null) {
            for (int i = 1; i < basalRates.size(); i++) {
                addBasalRate.callOnClick();
                basalCount--;
            }
        }


        // Set the existing settings
        mPumpType.setText(LoginActivity.mUser.getPumpType());
        mTargetBloodSugar.setText(Float.toString(LoginActivity.mUser.getTargetBloodSugar()));
        mInsulinCarbRatio.setText(Float.toString(LoginActivity.mUser.getInsulinCarbRatio()));
        mActiveInsulinTime.setText(Float.toString(LoginActivity.mUser.getActiveInsulinTime()));
        mSensitivity.setText(Float.toString(LoginActivity.mUser.getSensitivity()));
        // Set Basal Rates
        if (basalRates.size() > 0) {
            for (int i = 0; i < basalCount; i++) {
                mBasalRates.get(i).setText(basalRates.get(i).toString());
            }
        }


        return view;
    }


    public class DBTask extends AsyncTask<Void, Void, Boolean> {

        DBTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            // setup AWS service configuration
            final CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getActivity().getApplicationContext(),
                    "us-east-1:d4ea7b2f-a140-47a4-b2cc-2b5698e4e9ad", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
            mapper = new DynamoDBMapper(ddbClient, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));

            // Set the user info
            LoginActivity.mUser.setPumpType(pumpType);
            LoginActivity.mUser.setTargetBloodSugar(targetBloodSugar);
            LoginActivity.mUser.setInsulinCarbRatio(insulinCarbRatio);
            LoginActivity.mUser.setActiveInsulinTime(activeInsulinTime);
            LoginActivity.mUser.setSensitivity(sensitivity);
            // Remove all nulls from basal rates list
            basalRates.removeAll(Collections.singleton(null));
            LoginActivity.mUser.setBasalRates(basalRates);


            try {
                // Save the user info to the db
                mapper.batchSave(LoginActivity.mUser);
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

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
//                finish();
            } else {
            }

            // Go back to the main page activity
            getActivity().onBackPressed();
        }

        @Override
        protected void onCancelled() {

        }


    }

}

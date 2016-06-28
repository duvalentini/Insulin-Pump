package com.example.gestalt.insulinpumpulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


/**
 * Created by aloverfield on 6/25/16.
 */

public class S3Activity extends AppCompatActivity {

    File fileToUpload;
    File fileToDownload;
    AmazonS3 s3;
    TransferUtility transferUtility;

    Button upload;
    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s3_layout);

        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        String test = "This is a test string for the test file to be uploaded to S3.";

        try {
            //saving the file as a xml
            FileOutputStream fOut = openFileOutput("test_file.txt", getApplicationContext().MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(test);
            osw.flush();
            osw.close();
        }
        catch (Throwable e) {
            Log.d("Exception","Exception:"+ e);
        }

        fileToUpload = getFileStreamPath("test_file.txt");

        try {
            //saving the file as a xml
            FileOutputStream fOut = openFileOutput("test_download.txt", getApplicationContext().MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write("");
            osw.flush();
            osw.close();
        }
        catch (Throwable e) {
            Log.d("Exception","Exception:"+ e);
        }

        fileToDownload = getFileStreamPath("test_download.txt");

        upload = (Button) findViewById(R.id.upload_s3);
        download = (Button) findViewById(R.id.download_s3);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                System.out.println(fileToUpload.canRead() + " " + fileToUpload.canWrite());
                setFileToUpload(v);
                System.out.println("File to upload");
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("File to download");
                setFileToDownload(v);
                System.out.println("File to download");
            }
        });
    }

    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:d4ea7b2f-a140-47a4-b2cc-2b5698e4e9ad", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    /**
     *  Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     */
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

    }

    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     * @param view
     */
    public void setFileToUpload(View view){

        System.out.println("In file upload");


        TransferObserver transferObserver = transferUtility.upload(
                "user-audio-files",     /* The bucket to upload to */
                "test_file.txt",    /* The key for the uploaded object */
                fileToUpload       /* The file where the data to upload exists */
        );

        System.out.println("");


        transferObserverListener(transferObserver);
    }

    /**
     *  This method is used to Download the file to S3 by using transferUtility class
     * @param view
     **/
    public void setFileToDownload(View view){

        TransferObserver transferObserver = transferUtility.download(
                "user-audio-files",     /* The bucket to download from */
                "s3textdownload.txt",    /* The key for the object to download */
                fileToDownload        /* The file to download the object to */
        );

        transferObserverListener(transferObserver);

    }

    /**
     * This is listener method of the TransferObserver
     * Within this listener method, we get status of uploading and downloading file,
     * to display percentage of the part of file to be uploaded or downloaded to S3
     * It displays an error, when there is a problem in  uploading or downloading file to or from S3.
     * @param transferObserver
     */

    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }

        });
    }
}

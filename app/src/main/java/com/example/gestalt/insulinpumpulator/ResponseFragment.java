package com.example.gestalt.insulinpumpulator;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Activity;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.util.Log;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Matt Carney on 6/18/16.
 */

public class ResponseFragment extends Fragment{

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = "test.mp3";

    private Button mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private Button   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private Button mSaveAudioButton = null;
    private Button mSaveTextButton = null;

    boolean mStartRecording;
    boolean mStartPlaying;

    private File audioFileToUpload;
    private File textFileToUpload;
    FileDescriptor fd;
    private AmazonS3 s3;
    private TransferUtility transferUtility;

    private TextInputEditText text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_response, container, false);

        // Create output audio file
//        try {
//            FileOutputStream fOut = getActivity().openFileOutput("test.mp3", getActivity().MODE_PRIVATE);
//            FileOutputStream fOut = getActivity().openFileOutput(Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + "/test.mp3", getActivity().MODE_PRIVATE);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        audioFileToUpload = getActivity().getFileStreamPath("test.mp3");
//        fileToUpload = getActivity().getFileStreamPath(Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/test.mp3");

        mRecordButton = (Button) view.findViewById(R.id.record_response);
        mRecordButton.setText("Start recording");
        mStartRecording = true;
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Record Pressed");

                onRecord(mStartRecording);
                if (mStartRecording) {
                    mRecordButton.setText("Stop recording");
                } else {
                    mRecordButton.setText("Start recording");
                }
                mStartRecording = !mStartRecording;

            }
        });

        mPlayButton = (Button) view.findViewById(R.id.play_response);
        mPlayButton.setText("Start playing");
        mStartPlaying = true;
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Play Pressed");

                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    mPlayButton.setText("Stop playing");
                } else {
                    mPlayButton.setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;

            }
        });

        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        mSaveAudioButton = (Button) view.findViewById(R.id.save_audio_response);
        mSaveAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Save Recording Pressed");

                audioFileToUpload = getActivity().getFileStreamPath("test.mp3");

                String name = LoginActivity.mUser.getName().replaceAll("\\s+","");
                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());

                TransferObserver transferObserver = transferUtility.upload(
                        "user-audio-files",     /* The bucket to upload to */
                        name + currentDateandTime + ".mp3",    /* The key for the uploaded object */
                        audioFileToUpload       /* The file where the data to upload exists */
                );

                transferObserverListener(transferObserver);

                Toast.makeText(getActivity().getApplicationContext(), "Your recording has been saved!", Toast.LENGTH_LONG).show();

            }
        });

        text = (TextInputEditText) view.findViewById(R.id.text_response);

        mSaveTextButton = (Button) view.findViewById(R.id.save_text_response);
        mSaveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Save Text Pressed");

                try {
                    FileOutputStream fOut = getActivity().openFileOutput("test.txt", getActivity().getApplicationContext().MODE_PRIVATE);
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);
                    osw.write(text.getText().toString());
                    osw.flush();
                    osw.close();
                    // TODO: close fOut?
                } catch (IOException e) {
                    e.printStackTrace();
                }

                textFileToUpload = getActivity().getFileStreamPath("test.txt");

                String name = LoginActivity.mUser.getName().replaceAll("\\s+","");
                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());

                TransferObserver transferObserver = transferUtility.upload(
                        "user-audio-files",     /* The bucket to upload to */
                        name + currentDateandTime + ".txt",    /* The key for the uploaded object */
                        textFileToUpload       /* The file where the data to upload exists */
                );

                transferObserverListener(transferObserver);

                Toast.makeText(getActivity().getApplicationContext(), "Your text response has been saved!", Toast.LENGTH_LONG).show();

            }
        });


        return view;
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(audioFileToUpload.getAbsolutePath());
//            mPlayer.setDataSource(Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + "/test.mp3");
//            mPlayer.setDataSource(fd);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mRecorder.setOutputFile(Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/test.mp3");
        mRecorder.setOutputFile(audioFileToUpload.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            e.printStackTrace();
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public void AudioRecordTest() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getActivity().getApplicationContext(),
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

        transferUtility = new TransferUtility(s3, getActivity().getApplicationContext());
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

    public class DBTask extends AsyncTask<Void, Void, Boolean> {

        DBTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {




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

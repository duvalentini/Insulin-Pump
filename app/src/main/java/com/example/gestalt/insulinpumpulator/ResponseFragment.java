package com.example.gestalt.insulinpumpulator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Permission;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.Permissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

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
    private AmazonS3 s3;
    private TransferUtility transferUtility;

    private TextInputEditText text;

    private ArrayList<String> audioFiles;
    private ArrayList<String> textFiles;

    private ArrayAdapter<String> audioAdapter;
    private ArrayAdapter<String> textAdapter;

    private ListView audioList;
    private ListView textList;

    private File audioFileToDownload;
    private File textFileToDownload;

    private DynamoDBMapper mapper;
    private DBTask mDBTask = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_response, container, false);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        }

        // Get references to ListViews
        audioList = (ListView) view.findViewById(R.id.audio_list);
        textList = (ListView) view.findViewById(R.id.text_list);

        // Get user's previous responses
        audioFiles = LoginActivity.mUser.getAudioFiles();
        if (audioFiles == null) {
            audioFiles = new ArrayList<>();
        }
        textFiles = LoginActivity.mUser.getTextFiles();
        if (textFiles == null) {
            textFiles = new ArrayList<>();
        }

        // Set up audio adapter
        audioAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, audioFiles);
        audioList.setAdapter(audioAdapter);

        // Set up text adapter
        textAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, textFiles);
        textList.setAdapter(textAdapter);


        audioFileToUpload = getActivity().getFileStreamPath("test.mp3");

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

                onPlay(mStartPlaying, audioFileToUpload);
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

                // Save file name
                audioFiles.add(name + currentDateandTime + ".mp3");
                // Update UI
                updateUI();
                // Run the DB AsyncTask to save the values to the database
                mDBTask = new DBTask();
                mDBTask.execute((Void) null);

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

                // Save file name
                textFiles.add(name + currentDateandTime + ".txt");
                // Update UI
                updateUI();
                // Run the DB AsyncTask to save the values to the database
                mDBTask = new DBTask();
                mDBTask.execute((Void) null);

                Toast.makeText(getActivity().getApplicationContext(), "Your text response has been saved!", Toast.LENGTH_LONG).show();

            }
        });

        ////////////////////////////////////////////////////////////////
        // MY RESPONSES STUFF
        ////////////////////////////////////////////////////////////////

        audioList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                String fileName = ((TextView) v.findViewById(audioList.getChildAt(i).getId())).getText().toString();
                System.out.println("FILENAME = " + fileName);

                audioFileToDownload = getActivity().getFileStreamPath("temp.mp3");
                audioFileToDownload.delete();

                if (mPlayer != null) {
                    mPlayer.release();
                    mPlayer = null;
                }

                TransferObserver transferObserver = transferUtility.download(
                        "user-audio-files",     /* The bucket to download from */
                        fileName,    /* The key for the object to download */
                        audioFileToDownload        /* The file to download the object to */
                );

                transferAudioDownloadObserverListener(transferObserver);


            }
        });

        textList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                System.out.println("Text Item Clicked");

                String fileName = ((TextView) v.findViewById(textList.getChildAt(i).getId())).getText().toString();
                System.out.println("FILENAME = " + fileName);

                textFileToDownload = getActivity().getFileStreamPath("temp.txt");
                textFileToDownload.delete();

                TransferObserver transferObserver = transferUtility.download(
                        "user-audio-files",     /* The bucket to download from */
                        fileName,    /* The key for the object to download */
                        textFileToDownload        /* The file to download the object to */
                );

                transferTextDownloadObserverListener(transferObserver, v);


            }
        });



        updateUI();




        return view;
    }

    private void updateUI() {

        LoginActivity.mUser.setAudioFiles(audioFiles);
        LoginActivity.mUser.setTextFiles(textFiles);
        audioAdapter.notifyDataSetChanged();
        textAdapter.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(audioList);
        setListViewHeightBasedOnChildren(textList);

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start, File file) {
        if (start) {
            startPlaying(file);
        } else {
            stopPlaying();
        }
    }

    private void startPlaying(File file) {
        mPlayer = new MediaPlayer();
        try {
//            mPlayer.setDataSource(audioFileToUpload.getAbsolutePath());
            mPlayer.setDataSource(file.getAbsolutePath());
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
                if (bytesTotal != 0) {
                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
                    Log.e("percentage", percentage + "");
                } else {
                    System.out.println("TOTAL BYTES = 0");
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }

        });
    }

    /**
     * For downloading audio
     * @param transferObserver
     */

    public void transferAudioDownloadObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");

                if (state == TransferState.COMPLETED) {
                    mPlayer = new MediaPlayer();
                    try {
                        mPlayer.setDataSource(audioFileToDownload.getAbsolutePath());
                        mPlayer.prepare();
                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                System.out.println("DONE PLAYING FILE");
                                mPlayer.release();
                                mPlayer = null;
                            }

                        });
                        mPlayer.start();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                        e.printStackTrace();
                    }
                }


            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                if (bytesTotal != 0) {
                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
                    Log.e("percentage", percentage + "");
                } else {
                    System.out.println("TOTAL BYTES = 0");
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }

        });
    }

    // For text
    public void transferTextDownloadObserverListener(TransferObserver transferObserver, final View v){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");


                if (state == TransferState.COMPLETED) {
                    // Open file for reading

                    // Get text of file and use in showPopup
                    String text = "AAAAAA";
                    try {
                        String content = new Scanner(textFileToDownload).useDelimiter("\\Z").next();
                        System.out.println(content);
                        text = content;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                    showPopup(v, text);
                }

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                if (bytesTotal != 0) {
                    int percentage = (int) (bytesCurrent / bytesTotal * 100);
                    Log.e("percentage", percentage + "");
                } else {
                    System.out.println("TOTAL BYTES = 0");
                }
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

            // setup AWS service configuration
            final CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getActivity().getApplicationContext(),
                    "us-east-1:d4ea7b2f-a140-47a4-b2cc-2b5698e4e9ad", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
            mapper = new DynamoDBMapper(ddbClient, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.UPDATE));

            // Set the user info
            LoginActivity.mUser.setAudioFiles(audioFiles);
            LoginActivity.mUser.setTextFiles(textFiles);


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
//            getActivity().onBackPressed();
        }

        @Override
        protected void onCancelled() {

        }


    }

    public static void setListViewHeightBasedOnChildren(final ListView listView) {
        listView.post(new Runnable() {
            @Override
            public void run() {
                ListAdapter listAdapter = listView.getAdapter();
                if (listAdapter == null) {
                    return;
                }
                int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
                int listWidth = listView.getMeasuredWidth();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(
                            View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


                    totalHeight += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = (int) ((totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))));
                listView.setLayoutParams(params);
                listView.requestLayout();

            }
        });
    }

    public void showPopup(View anchorView, String text) {

        System.out.println("IN SHOW POPUP");

        View popupView = getActivity().getLayoutInflater().inflate(R.layout.fragment_text, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
        TextView tv = (TextView) popupView.findViewById(R.id.pop_up);

        tv.setText(text);

        // Initialize more widgets from `popup_layout.xml`


        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

//        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, anchorView.getWidth()/2, anchorView.getHeight()/2);


        System.out.println("END SHOW POPUP");

    }

}

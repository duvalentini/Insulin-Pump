package com.example.gestalt.insulinpumpulator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment scenarioSelection;
    private Fragment avatarSelection;
    private Button scenarioSelectionButton;
    private Button avatarSelectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        scenarioSelection = new Fragment();
        avatarSelection = new Fragment();

        scenarioSelectionButton = (Button) findViewById(R.id.scenario_select);
        avatarSelectionButton = (Button) findViewById(R.id.avatarSelectionFragment);

        scenarioSelectionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fragmentTransaction.add(R.id.scenarioSelectionFragment, scenarioSelection);
                fragmentTransaction.commit();
            }
        });

        avatarSelectionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fragmentTransaction.add(R.id.avatarSelectionFragment, avatarSelection);
                fragmentTransaction.commit();
            }
        });
    }

    Intent intent = getIntent();

    public void logout(View view){
        this.finish();
    }
}

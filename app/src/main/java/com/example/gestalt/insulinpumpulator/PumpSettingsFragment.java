package com.example.gestalt.insulinpumpulator;

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

/**
 * Created by Matt Carney on 6/18/16.
 */

public class PumpSettingsFragment extends Fragment{

    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pump_settings, container, false);

        mLayout = (LinearLayout) view.findViewById(R.id.editTextGroupLayout);

        Button addBasalRate = (Button) view.findViewById(R.id.add_basal_rate);
        addBasalRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Add Basal Rate Pressed");

                mEditText = createNewEditText("Basal Rate");
                // TODO: Add attributes so it looks like the other basal rate boxes

                mLayout.addView(mEditText);


                //createNewEditText("Basal Rate 1");
                //addBasalRateEditText();
            }
        });







        return view;
    }


    private EditText createNewEditText(String text) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(getActivity());
        editText.setLayoutParams(lparams);
        editText.setText("New text: " + text);
        return editText;
    }



    private void addBasalRateEditText() {

//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.editTextGroupLayout);
//        EditText editTextView = new EditText(this);
//        editTextView.setGravity(Gravity.CENTER);

//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT, 1);

//        editTextView.setLayoutParams(params);

//        linearLayout.addView(editTextView);

    }


}

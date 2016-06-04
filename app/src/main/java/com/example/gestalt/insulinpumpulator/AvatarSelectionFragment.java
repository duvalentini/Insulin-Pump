package com.example.gestalt.insulinpumpulator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by aloverfield on 5/25/16.
 */

public class AvatarSelectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avatar_selection, container, false);
        Button doSomething = (Button) view.findViewById(R.id.do_something);
        doSomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do a thing
                System.out.println("Boop pressed");
            }
        });
        Button doSomethingElse = (Button) view.findViewById(R.id.do_something_else);
        doSomethingElse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do a different thing
                System.out.println("Doop pressed");
            }
        });
        return view;
    }
}

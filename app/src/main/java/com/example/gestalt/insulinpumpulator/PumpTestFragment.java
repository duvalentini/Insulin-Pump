package com.example.gestalt.insulinpumpulator;

/**
 * Created by Joshua on 6/27/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;




public class PumpTestFragment extends Fragment {

    private LinearLayout mLayout;
    private ArrayList<String> subMenuNames;
    private MedtronicPump pump;
    private TextView subMenu1;
    private TextView subMenu2;
    private TextView subMenu3;
    private TextView menuName;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pump_test, container, false);


        pump = new MedtronicPump(getContext());
        subMenuNames = pump.getSubMenuNames();

        menuName = (TextView) view.findViewById(R.id.pumpMenuName);
        menuName.setText(pump.getMenuName());
        subMenuNames = pump.getSubMenuNames();

        subMenu1 =(TextView) view.findViewById(R.id.subMenu1);
        subMenu2 = (TextView) view.findViewById(R.id.subMenu2);
        subMenu3 = (TextView) view.findViewById(R.id.subMenu3);
        setMenuNames(subMenu1,subMenu2,subMenu3);

        Button up = (Button) view.findViewById(R.id.upButton);//*************
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Pressed the Up Button");
                pump.up();
                setMenuNames(subMenu1, subMenu2, subMenu3);
            }
        });

        Button down = (Button) view.findViewById(R.id.downButton);//*************
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Down Pressed");
                pump.down();
                setMenuNames(subMenu1, subMenu2, subMenu3);

            }
        });
        Button enter = (Button) view.findViewById(R.id.enterButton);//*************
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Enter Pressed");
                pump.confirm();
                menuName.setText(pump.getMenuName());
                subMenuNames= pump.getSubMenuNames();
                setMenuNames(subMenu1, subMenu2, subMenu3);

            }
        });
        Button back = (Button) view.findViewById(R.id.backButton);//*************
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Back Pressed");
                pump.back();
                menuName.setText(pump.getMenuName());
                subMenuNames= pump.getSubMenuNames();
                setMenuNames(subMenu1, subMenu2, subMenu3);

            }
        });


        return view;
    }

    private void setMenuNames(TextView one, TextView two, TextView three){
        int current = pump.getSubCurrent();
        int top;
        int bottom;
        int max = subMenuNames.size();
        if(current+3>max){
            bottom = max-1;
        }
        else{
            bottom = current+2;
        }
        if(current-3<0){
            top = 0;
        }
        else{
            top = current-2;
        }
        if(subMenuNames.size()<=3){
            if(current ==0){
                one.setText(subMenuNames.get(current)+"<");
            }
            else if(subMenuNames.size()>0){
                one.setText(subMenuNames.get(0));
            }
            else{
                one.setText("");
            }
            if(current ==1){
                two.setText(subMenuNames.get(current)+"<");
            }
            else if(subMenuNames.size()>1){
                two.setText(subMenuNames.get(1));
            }
            else{
                two.setText("");
            }
            if(current ==2){
                three.setText(subMenuNames.get(current)+"<");
            }
            else if(subMenuNames.size()>2){
                three.setText(subMenuNames.get(2));
            }
            else{
                three.setText("");
            }

        }
        else{
            if(current==top){
                one.setText(subMenuNames.get(top)+"<");
                two.setText(subMenuNames.get(top+1));
                three.setText(subMenuNames.get(top+2));
            }
            else if(current==bottom){
                three.setText(subMenuNames.get(bottom)+"<");
                two.setText(subMenuNames.get(bottom-1));
                one.setText(subMenuNames.get(bottom-2));
            }
            else{
                one.setText(subMenuNames.get(current-1));
                two.setText(subMenuNames.get(current)+"<");
                three.setText(subMenuNames.get(current+1));
            }

        }
    }
}

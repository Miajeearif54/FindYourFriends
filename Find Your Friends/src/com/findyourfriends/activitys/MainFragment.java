package com.findyourfriends.activitys;

import com.les.findyourfriends.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment{
    
    @Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        //ve se esse R.layout eh isso mesmo. O original era .main
        View view = inflater.inflate(R.layout.activity_main, container, false);

        return view;
    }

}

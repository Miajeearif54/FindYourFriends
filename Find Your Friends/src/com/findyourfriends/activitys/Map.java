package com.findyourfriends.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.les.findyourfriends.R;


/**
 * Copyright (C) 2014 Embedded Systems and Pervasive Computing Lab - UFCG
 * All rights reserved.
 */

public class Map extends Activity {

	private Context mContext;
	private GoogleMap googleMap;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mContext = getApplicationContext();

		initilizeMap();
	}

	private void initilizeMap() {
		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}

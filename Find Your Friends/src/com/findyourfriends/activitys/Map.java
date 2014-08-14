package com.findyourfriends.activitys;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.les.findyourfriends.R;


/**
 * Copyright (C) 2014 Embedded Systems and Pervasive Computing Lab - UFCG
 * All rights reserved.
 */

public class Map extends Activity implements LocationListener {

	private Context mContext;
	private GoogleMap googleMap;
	private LocationManager locationManager;
    private String provider;
    private Marker startPerc;
    
    private ImageButton editar, grupos, meusGrupos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mContext = getApplicationContext();
		
		initilizeMap();
		
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabledGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        habilitaGPS(enabledGPS);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }
        
        grupos = (ImageButton) findViewById(R.id.grupos);        
        grupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
			    Intent i = new Intent(mContext, ViewGroupActivity.class);
			    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
			    
			}
		});
              
        meusGrupos = (ImageButton) findViewById(R.id.meusGrupos);        
        meusGrupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, MeusGruposActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
        
        editar = (ImageButton) findViewById(R.id.editar);
        editar.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, EditarActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
        
	}

	private void habilitaGPS(boolean enabledGPS) {
		if (!enabledGPS) {
            Toast.makeText(this, "GPS signal not found", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
	}
	
	private void initilizeMap() {
		try {
			if (googleMap == null) {
				googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.setMyLocationEnabled(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if(startPerc != null){
			startPerc.remove();
		}
		
		double lat =  location.getLatitude();
        double lng = location.getLongitude();
        LatLng coordinate = new LatLng(lat, lng);
        
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));

        
       /* startPerc = googleMap.addMarker(new MarkerOptions()
        .position(coordinate)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));*/
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }
    
    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
       	locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
    
    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    
    

	
}

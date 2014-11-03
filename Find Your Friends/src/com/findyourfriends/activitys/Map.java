package com.findyourfriends.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import com.les.findyourfriends.R;

/**
 * Copyright (C) 2014 Embedded Systems and Pervasive Computing Lab - UFCG All
 * rights reserved.
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

        mContext = getApplicationContext();

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabledGPS = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        habilitaGPS(enabledGPS);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        Intent it = getIntent();
        boolean exibirBotoes = it.getBooleanExtra("mostrar_botoes", false);

        if (exibirBotoes) {
            setContentView(R.layout.map);

            initilizeMap(location, exibirBotoes);

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
        } else {
            setContentView(R.layout.mapa_grupo);
            initilizeMap(location, exibirBotoes);
        }

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        if (item.getItemId() == R.id.about) {
            final Intent i = new Intent(mContext, About.class);
            startActivity(i);
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void habilitaGPS(boolean enabledGPS) {
        if (!enabledGPS) {
            Toast.makeText(this, "GPS signal not found", Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void initilizeMap(Location location, boolean exibirBotoes) {
        try {
            if (googleMap == null) {
                if (exibirBotoes) {
                    googleMap = ((MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map)).getMap();
                } else {
                    Intent it2 = getIntent();
                    googleMap = ((MapFragment) getFragmentManager()
                            .findFragmentById(R.id.mapGrupo)).getMap();

                    ArrayList<String> usuariosParse = it2
                            .getStringArrayListExtra("NOMES");

                    ArrayList<String> latParse = it2
                            .getStringArrayListExtra("LATITUDE");

                    ArrayList<String> longParse = it2
                            .getStringArrayListExtra("LONGITUDE");

                    double latitude, longitude;
                    int indice = 0;

                    for (String nome : usuariosParse) {
                        String userName = mudaCaractere(nome, "_", " ");

                        if (indice < latParse.size()) {

                            longitude = Double.parseDouble(longParse
                                    .get(indice));
                            latitude = Double.parseDouble(latParse.get(indice));
                            
                            marcaUsuario(userName, latitude, longitude);
                            indice++;
                        }
                    }

                    googleMap.setOnMapClickListener(new OnMapClickListener() {

                        @Override
                        public void onMapClick(LatLng latLng) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("Ponto de encontro");

                            markerOptions.icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.marker));
                             //googleMap.clear();
                            googleMap.animateCamera(CameraUpdateFactory
                                    .newLatLng(latLng));
                            googleMap.addMarker(markerOptions);
                        }
                    });
                }
            }
            
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);

            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng coordinate = new LatLng(lat, lng);

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    coordinate, 15));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String mudaCaractere(String str, String antigo, String novo){
        str = str.replace(antigo, novo);
        return str;
    }

    private void marcaUsuario(String nome, double latitude, double longitude) {
        
        LatLng coordinate = new LatLng(latitude, longitude);
               
        startPerc = googleMap.addMarker(new MarkerOptions()
                .title(nome)
                .position(coordinate)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_launcher)).visible(true));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (startPerc != null) {
            //startPerc.remove();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
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

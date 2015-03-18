/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
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

public class Map extends Activity implements LocationListener,
        OnMapLoadedCallback {

    /** The Constant COUNTDOWNTIMER_PARAM. */
    private static final int COUNTDOWNTIMER_PARAM = 5000;
    
    /** The Constant COUNTDOWNTIMER_PARAM_LOADED. */
    private static final int COUNTDOWNTIMER_PARAM_LOADED = 10000;

    /** The m context. */
    private Context mContext;
    
    /** The google map. */
    private GoogleMap googleMap;
    
    /** The location manager. */
    private LocationManager locationManager;
    
    /** The provider. */
    private String provider;
    
    /** The start perc. */
    private Marker startPerc;

    /** The tipo de ponto dialog. */
    private AlertDialog tipoDePontoDialog;
    
    /** The tipos de ponto. */
    private final CharSequence[] tiposDePonto = { "Ponto de encontro",
            "Local especifico" };
    
    /** The ponto de econtro. */
    private boolean pontoDeEcontro;
    
    /** The loaded map. */
    private Boolean loadedMap = false;

    /** The meus grupos. */
    private ImageButton editar, grupos, meusGrupos;
    
    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";
    private String nomePontoBD;
    
    double latPonto, longePonto;
    
    String nomePontoEncontro;
    
    /** The id grupo. */
    private Integer idGrupo;
    
    
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        LocationManager service = 
                (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabledGPS = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        habilitaGPS(enabledGPS);

        locationManager = 
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        Intent it = getIntent();
        boolean exibirBotoes = it.getBooleanExtra("mostrar_botoes", false);
        // boolean exibirBotoes = false;
        Bundle param = it.getExtras();
        idGrupo = param.getInt("KEY_ID");
        latPonto = param.getDouble("latPonto");
        longePonto = param.getDouble("lngPonto");
        nomePontoEncontro = param.getString("nomePonto");
        
        

        if (exibirBotoes) {
            setContentView(R.layout.map);
            initilizeMap(location, exibirBotoes);
            countLoadedMap();
            if (location != null) {
                onLocationChanged(location);
            }

            grupos = (ImageButton) findViewById(R.id.grupos);
            grupos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent i = new Intent(mContext, ViewGroupActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);

                }
            });

            meusGrupos = (ImageButton) findViewById(R.id.meusGrupos);
            meusGrupos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent i = new Intent(mContext, MeusGruposActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
            });

            editar = (ImageButton) findViewById(R.id.editar);
            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent i = new Intent(mContext, EditarActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
            });
        } else {
            setContentView(R.layout.mapa_grupo);
            initilizeMap(location, exibirBotoes);
            if (location != null) {
                onLocationChanged(location);
            }
        }

    }

    /**
     * Count loaded map.
     */
    private void countLoadedMap() {
        final CountDownTimer counter = new CountDownTimer(
                COUNTDOWNTIMER_PARAM_LOADED, COUNTDOWNTIMER_PARAM_LOADED) {

            @Override
            public void onTick(final long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (!loadedMap) {
                    setInternetVisible();
                }
            }
        };
        counter.start();
    }

    /**
     * Sets the internet visible.
     */
    private void setInternetVisible() {
        final LinearLayout layout = 
                (LinearLayout) findViewById(R.id.LayoutNoInternet);
        layout.setVisibility(LinearLayout.VISIBLE);
        final CountDownTimer counter = new CountDownTimer(COUNTDOWNTIMER_PARAM,
                COUNTDOWNTIMER_PARAM) {

            @Override
            public void onTick(final long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                layout.setVisibility(LinearLayout.INVISIBLE);
            }
        };
        counter.start();

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
     */
    @Override
    public final boolean onMenuItemSelected(final int featureId, 
            final MenuItem item) {

        // about - depois mudar
       /* if (item.getItemId() == R.id.perfil) {
            final Intent i = new Intent(mContext, PerfilActivity.class);
            startActivity(i);
            finish();
            return true;
        }*/

        if (item.getItemId() == R.id.about2) {
            final Intent i = new Intent(mContext, About.class);
            startActivity(i);
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * Habilita gps.
     *
     * @param enabledGPS the enabled gps
     */
    private void habilitaGPS(final boolean enabledGPS) {
        if (!enabledGPS) {
            Toast.makeText(this, "GPS signal not found", Toast.LENGTH_LONG)
                    .show();
            Intent intent = 
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    /**
     * Initilize map.
     *
     * @param location the location
     * @param exibirBotoes the exibir botoes
     */
    private void initilizeMap(final Location location, 
            final boolean exibirBotoes) {
        try {

            if (googleMap == null) {

                if (exibirBotoes) {

                    googleMap = ((MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map)).getMap();
                } else {

                    googleMap = ((MapFragment) getFragmentManager()
                            .findFragmentById(R.id.mapGrupo)).getMap();

                    googleMap.setOnMapClickListener(new OnMapClickListener() {

                        @Override
                        public void onMapClick(final LatLng latLng) {

                            selecionarTipoPonto(latLng);

                        }
                    });
                    
                    Log.d("ponto", "latponto: " + latPonto);
                    
                    LatLng coordinatePonto = new LatLng(latPonto, longePonto);
                    googleMap.addMarker(new MarkerOptions()
                    .title(nomePontoEncontro)
                    .position(coordinatePonto)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.marker)).visible(true));
                    
                    
                    
                    
                    Intent it2 = getIntent();

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
                                       
                }
                

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMyLocationEnabled(true);

                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng coordinate = new LatLng(lat, lng);
                
                googleMap.setOnMapLoadedCallback(this);

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        coordinate, 14));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Selecionar tipo ponto.
     *
     * @param latLng the lat lng
     */
    public final void selecionarTipoPonto(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Defina o nome do Ponto");

        final EditText nomePonto = new EditText(this);
        builder.setView(nomePonto);

        builder.setSingleChoiceItems(tiposDePonto, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, 
                            final int item) {

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        
//                        double lat, lng;

                        switch (item) {
                        case 0:

                            if (TextUtils.isEmpty(nomePonto.getText()
                                    .toString())) {
                                markerOptions.title("Ponto de encontro");
                            } else {
                                markerOptions.title(nomePonto.getText()
                                        .toString());
                            }
                            
                            

                            nomePontoBD=markerOptions.getTitle().toString();
                            
                            markerOptions.icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.marker));
                            googleMap.animateCamera(CameraUpdateFactory
                                    .newLatLng(latLng));
                            googleMap.addMarker(markerOptions);
                            
                            latPonto = markerOptions.getPosition().latitude;
                            longePonto = markerOptions.getPosition().longitude;
                            Log.d("ponto", "pontoEncontro lat: " + latPonto + " lng:" + longePonto);
                            new AceitarPonto().execute();
                            break;

                        case 1:
                            if (TextUtils.isEmpty(nomePonto.getText()
                                    .toString())) {
                                markerOptions.title("Local especifico");
                            } else {
                                markerOptions.title(nomePonto.getText()
                                        .toString());
                            }
                            googleMap.animateCamera(CameraUpdateFactory
                                    .newLatLng(latLng));
                            googleMap.addMarker(markerOptions);
                            
                            double latLocalEspecifico = markerOptions.getPosition().latitude;
                            double lngLocalEspecifico = markerOptions.getPosition().longitude;
                            Log.d("ponto", "local especifico lat: " + latLocalEspecifico + " lng:" + lngLocalEspecifico);
                            
                            break;
                        default:
                            break;
                        }
                        tipoDePontoDialog.dismiss();
                    }
                });

        tipoDePontoDialog = builder.create();
        tipoDePontoDialog.show();
    }
    

    /**
     * Muda caractere.
     *
     * @param str the str
     * @param antigo the antigo
     * @param novo the novo
     * @return the string
     */
    public final String mudaCaractere(final String str, 
            final String antigo, final String novo) {
        return str.replace(antigo, novo);
    }

    /**
     * Marca usuario.
     *
     * @param nome the nome
     * @param latitude the latitude
     * @param longitude the longitude
     */
    private void marcaUsuario(final String nome, 
            final double latitude, final double longitude) {

        LatLng coordinate = new LatLng(latitude, longitude);

        startPerc = googleMap.addMarker(new MarkerOptions()
                .title(nome)
                .position(coordinate)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_launcher)).visible(true));
    }

    /* (non-Javadoc)
     * @see android.location.LocationListener
     * #onLocationChanged(android.location.Location)
     */
    @Override
    public final void onLocationChanged(final Location location) {
        if (startPerc != null) {
            // startPerc.remove();
        }
    }

    /* (non-Javadoc)
     * @see android.location.LocationListener
     * #onStatusChanged(java.lang.String, int, android.os.Bundle)
     */
    @Override
    public void onStatusChanged(final String provider, 
            final int status, final Bundle extras) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see android.location.LocationListener
     * #onProviderDisabled(java.lang.String)
     */
    @Override
    public final void onProviderDisabled(final String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    /* (non-Javadoc)
     * @see android.location.LocationListener
     * #onProviderEnabled(java.lang.String)
     */
    @Override
    public final void onProviderEnabled(final String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    /* Request updates at startup */
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected final void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected final void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public final void onBackPressed() {
        this.finish();
    }

    /* (non-Javadoc)
     * @see com.google.android.gms.maps.GoogleMap.
     * OnMapLoadedCallback#onMapLoaded()
     */
    @Override
    public final void onMapLoaded() {
        loadedMap = true;
    }

    private class AceitarPonto extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("Entrou", ""+ idGrupo);
            Log.d("Entrou", ""+ latPonto);
            Log.d("Entrou", ""+ longePonto);
            Log.d("Entrou", ""+ nomePontoBD);
            
            nomePontoBD = mudaCaractere(nomePontoBD, " ", "_");
            
            new JSONParse(urlBD
                    + "/findYouFriends/grupo/updatePontoEncontro?latPontoEncontro=" + latPonto
                    + "&lngPontoEncontro=" + longePonto + "&nomePontoEncontro=" + nomePontoBD + "&idGrupo=" + idGrupo);
            return null;
        
        }
    }
}

    




/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class GPSManager implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationManager mLocationManager;
    private String provider;
    private double latitude, longitude;
    private Context context;
    private LocationListener locationListener;

    private com.google.android.gms.location.LocationListener mLocationListenerPlayServices;
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    public GPSManager(Context context) {
        this.context = context;

        this.mLocationClient = new LocationClient(this.context, this, this);
        this.mLocationListenerPlayServices = new MyLocationListenerPlayServices();

        this.mLocationManager = (LocationManager) this.context
                .getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListenerLocal();

        this.mLocationRequest = LocationRequest.create();
        this.mLocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // Use
                                                                      // high
                                                                      // accuracy
        this.mLocationRequest.setInterval(300000);
        this.mLocationRequest.setFastestInterval(60000);

    }

    public void searchProvider() {
        provider = LocationManager.GPS_PROVIDER;
    }

    public void updateCurrentPosition() {
        if (provider != null && provider.equals(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(provider, 10, 0, locationListener);
        }
    }

    public void stopUpdates(boolean clearData) {
        mLocationManager.removeUpdates(locationListener);
        if (isConnected()) {
            mLocationClient
                    .removeLocationUpdates(mLocationListenerPlayServices);
        }
        if (clearData) {
            setLatitude(0);
            setLongitude(0);
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isGPSEnable() {
        return (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    public boolean isNetworkEnable() {
        return (mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) ;
    }

    public boolean isCurrentPositionNull() {
        return (latitude == 0.0 && longitude == 0.0);
    }

    public boolean isConnected() {
        return mLocationClient.isConnected();
    }

    public void disconnectPlayServices() {
        if (isConnected()) {
            mLocationClient.disconnect();
        }
    }

    private class LocationListenerLocal implements LocationListener {

        @Override
        public void onLocationChanged(Location newLocation) {
            latitude = newLocation.getLatitude();
            longitude = newLocation.getLongitude();
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }

    }

    private class MyLocationListenerPlayServices implements
            com.google.android.gms.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        provider = LocationManager.GPS_PROVIDER;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onDisconnected() {
        provider = LocationManager.GPS_PROVIDER;
    }

  
}

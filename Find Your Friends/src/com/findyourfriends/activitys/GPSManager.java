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

/**
 * The Class GPSManager.
 */
public class GPSManager implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    /** The m location manager. */
    private LocationManager mLocationManager;

    /** The provider. */
    private String provider;

    /** The longitude. */
    private double latitude, longitude;

    /** The context. */
    private Context context;

    /** The location listener. */
    private LocationListener locationListener;

    /** The m location listener play services. */
    private com.google.android.gms.location.LocationListener mLocationListenerPlayServices;

    /** The m location client. */
    private LocationClient mLocationClient;

    /** The m location request. */
    private LocationRequest mLocationRequest;

    /**
     * Instantiates a new GPS manager.
     * 
     * @param contx
     *            the context
     */
    public GPSManager(final Context contx) {
        this.context = contx;

        this.mLocationClient = new LocationClient(this.context, this, this);
        this.mLocationListenerPlayServices = new MyLocationListenerPlayServices();

        this.mLocationManager = (LocationManager) this.context
                .getSystemService(Context.LOCATION_SERVICE);

        this.locationListener = new LocationListenerLocal();

        this.mLocationRequest = LocationRequest.create();
        this.mLocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.mLocationRequest.setInterval(300000);
        this.mLocationRequest.setFastestInterval(60000);

    }

    /**
     * Search provider.
     */
    public final void searchProvider() {
        provider = LocationManager.GPS_PROVIDER;
    }

    /**
     * Update current position.
     */
    public final void updateCurrentPosition() {
        if (provider != null && provider.equals(LocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(provider, 10, 0,
                    locationListener);
        }
    }

    /**
     * Stop updates.
     * 
     * @param clearData
     *            the clear data
     */
    public final void stopUpdates(final boolean clearData) {
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

    /**
     * Gets the latitude.
     * 
     * @return the latitude
     */
    public final double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     * 
     * @return the longitude
     */
    public final double getLongitude() {
        return longitude;
    }

    /**
     * Gets the provider.
     * 
     * @return the provider
     */
    public final String getProvider() {
        return provider;
    }

    /**
     * Sets the latitude.
     * 
     * @param lat
     *            the new latitude
     */
    public final void setLatitude(final double lat) {
        latitude = lat;
    }

    /**
     * Sets the longitude.
     * 
     * @param lng
     *            the new longitude
     */
    public final void setLongitude(final double lng) {
        longitude = lng;
    }

    /**
     * Checks if is GPS enable.
     * 
     * @return true, if is GPS enable
     */
    public final boolean isGPSEnable() {
        return (mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    /**
     * Checks if is network enable.
     * 
     * @return true, if is network enable
     */
    public final boolean isNetworkEnable() {
        return (mLocationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    /**
     * Checks if is current position null.
     * 
     * @return true, if is current position null
     */
    public final boolean isCurrentPositionNull() {
        return (latitude == 0.0 && longitude == 0.0);
    }

    /**
     * Checks if is connected.
     * 
     * @return true, if is connected
     */
    public final boolean isConnected() {
        return mLocationClient.isConnected();
    }

    /**
     * Disconnect play services.
     */
    public final void disconnectPlayServices() {
        if (isConnected()) {
            mLocationClient.disconnect();
        }
    }

    /**
     * The Class LocationListenerLocal.
     */
    private class LocationListenerLocal implements LocationListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.location.LocationListener#onLocationChanged(android.location
         * .Location)
         */
        @Override
        public void onLocationChanged(final Location newLocation) {
            latitude = newLocation.getLatitude();
            longitude = newLocation.getLongitude();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.location.LocationListener#onProviderDisabled(java.lang.String
         * )
         */
        @Override
        public void onProviderDisabled(final String arg0) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.location.LocationListener#onProviderEnabled(java.lang.String)
         */
        @Override
        public void onProviderEnabled(final String arg0) {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * android.location.LocationListener#onStatusChanged(java.lang.String,
         * int, android.os.Bundle)
         */
        @Override
        public void onStatusChanged(final String arg0, final int arg1,
                final Bundle arg2) {
        }

    }

    /**
     * The Class MyLocationListenerPlayServices.
     */
    private class MyLocationListenerPlayServices implements
            com.google.android.gms.location.LocationListener {

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.google.android.gms.location.LocationListener#onLocationChanged
         * (android.location.Location)
         */
        @Override
        public void onLocationChanged(final Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.android.gms.common.GooglePlayServicesClient.
     * OnConnectionFailedListener
     * #onConnectionFailed(com.google.android.gms.common.ConnectionResult)
     */
    @Override
    public final void onConnectionFailed(final ConnectionResult result) {
        provider = LocationManager.GPS_PROVIDER;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
     * #onConnected(android.os.Bundle)
     */
    @Override
    public void onConnected(final Bundle connectionHint) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
     * #onDisconnected()
     */
    @Override
    public final void onDisconnected() {
        provider = LocationManager.GPS_PROVIDER;
    }

}

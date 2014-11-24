package com.findyourfriends.activitys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.les.findyourfriends.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class PerfilActivity extends Activity{
    private static final String TAG = "PerfilActivity";
    
    private Context mContext;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imagemPerfil;
    
    private GoogleApiClient mGoogleApiClient;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        mContext = getApplicationContext();
              
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imagemPerfil = (ImageView) findViewById(R.id.imgProfilePic);
        if (hasExternalStoragePublicPicture()){
            try {
                readImagem();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutFromGplus();
            }
        });
        
        btnRevokeAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeGplusAccess();
            }
        });
        

    }
    
    
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            MainActivity.editor.putBoolean("logado", false);
            MainActivity.editor.commit();
            
            Intent in = new Intent(mContext, MainActivity.class);
            startActivity(in);
            finish();
        }
    }

    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                        }

                    });
        }
    }
    
    void deleteExternalStoragePublicPicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File file = new File(path, "ImagemPerfil.jpg");
        file.delete();
    }

    boolean hasExternalStoragePublicPicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File file = new File(path, "ImagemPerfil.jpg");
        return file.exists();
    }
    
    public void readImagem() throws FileNotFoundException{
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        File file = new File(path, "ImagemPerfil.jpg");
        InputStream is = new FileInputStream(file);
        Bitmap bitmapPerfil = BitmapFactory.decodeStream(is);
        imagemPerfil.setImageBitmap(bitmapPerfil);
    }
}

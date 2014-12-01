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
import android.widget.TextView;



public class PerfilActivity extends Activity{
    private static final String TAG = "PerfilActivity";
    
    private Context mContext;
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imagemPerfil;
    private TextView userName, userEmail;
    
    private GoogleApiClient mGoogleApiClient;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        mContext = getApplicationContext();
        
        //mGoogleApiClient = MainActivity.mGoogleApiClient;
              
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imagemPerfil = (ImageView) findViewById(R.id.imgProfilePic);
        userName = (TextView) findViewById(R.id.txtName);
        userEmail = (TextView) findViewById(R.id.txtEmail);
        
        //pega a string "nome/email" caso nao encontrado "NOME/EMAIL"
        String nome = MainActivity.status.getString("nome", "NOME");
        String email = MainActivity.status.getString("email", "EMAIL");
        
        userName.setText(nome);
        userEmail.setText(email);
        
        if (hasExternalStoragePublicPicture()){
            try {
                readImagem();
            } catch (FileNotFoundException e) {
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
        Log.d("logout", "clicado logout");
        
        MainActivity.editor.clear();
        MainActivity.editor.putBoolean("logado", false);
        MainActivity.editor.putBoolean("sair", true);
        MainActivity.editor.commit();

        Intent in = new Intent(this, MainActivity.class);

        startActivity(in);
        finish();
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
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);       
        File file = new File(path, "ImagemPerfil.jpg");
        file.delete();
    }

    boolean hasExternalStoragePublicPicture() {
        //TODO        
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ File.separator + ".FindYourFriends" + File.separator;        
        File file = new File(path, "ImagemPerfil.jpg");
        return file.exists();
    }
    
    public void readImagem() throws FileNotFoundException{
        String path  = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ File.separator + ".FindYourFriends" + File.separator;
        File file = new File(path, "ImagemPerfil.jpg");
        
        InputStream is = new FileInputStream(file);
        Bitmap bitmapPerfil = BitmapFactory.decodeStream(is);
        imagemPerfil.setImageBitmap(bitmapPerfil);
    }
    
    @Override
    public void onBackPressed() {
        Intent in = new Intent(mContext, Map.class);
        in.putExtra("mostrar_botoes", true);
        startActivity(in);
        finish();
    }
}

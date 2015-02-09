/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.les.findyourfriends.R;

import com.facebook.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * The Class MainActivity.
 */
public class MainActivity extends FragmentActivity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {

    //mudei de extends Activity pra Fragment Activity
    //mainFragmant do login do FB
    /** The main fragment. */
    private MainFragment mainFragment;
    
    /** The person name. */
    private String email = "", personName;

    /** The Constant RC_SIGN_IN. */
    private static final int RC_SIGN_IN = 0;
    
    /** The Constant TAG. */
    private static final String TAG = "LoginActivity";
    
    /** The Constant PROFILE_PIC_SIZE. */
    private static final int PROFILE_PIC_SIZE = 350;
    
    /** The m google api client. */
    public static GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    
    /** The m sign in clicked. */
    private boolean mSignInClicked;

    /** The m connection result. */
    private ConnectionResult mConnectionResult;

    /** The btn sign in. */
    private SignInButton btnSignIn;
    
    /** The continuar. */
    private Button btnSignOut, btnRevokeAccess, continuar;
    
    /** The img profile pic. */
    private ImageView imgProfilePic;
    
    /** The txt email. */
    private TextView txtName, txtEmail;
    
    /** The ll profile layout. */
    private LinearLayout llProfileLayout;

    /** The m context. */
    private Context mContext;
    
    /** The Constant LOGIN. */
    public static final String LOGIN = "StatusLogin";
    
    /** The status. */
    public static SharedPreferences status;
    
    /** The editor. */
    public static SharedPreferences.Editor editor;
    
    /** The sair. */
    public static boolean logado, sair;
    
    /** The full path. */
    private String fullPath;
    
    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        
        

        // testar conexao
        haveNetworkConnection();
        status = getSharedPreferences(LOGIN, 0); 
        editor = status.edit();
        
        logado = status.getBoolean("logado", false);
            
        if (logado){
            if(haveNetworkConnection()){
                Intent in = new Intent(mContext, Map.class);
                in.putExtra("mostrar_botoes", true);
                startActivity(in);
                finish();
            }
        }
                 
        loadView();
        verificaMemoria();

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API) 
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();


        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()) {
                   new ListUsers().execute(email);

                    Session.delInstancia();
                    Session.getInstancia().setDono(email); // login.getText().toString()

                    /*Intent i = new Intent(mContext, Map.class);
                    i.putExtra("mostrar_botoes", true);
                    startActivity(i);
                    finish();*/
                } else {
                    confirmacaoDeRede();
                }

            }
        });
        
        btnSignIn.setVisibility(View.GONE);
        btnSignOut.setVisibility(View.GONE);
        btnRevokeAccess.setVisibility(View.GONE);
        llProfileLayout.setVisibility(View.GONE);
        continuar.setVisibility(View.GONE);
        
      //botao do FB
        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
            .beginTransaction()
            .add(android.R.id.content, mainFragment)
            .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
            .findFragmentById(android.R.id.content);
        }
       
    }
    
    /**
     * Load view.
     */
    private void loadView(){
        continuar = (Button) findViewById(R.id.buttonContinuar);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
    }
    
    //------------------------- VERIFICAR SDCARD-------------
    /**
     * Verifica memoria.
     */
    private void verificaMemoria(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
            
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }
    //--------------------------------------------------------------

    /**
     * Confirmacao de rede.
     */
    @SuppressWarnings("deprecation")
    private void confirmacaoDeRede() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.semConexao);
        alertDialog.setMessage("Verifique sua conexão com a internet");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        // alertDialog.setIcon(R.drawable.icon); MUDAR O ICONE
        alertDialog.show();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.perfil, menu);
        return true;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onMenuItemSelected(int, android.view.MenuItem)
     */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        if (item.getItemId() == R.id.about) {
            final Intent i = new Intent(mContext, About.class);
            startActivity(i);
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    

    // ------      Inicia google    -----------------------
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStop()
     */
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_sign_in:
            // Signin button clicked
            signInWithGplus();
            break;
        case R.id.btn_sign_out:
            // Signout button clicked
            signOutFromGplus();
            break;
        case R.id.btn_revoke_access:
            // Revoke access button clicked
            revokeGplusAccess();
            break;
        }
    }

    /* (non-Javadoc)
     * @see com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        updateUI(false);
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
            Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(android.os.Bundle)
     */
    @Override
    public void onConnected(Bundle arg0) {
        sair = status.getBoolean("sair", false);
        
        if (sair) {
            signOutFromGplus();
            editor.putBoolean("sair", false);
            editor.commit();
        } else {
        
        // Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        getProfileInformation();
        
        
       
        /*Intent it = getIntent();
        boolean perfil = it.getBooleanExtra("perfil", false);
        if (!perfil || mSignInClicked) {
            Intent i = new Intent(mContext, Map.class);
            i.putExtra("mostrar_botoes", true);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
            
        } else {
            updateUI(true);
        }
        
        mSignInClicked = false;*/
        // Update the UI after signin
        updateUI(true);
        }
        
    }

    /* (non-Javadoc)
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnectionSuspended(int)
     */
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);

    }

    /**
     * Update ui.
     *
     * @param isSignedIn the is signed in
     */
    private void updateUI(boolean isSignedIn) {
        //TODO        
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
            continuar.setVisibility(View.VISIBLE);
           
            editor.putString("nome", personName);
            editor.putString("email", email);
            
            editor.putBoolean("logado", true);
            editor.commit();
        
            
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
            continuar.setVisibility(View.GONE);
            
            editor.putString("nome", "NOM");
            editor.putString("email", "EMAIL");
            
            editor.putBoolean("logado", false);
            editor.commit();

        }
    }

    /**
     * Have network connection.
     *
     * @return true, if successful
     */
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    // login----------------------------
    /**
     * Sign in with gplus.
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Resolve sign in error.
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    // -------------------

    /**
     * Gets the profile information.
     *
     * @return the profile information
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                // String personName = currentPerson.getDisplayName();
                personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                txtName.setText(personName);
                txtEmail.setText(email);

                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The Class LoadProfileImage.
     */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        
        /** The bm image. */
        ImageView bmImage;

        /**
         * Instantiates a new load profile image.
         *
         * @param bmImage the bm image
         */
        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            
            try {
                //TODO
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            createExternalStoragePublicPicture(urldisplay);
            return mIcon11;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        protected void onPostExecute(Bitmap result) {
            Log.d("memoria", "carregou a foto");
            bmImage.setImageBitmap(result);         
        }
    }

    /**
     * Sign out from gplus.
     */
    private void signOutFromGplus() {
        //TODO
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateUI(false);
            mSignInClicked = false;
        } 
    }

    /**
     * Revoke gplus access.
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
    }

    /**
     * The Class ListUsers.
     */
    private class ListUsers extends AsyncTask<String, Void, Boolean> {

        /** The dialog. */
        private ProgressDialog dialog;
        
        /** The login. */
        private String login;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this,
                    "Verificando usuários",
                    "Aguarde, o sistema está verificando a sua conta");
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(String... params) {
            login = params[0];
            String url = urlBD + "/findYouFriends/usuario/getCurrentLocation?login="
                    + login;
            return new JSONParse(url).isNull();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result) {
                new CadastrarUsuario().execute(login);
            } else {
                new CapturaID().execute(login);
            }
        }
    }

    /**
     * The Class CadastrarUsuario.
     */
    private class CadastrarUsuario extends AsyncTask<String, Void, Void> {

        /** The dialog. */
        private ProgressDialog dialog;
        
        /** The login. */
        private String login;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = ProgressDialog.show(MainActivity.this, "Cadastrando",
                    "Você esta sendo cadastrado");*/
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(String... params) {

            login = params[0];

            String nome = mudaCaractere(personName, " ", "_");
            Log.d("renan", nome);

            String url = urlBD + "/findYouFriends/usuario/saveUser?"
                    + "login="
                    + login
                    + "&latitude="
                    + "0"
                    + "&longitude="
                    + "0" + "&nome=" + nome;

            new JSONParse(url);

            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            new CapturaID().execute(login);
            //dialog.dismiss();
            
            Intent i = new Intent(mContext, Map.class);
            i.putExtra("mostrar_botoes", true);
            startActivity(i);
            finish();
        }
    }

    /**
     * The Class CapturaID.
     */
    private class CapturaID extends AsyncTask<String, Void, Integer> {

        /** The dialog. */
        private ProgressDialog dialog;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*dialog = ProgressDialog.show(MainActivity.this, "Cadastrando",
                    "Você esta sendo cadastrado");*/
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Integer doInBackground(String... params) {

            String url = urlBD + "/findYouFriends//usuario/getCurrentLocation?login="
                    + params[0];
            return new JSONParse(url).getIdUsuario();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            Session.getInstancia().setIdUser(result);

            //dialog.dismiss();
            
            Intent i = new Intent(mContext, Map.class);
            i.putExtra("mostrar_botoes", true);
            startActivity(i);
            finish();
        }

    }

    /**
     * Muda caractere.
     *
     * @param str the str
     * @param antigo the antigo
     * @param novo the novo
     * @return the string
     */
    public String mudaCaractere(String str, String antigo, String novo) {
        str = str.replace(antigo, novo);
        return str;
    }
    
    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
       finish();
    }
    
    
    //TODO
    //----------------------- Criando Imagem -----------------------

    /**
     * Creates the path.
     */
    private void createPath() {
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            fullPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + File.separator
                    + ".FindYourFriends"
                    + File.separator;
        } else {
            fullPath = getFilesDir() + File.separator + ".FindYourFriends"
                    + File.separator;
        }

        final File dir = new File(fullPath);
        if (!dir.exists() && !dir.mkdirs()) {
            Log.e("erro dirs", "create dirs erro!");
        }
    }
    
    /**
     * Creates the external storage public picture.
     *
     * @param param the param
     */
    void createExternalStoragePublicPicture(String param) {

        createPath();
        //File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File file = new File(fullPath, "ImagemPerfil.jpg");

        try {
            //path.mkdirs();

            URL url = new URL(param);
            InputStream is = new BufferedInputStream(url.openStream());           
            //InputStream is = getResources().openRawResource(R.drawable.renan_perfil);
            OutputStream os = new FileOutputStream(file);
            
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=is.read(buf)))
            {
            os.write(buf, 0, n);
            }
            
            //byte[] data = new byte[is.available()];
            //is.read(data);
            //os.write(data);
            is.close();
            os.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(this,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }  
    
    // Para controse de uso atraves do FB
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
      super.onResume();

      // Logs 'install' and 'app activate' App Events.
      AppEventsLogger.activateApp(this);
    }
    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
      super.onPause();

      // Logs 'app deactivate' App Event.
      AppEventsLogger.deactivateApp(this);
    }
    
    //------------------------- PRIVATE PICTURE-----------------------
    /*public void createExternalStoragePrivatePicture() {
        // Create a path where we will place our picture in our own private
        // pictures directory.  Note that we don't really need to place a
        // picture in DIRECTORY_PICTURES, since the media scanner will see
        // all media in these directories; this may be useful with other
        // media types such as DIRECTORY_MUSIC however to help it classify
        // your media for display to the user.
        File path = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File file = new File(path, "ImagemPerfilPrivate.jpg");

        try {
            // Very simple code to copy a picture from the application's
            // resource into the external file.  Note that this code does
            // no error checking, and assumes the picture is small (does not
            // try to copy it in chunks).  Note that if external storage is
            // not currently mounted this will silently fail.
            InputStream is = getResources().openRawResource(R.drawable.renan_perfil);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();
            
         // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(this,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
            
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

   public void deleteExternalStoragePrivatePicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, "ImagemPerfil.jpg");
            file.delete();
        }
    }

    public boolean hasExternalStoragePrivatePicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, "ImagemPerfil.jpg");
            return file.exists();
        }
        return false;
    }*/
    
    
}

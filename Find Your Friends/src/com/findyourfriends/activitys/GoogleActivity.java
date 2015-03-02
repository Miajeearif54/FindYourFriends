package com.findyourfriends.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.les.findyourfriends.R;

public class GoogleActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener {
    private static final int SIGN_IN_CODE = 56465;
    private GoogleApiClient googleApiClient;
    private ConnectionResult connectionResult;

    private boolean isConsentScreenOpened;
    private boolean isSignInButtonClicked;

    // VIEWS
    private LinearLayout llContainerAll;
    private ProgressBar pbContainer;

    private LinearLayout llLoginForm;
    private Button btSignIn;
    private Button btSignInCustom;
    private SignInButton btSignInDefault;

    private LinearLayout llConnected;
    private ImageView ivProfile;
    private ProgressBar pbProfile;
    private TextView tvId;
    private TextView tvLanguage;
    private TextView tvName;
    private TextView tvUrlProfile;
    private TextView tvEmail;
    private Button btSignOut;
    private Button btRevokeAccess;

    private Button btContinuar;
    private LinearLayout llInfoUsuario;
    
    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_google);

        Log.d("login", "entrou Google");

        accessViews();
               

        googleApiClient = new GoogleApiClient.Builder(GoogleActivity.this)
                .addConnectionCallbacks(GoogleActivity.this)
                .addOnConnectionFailedListener(GoogleActivity.this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();

        btContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                
                if (haveNetworkConnection()) {
                    tvEmail = (TextView) findViewById(R.id.txtEmail);
                    new ListUsers().execute(tvEmail.getText().toString());
                } else {
                    confirmacaoDeRede();
                }

                

//                Intent in = new Intent(getApplicationContext(), Map.class);
//                in.putExtra("mostrar_botoes", true);
//                startActivity(in);
//                finish();
            }
        });

        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("login", "sair");
                if (googleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                    showUi(false, false);
                    
                    LoginActivity.editorStatusLogin.putBoolean("logado", false);
                    LoginActivity.editorStatusLogin.apply();

                    Intent in = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(in);
                    finish();
                }

            }
        });

    }
    
    
    @Override
    public void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();

            Log.d("login", "conectando");

            isSignInButtonClicked = true;
            // showUi(false, true);
            resolveSignIn();

        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN_CODE) {
            isConsentScreenOpened = false;

            if (resultCode != RESULT_OK) {
                isSignInButtonClicked = false;
            }

            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    // UTIL
    public void accessViews() {
        /*
         * llContainerAll = (LinearLayout) findViewById(R.id.llContainerAll);
         * pbContainer = (ProgressBar) findViewById(R.id.pbContainer);
         * 
         * // NOT CONNECTED llLoginForm = (LinearLayout)
         * findViewById(R.id.llLoginForm); btSignIn = (Button)
         * findViewById(R.id.btSignIn); btSignInCustom = (Button)
         * findViewById(R.id.btSignInCustom); btSignInDefault = (SignInButton)
         * findViewById(R.id.btSignInDefault);
         * 
         * // CONNECTED llConnected = (LinearLayout)
         * findViewById(R.id.llConnected); ivProfile = (ImageView)
         * findViewById(R.id.ivProfile); pbProfile = (ProgressBar)
         * findViewById(R.id.pbProfile); tvId = (TextView)
         * findViewById(R.id.tvId); tvLanguage = (TextView)
         * findViewById(R.id.tvLanguage); tvName = (TextView)
         * findViewById(R.id.tvName); tvUrlProfile = (TextView)
         * findViewById(R.id.tvUrlProfile); tvEmail = (TextView)
         * findViewById(R.id.tvEmail); btSignOut = (Button)
         * findViewById(R.id.btSignOut); btRevokeAccess = (Button)
         * findViewById(R.id.btRevokeAccess);
         * 
         * // LISTENER btSignIn.setOnClickListener(GoogleActivity.this);
         * btSignInDefault.setOnClickListener(GoogleActivity.this);
         * btSignInCustom.setOnClickListener(GoogleActivity.this);
         * btSignOut.setOnClickListener(GoogleActivity.this);
         * btRevokeAccess.setOnClickListener(GoogleActivity.this);
         */

        ivProfile = (ImageView) findViewById(R.id.imgProfilePic);
        tvName = (TextView) findViewById(R.id.txtName);
        tvEmail = (TextView) findViewById(R.id.txtEmail);

        btSignOut = (Button) findViewById(R.id.btn_sign_out);
        btContinuar = (Button) findViewById(R.id.buttonContinuar);
        llInfoUsuario = (LinearLayout) findViewById(R.id.llProfile);

    }

    public void showUi(boolean status, boolean statusProgressBar) {
        if (!statusProgressBar) {
            btSignOut.setVisibility(View.INVISIBLE);
            btContinuar.setVisibility(View.INVISIBLE);
            llInfoUsuario.setVisibility(View.INVISIBLE);
            /*
             * llContainerAll.setVisibility(View.VISIBLE);
             * pbContainer.setVisibility(View.GONE);
             * 
             * llLoginForm.setVisibility(status ? View.GONE : View.VISIBLE);
             * llConnected.setVisibility(!status ? View.GONE : View.VISIBLE);
             */
        } else {
            btSignOut.setVisibility(View.VISIBLE);
            btContinuar.setVisibility(View.VISIBLE);
            llInfoUsuario.setVisibility(View.VISIBLE);
            
            /*
             * llContainerAll.setVisibility(View.GONE);
             * pbContainer.setVisibility(View.VISIBLE);
             */
        }
    }

    /*
     * public void loadImage(final ImageView ivImg, final ProgressBar pbImg,
     * final String urlImg){ RequestQueue rq =
     * Volley.newRequestQueue(GoogleActivity.this); ImageLoader il = new
     * ImageLoader(rq, new ImageLoader.ImageCache() {
     * 
     * @Override public void putBitmap(String url, Bitmap bitmap) {
     * pbImg.setVisibility(View.GONE); }
     * 
     * @Override public Bitmap getBitmap(String url) { return null; } });
     * pbImg.setVisibility(View.VISIBLE); il.get(urlImg,
     * il.getImageListener(ivImg, pbImg.getId(), pbImg.getId())); }
     */

    public void resolveSignIn() {
        if (connectionResult != null && connectionResult.hasResolution()) {
            try {
                isConsentScreenOpened = true;
                connectionResult.startResolutionForResult(GoogleActivity.this,
                        SIGN_IN_CODE);
            } catch (SendIntentException e) {
                isConsentScreenOpened = false;
                googleApiClient.connect();
            }
        }
    }

    public void getDataProfile() {
        Person p = Plus.PeopleApi.getCurrentPerson(googleApiClient);

        if (p != null) {
            String id = p.getId();
            String name = p.getDisplayName();
            String language = p.getLanguage();
            String profileUrl = p.getUrl();
            String imageUrl = p.getImage().getUrl();
            String email = Plus.AccountApi.getAccountName(googleApiClient);

            // tvId.setText(id);
            // tvLanguage.setText(language);
            tvName.setText(name);
            tvEmail.setText(email);
            Session.getInstancia().setDono(email);
            Session.getInstancia().setIdUser(Integer.getInteger(id));
            Log.d("login", "Nome: " + name);
            Log.d("login", "Email: " + email);
            
            
            LoginActivity.editorStatusLogin.putString("Name", name);
            LoginActivity.editorStatusLogin.putString("Email", email);
            LoginActivity.editorStatusLogin.apply();

            // tvUrlProfile.setText(profileUrl);
            // Linkify.addLinks(tvUrlProfile, Linkify.WEB_URLS);

            // Log.i("Script", "IMG before: "+imageUrl);
            // imageUrl = imageUrl.substring(0, imageUrl.length() - 2)+"200";
            // Log.i("Script", "IMG after: "+imageUrl);
            // loadImage(ivProfile, pbProfile, imageUrl);
        } else {
            Toast.makeText(GoogleActivity.this, "Dados n�o liberados",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // LISTENERS
    /*
     * @Override public void onClick(View v) { Log.d("login", "click click");
     * if(v.getId() == R.id.btn_sign_out){ Log.d("login", "click sair");
     * if(googleApiClient.isConnected()){
     * Plus.AccountApi.clearDefaultAccount(googleApiClient);
     * googleApiClient.disconnect(); //googleApiClient.connect(); showUi(false,
     * false); } } else if(v.getId() == R.id.btn_revoke_access){
     * if(googleApiClient.isConnected()){
     * Plus.AccountApi.clearDefaultAccount(googleApiClient);
     * Plus.AccountApi.revokeAccessAndDisconnect
     * (googleApiClient).setResultCallback(new ResultCallback<Status>(){
     * 
     * @Override public void onResult(Status result) { finish(); } }); } } }
     */

    @Override
    public void onConnected(Bundle connectionHint) {
        isSignInButtonClicked = true;
        showUi(true, true);
        getDataProfile();
        
        LoginActivity.editorStatusLogin.putBoolean("logado", true);
        LoginActivity.editorStatusLogin.apply();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
        showUi(false, false);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                    GoogleActivity.this, 0).show();
            return;
        }

        if (!isConsentScreenOpened) {
            connectionResult = result;

            if (isSignInButtonClicked) {
                resolveSignIn();
            }
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

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(GoogleActivity.this,
                    "Verificando usuários",
                    "Aguarde, o sistema está verificando a sua conta");
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(final String... params) {
            login = params[0];
            String url = urlBD
                    + "/findYouFriends/usuario/getCurrentLocation?login="
                    + login;
            boolean usuarioCadastrado = new JSONParse(url).isNull();
            Log.d("werton", "precisa cadastrar? = " + usuarioCadastrado);
            return usuarioCadastrado;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result) {
                Log.d("werton", "captura id");
                //new CapturaID().execute(login);
                new CadastrarUsuario().execute(login);
            } else {                
                Log.d("werton", "cadastrar");
                //new CadastrarUsuario().execute(login);
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

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(GoogleActivity.this, "Cadastrando",
                    "Você está sendo cadastrado");

        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(final String... params) {

            login = params[0];
            tvName = (TextView) findViewById(R.id.txtName);

            String nome = mudaCaractere(tvName.getText().toString(), " ", "_");
            String url = urlBD + "/findYouFriends/usuario/saveUser?" + "login="
                    + login + "&latitude=" + "0" + "&longitude=" + "0"
                    + "&nome=" + nome;
            new JSONParse(url);

            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            dialog.dismiss();

            new CapturaID().execute(login);
        }
    }

    /**
     * The Class CapturaID.
     */
    private class CapturaID extends AsyncTask<String, Void, Integer> {

        /** The dialog. */
        private ProgressDialog dialog;

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
             * dialog = ProgressDialog.show(MainActivity.this, "Cadastrando",
             * "Você esta sendo cadastrado");
             */
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Integer doInBackground(final String... params) {
            String url = urlBD
                    + "/findYouFriends/usuario/getCurrentLocation?login="
                    + params[0];
            Integer x = new JSONParse(url).getIdUsuario();
            Log.d("werton", "ID: " + x);
            return x;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Integer result) {
            super.onPostExecute(result);

            Session.getInstancia().setIdUser(result);

            //dialog.dismiss();

            Intent i = new Intent(getApplicationContext(), Map.class);
            i.putExtra("mostrar_botoes", true);
            startActivity(i);
            //finish();
        }

    }
    
    /**
     * Muda caractere.
     * 
     * @param str
     *            the str
     * @param antigo
     *            the antigo
     * @param novo
     *            the novo
     * @return the string
     */
    public final String mudaCaractere(String str, final String antigo,
            final String novo) {
        str = str.replace(antigo, novo);
        return str;
    }
    
    /**
     * Confirmacao de rede.
     */
    @SuppressWarnings("deprecation")
    private void confirmacaoDeRede() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.semConexao);
        alertDialog.setMessage("Verifique sua conexão com a internet");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    
    
}

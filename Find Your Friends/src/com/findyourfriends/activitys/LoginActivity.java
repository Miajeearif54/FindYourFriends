package com.findyourfriends.activitys;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.common.SignInButton;
import com.les.findyourfriends.R;

public class LoginActivity extends Activity{
    /** The main fragment. */
    private MainFragment mainFragment;
    private Context context;
    
    /** The Constant TAG. */
    private static final String TAG = "MainFragment";
    
    private TextView tvName, tvEmail;
    //private ProfilePictureView foto;
    private LinearLayout llInfoUser;
    
    private SignInButton btnSignIn;
    private Button btContinuar;
    private ImageView logo;
    
    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";
    
    /** The logado. */
    private static boolean logado;
    
    /** The status. */
    public static SharedPreferences statusLogin;
    
    /** The editor. */
    public static SharedPreferences.Editor editorStatusLogin;


    /** The ui helper. */
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChanged(session, state, exception);
        }
    };
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        logo = (ImageView) findViewById(R.id.logo);
        
        context = getApplicationContext();
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        //authButton.setReadPermissions(Arrays.asList("public_profile"));
        authButton.setPublishPermissions(Arrays.asList("email", "public_profile"));
        
        tvName = (TextView) findViewById(R.id.txtName);
        tvEmail = (TextView) findViewById(R.id.txtEmail);
        //foto = (ProfilePictureView) findViewById(R.id.imgProfilePic);
        
        llInfoUser = (LinearLayout) findViewById(R.id.llProfile);
        
        //botao login google
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (haveNetworkConnection()) {
                    Intent in = new Intent(context, GoogleActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    confirmacaoDeRede();
                }
                
            }
        });
        
        btContinuar = (Button) findViewById(R.id.buttonContinuar);
        btContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                
                if (haveNetworkConnection()) {
                    tvEmail = (TextView) findViewById(R.id.txtEmail);
                    new ListUsers().execute(tvEmail.getText().toString());
                } else {
                    confirmacaoDeRede();
                }
                
                /*Intent in = new Intent(getApplicationContext(), Map.class);
                in.putExtra("mostrar_botoes", true);
                startActivity(in);
                finish();*/
            }         
        });
        
        statusLogin = getPreferences(MODE_PRIVATE);
        editorStatusLogin = statusLogin.edit();
        logado = statusLogin.getBoolean("logado", false);
        
        if (logado) {
            if (haveNetworkConnection()) {
                Intent in = new Intent(context, GoogleActivity.class);
                startActivity(in);
                finish();
            } else {
                confirmacaoDeRede();
            }
        }
       
    }
    
    /**
     * Troca o status. 
     * 
     * @param statusParam 
     */
    public static void setStatus(final SharedPreferences statusParam) {
        LoginActivity.statusLogin = statusParam;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Session session = Session.getActiveSession();
        if(session != null && (session.isClosed() || session.isOpened())){
            onSessionStateChanged(session, session.getState(), null);
        }
        
        uiHelper.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    
 // METHODS FACEBOOK
    public void onSessionStateChanged(final Session session, SessionState state, Exception exception){
        if(session != null && session.isOpened()){
            Log.i("Script", "Usuário conectado");
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if(user != null){
                        
                        btnSignIn.setVisibility(View.GONE);
                        
                        llInfoUser.setVisibility(View.VISIBLE);
                        btContinuar.setVisibility(View.VISIBLE);
                        //logo.setVisibility(View.VISIBLE);
                        
                        tvName.setText(user.getFirstName()+" "+user.getLastName());
                       
                        tvEmail.setText(user.getProperty("email").toString());
                                                
                        com.findyourfriends.activitys.Session.getInstancia().setDono(user.getProperty("email").toString());
                        com.findyourfriends.activitys.Session.getInstancia().setIdUser(Integer.getInteger(user.getId()));
                       
                        //foto.setProfileId(user.getId());
                    }
                }
            }).executeAsync();
        }
        else{
            Log.i("Script", "Usuário não conectado");
            llInfoUser.setVisibility(View.GONE);
            btContinuar.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            //logo.setVisibility(View.GONE);
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
            dialog = ProgressDialog.show(LoginActivity.this,
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
                //new CapturaID().execute(login);
                new CadastrarUsuario().execute(login);
            } else {                
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

            dialog = ProgressDialog.show(LoginActivity.this, "Cadastrando",
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
            
            com.findyourfriends.activitys.Session.getInstancia().setIdUser(result);
            //Session.getInstancia().setIdUser(result);

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
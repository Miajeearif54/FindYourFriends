package com.findyourfriends.activitys;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    
    private TextView userNameView;
    private View view;
    /** The Constant TAG. */
    private static final String TAG = "MainFragment";
    
    private TextView tvName, tvEmail;
    //private ProfilePictureView foto;
    private LinearLayout llInfoUser;
    
    private SignInButton btnSignIn;
    private Button btContinuar;
    private ImageView logo;

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
        
        
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("login", "clicou Google");
                
                Intent in = new Intent(context, GoogleActivity.class);
                startActivity(in);
                finish();

            }
        });
        
        btContinuar = (Button) findViewById(R.id.buttonContinuar);
        btContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("login", "continuar");
                Intent in = new Intent(getApplicationContext(), Map.class);
                in.putExtra("mostrar_botoes", true);
                startActivity(in);
                finish();
            }         
        });
       
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
                        logo.setVisibility(View.VISIBLE);
                        
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
            logo.setVisibility(View.GONE);
        }
    }
}
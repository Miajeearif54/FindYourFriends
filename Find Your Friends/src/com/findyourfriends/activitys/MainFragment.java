/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.*;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.les.findyourfriends.R;

/**
 * The Class MainFragment.
 */
public class MainFragment extends Activity {
    
    private TextView userNameView;
    private View view;
    /** The Constant TAG. */
    private static final String TAG = "MainFragment";

    /** The ui helper. */
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChanged(session, state, exception);
        }
    };
   
    
   /* @Override
    public View onCreateView(LayoutInflater inflater, 
            ViewGroup container, 
            Bundle savedInstanceState) {
        
        view = inflater.inflate(R.layout.login, container, false);
        
        //userNameView = (TextView) view.findViewById(R.id.txtName);

    
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
        
        //authButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));
        //authButton.setReadPermissions(Arrays.asList("email"));
         

        return view;

    }*/
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_facebook);
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
      
        /*LoginButton lb = (LoginButton) findViewById(R.id.fbLogin);
        lb.setPublishPermissions(Arrays.asList("email", "public_profile", "user_friends"));*/
        
    }


   /* private void onSessionStateChange(Session session, SessionState state,
            Exception exception) {
        
        Log.d("login", "entrou onSessionState");
        Request request = Request.newMeRequest(session, 
                new Request.GraphUserCallback() {
            
            @Override
            public void onCompleted(GraphUser user, Response response) {
                
                LinearLayout llProfileLayout = (LinearLayout) view.findViewById(R.id.llProfile);
                if (user != null) {
                    
                    
                    llProfileLayout.setVisibility(View.VISIBLE);

                    userNameView = (TextView) view.findViewById(R.id.txtName);
                    userNameView.setText(user.getName());
                    Log.d("login", "entrou FB");
                }
                
                
                
            }
        });
        request.executeAsync();
        
        
        
        
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }*/

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
                        TextView tv = (TextView) findViewById(R.id.txtName);
                        tv.setText(user.getFirstName()+" "+user.getLastName());
                        
                        tv = (TextView) findViewById(R.id.txtEmail);
                        tv.setText(user.getProperty("email").toString());
                        
                        
                        
                        ProfilePictureView ppv = (ProfilePictureView) findViewById(R.id.imgProfilePic);
                        ppv.setProfileId(user.getId());
                    }
                }
            }).executeAsync();
        }
        else{
            Log.i("Script", "Usuário não conectado");
        }
    }
    
    
}

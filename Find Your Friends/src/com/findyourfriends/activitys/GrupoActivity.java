package com.findyourfriends.activitys;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.les.findyourfriends.R;

public class GrupoActivity extends Activity{
    private Context mContext;
    private ImageButton editar, meusGrupos;
    private Button visualizarMapa;
    private Integer idGrupo;
    private String nameGrupo;
    private GPSManager gpsManager;
    private List<Usuario> usuariosDoGrupo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_users_in_groups);
        mContext = getApplicationContext();
        
        
        visualizarMapa = (Button) findViewById(R.id.visualizarMapa);
        visualizarMapa.setOnClickListener(new View.OnClickListener() {  
            @Override
            public void onClick(View v) {
                if(gpsManager == null){
                    gpsManager = new GPSManager(mContext);
                }
                gpsManager.searchProvider();
                gpsManager.updateCurrentPosition();
                
                new AtualizaPosicao().execute();
                
                
            }
        });
        
        
        Intent it = getIntent();
        Bundle param = it.getExtras();
        //nameGrupo = param.getString("KEY_NAME");
        idGrupo = param.getInt("KEY_ID");
        
        new CapturaJSON().execute();
    }
    
    
    private class CapturaJSON extends AsyncTask<Void, Void, List<Usuario>> {
        private ProgressDialog dialog;
        

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(GrupoActivity.this, "Aguarde", "Gerando lista de usuários nesse grupo.");
        }

        @Override
        protected List<Usuario> doInBackground(Void... params) {
            return getJSON();
        }

        @Override
        protected void onPostExecute(List<Usuario> result) {
            super.onPostExecute(result);
            
           usuariosDoGrupo = new ArrayList<Usuario>();
           
            for (Usuario usuario : result) {
                List<Integer> idsGruposDoUsuario = usuario.getIdGrupos();
                for (Integer idGrupoDoUsuario : idsGruposDoUsuario) {
                    if(idGrupoDoUsuario == idGrupo){
                        usuariosDoGrupo.add(usuario);
                    }
                }
            }
            
            ListView list = (ListView) findViewById(R.id.userInGroup);
            UsuarioAdapter adapter = new UsuarioAdapter(getApplicationContext(), usuariosDoGrupo);
            list.setAdapter(adapter);
                        
            dialog.dismiss();
        }

        private List<Usuario> getJSON() {
            JSONParse parser = new JSONParse("http://23.227.167.93:8081/findYouFriends/usuario/listUsers");
            return parser.getUsuariosBD();
        }
    }
    
    private class AtualizaPosicao extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(GrupoActivity.this, "Aguarde", "Atualizando Posição");
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (gpsManager.isCurrentPositionNull()) {
            }
            
            new JSONParse("http://23.227.167.93:8081/findYouFriends/usuario/updateLocation?"
                        + "id=" + Session.getInstancia().getIdUser()
                        + "&latitude=" + gpsManager.getLatitude()
                        + "&longitude=" + gpsManager.getLongitude());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            Bundle param = new Bundle();
            ArrayList<String> usuariosParse = new ArrayList<String>();
            ArrayList<String> latParse = new ArrayList<String>();
            ArrayList<String> longParse = new ArrayList<String>();
           
            for (Usuario usuario : usuariosDoGrupo) {
                usuariosParse.add(usuario.getNome());
                
                String latitude = Double.toString(usuario.getLatitude());
                latParse.add(latitude);
                
                String longitude = Double.toString(usuario.getLongitude());
                latParse.add(longitude);
            }
                       
            param.putStringArrayList("NOMES", usuariosParse);
            param.putStringArrayList("LATITUDE", latParse);
            param.putStringArrayList("LONGITUDE", longParse);

            Intent i = new Intent(mContext, Map.class);
            i.putExtras(param);
            startActivity(i);
            dialog.dismiss();
        }
        
    }
    
}



/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.les.findyourfriends.R;

/**
 * The Class GrupoActivity.
 */
public class GrupoActivity extends Activity {
    
    /** The m context. */
    private Context mContext;
    
    /** The meus grupos. */
    private ImageButton editar, meusGrupos;
    
    /** The visualizar mapa. */
    private Button visualizarMapa;
    
    /** The id grupo. */
    private Integer idGrupo;
    
    /** The name grupo. */
    private String nameGrupo;
    
    /** The gps manager. */
    private GPSManager gpsManager;
    
    /** The usuarios do grupo. */
    private List<Usuario> usuariosDoGrupo;
    
    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_users_in_groups);
        mContext = getApplicationContext();
        
        
        visualizarMapa = (Button) findViewById(R.id.visualizarMapa);
        visualizarMapa.setOnClickListener(new View.OnClickListener() {  
            @Override
            public void onClick(final View v) {
                if (gpsManager == null) {
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
    
    
    /**
     * The Class CapturaJSON.
     */
    private class CapturaJSON extends AsyncTask<Void, Void, List<Usuario>> {
        
        /** The dialog. */
        private ProgressDialog dialog;
        

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(GrupoActivity.this,
                    "Aguarde",
                    "Gerando lista de usuários nesse grupo.");
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected List<Usuario> doInBackground(final Void... params) {
            return getJSON();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final List<Usuario> result) {
            super.onPostExecute(result);
            
           usuariosDoGrupo = new ArrayList<Usuario>();
           
            for (Usuario usuario : result) {
                List<Integer> idsGruposDoUsuario = usuario.getIdGrupos();
                for (Integer idGrupoDoUsuario : idsGruposDoUsuario) {
                    if (idGrupoDoUsuario == idGrupo) {
                        usuariosDoGrupo.add(usuario);
                    }
                }
            }
            
            ListView list = (ListView) findViewById(R.id.userInGroup);
            UsuarioAdapter adapter = new UsuarioAdapter(getApplicationContext(), usuariosDoGrupo);
            list.setAdapter(adapter);
                        
            dialog.dismiss();
        }

        /**
         * Gets the json.
         *
         * @return the json
         */
        private List<Usuario> getJSON() {
            JSONParse parser = new JSONParse(urlBD
                                    + "/findYouFriends/usuario/listUsers");
            return parser.getUsuariosBD();
        }
    }
    
    /**
     * The Class AtualizaPosicao.
     */
    private class AtualizaPosicao extends AsyncTask<Void, Void, Void> {
        
        /** The dialog. */
        private ProgressDialog dialog;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(GrupoActivity.this,
                    "Aguarde",
                    "Atualizando Posição");
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(final Void... params) {
            while (gpsManager.isCurrentPositionNull()) {
            }
            
            new JSONParse(urlBD + "/findYouFriends/usuario/updateLocation?"
                        + "id=" + Session.getInstancia().getIdUser()
                        + "&latitude=" + gpsManager.getLatitude()
                        + "&longitude=" + gpsManager.getLongitude());
            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Void result) {
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
                longParse.add(longitude);
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



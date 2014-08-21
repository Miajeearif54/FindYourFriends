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
import android.widget.ImageButton;
import android.widget.ListView;

import com.les.findyourfriends.R;

public class GrupoActivity extends Activity{
    private Context mContext;
    private ImageButton editar, meusGrupos;
    private Integer idGrupo = 10;
    private String nameGrupo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_users_in_groups);
        mContext = getApplicationContext();
        
        Intent it = getIntent();
        Bundle param = it.getExtras();
        nameGrupo = param.getString("KEY_NAME");
        idGrupo = param.getInt("KEY_ID");
        
        new CapturaJSON().execute();
    }
    
    private class CapturaJSON extends AsyncTask<Void, Void, List<Usuario>> {
        private ProgressDialog dialog;
        private List<Usuario> usuariosDoGrupo;

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
           
           Log.d("werton", "idGrupo = " + idGrupo);
           Log.d("werton", result.size()+"");
           
            
            for (Usuario usuario : result) {
                List<Integer> idsGruposDoUsuario = usuario.getIdGrupos();
                for (Integer idGrupoDoUsuario : idsGruposDoUsuario) {
                    Log.d("werton", idGrupoDoUsuario+"");
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
            JSONParse parser = new JSONParse("http://23.227.167.93:8085/findYouFriends/usuario/listUsers");
            return parser.getUsuariosBD();
        }
    }
    
}



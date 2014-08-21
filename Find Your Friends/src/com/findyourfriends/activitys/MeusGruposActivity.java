package com.findyourfriends.activitys;

import java.util.ArrayList;
import java.util.List;

import com.les.findyourfriends.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MeusGruposActivity extends Activity{
	private Context mContext;
	private static final Object USUARIO = Session.getInstancia().getDono();
	private ImageButton editar, grupos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meus_grupos);
		mContext = getApplicationContext();
		
		grupos = (ImageButton) findViewById(R.id.grupos);        
        grupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, ViewGroupActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
        
        editar = (ImageButton) findViewById(R.id.editar);
        editar.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, EditarActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
        
        new CapturaJSON().execute();
		
	}
	
	private class CapturaJSON extends AsyncTask<Void, Void, List<Integer>> {
        
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MeusGruposActivity.this, "Aguarde", "Estamos conferindo seus grupos ...");
        }
        
        @Override
        protected List<Integer> doInBackground(Void... params) {
            return getJSON(Session.getInstancia().getDono());
        }

        @Override
        protected void onPostExecute(List<Integer> result) {
            super.onPostExecute(result);
            
            new RecuperaGruposDoUsuario().execute(result);
               
            dialog.dismiss();
        }

        private List<Integer> getJSON(String login) {
            String url = "http://23.227.167.93:8085/findYouFriends/usuario/getCurrentLocation?login="  + login;
            JSONParse parser = new JSONParse(url);
            return parser.getGruposUsuarios();
        }
    }
	
	private class RecuperaGruposDoUsuario extends AsyncTask<List<Integer>, Void, List<Grupo>> {
        private ProgressDialog dialog;
        private List<Integer> idGrupos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MeusGruposActivity.this, "Aguarde", "Gerando lista de grupos.");
        }

        @Override
        protected List<Grupo> doInBackground(List<Integer>... params) {
            idGrupos = params[0];
            return getJSON();
        }

        @Override
        protected void onPostExecute(List<Grupo> result) {
            super.onPostExecute(result);
            
            List<Grupo> gruposDoUsuario = new ArrayList<Grupo>();
            
            for (Grupo grupo : result) {
                for (Integer idGrupo : idGrupos) {
                    if (grupo.getId() == idGrupo){
                        gruposDoUsuario.add(grupo);
                        break;
                    }
                }
            }
            
            
            ListView list = (ListView) findViewById(R.id.listMyGroups);
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(), gruposDoUsuario);
            list.setAdapter(adapter);
            
            dialog.dismiss();
        }

        private List<Grupo> getJSON() {
            JSONParse parser = new JSONParse("http://23.227.167.93:8085/findYouFriends/grupo/listGroups");
            return parser.getGruposBD();
        }
    }
}
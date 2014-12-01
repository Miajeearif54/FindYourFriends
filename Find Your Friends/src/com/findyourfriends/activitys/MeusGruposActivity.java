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
	private String urlBD = "http://150.165.15.89:10008";
	
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
            String url = urlBD + "/findYouFriends/usuario/getCurrentLocation?login="  + login;
            JSONParse parser = new JSONParse(url);
            return parser.getGruposUsuarios();
        }
    }
	
	private class RecuperaGruposDoUsuario extends AsyncTask<List<Integer>, Void, List<Grupo>> {
        private ProgressDialog dialog;
        private List<Integer> idGrupos;
        private List<Grupo> gruposDoUsuario;

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
            
            gruposDoUsuario = new ArrayList<Grupo>();
            
            for (Grupo grupo : result) {
                for (Integer idGrupo : idGrupos) {
                    if (grupo.getId() == idGrupo && grupo.isAtivo()){
                        grupo.setNome(mudaCaractere(grupo.getNome(), "_", " "));
                        gruposDoUsuario.add(grupo);
                        break;
                    }
                }
            }
            
            
            ListView list = (ListView) findViewById(R.id.listMyGroups);
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(), gruposDoUsuario);
            list.setAdapter(adapter);
            
            list.setOnItemClickListener(new OnItemClickListener() {
                
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    String name = ((TextView) view.findViewById(R.id.nomeGrupo)).getText().toString();
                    String idGrupo = ((TextView) view.findViewById(R.id.idGrupo)).getText().toString();
                    
                    Bundle param = new Bundle();
                    
                    for (Grupo grupo : gruposDoUsuario) {
                        if(String.valueOf(grupo.getId()).equals(idGrupo)){
                            param.putString("KEY_NAME", grupo.getNome());
                            param.putInt("KEY_ID", grupo.getId());
                            
                        }
                    }
                    
                    Intent intent = new Intent(getApplicationContext(), GrupoActivity.class);
                    intent.putExtras(param);
                    startActivity(intent);
                }
            });
            
            dialog.dismiss();
        }

        private List<Grupo> getJSON() {
            JSONParse parser = new JSONParse(urlBD + "/findYouFriends/grupo/listGroups");
            return parser.getGruposBD();
        }
    }
	
	   public String mudaCaractere(String str, String antigo, String novo) {
	        str = str.replace(antigo, novo);
	        return str;
	    }
}
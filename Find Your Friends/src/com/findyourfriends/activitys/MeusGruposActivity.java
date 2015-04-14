/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.les.findyourfriends.R;

/**
 * The Class MeusGruposActivity.
 */
public class MeusGruposActivity extends Activity {
	
	/** The m context. */
	private Context mContext;
	
	/** The Constant USUARIO. */
	private static final Object USUARIO = Session.getInstancia().getDono();
	
	/** The grupos. */
	private ImageButton editar, grupos;
	
	/** The url bd. */
	private String urlBD = "http://150.165.98.11:8080";
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    protected final void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meus_grupos);
		mContext = getApplicationContext();
		
		grupos = (ImageButton) findViewById(R.id.grupos);        
        grupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(final View v) {
				Intent i = new Intent(mContext, ViewGroupActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
        
        editar = (ImageButton) findViewById(R.id.editar);
        editar.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(final View v) {
				Intent i = new Intent(mContext, EditarActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
        
        new CapturaJSON().execute();
		
	}
	
	/**
	 * The Class CapturaJSON.
	 */
	private class CapturaJSON extends AsyncTask<Void, Void, List<Integer>> {
        
        /** The dialog. */
        private ProgressDialog dialog;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MeusGruposActivity.this, 
                    "Aguarde", "Estamos conferindo seus grupos ...");
        }
        
        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected List<Integer> doInBackground(final Void... params) {
            return getJSON(Session.getInstancia().getDono());
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final List<Integer> result) {
            super.onPostExecute(result);
                        
            new RecuperaGruposDoUsuario().execute(result);
            
            dialog.dismiss();
 
        }

        /**
         * Gets the json.
         *
         * @param login the login
         * @return the json
         */
        private List<Integer> getJSON(final String login) {
            String url = urlBD 
                    + "/findYouFriends/usuario/getCurrentLocation?login="  
                    + login;
            JSONParse parser = new JSONParse(url);
            return parser.getGruposUsuarios();
        }
    }
	
	/**
	 * The Class RecuperaGruposDoUsuario.
	 */
	private class RecuperaGruposDoUsuario extends 
	AsyncTask<List<Integer>, Void, List<Grupo>> {
        
        /** The dialog. */
        private ProgressDialog dialog;
        
        /** The id grupos. */
        private List<Integer> idGrupos;
        
        /** The grupos do usuario. */
        private List<Grupo> gruposDoUsuario;
        
        private EditText editsearch;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MeusGruposActivity.this, 
                    "Aguarde", "Gerando lista de grupos.");
            
            dialog.setCancelable(true);
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected List<Grupo> doInBackground(final List<Integer>... params) {
            idGrupos = params[0];
            return getJSON();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final List<Grupo> result) {
            super.onPostExecute(result);
            
            gruposDoUsuario = new ArrayList<Grupo>();
           
            
            for (Grupo grupo : result) {
                Log.d("grupos", "grupo: " + grupo.getNome());
                for (Integer idGrupo : idGrupos) {
                    if (grupo.getId() == idGrupo && grupo.isAtivo()) {
                        grupo.setNome(mudaCaractere(grupo.getNome(), "_", " "));
                        gruposDoUsuario.add(grupo);
                        
                        break;
                    }
                }
            }
            
            ListView list = (ListView) findViewById(R.id.listMyGroups);
            final GrupoAdapter adapter = new GrupoAdapter(
                    getApplicationContext(), gruposDoUsuario, idGrupos, false);
            list.setAdapter(adapter);
            
            editsearch = (EditText) findViewById(R.id.search);


            // Capture Text in EditText
            editsearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    String text = editsearch.getText().toString()
                            .toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                        int arg2, int arg3) {
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1,
                        int arg2, int arg3) {
                }
            });
            
            list.setOnItemClickListener(new OnItemClickListener() {
                
                @Override
                public void onItemClick(final AdapterView<?> parent, 
                        final View view,
                        final int position, final long id) {
                    
                    String name = ((TextView) 
                            view.findViewById(R.id.nomeGrupo)).getText()
                            .toString();
                    
                    String idGrupo = ((TextView) 
                            view.findViewById(R.id.idGrupo)).getText()
                            .toString();
                    
                    Bundle param = new Bundle();
                    
                    for (Grupo grupo : gruposDoUsuario) {
                        if (String.valueOf(grupo.getId()).equals(idGrupo)) {
                            param.putString("KEY_NAME", grupo.getNome());
                            param.putInt("KEY_ID", grupo.getId());
                            param.putString("KEY_DONO", grupo.getDono());
                            
                            Log.d("VIEWGROUP DONO DO GRUPO", ""+grupo.getDono());                          
                        }
                    } 
                    
                    param.putDouble("latPonto", gruposDoUsuario.get(position).getLatitude());
                    param.putDouble("lngPonto", gruposDoUsuario.get(position).getLongitude());
                    param.putString("nomePonto", gruposDoUsuario.get(position).getNomePonto());
                    
                    Intent intent = new Intent(getApplicationContext(), 
                            GrupoActivity.class);
                    intent.putExtras(param);
                    startActivity(intent);
                }
            });
            
            dialog.dismiss();
        }

        /**
         * Gets the json.
         *
         * @return the json
         */
        private List<Grupo> getJSON() {
            JSONParse parser = new JSONParse(urlBD 
                    + "/findYouFriends/grupo/listGroups");
            return parser.getGruposBD();
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
   	public final String mudaCaractere(String str, 
   	        final String antigo, final String novo) {
	        str = str.replace(antigo, novo);
	        return str;
	    }
}
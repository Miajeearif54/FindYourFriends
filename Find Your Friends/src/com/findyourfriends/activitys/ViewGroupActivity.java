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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ViewGroupActivity extends Activity{
	private Context mContext;
	private ImageButton editar, meusGrupos;
	private String urlBD = "http://150.165.15.89:10008";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_groups);
		mContext = getApplicationContext();
              
        meusGrupos = (ImageButton) findViewById(R.id.meusGrupos);        
        meusGrupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, MeusGruposActivity.class);
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
	
	private class CapturaJSON extends AsyncTask<Void, Void, List<Grupo>> {
        private ProgressDialog dialog;
        private List<Grupo> gruposParaMostrar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            
            dialog = ProgressDialog.show(ViewGroupActivity.this, "Aguarde", "Gerando lista de grupos.");
        }

        @Override
        protected List<Grupo> doInBackground(Void... params) {
            return getJSON();
        }

        @Override
        protected void onPostExecute(List<Grupo> result) {
            super.onPostExecute(result);
            
           gruposParaMostrar = new ArrayList<Grupo>();
            
            for (Grupo grupo : result) {
                if(grupo.isAtivo()){
                    gruposParaMostrar.add(grupo);
                }
            }
            
            ListView list = (ListView) findViewById(R.id.listGroups);
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(), gruposParaMostrar);
            list.setAdapter(adapter);
            
            list.setOnItemClickListener(new OnItemClickListener() {
                
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    // getting values from selected ListItem
                    String name = ((TextView) view.findViewById(R.id.nomeGrupo)).getText().toString();
                    String idGrupo = ((TextView) view.findViewById(R.id.idGrupo)).getText().toString();
                    
//                    String cost = ((TextView) view.findViewById(R.id.cost)).getText().toString();
//                    String description = ((TextView) view.findViewById(R.id.desciption)).getText().toString();
//                     
                    // Starting new intent
                    
                    Bundle param = new Bundle();
                    
                    for (Grupo grupo : gruposParaMostrar) {
                        if(String.valueOf(grupo.getId()).equals(idGrupo)){
                            param.putString("KEY_NAME", grupo.getNome());
                            param.putInt("KEY_ID", grupo.getId());
                            param.putString("KEY_SENHA", grupo.getSenha());
                        }
                    }
                    
                    Intent intent = new Intent(getApplicationContext(), EntraNoGrupo.class);
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
	
	
	
}



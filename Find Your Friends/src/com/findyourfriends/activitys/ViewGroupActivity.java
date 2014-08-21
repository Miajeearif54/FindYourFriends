package com.findyourfriends.activitys;

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
	private List<Grupo> gruposBackup;
	
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
            
            gruposBackup = result;
            
            ListView list = (ListView) findViewById(R.id.listGroups);
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(), gruposBackup);
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
                    
                    for (Grupo grupo : gruposBackup) {
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
            JSONParse parser = new JSONParse("http://23.227.167.93:8085/findYouFriends/grupo/listGroups");
            return parser.getGruposBD();
        }
    }
	
	
	
}



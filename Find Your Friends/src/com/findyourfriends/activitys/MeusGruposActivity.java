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
import android.widget.ImageButton;
import android.widget.ListView;

public class MeusGruposActivity extends Activity{
	private Context mContext;
	private static final Object USUARIO = "werton";
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
	
	private class CapturaJSON extends AsyncTask<Void, Void, List<Grupo>> {
        
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MeusGruposActivity.this, "Aguarde", "Gerando lista de seus grupos.");
        }

        @Override
        protected List<Grupo> doInBackground(Void... params) {
            return getJSON();
        }

        @Override
        protected void onPostExecute(List<Grupo> result) {
            super.onPostExecute(result);
            
            ListView list = (ListView) findViewById(R.id.listMyGroups);
            List<Grupo> meusGrupos = new ArrayList<Grupo>();
            for (Grupo grupo : result) {
                if(grupo.getDono().equals(USUARIO)){
                    meusGrupos.add(grupo);
                }
            }
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(), meusGrupos);
            list.setAdapter(adapter);
            
            dialog.dismiss();
        }

        private List<Grupo> getJSON() {
            JSONParse parser = new JSONParse("http://23.227.167.93:8085/findYouFriends/grupo/listGroups");
            return parser.getGruposBD();
        }
    }
}
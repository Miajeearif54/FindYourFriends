package com.findyourfriends.activitys;

import java.util.ArrayList;
import java.util.List;

import com.les.findyourfriends.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EditarActivity extends Activity{
	private Context mContext;
	private Button criarGrupo;
	private ImageButton grupos, meusGrupos;
	private static final Object DONO= Session.getInstancia().getDono();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar);
		mContext = getApplicationContext();
		
		
		criarGrupo = (Button) findViewById(R.id.criarGrupo);
        criarGrupo.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, CriarGrupoActivity.class);
				startActivity(i);
			}
		});
        
        grupos = (ImageButton) findViewById(R.id.grupos);        
        grupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, ViewGroupActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
              
        meusGrupos = (ImageButton) findViewById(R.id.meusGrupos);        
        meusGrupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, MeusGruposActivity.class);
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
            dialog = ProgressDialog.show(EditarActivity.this, "Aguarde", "Gerando lista de seus grupos.");
        }

        @Override
        protected List<Grupo> doInBackground(Void... params) {
            return getJSON();
        }

        @Override
        protected void onPostExecute(List<Grupo> result) {
            super.onPostExecute(result);
            
            ListView list = (ListView) findViewById(R.id.listEditar);
            List<Grupo> editarGrupos = new ArrayList<Grupo>();
            for (Grupo grupo : result) {
                if(grupo.getDono().equals(DONO) && grupo.isAtivo()){
                    editarGrupos.add(grupo);
                }
            }
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(), editarGrupos);
            list.setAdapter(adapter);
            
            list.setOnItemClickListener(new OnItemClickListener() {
                
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    // getting values from selected ListItem
                    String name = ((TextView) view.findViewById(R.id.nomeGrupo)).getText().toString();
                    String idGrupo = ((TextView) view.findViewById(R.id.idGrupo)).getText().toString();
                    
                    openAlert("Excluir Grupo", "Voc� realmente gostaria de excluir o grupo \"" + name +"\"?");
                }
            });
            
            dialog.dismiss();
        }

        private List<Grupo> getJSON() {
            JSONParse parser = new JSONParse("http://23.227.167.93:8085/findYouFriends/grupo/listGroups");
            return parser.getGruposBD();
        }
    }

    private void openAlert(String title, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int id) {
                //COLOCAR O LINK PARA EXCLUIR AQUI!
                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
            }
        });
        
        alertDialogBuilder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int id) {
            }
        });
    
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }


    
}
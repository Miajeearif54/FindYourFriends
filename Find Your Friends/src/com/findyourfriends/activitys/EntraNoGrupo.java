/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.les.findyourfriends.R;

public class EntraNoGrupo extends Activity {
    private String urlBD = "http://150.165.15.89:10008";
    private Integer idGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrar_grupo);
        
        Intent it = getIntent();
        Bundle param = it.getExtras();
        String nameGrupo = param.getString("KEY_NAME");
        idGrupo = param.getInt("KEY_ID");
        final String senhaGrupo = param.getString("KEY_SENHA");
        
        
        EditText nome = (EditText) findViewById(R.id.edNomeGrupo_entrar);    
        nome.setText(nameGrupo);
        
        Button entrar = (Button) findViewById(R.id.entrar);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senha = ((EditText) findViewById(R.id.edSenha_entrar)).getText().toString();
                
                if(senhaGrupo.equals(senha)){     
                    new EntraNoGrupoAsync().execute();
                }
                                
            } });
        
        Button voltar = (Button) findViewById(R.id.cancelarEntrarGrupo);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            } });
        
    }
    
    
private class EntraNoGrupoAsync extends AsyncTask<Void, Void, Void> {
        
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(EntraNoGrupo.this, "Aguarde", "Adicionando você ao grupo.");
        }

        @Override
        protected Void doInBackground(Void... params) {
            new JSONParse(urlBD + "/findYouFriends/grupo/addUser?idGrupo="+ idGrupo+"&idUsuario="+ Session.getInstancia().getIdUser());
            Log.d("id", "id user: " + Session.getInstancia().getIdUser());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Bundle param = new Bundle();
            param.putInt("KEY_ID",idGrupo);
            
            Intent i = new Intent(getApplicationContext(), GrupoActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.putExtras(param);
            startActivity(i);
            
            dialog.dismiss();
            finish();
        }
    }

}

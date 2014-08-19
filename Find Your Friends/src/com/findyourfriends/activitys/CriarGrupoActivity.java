package com.findyourfriends.activitys;

import java.util.List;

import com.les.findyourfriends.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CriarGrupoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_grupo);
        
        
        
        Button salvar = (Button) findViewById(R.id.salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nome = (EditText) findViewById(R.id.edNomeGrupo);
                EditText senha = (EditText) findViewById(R.id.edSenha);
                
                String nomeS = nome.getText().toString();
                String senhaS = senha.getText().toString();
                
                Log.d("werton", nomeS);
                Log.d("werton", senhaS);
                
                
                new cadastraGrupo().execute(nomeS, senhaS);
            } });
        
        
    }
    
    
    
    private class cadastraGrupo extends AsyncTask<String, Void, Void> {
        
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(CriarGrupoActivity.this, "Criando Grupo", "Aguarde, o sistema est� cadastrando o grupo.");
        }

        @Override
        protected Void doInBackground(String... params) {
            String nomeDoGrupo = params[0];
            String senhaDoGrupo = params[1];
            
            String url = "http://23.227.167.93:8085/findYouFriends/grupo/saveGroup?"
                    + "nome="     + nomeDoGrupo
                    + "&dono="    + Session.getInstancia().getDono()
                    + "&duracao=" + "50"
                    + "&senha="   + senhaDoGrupo;
            
            
            new JSONParse(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            dialog.dismiss();
        }
    }

}

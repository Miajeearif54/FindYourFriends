/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import com.les.findyourfriends.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * The Class CriarGrupoActivity.
 */
public class CriarGrupoActivity extends Activity {
    
    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
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
                
                new cadastraGrupo().execute(nomeS, senhaS);
            } });
        
        Button voltar = (Button) findViewById(R.id.cancelar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            } });
        
        
    }
    
    /**
     * The Class cadastraGrupo.
     */
    private class cadastraGrupo extends AsyncTask<String, Void, Void> {
        
        /** The dialog. */
        private ProgressDialog dialog;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(CriarGrupoActivity.this, "Criando Grupo", "Aguarde, o sistema está cadastrando o grupo.");
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(String... params) {
            String nomeDoGrupo = params[0];
            String senhaDoGrupo = params[1];
            
            nomeDoGrupo = mudaCaractere(nomeDoGrupo, " ", "_");
            
            String url = urlBD + "/findYouFriends/grupo/saveGroup?"
                    + "nome="     + nomeDoGrupo
                    + "&dono="    + Session.getInstancia().getDono()
                    + "&duracao=" + "0"
                    + "&ativo="   + true
                    + "&senha="   + senhaDoGrupo;
            
            new JSONParse(url);
            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            
            
            Intent i = new Intent(getApplicationContext(), MeusGruposActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            dialog.dismiss();
            finish();
            
            
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
    public String mudaCaractere(String str, String antigo, String novo) {
        str = str.replace(antigo, novo);
        return str;
    }

}

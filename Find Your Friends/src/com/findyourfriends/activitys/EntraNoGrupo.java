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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.les.findyourfriends.R;

/**
 * The Class EntraNoGrupo.
 */
public class EntraNoGrupo extends Activity {
    
    /** The url bd. */
    private String urlBD = "http://150.165.98.11:8080";
    
    /** The id grupo. */
    private Integer idGrupo;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
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
            public void onClick(final View v) {
                EditText edEntrar = (EditText) findViewById(R.id.edSenha_entrar);
                String senha = edEntrar.getText().toString();
                
                if (senhaGrupo.equals(senha)) {     
                    new EntraNoGrupoAsync().execute();
                }
                                
            } });
        
        Button voltar = (Button) findViewById(R.id.cancelarEntrarGrupo);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            } });
        
    }
    
    
/**
 * The Class EntraNoGrupoAsync.
 */
private class EntraNoGrupoAsync extends AsyncTask<Void, Void, Void> {
        
        /** The dialog. */
        private ProgressDialog dialog;

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(EntraNoGrupo.this,
                    "Aguarde",
                    "Adicionando você ao grupo.");
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(final Void... params) {
            new JSONParse(urlBD
                    + "/findYouFriends/grupo/addUser?idGrupo=" + idGrupo
                    + "&idUsuario=" + Session.getInstancia().getIdUser());
            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            Bundle param = new Bundle();
            param.putInt("KEY_ID", idGrupo);
            
            Intent i = new Intent(getApplicationContext(), GrupoActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.putExtras(param);
            startActivity(i);
            
            dialog.dismiss();
            finish();
        }
    }

}

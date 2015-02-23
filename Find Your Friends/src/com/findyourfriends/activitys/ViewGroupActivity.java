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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.les.findyourfriends.R;

/**
 * The Class ViewGroupActivity.
 */
public class ViewGroupActivity extends Activity {

    /** The m context. */
    private Context mContext;

    /** The meus grupos. */
    private ImageButton editar, meusGrupos;

    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_groups);
        mContext = getApplicationContext();

        meusGrupos = (ImageButton) findViewById(R.id.meusGrupos);
        meusGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(mContext, MeusGruposActivity.class);
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

        new RecuperaGruposUsuario().execute();

    }

    private class RecuperaGruposUsuario extends
            AsyncTask<Void, Void, List<Integer>> {

        /** The dialog. */
        private ProgressDialog dialog;

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ViewGroupActivity.this, "Aguarde",
                    "Gerando lista de grupos.");

        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected List<Integer> doInBackground(final Void... params) {
            return getJSON(Session.getInstancia().getDono());
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final List<Integer> result) {
            super.onPostExecute(result);
            new CapturaJSON().execute(result);

            dialog.dismiss();
        }

        /**
         * Gets the json.
         * 
         * @param login
         *            the login
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
     * The Class CapturaJSON.
     */
    private class CapturaJSON extends
            AsyncTask<List<Integer>, Void, List<Grupo>> {

        /** The dialog. */
        private ProgressDialog dialog;

        /** The grupos para mostrar. */
        private List<Grupo> gruposParaMostrar;

        private GrupoAdapter adapter;

        private EditText editsearch;

        private List<Integer> idGruposUsuario;

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ViewGroupActivity.this, "Aguarde",
                    "Gerando lista de grupos.");
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected List<Grupo> doInBackground(final List<Integer>... params) {
            idGruposUsuario = params[0];
            return getJSON();
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final List<Grupo> result) {
            super.onPostExecute(result);

            gruposParaMostrar = new ArrayList<Grupo>();

            for (Grupo grupo : result) {
                if (grupo.isAtivo()) {
                    gruposParaMostrar.add(grupo);
                }
            }

            ListView list = (ListView) findViewById(R.id.listGroups);
            adapter = new GrupoAdapter(getApplicationContext(),
                    gruposParaMostrar, idGruposUsuario, true);
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
                public void beforeTextChanged(CharSequence s, int start,
                        int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                        int before, int count) {
                }
            });

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> parent,
                        final View view, final int position, final long id) {

                    String name = ((TextView) view.findViewById(R.id.nomeGrupo))
                            .getText().toString();

                    String idGrupo = ((TextView) view
                            .findViewById(R.id.idGrupo)).getText().toString();
                    Log.d("CLICOU NO ", "" + idGrupo);

                    // TODO melhorar
                    boolean isRequisitar = true;
                    for (Integer i : idGruposUsuario) {
                        if (i.toString().equals(idGrupo)) {
                            isRequisitar = false;
                            break;
                        }
                    }
                    if (isRequisitar) {
                        return;
                    }
                    // fim melhorar

                    Bundle param = new Bundle();

                    for (Grupo grupo : gruposParaMostrar) {
                        if (String.valueOf(grupo.getId()).equals(idGrupo)) {
                            param.putString("KEY_NAME", grupo.getNome());
                            param.putInt("KEY_ID", grupo.getId());
                            param.putString("KEY_DONO", grupo.getDono());
                            break;
                        }
                    }

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
}
/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

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

        new CapturaJSON().execute();

    }

    /**
     * The Class CapturaJSON.
     */
    private class CapturaJSON extends AsyncTask<Void, Void, List<Grupo>> {

        /** The dialog. */
        private ProgressDialog dialog;

        /** The grupos para mostrar. */
        private List<Grupo> gruposParaMostrar;

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
        protected List<Grupo> doInBackground(final Void... params) {
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
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(),
                    gruposParaMostrar);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> parent,
                        final View view, final int position, final long id) {
                    // getting values from selected ListItem
                    String name = ((TextView) view.findViewById(R.id.nomeGrupo))
                            .getText().toString();
                    String idGrupo = ((TextView) view
                            .findViewById(R.id.idGrupo)).getText().toString();

                    Bundle param = new Bundle();

                    for (Grupo grupo : gruposParaMostrar) {
                        if (String.valueOf(grupo.getId()).equals(idGrupo)) {
                            param.putString("KEY_NAME", grupo.getNome());
                            param.putInt("KEY_ID", grupo.getId());
                            param.putString("KEY_SENHA", grupo.getSenha());
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(),
                            EntraNoGrupo.class);
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

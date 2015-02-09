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

/**
 * The Class EditarActivity.
 */
public class EditarActivity extends Activity {

    /** The m context. */
    private Context mContext;

    /** The criar grupo. */
    private Button criarGrupo;

    /** The meus grupos. */
    private ImageButton grupos, meusGrupos;

    /** The Constant DONO. */
    private static final Object DONO = Session.getInstancia().getDono();

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
        setContentView(R.layout.editar);
        mContext = getApplicationContext();

        criarGrupo = (Button) findViewById(R.id.criarGrupo);
        criarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(mContext, CriarGrupoActivity.class);
                startActivity(i);
            }
        });

        grupos = (ImageButton) findViewById(R.id.grupos);
        grupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(mContext, ViewGroupActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

        meusGrupos = (ImageButton) findViewById(R.id.meusGrupos);
        meusGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(mContext, MeusGruposActivity.class);
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

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(EditarActivity.this, "Aguarde",
                    "Gerando lista de seus grupos.");
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

            ListView list = (ListView) findViewById(R.id.listEditar);
            List<Grupo> editarGrupos = new ArrayList<Grupo>();
            for (Grupo grupo : result) {
                if (grupo.getDono().equals(DONO) && grupo.isAtivo()) {
                    editarGrupos.add(grupo);
                }
            }
            GrupoAdapter adapter = new GrupoAdapter(getApplicationContext(),
                    editarGrupos);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> parent,
                        final View view, final int position, final long id) {
                    // getting values from selected ListItem
                    String name = ((TextView) view.findViewById(R.id.nomeGrupo))
                            .getText().toString();
                    Integer idGrupo = Integer.parseInt(((TextView) view
                            .findViewById(R.id.idGrupo)).getText().toString());

                    openAlert("Excluir Grupo",
                            "Você realmente gostaria de excluir o grupo \""
                                    + name + "\"? :(", idGrupo);
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

    /**
     * Open alert.
     * 
     * @param title
     *            the title
     * @param msg
     *            the msg
     * @param idGrupo
     *            the id grupo
     */
    private void openAlert(final String title, final String msg,
            final Integer idGrupo) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(msg);

        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int id) {
                        new RemoveGrupo().execute(idGrupo);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int id) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    /**
     * The Class RemoveGrupo.
     */
    private class RemoveGrupo extends AsyncTask<Integer, Void, Void> {

        /** The dialog. */
        private ProgressDialog dialog;

        /** The id grupo. */
        private Integer idGrupo;

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(EditarActivity.this,
                    "Aguarde",
                    "Removendo o grupo");
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(final Integer... params) {
            idGrupo = params[0];
            new JSONParse(urlBD + "/findYouFriends/grupo/updateStatus?idGrupo="
                    + idGrupo + "&status=false");
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            
            dialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), Map.class);
            startActivity(intent);

        }

    }

}

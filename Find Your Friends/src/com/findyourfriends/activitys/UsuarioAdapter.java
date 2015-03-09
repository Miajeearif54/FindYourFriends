/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.les.findyourfriends.R;

/**
 * The Class UsuarioAdapter.
 */
public class UsuarioAdapter extends BaseAdapter {

    /** The m usuario. */
    private List<Usuario> mUsuario;

    /** The m inflater. */
    private LayoutInflater mInflater;

    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";

    private Integer idGrupo;

    private String donoGrupo;

    private List<Integer> pendentesGrupo;
    
    private ImageView botao;

    /**
     * Instantiates a new usuario adapter.
     * 
     * @param context
     *            the context
     * @param usuario
     *            the usuario
     */
    public UsuarioAdapter(final Context context, final List<Usuario> usuario,
            boolean isRequisicao, Integer idGrupo, String donoGrupo,
            List<Integer> pendentesGrupo) {
        mInflater = LayoutInflater.from(context);
        mUsuario = usuario;
        this.idGrupo = idGrupo;
        this.donoGrupo = donoGrupo;
        this.pendentesGrupo = new ArrayList<Integer>(pendentesGrupo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {
        return mUsuario.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final Object getItem(final int index) {
        return mUsuario.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public final long getItemId(final int index) {
        return index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public final View getView(final int posicao, final View view,
            final ViewGroup viewGroup) {
        final Usuario usuario = mUsuario.get(posicao);
        final View viewAux = mInflater.inflate(R.layout.usuario_adapter_item, null);
        String nome = mudaCaractere(usuario.getNome(), "_", " ");
        TextView tvNome = (TextView) viewAux.findViewById(R.id.nomeUsuario);
        tvNome.setText(nome);

        botao = (ImageView) viewAux.findViewById(R.id.button);

        if (pendentesGrupo.contains(usuario.getIdUsuario())) {
            botao.setImageDrawable(viewAux.getResources().getDrawable(
                    R.drawable.ic_yes));
        }
        
        if (usuario.getLogin().equals(donoGrupo)) {
            botao.setVisibility(View.GONE);
        }

        botao.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pendentesGrupo.contains(usuario.getIdUsuario())) {
                    new AceitarRequisicao().execute(usuario.getIdUsuario(),
                            idGrupo);
                    
                    botao.setImageDrawable(viewAux.getResources().getDrawable(
                            R.drawable.ic_delete));
                    pendentesGrupo.remove(usuario.getIdUsuario());
                    
                } else {
                    // Remocao de usuario
                    new RejeitarRequisicao().execute(usuario.getIdUsuario(),
                            idGrupo);
                    mUsuario.remove(usuario);
                    /*botao.setImageDrawable(viewAux.getResources().getDrawable(
                            R.drawable.ic_yes));
                    pendentesGrupo.add(usuario.getIdUsuario());*/
                }
                notifyDataSetChanged();
            }
        });

        return viewAux;
    }

    /**
     * Muda caractere.
     * 
     * @param str
     *            the str
     * @param antigo
     *            the antigo
     * @param novo
     *            the novo
     * @return the string
     */
    public final String mudaCaractere(final String str, final String antigo,
            final String novo) {
        return str.replace(antigo, novo);
    }

    private class RejeitarRequisicao extends AsyncTask<Integer, Void, Void> {

        int idUsuario;
        int idGrupo;

        @Override
        protected Void doInBackground(final Integer... params) {
            idUsuario = params[0];
            idGrupo = params[1];
            new JSONParse(urlBD
                    + "/findYouFriends/grupo/denyPermission?idGrupo=" + idGrupo
                    + "&idUsuario=" + idUsuario);
            return null;
        }
    }

    private class AceitarRequisicao extends AsyncTask<Integer, Void, Void> {

        int idUsuario;
        int idGrupo;

        @Override
        protected Void doInBackground(final Integer... params) {
            idUsuario = params[0];
            idGrupo = params[1];
            new JSONParse(urlBD
                    + "/findYouFriends/grupo/approveRequest?idGrupo=" + idGrupo
                    + "&idUsuario=" + idUsuario);
            return null;
        }
    }
}

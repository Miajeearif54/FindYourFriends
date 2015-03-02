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

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.les.findyourfriends.R;

/**
 * The Class GrupoAdapter.
 */
public class GrupoAdapter extends BaseAdapter {

    /** The m grupos. */
    private List<Grupo> mGrupos;

    /** The m inflater. */
    private LayoutInflater mInflater;

    private List<Grupo> auxlist;

    private List<Integer> idGruposUsuario;

    private boolean isRequisicao;
    
    /** The url bd. */
    private String urlBD = "http://150.165.15.89:10008";

    private ImageView botao;
    
    /**
     * Instantiates a new grupo adapter.
     * 
     * @param context
     *            the context
     * @param grupos
     *            the grupos
     */
    public GrupoAdapter(final Context context, final List<Grupo> grupos,
            final List<Integer> idGruposUsuario, boolean isRequisicao) {
        mInflater = LayoutInflater.from(context);
        mGrupos = grupos;
        auxlist = new ArrayList<Grupo>(grupos);
        this.idGruposUsuario = idGruposUsuario;
        this.isRequisicao = isRequisicao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {
        return mGrupos.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final Object getItem(final int index) {
        return mGrupos.get(index);
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
        final View viewAux = mInflater.inflate(R.layout.grupo_adapter_item,
                null);
        final Grupo grupo = mGrupos.get(posicao);

        TextView tvNome = (TextView) viewAux.findViewById(R.id.nomeGrupo);
        tvNome.setText(grupo.getNome());

        TextView tvId = (TextView) viewAux.findViewById(R.id.idGrupo);
        tvId.setText(String.valueOf(grupo.getId()));

        botao = (ImageView) viewAux.findViewById(R.id.botao);

        for (Integer i : idGruposUsuario) {
            if (i.toString().equals(grupo.getId().toString())) {
                botao.setVisibility(View.GONE);
                return viewAux;
            }
        }

        if (isRequisicao) {
            if (grupo.getUsuariosPendentes().contains(Session.getInstancia().getIdUser())) {
                botao.setImageDrawable(viewAux.getResources().getDrawable(
                               R.drawable.ic_clock));
            } else {
                // Requisitando entrada em grupo
                botao.setImageDrawable(viewAux.getResources().getDrawable(
                        R.drawable.ic_lock));
                
                botao.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       ((ImageView)v).setImageDrawable(viewAux.getResources().getDrawable(
                               R.drawable.ic_clock));
                       new AddListaRequisitantes().execute(grupo.getId());
                       //TODO adicionar usuario a lista de requisicoes e salvar 
                    }
                });
            }
        } else {
            // Deletando grupo
            botao.setImageDrawable(viewAux.getResources().getDrawable(
                    R.drawable.ic_delete));
            botao.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mGrupos.remove(posicao);
                    notifyDataSetChanged();
                    new RemoveGrupo().execute(grupo.getId());
                }
            });
        }

        return viewAux;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mGrupos.clear();
        if (charText.length() == 0) {
            mGrupos.addAll(auxlist);
        } else {
            for (Grupo g : auxlist) {
                if (g.getNome().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mGrupos.add(g);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * The Class RemoveGrupo.
     */
    private class RemoveGrupo extends AsyncTask<Integer, Void, Void> {

        /** The id grupo. */
        private Integer idGrupo;

        @Override
        protected Void doInBackground(final Integer... params) {
            idGrupo = params[0];
            new JSONParse(urlBD + "/findYouFriends/grupo/updateStatus?idGrupo="
                    + idGrupo + "&status=false");
            return null;
        }
    }
    
    private class AddListaRequisitantes extends AsyncTask<Integer, Void, Void> {

        /** The id grupo. */
        private Integer idGrupo;

        @Override
        protected Void doInBackground(final Integer... params) {
            idGrupo = params[0];
            new JSONParse(urlBD + "/findYouFriends/grupo/groupApprovalRequest?idGrupo="
                    + idGrupo + "&idUsuario=" + Session.getInstancia().getIdUser());
            return null;
        }
    }
}
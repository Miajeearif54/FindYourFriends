/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.List;

import com.les.findyourfriends.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * The Class GrupoAdapter.
 */
public class GrupoAdapter extends BaseAdapter {

    /** The m grupos. */
    private List<Grupo> mGrupos;
    
    /** The m inflater. */
    private LayoutInflater mInflater;

    /**
     * Instantiates a new grupo adapter.
     *
     * @param context the context
     * @param grupos the grupos
     */
    public GrupoAdapter(final Context context, final List<Grupo> grupos) {
        mInflater = LayoutInflater.from(context);
        mGrupos = grupos;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {
        return mGrupos.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final Object getItem(final int index) {
        return mGrupos.get(index);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public final long getItemId(final int index) {
        return index;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public final View getView(final int posicao, final View view, final ViewGroup viewGroup) {
        Grupo grupo = mGrupos.get(posicao);
        
        TextView tvNome = (TextView) view.findViewById(R.id.nomeGrupo);
        tvNome.setText(grupo.getNome());
        
        TextView tvId = (TextView) view.findViewById(R.id.idGrupo);
        tvId.setText(String.valueOf(grupo.getId()));

        return mInflater.inflate(R.layout.grupo_adapter_item, null);
    }

}

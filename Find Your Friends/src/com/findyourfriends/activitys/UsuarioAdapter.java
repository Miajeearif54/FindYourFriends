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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * The Class UsuarioAdapter.
 */
public class UsuarioAdapter extends BaseAdapter {

    /** The m usuario. */
    private List<Usuario> mUsuario;
    
    /** The m inflater. */
    private LayoutInflater mInflater;

    /**
     * Instantiates a new usuario adapter.
     *
     * @param context the context
     * @param usuario the usuario
     */
    public UsuarioAdapter(Context context, List<Usuario> usuario) {
        mInflater = LayoutInflater.from(context);
        mUsuario = usuario;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mUsuario.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int index) {
        return mUsuario.get(index);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int index) {
        return index;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.usuario_adapter_item, null);
        Usuario usuario = mUsuario.get(posicao);
        
        String nome = mudaCaractere(usuario.getNome(), "_", " ");
        
        
        TextView tvNome = (TextView) view.findViewById(R.id.nomeUsuario);
        tvNome.setText(nome);
        
        return view;
    }
    
    /**
     * Muda caractere.
     *
     * @param str the str
     * @param antigo the antigo
     * @param novo the novo
     * @return the string
     */
    public String mudaCaractere(String str, String antigo, String novo){
        str = str.replace(antigo, novo);
        return str;
    }
}

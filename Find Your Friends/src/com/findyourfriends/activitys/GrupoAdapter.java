package com.findyourfriends.activitys;

import java.util.List;

import com.les.findyourfriends.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class GrupoAdapter extends BaseAdapter {

    private List<Grupo> mGrupos;
    private LayoutInflater mInflater;

    public GrupoAdapter(Context context, List<Grupo> grupos) {
        mInflater = LayoutInflater.from(context);
        mGrupos = grupos;
    }

    @Override
    public int getCount() {
        return mGrupos.size();
    }

    @Override
    public Object getItem(int index) {
        return mGrupos.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.grupo_adapter_item, null);
        Grupo grupo = mGrupos.get(posicao);
        
        TextView tvNome = (TextView) view.findViewById(R.id.nomeGrupo);
        tvNome.setText(grupo.getNome());


        return view;
    }

}

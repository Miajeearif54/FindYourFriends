package com.findyourfriends.activitys;

import com.les.findyourfriends.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CriarGrupoActivity extends Activity{
	private Context mContext;    
    private ImageButton editar, grupos, meusGrupos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.criar_grupo);
		mContext = getApplicationContext();
		
		grupos = (ImageButton) findViewById(R.id.grupos);        
        grupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, ViewGroupActivity.class);
				startActivity(i);
			}
		});
              
        meusGrupos = (ImageButton) findViewById(R.id.meusGrupos);        
        meusGrupos.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, MeusGruposActivity.class);
				startActivity(i);
			}
		});
        
        editar = (ImageButton) findViewById(R.id.editar);
        editar.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, EditarActivity.class);
				startActivity(i);
			}
		});
		
	}
}
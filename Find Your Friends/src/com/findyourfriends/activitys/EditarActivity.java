package com.findyourfriends.activitys;

import com.les.findyourfriends.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class EditarActivity extends Activity{
	private Context mContext;
	private Button criarGrupo;
	private ImageButton editar, grupos, meusGrupos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar);
		mContext = getApplicationContext();
		
		
		criarGrupo = (Button) findViewById(R.id.criarGrupo);
        criarGrupo.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, CriarGrupoActivity.class);
				startActivity(i);
			}
		});
        
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
		
	}
}
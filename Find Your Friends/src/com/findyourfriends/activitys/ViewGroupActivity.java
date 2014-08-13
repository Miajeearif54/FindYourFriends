package com.findyourfriends.activitys;

import java.util.List;

import com.les.findyourfriends.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class ViewGroupActivity extends Activity{
	private Context mContext;
	private ImageButton editar, grupos, meusGrupos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_groups);
		mContext = getApplicationContext();
              
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



package com.findyourfriends.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.les.findyourfriends.R;

public class EntraNoGrupo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrar_grupo);
        
        Intent it = getIntent();
        Bundle param = it.getExtras();
        String name = param.getString("KEY_NAME");
        
        EditText nome = (EditText) findViewById(R.id.edNomeGrupo_entrar);    
        nome.setText(name);
                
        
        Button entrar = (Button) findViewById(R.id.entrar);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                                
            } });
        
        
    }
    
   

}

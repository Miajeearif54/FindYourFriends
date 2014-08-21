package com.findyourfriends.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        String nameGrupo = param.getString("KEY_NAME");
        final String senhaGrupo = param.getString("KEY_SENHA");
        
        
        EditText nome = (EditText) findViewById(R.id.edNomeGrupo_entrar);    
        nome.setText(nameGrupo);
        
        Button entrar = (Button) findViewById(R.id.entrar);
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senha = ((EditText) findViewById(R.id.edSenha_entrar)).getText().toString();
                
                if(senhaGrupo.equals(senha)){
                    Log.d("werton", "TRUE");
                    Intent i = new Intent(getApplicationContext(), MeusGruposActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
                                
            } });
        
    }
    
   

}

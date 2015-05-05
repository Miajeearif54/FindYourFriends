package com.findyourfriends.activitys;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private static Timer timer = new Timer();
    private String urlBD = "http://150.165.98.11:8080";
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("renan", "bind");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    
    @Override
    public void onCreate() {
        Log.d("renan", "Create");
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
       
    }
 
    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("renan", "start");
        //new CapturaJSON().execute();
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        
       timer.scheduleAtFixedRate(new mainTask(), 0, 300000); //5min  
   
    }
 
    private class mainTask extends TimerTask{
        
        @Override
        public void run() {
            Log.d("renan", "Task");
            new CapturaJSON().execute();
            
        }
        
    }
    
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        
    }
    
    
private class CapturaJSON extends AsyncTask<Void, Void, List<Integer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        
        @Override
        protected List<Integer> doInBackground(final Void... params) {
            return getJSON(Session.getInstancia().getDono());
        }

        @Override
        protected void onPostExecute(final List<Integer> result) {
            super.onPostExecute(result);
                        
            new RecuperaPonto().execute(result);
 
        }

        private List<Integer> getJSON(final String login) {
            String url = urlBD 
                    + "/findYouFriends/usuario/getCurrentLocation?login="  
                    + login;
            JSONParse parser = new JSONParse(url);
            return parser.getGruposUsuarios();
        }
    }
    
    
    private class RecuperaPonto extends AsyncTask<List<Integer>, Void, List<Grupo>> {
                        
            /** The id grupos. */
            private List<Integer> idGrupos;
            
            /** The grupos do usuario. */
            private List<Grupo> gruposDoUsuario;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Grupo> doInBackground(final List<Integer>... params) {
                idGrupos = params[0];
                return getJSON();
            }

            @Override
            protected void onPostExecute(final List<Grupo> result) {
                super.onPostExecute(result);
                
                gruposDoUsuario = new ArrayList<Grupo>();

                for (Grupo grupo : result) {
                    for (Integer idGrupo : idGrupos) {
                        if (grupo.getId() == idGrupo && grupo.isAtivo()) {
                            grupo.setNome(mudaCaractere(grupo.getNome(), "_", " "));
                            Log.d("ponto", "meu grupo: " + grupo.getNome());
                            Log.d("ponto", "meu grupo lat: " + grupo.getLatitude());
                            gruposDoUsuario.add(grupo);
                            break;
                        }
                    }
                }
            }
                       
            /**
             * Gets the json.
             *
             * @return the json
             */
            private List<Grupo> getJSON() {
                JSONParse parser = new JSONParse(urlBD 
                        + "/findYouFriends/grupo/listGroups");
                return parser.getGruposBD();
            }
    }
    
    
    public final String mudaCaractere(final String str, 
            final String antigo, final String novo) {
        return str.replace(antigo, novo);
    }
}

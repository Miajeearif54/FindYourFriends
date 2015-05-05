package com.findyourfriends.activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MyService extends Service {
    private static Timer timer = new Timer();
    private String urlBD = "http://150.165.98.11:8080";
    private HashMap<String, String> mapAtual;
    
    public Integer idUsuario;

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
        mapAtual = new HashMap<String, String>();
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d("renan", "start");
        // new CapturaJSON().execute();
        // For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        
        timer.scheduleAtFixedRate(new mainTask(), 0, 10000); // 5min 300000
    }

    private class mainTask extends TimerTask {

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
            //idUsuario = parser.getIdUsuario();
            return parser.getGruposUsuarios();
        }
    }

    private class RecuperaPonto extends
            AsyncTask<List<Integer>, Void, List<Grupo>> {

        /** The id grupos. */
        private List<Integer> idGrupos;

        /** The grupos do usuario. */
        private List<Grupo> gruposDoUsuario;

        @Override
        protected void onPreExecute() {
            Log.d("renan", "Pre");
            super.onPreExecute();
        }

        @Override
        protected List<Grupo> doInBackground(final List<Integer>... params) {
            idGrupos = params[0];
            Log.d("renan", "Background");
            return getJSON();
        }

        @Override
        protected void onPostExecute(final List<Grupo> result) {
            super.onPostExecute(result);
            
            Log.d("renan", "Post");

            gruposDoUsuario = new ArrayList<Grupo>();
            

            if (mapAtual.isEmpty()) {
                Log.d("renan", "if");
                for (Grupo grupo : result) {
                    
                    Log.d("ponto", "tods os grupos: " + grupo.getNome());
                    Log.d("ponto", "size grupo: " + idGrupos.size());
                    for (Integer idGrupo : idGrupos) { 
                        
                        if (grupo.getId() == idGrupo && grupo.isAtivo()) {
                            grupo.setNome(mudaCaractere(grupo.getNome(), "_",
                                    " "));
                            Log.d("ponto", "meu grupo: " + grupo.getNome());
                            Log.d("ponto","meu grupo lat: " + grupo.getLatitude());
                            gruposDoUsuario.add(grupo);

                            mapAtual.put(grupo.getNome(), grupo.getLatitude()
                                    + " " + grupo.getLongitude());
                            break;
                        }
                    }
                }
            } else {
                Log.d("renan", "else");
                for (Grupo grupo : result) {
                    for (Integer idGrupo : idGrupos) {
                        if (grupo.getId() == idGrupo && grupo.isAtivo()) {
                            grupo.setNome(mudaCaractere(grupo.getNome(), "_",
                                    " "));
                            if (mapAtual.containsKey(grupo.getNome())) {
                                if (!(mapAtual.get(grupo.getNome())
                                        .equals(grupo.getLatitude() + " "
                                                + grupo.getLongitude()))) {
                                    // notifique
                                    gerarNotificacao();
                                    Log.d("renan", "notificou");
                                    mapAtual.remove(grupo.getNome());
                                    mapAtual.put(
                                            grupo.getNome(),
                                            grupo.getLatitude() + " "
                                                    + grupo.getLongitude());
                                }
                            } else {
                                mapAtual.put(
                                        grupo.getNome(),
                                        grupo.getLatitude() + " "
                                                + grupo.getLongitude());
                                break;
                            }
                        }
                    }
                }
            }
            
            Log.d("renan", "fim");

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

    public final String mudaCaractere(final String str, final String antigo,
            final String novo) {
        return str.replace(antigo, novo);
    }
    
    public void gerarNotificacao(){       
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, Splash.class), 0); //classe de inicio ao clicar
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle("Novo ponto criado");
        builder.setContentText("Um novo ponto de encontro foi criado");
        builder.setSmallIcon(com.les.findyourfriends.R.drawable.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), com.les.findyourfriends.R.drawable.ic_launcher));
        builder.setContentIntent(p);
        
        
        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(com.les.findyourfriends.R.drawable.ic_launcher, n);
        
        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch(Exception e){}
    }
    
}

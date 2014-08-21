package com.findyourfriends.activitys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JSONParse {
    
    protected URL feedUrl;
    private JSONArray json;
    private String jsonString; 
    
    private static final String DONO = "dono";
    private static final String ATIVO = "ativo";
    private static final String NOME = "nome";
    private static final String SENHA = "senha";
    private static final String USUARIOS = "usuarios";
    
    private static final String ID = "id";
    
    private boolean adicionou;
    
    public JSONParse(String feedUrl) {
        adicionou = true;
        try {
            this.feedUrl = new URL(feedUrl);
            json();
        } catch (MalformedURLException e) {
            adicionou = false;
        }
        
    }
    
    private void json() {
        try {
            jsonString = convertStreamToString(feedUrl.openConnection().getInputStream());
//            String jsonString = "[{\"class\":\"findyoufriends.Grupo\",\"id\":1,\"dono\":\"werton007\",\"duracao\":60,\"nome\":\"Forninho\",\"senha\":\"123\",\"usuarios\":[{\"class\":\"Usuario\",\"id\":1}]},"
//                    + "{\"class\":\"findyoufriends.Grupo\",\"id\":2,\"dono\":\"werton\",\"duracao\":132,\"nome\":\"Plecas\",\"senha\":\"adsad\",\"usuarios\":[]}," 
//                    + "{\"class\":\"findyoufriends.Grupo\",\"id\":3,\"dono\":\"Ines Brasil\",\"duracao\":123123,\"nome\":\"xpto\",\"senha\":\"adsa\",\"usuarios\":[]},"
//                    + "{\"class\":\"findyoufriends.Grupo\",\"id\":4,\"dono\":\"pff\",\"duracao\":12313,\"nome\":\"Pff\",\"senha\":\"ww\",\"usuarios\":[]}]";
            json = new JSONArray(jsonString);
            
        } catch (JSONException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String convertStreamToString(final InputStream input) {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        final StringBuilder sBuf = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
        } catch (IOException e) {
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            }
        }
        Log.d("werton", sBuf.toString());
        return sBuf.toString();
    }
    
    public boolean isNull(){
        return (json == null);
    }
    
    public List<Grupo> getGruposBD() {
        List<Grupo> grupos = new ArrayList<Grupo>();

        if(json != null){
            for (int i = 0; i < json.length(); i++) {
                JSONObject grupo;
                try {
                    grupo = json.getJSONObject(i);
                    grupos.add(recuperaGrupos(grupo));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
    
            }
        }

        return grupos;
    }
    
    public List<Usuario> getUsuariosBD() {
        List<Usuario> usuarios = new ArrayList<Usuario>();

        if(json != null){
            for (int i = 0; i < json.length(); i++) {
                JSONObject usuario;
                try {
                    usuario = json.getJSONObject(i);
                    usuarios.add(recuperaUsuarios(usuario));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
    
            }
        }

        return usuarios;
    }
    
    public Integer getIdUsuario(){
        try {
            JSONObject idUser = new JSONObject(jsonString);
            return convert(idUser.get("id"), Integer.class);
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
    
    public List<Integer> getGruposUsuarios() {
        List<Integer> idGrupos = new ArrayList<Integer>();
        try {
            JSONObject usuario = new JSONObject(jsonString);
            JSONArray gUsuarios = convert(usuario.get("grupos"), JSONArray.class);
            
            for (int i = 0; i < gUsuarios.length(); i++) {
                JSONObject grupo;
                try {
                    grupo = gUsuarios.getJSONObject(i);
                    idGrupos.add(convert(grupo.get("id"), Integer.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return idGrupos;
    }
    
    public List<Integer> getIds(JSONArray ids) {
        List<Integer> idList = new ArrayList<Integer>();
        for (int i = 0; i < ids.length(); i++) {
            JSONObject usuario;
            try {
                usuario = ids.getJSONObject(i);
                idList.add(convert(usuario.get("id"), Integer.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return idList;
    }
    
    private Usuario recuperaUsuarios(JSONObject item) throws JSONException {
        return new Usuario(convert(item.get(ID), Integer.class),
                convert(item.get("login"), String.class),
                Double.parseDouble(convert(item.get("latitude"), String.class)),
                Double.parseDouble(convert(item.get("longitude"), String.class)),
                convert(item.get(NOME), String.class),
                getIds(convert(item.get("grupos"), JSONArray.class))); //aqui deve ser colocado a lista de usuarios (os id deles);
    }
    
    private Grupo recuperaGrupos(JSONObject item) throws JSONException {
        String nome = convert(item.get(NOME), String.class);
        nome = mudaCaractere(nome, "_", " ");   
        
        return new Grupo(convert(item.get(ID), Integer.class),
                nome,
                convert(item.get(DONO), String.class),
                convert(item.get(ATIVO), Boolean.class),
                convert(item.get(SENHA), String.class),
                getIds(convert(item.get(USUARIOS), JSONArray.class))); //aqui deve ser colocado a lista de usuarios (os id deles);
    }
    
    /**
     * O metodo Auxiliar que converte um Objeto do JSON para um tipo especificado.
     * 
     * @param obj - O objesto do JSON.
     * @param type - O tipo que sera o Objeto.
     * @return O objeto convertido com algum tipo.
     */
    
    @SuppressWarnings("unchecked")
    private <T> T convert(Object obj, Class<T> type) {
        if (obj == null) {
            return null;        
        }
        return (T) obj;
    }
    
    public String mudaCaractere(String str, String antigo, String novo){
        str = str.replace(antigo, novo);
        return str;
    }
     

}

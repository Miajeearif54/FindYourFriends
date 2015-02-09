/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class JSONParse.
 */
public class JSONParse {

    /** The feed url. */
    private URL feedUrl;

    /** The json. */
    private JSONArray json;

    /** The json string. */
    private String jsonString;

    /** The Constant DONO. */
    private static final String DONO = "dono";

    /** The Constant ATIVO. */
    private static final String ATIVO = "ativo";

    /** The Constant NOME. */
    private static final String NOME = "nome";

    /** The Constant SENHA. */
    private static final String SENHA = "senha";

    /** The Constant USUARIOS. */
    private static final String USUARIOS = "usuarios";

    /** The Constant ID. */
    private static final String ID = "id";

    /** The adicionou. */
    private static boolean adicionou;

    /**
     * Instantiates a new JSON parse.
     * 
     * @param feedUrlParam
     *            the feed url
     */
    public JSONParse(final String feedUrlParam) {
        adicionou = true;
        try {
            feedUrl = new URL(feedUrlParam);
            json();
        } catch (MalformedURLException e) {
            adicionou = false;
        }

    }
    
    /**
     * 
     * @return a requisição do usuario foi feita com sucesso.
     */
    public static boolean getAdicionou() {
        return adicionou;
    }
    

    /**
     * Json.
     */
    private void json() {
        try {
            jsonString = convertStreamToString(feedUrl.openConnection()
                    .getInputStream());
            json = new JSONArray(jsonString);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert stream to string.
     * 
     * @param input
     *            the input
     * @return the string
     */
    private String convertStreamToString(final InputStream input) {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                input));
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
        return sBuf.toString();
    }

    /**
     * Checks if is null.
     * 
     * @return true, if is null
     */
    public final boolean isNull() {
        return (json == null);
    }

    /**
     * Gets the grupos bd.
     * 
     * @return the grupos bd
     */
    public final List<Grupo> getGruposBD() {
        List<Grupo> grupos = new ArrayList<Grupo>();

        if (json != null) {
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

    /**
     * Gets the usuarios bd.
     * 
     * @return the usuarios bd
     */
    public final List<Usuario> getUsuariosBD() {
        List<Usuario> usuarios = new ArrayList<Usuario>();

        if (json != null) {
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

    /**
     * Gets the id usuario.
     * 
     * @return the id usuario
     */
    public final Integer getIdUsuario() {
        try {
            /*
             * if (jsonString == null) { return -1; }
             */
            JSONObject idUser = new JSONObject(jsonString);
            return convert(idUser.get("id"), Integer.class);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets the grupos usuarios.
     * 
     * @return the grupos usuarios
     */
    public final List<Integer> getGruposUsuarios() {
        List<Integer> idGrupos = new ArrayList<Integer>();
        try {
            if (jsonString == null) {
                return idGrupos;
            }
            JSONObject usuario = new JSONObject(jsonString);
            JSONArray gUsuarios = convert(usuario.get("grupos"),
                    JSONArray.class);

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

    /**
     * Gets the ids.
     * 
     * @param ids
     *            the ids
     * @return the ids
     */
    public final List<Integer> getIds(final JSONArray ids) {
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

    /**
     * Recupera usuarios.
     * 
     * @param item
     *            the item
     * @return the usuario
     * @throws JSONException
     *             the JSON exception
     */
    private Usuario recuperaUsuarios(final JSONObject item)
            throws JSONException {
        return new Usuario(
                convert(item.get(ID), Integer.class),
                convert(item.get("login"), String.class),
                Double.parseDouble(convert(item.get("latitude"), String.class)),
                Double.parseDouble(convert(item.get("longitude"), String.class)),
                convert(item.get(NOME), String.class), getIds(convert(
                        item.get("grupos"), JSONArray.class)));
        // aqui deve ser colocado a lista de usuarios (os id deles);
    }

    /**
     * Recupera grupos.
     * 
     * @param item
     *            the item
     * @return the grupo
     * @throws JSONException
     *             the JSON exception
     */
    private Grupo recuperaGrupos(final JSONObject item) throws JSONException {
        String nome = convert(item.get(NOME), String.class);
        nome = mudaCaractere(nome, "_", " ");

        return new Grupo(convert(item.get(ID), Integer.class), nome, convert(
                item.get(DONO), String.class), convert(item.get(ATIVO),
                Boolean.class), convert(item.get(SENHA), String.class),
                getIds(convert(item.get(USUARIOS), JSONArray.class)));
        // aqui deve ser colocado a lista de usuarios (os id deles);
    }

    /**
     * O metodo Auxiliar que converte um Objeto do JSON para um tipo
     * especificado.
     * 
     * @param <T>
     *            the generic type
     * @param obj
     *            - O objesto do JSON.
     * @param type
     *            - O tipo que sera o Objeto.
     * @return O objeto convertido com algum tipo.
     */

    @SuppressWarnings("unchecked")
    private <T> T convert(final Object obj, final Class<T> type) {
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    /**
     * Muda caractere.
     * 
     * @param str
     *            the str
     * @param antigo
     *            the antigo
     * @param novo
     *            the novo
     * @return the string
     */
    public final String mudaCaractere(final String str, final String antigo,
            final String novo) {
        return str.replace(antigo, novo);
    }

}

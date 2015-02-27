/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.List;

/**
 * The Class Usuario.
 */
public class Usuario {

    /** The login. */
    private String nome, login;

    /** The longitude. */
    private Double latitude, longitude;

    /** The id usuario. */
    private Integer idUsuario;

    /** The id grupos. */
    private List<Integer> idGrupos;
    
    /** The id grupos. */
    private List<Integer> idGruposInscritos;
    
    

    //TODO colocar email
    
    /**
     * Instantiates a new usuario.
     * 
     * @param idUsuarioParam
     *            the id usuario
     * @param loginParam
     *            the login
     * @param latitudeParam
     *            the latitude
     * @param longitudeParam
     *            the longitude
     * @param nomeParam
     *            the nome
     * @param idGruposParam
     *            the id grupos
     */
    public Usuario(final Integer idUsuarioParam, final String loginParam,
            final Double latitudeParam, final Double longitudeParam,
            final String nomeParam, final List<Integer> idGruposParam, final List<Integer> idGruposInscritosParam) {
        login = loginParam;
        nome = nomeParam;
        latitude = latitudeParam;
        longitude = longitudeParam;
        idUsuario = idUsuarioParam;
        idGrupos = idGruposParam;
        idGruposInscritos = idGruposInscritosParam;
    }

    /**
     * Gets the latitude.
     * 
     * @return the latitude
     */
    public final Double getLatitude() {
        return latitude;
    }

    /**
     * Gets the id grupos.
     * 
     * @return the id grupos
     */
    public final List<Integer> getIdGrupos() {
        return idGrupos;
    }

    /**
     * Gets the id usuario.
     * 
     * @return the id usuario
     */
    public final Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Gets the login.
     * 
     * @return the login
     */
    public final String getLogin() {
        return login;
    }

    /**
     * Gets the longitude.
     * 
     * @return the longitude
     */
    public final Double getLongitude() {
        return longitude;
    }

    /**
     * Gets the nome.
     * 
     * @return the nome
     */
    public final String getNome() {
        return nome;
    }

}

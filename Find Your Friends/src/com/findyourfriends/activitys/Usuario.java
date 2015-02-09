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
    
    /**
     * Instantiates a new usuario.
     *
     * @param idUsuario the id usuario
     * @param login the login
     * @param latitude the latitude
     * @param longitude the longitude
     * @param nome the nome
     * @param idGrupos the id grupos
     */
    public Usuario(Integer idUsuario, String login, Double latitude, Double longitude, String nome, List<Integer> idGrupos) {
        this.login = login;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idUsuario = idUsuario;
        this.idGrupos = idGrupos;
    }
    
    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }
    
    /**
     * Gets the id grupos.
     *
     * @return the id grupos
     */
    public List<Integer> getIdGrupos() {
        return idGrupos;
    }
    
    /**
     * Gets the id usuario.
     *
     * @return the id usuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    /**
     * Gets the login.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }
    
    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }
    
    /**
     * Gets the nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

}

/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.List;

public class Usuario {
    
    private String nome, login;
    private Double latitude, longitude;
    private Integer idUsuario;
    private List<Integer> idGrupos;
    
    public Usuario(Integer idUsuario, String login, Double latitude, Double longitude, String nome, List<Integer> idGrupos) {
        this.login = login;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idUsuario = idUsuario;
        this.idGrupos = idGrupos;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public List<Integer> getIdGrupos() {
        return idGrupos;
    }
    
    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    public String getLogin() {
        return login;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public String getNome() {
        return nome;
    }

}

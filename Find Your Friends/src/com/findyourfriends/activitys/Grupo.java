/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

import java.util.List;

/**
 * The Class Grupo.
 */
public class Grupo {
    
    /** The senha. */
    private String nome, dono, senha;
    
    /** The ativo. */
    private boolean ativo;
    
    /** The id. */
    private Integer id ;
    
    /** The usuarios. */
    private List<Integer> usuarios;
    
    /**
     * Instantiates a new grupo.
     *
     * @param id the id
     * @param nome the nome
     * @param dono the dono
     * @param ativo the ativo
     * @param senha the senha
     * @param usuarios the usuarios
     */
    public Grupo(Integer id, String nome, String dono, Boolean ativo, String senha, List<Integer> usuarios) {
        this.nome = nome;
        this.dono = dono;
        this.senha = senha;
        this.ativo = ativo;
        this.usuarios = usuarios;
        this.id = id;
    }
    
    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Gets the dono.
     *
     * @return the dono
     */
    public String getDono() {
        return dono;
    }
    
    /**
     * Checks if is ativo.
     *
     * @return true, if is ativo
     */
    public boolean isAtivo() {
        return ativo;
    }
    
    /**
     * Gets the nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Sets the nome.
     *
     * @param nome the new nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Gets the senha.
     *
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }
    
    /**
     * Gets the usuarios.
     *
     * @return the usuarios
     */
    public List<Integer> getUsuarios() {
        return usuarios;
    }
}

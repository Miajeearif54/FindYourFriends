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
    private Integer id;

    /** The usuarios. */
    private List<Integer> usuarios;
    
    /** The usuarios. */
    private List<Integer> usuariosPendentes;
    
    

    /**
     * Instantiates a new grupo.
     * 
     * @param idParam
     *            the idParam
     * @param nomeParam
     *            the nomeParam
     * @param donoParam
     *            the donoParam
     * @param ativoParam
     *            the ativoParam
     * @param senhaParam
     *            the senhaParam
     * @param usuariosParam
     *            the usuariosParam
     * @param usuariosPendentesParam
     *            the usuariosPendentesParam
     * 
     */
    public Grupo(final Integer idParam, final String nomeParam,
            final String donoParam, final Boolean ativoParam,
            final String senhaParam, final List<Integer> usuariosParam,
            final List<Integer> usuariosPendentesParam) {
        nome = nomeParam;
        dono = donoParam;
        senha = senhaParam;
        ativo = ativoParam;
        usuarios = usuariosParam;
        usuariosPendentes = usuariosPendentesParam;
        id = idParam;
    }

    /**
     * Gets the id.
     * 
     * @return the id.
     */
    public final Integer getId() {
        return id;
    }

    /**
     * Gets the dono.
     * 
     * @return the dono.
     */
    public final String getDono() {
        return dono;
    }

    /**
     * Checks if is ativo.
     * 
     * @return true, if is ativo.
     */
    public final boolean isAtivo() {
        return ativo;
    }

    /**
     * Gets the nome.
     * 
     * @return the nome.
     */
    public final String getNome() {
        return nome;
    }

    /**
     * Sets the nome.
     * 
     * @param nomeParam
     *            the new nome.
     */
    public final void setNome(final String nomeParam) {
        nome = nomeParam;
    }

    /**
     * Gets the senha.
     * 
     * @return the senha.
     */
    public final String getSenha() {
        return senha;
    }

    /**
     * Gets the usuarios.
     * 
     * @return the usuarios.
     */
    public final List<Integer> getUsuarios() {
        return usuarios;
    }
    
    /**
     * Gets the usuariosPendentes.
     * 
     * @return the usuariosPendentes.
     */
    public final List<Integer> getUsuariosPendentes() {
        return usuariosPendentes;
    }
}

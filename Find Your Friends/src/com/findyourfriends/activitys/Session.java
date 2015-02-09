/*
 * Universidade Federal de Campina Grande (UFCG).
 * Disciplina: Projeto I
 * Desenvolvedores: Carla Sukeyosi, Diego Ernesto
 *                  Renan Pinto, Talita Lobo e Werton Guimarães.
 */

package com.findyourfriends.activitys;

/**
 * The Class Session.
 */
public class Session {

    /** The instancia. */
    private static Session instancia;
    
    /** The dono. */
    private String dono;
    
    /** The id user. */
    private Integer idUser;

    /**
     * Instantiates a new session.
     */
    protected Session() {
    }

    /**
     * Gets the instancia.
     *
     * @return the instancia
     */
    public static Session getInstancia() {
        if (instancia == null) {
            instancia = new Session();
        }
        return instancia;
    }
    
    /**
     * Del instancia.
     */
    public static void delInstancia(){
        instancia = null;
    }
    
    /**
     * Sets the dono.
     *
     * @param dono the new dono
     */
    public void setDono(String dono) {
        this.dono = dono;
    }
    
    /**
     * Gets the id user.
     *
     * @return the id user
     */
    public Integer getIdUser() {
        return idUser;
    }
    
    /**
     * Sets the id user.
     *
     * @param idUser the new id user
     */
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    
    /**
     * Gets the dono.
     *
     * @return the dono
     */
    public String getDono() {
        return dono;
    }
    
}

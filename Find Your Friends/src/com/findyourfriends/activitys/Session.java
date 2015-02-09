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
    public static void delInstancia() {
        instancia = null;
    }
    
    /**
     * Sets the dono.
     *
     * @param donoParam the new dono
     */
    public final void setDono(final String donoParam) {
        dono = donoParam;
    }
    
    /**
     * Gets the id user.
     *
     * @return the id user
     */
    public final Integer getIdUser() {
        return idUser;
    }
    
    /**
     * Sets the id user.
     *
     * @param idUserParam the new id user
     */
    public final void setIdUser(final Integer idUserParam) {
        idUser = idUserParam;
    }
    
    
    /**
     * Gets the dono.
     *
     * @return the dono
     */
    public final String getDono() {
        return dono;
    }
    
}

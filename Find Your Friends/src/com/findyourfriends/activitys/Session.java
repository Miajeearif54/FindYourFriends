package com.findyourfriends.activitys;

public class Session {

    private static Session instancia;
    private String dono;

    protected Session() {
    }

    public static Session getInstancia() {
        if (instancia == null) {
            instancia = new Session();
        }
        return instancia;
    }
    
    public static void delInstancia(){
        instancia = null;
    }
    
    public void setDono(String dono) {
        this.dono = dono;
    }
    
    public String getDono() {
        return dono;
    }
    
}

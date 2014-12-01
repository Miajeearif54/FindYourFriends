package com.findyourfriends.activitys;

import java.util.List;

public class Grupo {
    
    private String nome, dono, senha;
    private boolean ativo;
    private Integer id ;
    private List<Integer> usuarios;
    
    public Grupo(Integer id, String nome, String dono, Boolean ativo, String senha, List<Integer> usuarios) {
        this.nome = nome;
        this.dono = dono;
        this.senha = senha;
        this.ativo = ativo;
        this.usuarios = usuarios;
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getDono() {
        return dono;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public List<Integer> getUsuarios() {
        return usuarios;
    }
}

package com.findyourfriends.activitys;

import java.util.List;

public class Grupo {
    
    private String nome, dono, senha, duracao;
    private Integer id ;
    private List<Integer> usuarios;
    
    public Grupo(Integer id, String nome, String dono, String duracao, String senha, List<Integer> usuarios) {
        this.nome = nome;
        this.dono = dono;
        this.senha = senha;
        this.duracao = duracao;
        this.usuarios = usuarios;
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getDono() {
        return dono;
    }
    
    public String getDuracao() {
        return duracao;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public List<Integer> getUsuarios() {
        return usuarios;
    }
}

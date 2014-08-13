package com.findyourfriends.activitys;

import java.util.List;

public class Grupo {
    
    private String nome, dono, senha;
    private String duracao;
    private List<Usuario> usuarios;
    
    public Grupo(String nome, String dono, String duracao, String senha, List<Usuario> usuarios) {
        this.nome = nome;
        this.dono = dono;
        this.senha = senha;
        this.duracao = duracao;
        this.usuarios = usuarios;
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
    
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.tarefagerenc1.controller;

import br.com.tarefagerenc1.modelo.Tarefa;
import br.com.tarefagerenc1.modelo.Usuario;
import br.com.tarefagerenc1.modelo.exceptions.NonexistentEntityException;
import br.com.tarefasgerenc1.dao.TarefaDAO;
import br.com.tarefasgerenc1.dao.UsuarioDao;
import br.com.tarefasgerenc1.dao.UsuarioJpaController;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static jdk.nashorn.internal.objects.ArrayBufferView.length;
import sun.security.util.Length;

/**
 *
 * @author wesle
 */
public class UsuarioController {
    
    private Usuario usuario;
    private List<Usuario> usuarios;
    private UsuarioDao dao;

    public UsuarioController() {
        novoUsuario();
        usuarios = new ArrayList<Usuario>();
        dao = new UsuarioDao();
        //listarUsuarios();
    }

    public void inserirUsuario() {
        dao.create(usuario);
        novoUsuario();
    }

    private void novoUsuario() {
        usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void listarUsuarios(){
        usuarios = dao.findUsuarioEntities();
    }
    
    public List<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public boolean verificarSenha(String s1, String s2) {
        return s1.equals(s2);
    }
    
    public boolean autenticarUsuario(Usuario u) throws NoSuchAlgorithmException{
        usuarios = dao.findUsuarioEntities();
        
        for(Usuario usuario : usuarios){
            if ((u.getEmail().equals(usuario.getEmail())) && (converterSenhaMD5(u.getSenha()).equals(usuario.getSenha()))){
                u.setId(usuario.getId());
                u.setNome(usuario.getNome());
                return true;
            }
            
        }
        return false;        
    }    
    public String converterSenhaMD5(String senha) throws NoSuchAlgorithmException{
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(senha.getBytes(),0,senha.length());    
        return new BigInteger(1,m.digest()).toString(16);
    }

}

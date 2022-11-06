/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.tarefagerenc1.view;

import br.com.tarefagerenc1.controller.TarefaController;
import br.com.tarefagerenc1.controller.UsuarioController;
import br.com.tarefagerenc1.modelo.Tarefa;
import br.com.tarefagerenc1.modelo.Usuario;
import br.com.tarefagerenc1.modelo.exceptions.NonexistentEntityException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wesle
 */
public class UsuarioView {
    Scanner sc = new Scanner(System.in);   
    private UsuarioController u;
    
    public UsuarioView(){
        u = new UsuarioController();
    }
    
    public void menu() throws Exception{
        int opcao = 0;
        Scanner sc = new Scanner(System.in);
        do {
            colocarPontilhado();
            System.out.println("Bem vindo ao sistema de criação de tarefas "
                    + "\npor favor, escolha a opção desejada!!!");
            colocarPontilhado();
            System.out.println("1 - Cadastrar Usuario");
            System.out.println("2 - Autenticar Usuario");
            System.out.println("3 - Listar Usuario");
            
            opcao = sc.nextInt();
            
            switch(opcao){
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    loginUsuario();
                    break; 
                case 3:
                    listarUsuarios();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção Inválida");
            }
            
        }while(opcao != 0);
    }
    public void cadastrarUsuario() throws NoSuchAlgorithmException{
        
        System.out.println("---1 - Cadastro de Usuario");
        System.out.println("Entre com o nome: ");
        String nomeUsuario = sc.nextLine();
        u.getUsuario().setNome(nomeUsuario);
        
        System.out.println("Entre com o e-mail: ");
        String emailUsuario = sc.nextLine();
        u.getUsuario().setEmail(emailUsuario);
        
        System.out.println("Entre com a senha: ");
        String senha1Usuario = sc.nextLine();

        System.out.println("Digite a senha novamente: ");
        String senha2Usuario = sc.nextLine();
        
        boolean CadastraUsuario = u.verificarSenha(senha1Usuario, senha2Usuario);
        u.getUsuario().setSenha(u.converterSenhaMD5(senha1Usuario));
                
        if (CadastraUsuario == true){
            u.inserirUsuario();
            System.out.println("Usuario cadastrado com sucesso");
        }
        else{
            System.out.println("Senhas não conferem");
        }

    }
    
    public void loginUsuario() throws NoSuchAlgorithmException, Exception{
        
        System.out.println("Entre com o e-mail: ");
        String emailUsuario = sc.nextLine();
        u.getUsuario().setEmail(emailUsuario);
        
        System.out.println("Entre com a senha: ");
        String senha1Usuario = sc.nextLine();
        u.getUsuario().setSenha(senha1Usuario);
        
        if(u.autenticarUsuario(u.getUsuario())){
            System.out.println("Usuario autenticado com sucesso");
            TarefaView tv = new TarefaView();
            tv.menu(u.getUsuario());
        }
        else{
            System.out.println("E-mail ou Senha inválidos");
        }
    }
    
    
    public void listarUsuarios(){
        System.out.println("---3 - Listagem de usuários");

        u.listarUsuarios();
        for(Usuario usuario : u.getUsuarios()){
            imprimeUsuarios(usuario);
        }
    }
    
    public void imprimeUsuarios(Usuario u){
            System.out.println("ID: " + u.getId()+ " - Nome: " + u.getNome()
            + " - E-mail: " + u.getEmail());                 
    }
    private void colocarPontilhado(){
        System.out.println("-------------------------------------------");
    }
    
//    public void listarTarefas(){
//        System.out.println("--- 2 - Listar Tarefas");
//        u.listarTarefas();
//        for(Tarefa tarefa : u.getTarefas()){
//            //imprimeTarefa(tarefa);
//        }
//    }    
    
    
    public String formatarData(Calendar data){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        
        String dataConvertida = new SimpleDateFormat("dd/MM/yyyy").format(data.getTime());
        
        return dataConvertida;
    }
            
    
    public static void main(String[] args) throws Exception {
        UsuarioView uv = new UsuarioView();
        uv.menu();
    }
}

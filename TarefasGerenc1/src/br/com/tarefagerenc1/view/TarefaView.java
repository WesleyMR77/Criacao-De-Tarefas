/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.tarefagerenc1.view;

import br.com.tarefagerenc1.controller.TarefaController;
import br.com.tarefagerenc1.modelo.Tarefa;
import br.com.tarefagerenc1.modelo.Usuario;
import br.com.tarefagerenc1.modelo.exceptions.NonexistentEntityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TarefaView {
    private Usuario usuarioAutenticado; 
    private TarefaController tc;
    
    public TarefaView(){
        tc = new TarefaController();
    }
    
    public void menu(Usuario u) throws Exception{
        int opcao = 0;
        usuarioAutenticado = u;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("---Sistema de Gerenciamento de Tarefas");
            System.out.println("1 - Cadastrar Tarefa");
            System.out.println("2 - Listar Tarefas");
            System.out.println("3 - Alterar Tarefa");
            System.out.println("4 - Excluir Tarefa");
            System.out.println("5 - Finalizar Tarefa");
            System.out.println("6 - Listar Tarefas Finalizadas");
            System.out.println("7 - Listar Tarefas Finalizadas Por Data");
            System.out.println("0 - Sair");
            
            opcao = sc.nextInt();
            
            switch(opcao){
                case 1:
                    cadastrarTarefa(usuarioAutenticado);
                    break;
                case 2:
                    listarTarefas();
                    break;
                case 3:
                    AlterarTarefa();
                    break;
                case 4:
                    excluirTarefa();
                    break;
                case 5:
                    finalizarTarefa();
                    break;
                case 6:
                    listarTarefasFinalizadas();
                    break;
                case 7:
                    listarTarefasFinalizadasPorData();
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
    public void cadastrarTarefa(Usuario u){
        Scanner sc = new Scanner(System.in);
        String descricao, resp;
        System.out.println("---1 - Cadastro de Tarefa");
        System.out.println("Entre com a descricao");
        descricao = sc.nextLine();
        tc.getTarefa().setDescricao(descricao);
        System.out.println("A tarefa esta finalizada?(sim|não)");
        resp = sc.next();
        tc.getTarefa().setFinalizada(verificaResposta(resp));
        if(tc.getTarefa().isFinalizada()){
            tc.getTarefa().setDataFinalizacao(Calendar.getInstance());
        }
        tc.getTarefa().setUsuario(u);
        tc.inserirTarefa();

    }
    
    private Boolean verificaResposta(String resp){
        if(resp.equals("sim"))
            return true;
        else
            return false;
    }
    
    public void AlterarTarefa(){
        try {
            Scanner sc = new Scanner(System.in);
            String descricao, resp;
            tc.getTarefa().setUsuario(usuarioAutenticado);
            System.out.println("--- 3 - Alterar Tarefas");
            System.out.println("Digite o número da tarefa");
            Long id = sc.nextLong();
            
            tc.getTarefa().setId(id);
            
            System.out.println("Entre com a descricao");  
            sc.nextLine();
            descricao = sc.nextLine();  
           
            tc.getTarefa().setDescricao(descricao);
            
            tc.getTarefa().setUsuario(usuarioAutenticado);
            
            tc.alterarTarefa(tc.getTarefa());
            System.out.println("Tarefa Alterada com sucesso");
        } catch (Exception ex) {
            Logger.getLogger(TarefaView.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Não foi encontrado uma tarefa com id selecionado");
        }
    }
    
    public void excluirTarefa(){
        Scanner sc = new Scanner(System.in);
        tc.getTarefa().setUsuario(usuarioAutenticado);
        System.out.println("--- 4 - Excluir Tarefa");
        System.out.println("Digite o número da tarefa");
        Long id = sc.nextLong();
        try {
            tc.excluirTarefa(id);
            System.out.println("Tarefa Removida com sucesso");
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TarefaView.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Não foi encontrado uma tarefa com id selecionado");
        }
    }

    public void finalizarTarefa() throws Exception{
        Scanner sc = new Scanner(System.in);

        System.out.println("--- 5 - Finalização de Tarefa ---");
        System.out.println("Digite o número da tarefa");
        Long id = sc.nextLong();
        tc.getTarefa().setUsuario(usuarioAutenticado);
        if(tc.buscarTarefaPorId(id)){
            tc.finalizarTarefa();
            tc.alterarTarefa(tc.getTarefa());
            System.out.println("Tarefa Alterada com sucesso");
        }else{
            System.out.println("Não foi encontrada tarefa com esse id.");
        }
    }
    
    public void listarTarefas(){
        System.out.println("--- 2 - Listar Tarefas");
        tc.listarTarefas(usuarioAutenticado);
        for(Tarefa tarefa : tc.getTarefas()){
            imprimeTarefa(tarefa);   
        }
    }    
    
    public void listarTarefasFinalizadas(){
        System.out.println("---6 - Listar Tarefas Finalizadas");
        tc.listarTarefasFinalizadas(usuarioAutenticado);
        for (Tarefa tarefa: tc.getTarefas()){
            imprimeTarefa(tarefa);
        }
    }
    
    public void listarTarefasFinalizadasPorData() throws ParseException{
        Scanner sc = new Scanner(System.in);
        System.out.println("---7 - Listar Tarefas Finalizadas Por Data");
        System.out.println("Digite a data no seguinte padrão YYYY-MM-DD");  
        String date = sc.nextLine();

        
        Calendar dataTarefaFinalizadas = Calendar.getInstance(); 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dataTarefaFinalizadas.setTime(sdf.parse(date));
    
        tc.listarTarefasFinalizadasPorData(dataTarefaFinalizadas, usuarioAutenticado);
            
        for (Tarefa tarefa: tc.getTarefas()){
            imprimeTarefa(tarefa);
        }
    }   
    
    
    private void imprimeTarefa(Tarefa tarefa){
            System.out.println("ID: " + tarefa.getId()+ " - Descricao: " + tarefa.getDescricao()
            + " - Status: " + (tarefa.isFinalizada()? "finalizada " : "não finalizada ") 
            + (tarefa.isFinalizada()?
                    formatarData(tarefa.getDataFinalizacao()):
            " - Sem data de finalização "));         
    }
    
   
    public String formatarData(Calendar data){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        
        String dataConvertida = new SimpleDateFormat("dd/MM/yyyy").format(data.getTime());
        
        return dataConvertida;
    }
            
    
//    public static void main(String[] args) throws Exception {;
//        TarefaView tv = new TarefaView();
//        tv.menu();
//    }
}

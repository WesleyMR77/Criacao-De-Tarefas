/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.tarefagerenc1.controller;

import br.com.tarefagerenc1.modelo.Tarefa;
import br.com.tarefagerenc1.modelo.Usuario;
import br.com.tarefagerenc1.modelo.exceptions.NonexistentEntityException;
import br.com.tarefasgerenc1.dao.TarefaDAO;
import br.com.tarefasgerenc1.dao.TarefaJpaController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TarefaController {
    private Tarefa tarefa;
    private List<Tarefa> tarefas;
    private TarefaDAO dao;

    public TarefaController() {
        novaTarefa();
        tarefas = new ArrayList<Tarefa>();
        dao = new TarefaDAO();
        //listarTarefas(tarefa.getUsuario());
    }
    
    public void inserirTarefa(){
        dao.create(tarefa);
        novaTarefa();
    }
    
    private void novaTarefa(){
        tarefa = new Tarefa();
    }

    public Tarefa getTarefa() {
        return tarefa;
    }
    
    public void listarTarefas(Usuario u){
        tarefas = dao.findTarefaEntities(u);
    }
    public void listarTarefasFinalizadas(Usuario u){
        tarefas = dao.findAllFinished(u);
    }
    
    public void listarTarefasFinalizadasPorData(Calendar date, Usuario u){
        tarefas = dao.findAllDateSearch(date, u);
    }
    
    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void alterarTarefa(Tarefa tarefa) throws Exception{
        dao.edit(tarefa);
        listarTarefas(tarefa.getUsuario());
    }
    
    public void excluirTarefa(Long id) throws NonexistentEntityException{
        dao.destroy(id);
        listarTarefas(tarefa.getUsuario());
    }
    
    public boolean buscarTarefaPorId(Long id){
        novaTarefa();
        this.tarefa = dao.findTarefa(id);
        if(tarefa.getId() == id){
            return true;
        }
        else{ 
            return false;
        }
    }
    
    public void finalizarTarefa() throws Exception{
        this.tarefa.setFinalizada(true);
        this.tarefa.setDataFinalizacao(Calendar.getInstance());
        dao.edit(tarefa);
        listarTarefas(tarefa.getUsuario());
    }
}

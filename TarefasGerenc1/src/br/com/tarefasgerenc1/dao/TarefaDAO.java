/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tarefasgerenc1.dao;

import br.com.tarefagerenc1.modelo.Tarefa;
import br.com.tarefagerenc1.modelo.Usuario;
import br.com.tarefagerenc1.modelo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author renat
 */
public class TarefaDAO implements Serializable {

    public TarefaDAO() {
        this.emf = Persistence.createEntityManagerFactory("TarefasGerenc1PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarefa tarefa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tarefa tarefa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tarefa = em.merge(tarefa);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = tarefa.getId();
                if (findTarefa(id) == null) {
                    throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tarefa tarefa;
            try {
                tarefa = em.getReference(Tarefa.class, id);
                tarefa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarefa with id " + id + " no longer exists.", enfe);
            }
            em.remove(tarefa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tarefa> findTarefaEntities(Usuario u) {
        List<Tarefa> tarefas = null;
        EntityManager em = getEntityManager();
        try{
            tarefas = em.createNamedQuery("Tarefa.findAll").setParameter("usuario", u)
                .getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tarefas;
    }

    public List<Tarefa> findTarefaEntities(int maxResults, int firstResult) {
        return findTarefaEntities(false, maxResults, firstResult);
    }

    private List<Tarefa> findTarefaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarefa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tarefa findTarefa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarefa.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarefaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarefa> rt = cq.from(Tarefa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Tarefa> findAllFinished(Usuario u){
        List<Tarefa> tarefasFinalizadas = null;
        EntityManager em = getEntityManager();
        try{
            tarefasFinalizadas = em.createNamedQuery("Tarefa.findAllFinished").setParameter("usuario", u)
                .getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tarefasFinalizadas;
    }

    public List<Tarefa> findAllDateSearch(Calendar date, Usuario u){
        List<Tarefa> tarefasFinalizadas = null;
        EntityManager em = getEntityManager();
        try{
            tarefasFinalizadas = em.createNamedQuery("Tarefa.findAllDateSearch").setParameter("date", date).setParameter("usuario", u)
                .getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return tarefasFinalizadas;
    }    
}

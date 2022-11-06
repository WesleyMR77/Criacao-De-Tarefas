/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.tarefasgerenc1.dao;

import br.com.tarefagerenc1.modelo.Tarefa;
import br.com.tarefagerenc1.modelo.Usuario;
import br.com.tarefagerenc1.modelo.exceptions.IllegalOrphanException;
import br.com.tarefagerenc1.modelo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author wesle
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("TarefasGerenc1PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getTarefas() == null) {
            usuario.setTarefas(new ArrayList<Tarefa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tarefa> attachedTarefas = new ArrayList<Tarefa>();
            for (Tarefa tarefasTarefaToAttach : usuario.getTarefas()) {
                tarefasTarefaToAttach = em.getReference(tarefasTarefaToAttach.getClass(), tarefasTarefaToAttach.getId());
                attachedTarefas.add(tarefasTarefaToAttach);
            }
            usuario.setTarefas(attachedTarefas);
            em.persist(usuario);
            for (Tarefa tarefasTarefa : usuario.getTarefas()) {
                Usuario oldUsuarioOfTarefasTarefa = tarefasTarefa.getUsuario();
                tarefasTarefa.setUsuario(usuario);
                tarefasTarefa = em.merge(tarefasTarefa);
                if (oldUsuarioOfTarefasTarefa != null) {
                    oldUsuarioOfTarefasTarefa.getTarefas().remove(tarefasTarefa);
                    oldUsuarioOfTarefasTarefa = em.merge(oldUsuarioOfTarefasTarefa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Tarefa> tarefasOld = persistentUsuario.getTarefas();
            List<Tarefa> tarefasNew = usuario.getTarefas();
            List<String> illegalOrphanMessages = null;
            for (Tarefa tarefasOldTarefa : tarefasOld) {
                if (!tarefasNew.contains(tarefasOldTarefa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarefa " + tarefasOldTarefa + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Tarefa> attachedTarefasNew = new ArrayList<Tarefa>();
            for (Tarefa tarefasNewTarefaToAttach : tarefasNew) {
                tarefasNewTarefaToAttach = em.getReference(tarefasNewTarefaToAttach.getClass(), tarefasNewTarefaToAttach.getId());
                attachedTarefasNew.add(tarefasNewTarefaToAttach);
            }
            tarefasNew = attachedTarefasNew;
            usuario.setTarefas(tarefasNew);
            usuario = em.merge(usuario);
            for (Tarefa tarefasNewTarefa : tarefasNew) {
                if (!tarefasOld.contains(tarefasNewTarefa)) {
                    Usuario oldUsuarioOfTarefasNewTarefa = tarefasNewTarefa.getUsuario();
                    tarefasNewTarefa.setUsuario(usuario);
                    tarefasNewTarefa = em.merge(tarefasNewTarefa);
                    if (oldUsuarioOfTarefasNewTarefa != null && !oldUsuarioOfTarefasNewTarefa.equals(usuario)) {
                        oldUsuarioOfTarefasNewTarefa.getTarefas().remove(tarefasNewTarefa);
                        oldUsuarioOfTarefasNewTarefa = em.merge(oldUsuarioOfTarefasNewTarefa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tarefa> tarefasOrphanCheck = usuario.getTarefas();
            for (Tarefa tarefasOrphanCheckTarefa : tarefasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Tarefa " + tarefasOrphanCheckTarefa + " in its tarefas field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

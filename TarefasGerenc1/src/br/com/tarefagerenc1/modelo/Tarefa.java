/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tarefagerenc1.modelo;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author renat
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Tarefa.findAll", query = "SELECT t FROM Tarefa t WHERE t.usuario = :usuario"),
    @NamedQuery(name = "Tarefa.findAllFinished", query = "SELECT t FROM Tarefa t WHERE t.finalizada = true and t.usuario = :usuario"),
    @NamedQuery(name = "Tarefa.findAllDateSearch", query = "SELECT t FROM Tarefa t WHERE t.finalizada = true and t.dataFinalizacao = :date and t.usuario = :usuario"),
})
public class Tarefa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false)
    private String descricao;
    private boolean finalizada;
    
    @Temporal(TemporalType.DATE)
    private Calendar dataFinalizacao;
    
    @ManyToOne
    @JoinColumn(name = "usuario", nullable = false)
    //@ForeignKey(name = "usuario_fk")
    private Usuario usuario = new Usuario();
    
    public Tarefa() {
    }

    public Tarefa(Long id, String descricao, boolean finalizada, Calendar dataFinalizacao, Usuario usuario) {
        this.id = id;
        this.descricao = descricao;
        this.finalizada = finalizada;
        this.dataFinalizacao = dataFinalizacao;
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario u) {
        this.usuario = u;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public Calendar getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(Calendar dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tarefa)) {
            return false;
        }
        Tarefa other = (Tarefa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.tarefagerenc1.modelo.Tarefa[ id=" + id + " ]";
    }
    
}

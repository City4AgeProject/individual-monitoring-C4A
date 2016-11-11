/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author misha
 */
@Entity
@Table(name = "relatedkws")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Relatedkws.findAll", query = "SELECT r FROM Relatedkws r"),
    @NamedQuery(name = "Relatedkws.findById", query = "SELECT r FROM Relatedkws r WHERE r.id = :id"),
    @NamedQuery(name = "Relatedkws.findByKeyword", query = "SELECT r FROM Relatedkws r WHERE r.keyword = :keyword"),
    @NamedQuery(name = "Relatedkws.findByUserId", query = "SELECT r FROM Relatedkws r WHERE r.userId = :userId")})
public class Relatedkws implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "keyword")
    private String keyword;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 16777215)
    @Column(name = "related_kws")
    private String relatedKws;

    public Relatedkws() {
    }

    public Relatedkws(Integer id) {
        this.id = id;
    }

    public Relatedkws(Integer id, String keyword, int userId, String relatedKws) {
        this.id = id;
        this.keyword = keyword;
        this.userId = userId;
        this.relatedKws = relatedKws;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRelatedKws() {
        return relatedKws;
    }

    public void setRelatedKws(String relatedKws) {
        this.relatedKws = relatedKws;
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
        if (!(object instanceof Relatedkws)) {
            return false;
        }
        Relatedkws other = (Relatedkws) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.city4age.dashboard.api.v1.Relatedkws[ id=" + id + " ]";
    }
    
}

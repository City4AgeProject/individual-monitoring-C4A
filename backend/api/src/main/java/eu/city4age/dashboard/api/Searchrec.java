/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author misha
 */
@Entity
@Table(name = "searchrec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Searchrec.findAll", query = "SELECT s FROM Searchrec s"),
    @NamedQuery(name = "Searchrec.findById", query = "SELECT s FROM Searchrec s WHERE s.id = :id"),
    @NamedQuery(name = "Searchrec.findByKeyword", query = "SELECT s FROM Searchrec s WHERE s.keyword = :keyword"),
    @NamedQuery(name = "Searchrec.findByProvider", query = "SELECT s FROM Searchrec s WHERE s.provider = :provider"),
    @NamedQuery(name = "Searchrec.findByLanguage", query = "SELECT s FROM Searchrec s WHERE s.language = :language"),
    @NamedQuery(name = "Searchrec.findByCountry", query = "SELECT s FROM Searchrec s WHERE s.country = :country"),
    @NamedQuery(name = "Searchrec.findByDate", query = "SELECT s FROM Searchrec s WHERE s.date = :date")})
public class Searchrec implements Serializable {

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
    @Size(min = 1, max = 50)
    @Column(name = "provider")
    private String provider;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "language")
    private String language;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "country")
    private String country;
    @Lob
    @Size(max = 16777215)
    @Column(name = "data")
    private String data;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Searchrec() {
    }

    public Searchrec(Integer id) {
        this.id = id;
    }

    public Searchrec(Integer id, String keyword, String provider, String language, String country, Date date) {
        this.id = id;
        this.keyword = keyword;
        this.provider = provider;
        this.language = language;
        this.country = country;
        this.date = date;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        if (!(object instanceof Searchrec)) {
            return false;
        }
        Searchrec other = (Searchrec) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.city4age.dashboard.api.v1.Searchrec[ id=" + id + " ]";
    }
    
}

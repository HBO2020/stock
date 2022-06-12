package com.enedis.habilitation.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Product sold by the Online store
 */
@Schema(description = "Product sold by the Online store")
@Entity
@Table(name = "habilitation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Habilitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "compte", nullable = false)
    private String compte;

    @Column(name = "entreprise")
    private Integer entreprise;

    @Column(name = "date_maj")
    private Instant dateMaj;

    @ManyToOne(optional = false)
    @NotNull
    private Fonction fonction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Habilitation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompte() {
        return this.compte;
    }

    public Habilitation compte(String compte) {
        this.setCompte(compte);
        return this;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public Integer getEntreprise() {
        return this.entreprise;
    }

    public Habilitation entreprise(Integer entreprise) {
        this.setEntreprise(entreprise);
        return this;
    }

    public void setEntreprise(Integer entreprise) {
        this.entreprise = entreprise;
    }

    public Instant getDateMaj() {
        return this.dateMaj;
    }

    public Habilitation dateMaj(Instant dateMaj) {
        this.setDateMaj(dateMaj);
        return this;
    }

    public void setDateMaj(Instant dateMaj) {
        this.dateMaj = dateMaj;
    }

    public Fonction getFonction() {
        return this.fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public Habilitation fonction(Fonction fonction) {
        this.setFonction(fonction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Habilitation)) {
            return false;
        }
        return id != null && id.equals(((Habilitation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Habilitation{" +
            "id=" + getId() +
            ", compte='" + getCompte() + "'" +
            ", entreprise=" + getEntreprise() +
            ", dateMaj='" + getDateMaj() + "'" +
            "}";
    }
}

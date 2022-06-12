package com.enedis.habilitation.domain;

import com.enedis.habilitation.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rattachement.
 */
@Entity
@Table(name = "rattachement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rattachement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "id_demande", nullable = false)
    private String idDemande;

    @NotNull
    @Column(name = "compte", nullable = false)
    private String compte;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "description_role")
    private String descriptionRole;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Column(name = "date_maj")
    private Instant dateMaj;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rattachement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdDemande() {
        return this.idDemande;
    }

    public Rattachement idDemande(String idDemande) {
        this.setIdDemande(idDemande);
        return this;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public String getCompte() {
        return this.compte;
    }

    public Rattachement compte(String compte) {
        this.setCompte(compte);
        return this;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public Status getStatus() {
        return this.status;
    }

    public Rattachement status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescriptionRole() {
        return this.descriptionRole;
    }

    public Rattachement descriptionRole(String descriptionRole) {
        this.setDescriptionRole(descriptionRole);
        return this;
    }

    public void setDescriptionRole(String descriptionRole) {
        this.descriptionRole = descriptionRole;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Rattachement dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateMaj() {
        return this.dateMaj;
    }

    public Rattachement dateMaj(Instant dateMaj) {
        this.setDateMaj(dateMaj);
        return this;
    }

    public void setDateMaj(Instant dateMaj) {
        this.dateMaj = dateMaj;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rattachement)) {
            return false;
        }
        return id != null && id.equals(((Rattachement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rattachement{" +
            "id=" + getId() +
            ", idDemande='" + getIdDemande() + "'" +
            ", compte='" + getCompte() + "'" +
            ", status='" + getStatus() + "'" +
            ", descriptionRole='" + getDescriptionRole() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateMaj='" + getDateMaj() + "'" +
            "}";
    }
}

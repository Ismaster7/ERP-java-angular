package br.com.desafio.tecnico.desafio.domain.entity.enterprise;

import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.InvalidDocumentExceptionException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "enterprise")
public class Enterprise implements Serializable {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enterprise_id")
    private Long enterpriseId;
    @Embedded
    private Cnpj cnpj;

    @Column(name = "trade_name", unique = true, nullable = false)
    private String tradeName;
    @Embedded
    private Cep cep;
    private String state;
    @ManyToMany(mappedBy = "enterprises")
    @JsonIgnoreProperties("enterprises")
    private Set<Supplier> suppliers = new HashSet<>();

    public Enterprise() {
    }

    public Enterprise(Cnpj cnpj, String tradeName, Cep cep, String state) {
        if (!cnpj.isValid()) {
            throw new InvalidDocumentExceptionException("CNPJ inválido");
        }
        this.cnpj = cnpj;
        this.tradeName = tradeName;
        this.cep = cep;
        this.state = state;
    }

    public void addSupplier(Supplier supplier) {
        this.suppliers.add(supplier);
        supplier.getEnterprises().add(this);
    }

    public void removeSupplier(Supplier supplier) {
        this.suppliers.remove(supplier);
        supplier.getEnterprises().remove(this);
    }
    public void clearSuppliers() {
        for (Supplier supplier : new HashSet<>(this.suppliers)) {
            this.removeSupplier(supplier);
        }
    }
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public void setCnpj(Cnpj cnpj) {
        this.cnpj = cnpj;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enterprise that = (Enterprise) o;
        return Objects.equals(enterpriseId, that.enterpriseId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(enterpriseId);
    }
}

package br.com.desafio.tecnico.desafio.domain.entity.supplier;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "fornecedor")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;
    // CPF ou CNPJ
    @Column(unique = true, nullable = false)
    private Document document;
    @Column(nullable = false)
    private String name;
    private String email;
    @Embedded
    private Cep cep;
    @Enumerated(EnumType.ORDINAL)
    private SupplierType type;
    private String rg;
    @Column(name = "birthday_date")
    private LocalDate birthDate;
    @ManyToMany
    @JoinTable(
            name = "enterprise_supplier",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "enterprise_id")
    )
    @JsonIgnoreProperties("suppliers")
    private Set<Enterprise> enterprises = new HashSet<>();
    public Supplier(){
    }
    public Supplier(Document document, String name, String email, Cep cep, String rg, LocalDate birthDate) {
        if (document.isCpf() && (rg == null || birthDate == null)) {
            throw new IllegalArgumentException("Pessoa física precisa de RG e Data de Nascimento");
        }
        this.document = document;
        this.name = name;
        this.email = email;
        this.cep = cep;
        this.rg = rg;
        this.birthDate = birthDate;
    }

    public void addEnterprise(Enterprise enterprise) {
        this.enterprises.add(enterprise);
        enterprise.getSuppliers().add(this);
    }

    public void removeEnterprise(Enterprise enterprise) {
        this.enterprises.remove(enterprise);
        enterprise.getSuppliers().remove(this);
    }



    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public SupplierType getType() {
        return type;
    }

    public void setType(SupplierType type) {
        this.type = type;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Enterprise> getEnterprises() {
        return enterprises;
    }

    public void setEnterprises(Set<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Supplier supplier = (Supplier) object;
        return Objects.equals(document, supplier.document) && Objects.equals(cep, supplier.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, cep);
    }
}

package br.com.desafio.tecnico.desafio.domain.entity;

import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
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

    @Column(unique = true)
    private Document cnpj;

    @Column(name = "trade_name", unique = true)
    private String tradeName;

    private Cep cep;
    @ManyToMany(mappedBy = "enterprises")
    private Set<Supplier> suppliers = new HashSet<>();

    public Enterprise() {
    }

    public Enterprise(Document cnpj, String tradeName, Cep cep) {
        if (!cnpj.isCnpj()) {
            throw new IllegalArgumentException("CNPJ inválido");
            /* criei este validador no construtor para lógicas de negócio interno.
            para validação da chegada da requisição, usei o @Validation do Spring mesmo.
             */
        }
        this.cnpj = cnpj;
        this.tradeName = tradeName;
        this.cep = cep;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Document getCnpj() {
        return cnpj;
    }

    public void setCnpj(Document cnpj) {
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


}

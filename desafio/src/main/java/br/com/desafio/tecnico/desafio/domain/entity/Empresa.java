package br.com.desafio.tecnico.desafio.domain.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empresa_id")
    private Long empresaId;

    @Column(unique = true)
    private String cnpj;

    @Column(name = "nome_fantasia", unique = true)
    private String nomeFantasia;

    private String cep;
    @ManyToMany(mappedBy = "empresas")
    private Set<Fornecedor> fornecedores = new HashSet<>();

    public Empresa() {
    }

    public Empresa(Long empresaId, String cnpj, String nomeFantasia, String cep) {
        this.empresaId = empresaId;
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.cep = cep;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(empresaId, empresa.empresaId) && Objects.equals(cnpj, empresa.cnpj) && Objects.equals(nomeFantasia, empresa.nomeFantasia) && Objects.equals(cep, empresa.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresaId, cnpj, nomeFantasia, cep);
    }
}

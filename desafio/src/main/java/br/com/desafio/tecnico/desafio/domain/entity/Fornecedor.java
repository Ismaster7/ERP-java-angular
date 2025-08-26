package br.com.desafio.tecnico.desafio.domain.entity;

import br.com.desafio.tecnico.desafio.domain.enums.Tipo;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fornecedor_id")
    private Long fornecedorId;
    // CPF ou CNPJ
    @Column(unique = true)
    private String documento;

    private String nome;
    private String email;
    private String cep;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private String rg;
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ManyToMany
    @JoinTable(
            name = "empresa_fornecedor",
            joinColumns = @JoinColumn(name = "fornecedor_id"),
            inverseJoinColumns = @JoinColumn(name = "empresa_id")
    )
    private Set<Empresa> empresas = new HashSet<>();

    public Fornecedor() {
    }

    public Fornecedor(Long fornecedorId, String documento, String nome, String email, String cep, Tipo tipo, String rg, LocalDate dataNascimento, Set<Empresa> empresas) {
        this.fornecedorId = fornecedorId;
        this.documento = documento;
        this.nome = nome;
        this.email = email;
        this.cep = cep;
        this.tipo = tipo;
        this.rg = rg;
        this.dataNascimento = dataNascimento;
        this.empresas = empresas;
    }

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Set<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Set<Empresa> empresas) {
        this.empresas = empresas;
    }
}

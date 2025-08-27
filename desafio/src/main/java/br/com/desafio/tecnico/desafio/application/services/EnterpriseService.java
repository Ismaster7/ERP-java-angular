package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.domain.entity.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.Supplier;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFound;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnterpriseService {
    private final CepService cepService;
    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository, CepService cepService){
        this.enterpriseRepository = enterpriseRepository;
        this.cepService = cepService;
    }

    public List<Enterprise> getAllEnterprise(){
        return enterpriseRepository.findAll();
    }

    public Enterprise getEnterpriseById(Long id){
        return enterpriseRepository.findById(id)
                .orElseThrow(EnterpriseNotFound::new);
    }

    public Enterprise saveEnterprise(Enterprise enterprise){
        // regra 1: CNPJ deve ser único
        boolean exists = enterpriseRepository.existsByCnpjValue(enterprise.getCnpj().getDocument());
        if (exists) {
            throw new IllegalArgumentException("Enterprise with this CNPJ already exists");
        }

        return enterpriseRepository.save(enterprise);
    }

    public Enterprise updateEnterprise(Long id, Enterprise newEnterprise) {
        var oldEnterprise = getEnterpriseById(id);

        if (newEnterprise.getCep() != null && !newEnterprise.getCep().equals(oldEnterprise.getCep())) {
            oldEnterprise.setCep(newEnterprise.getCep());
        }

        if (newEnterprise.getTradeName() != null && !newEnterprise.getTradeName().equals(oldEnterprise.getTradeName())) {
            oldEnterprise.setTradeName(newEnterprise.getTradeName());
        }

        if (newEnterprise.getCnpj() != null && !newEnterprise.getCnpj().equals(oldEnterprise.getCnpj())) {
            oldEnterprise.setCnpj(newEnterprise.getCnpj());
        }

        if (newEnterprise.getSuppliers() != null && !newEnterprise.getSuppliers().equals(oldEnterprise.getSuppliers())) {
            oldEnterprise.setSuppliers(newEnterprise.getSuppliers());
        }

        return enterpriseRepository.save(oldEnterprise);
    }


    public void removeEnterprise(Long id){
        enterpriseRepository.deleteById(id);
    }

    /**
     * Regras de negócio de vinculação de fornecedor
     */
    public void addSupplierToEnterprise(Long enterpriseId, Supplier supplier) {
        Enterprise enterprise = getEnterpriseById(enterpriseId);
        cepService.consultCep(enterprise.getCep().getValue());
        // regra 3: Empresa do PR não pode ter fornecedor PF menor de idade
        if (cepService.isFromSpecificState(new String[] ,enterprise.getCep().getValue()) && supplier.getDocument().isCpf()) {
            if (supplier.getBirthDate() == null || supplier.getBirthDate().plusYears(18).isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Fornecedor menor de idade não pode ser adicionado com Cep do Paraná");
            }
        }

        enterprise.addSupplier(supplier);
        enterpriseRepository.save(enterprise);
    }


}


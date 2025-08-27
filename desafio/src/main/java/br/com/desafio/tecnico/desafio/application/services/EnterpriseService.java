package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.domain.entity.Enterprise;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFound;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnterpriseService {
    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository){
        this.enterpriseRepository = enterpriseRepository;
    }

    public List<Enterprise> getAllEnterprise(){
        return enterpriseRepository.findAll();
    }

    public Enterprise getEnterpriseById(Long id){
        return enterpriseRepository.findById(id)
                .orElseThrow(EnterpriseNotFound::new);
    }

    public void saveEnterprise(Enterprise enterprise){
        enterpriseRepository.save(enterprise);
    }

    public void updateEnterprise(Enterprise newEnterprise){
        var oldEnterprise = getEnterpriseById(newEnterprise.getEnterpriseId());
        if(newEnterprise.getCep() != null && oldEnterprise.getCep() != newEnterprise.getCep())
            oldEnterprise.setCep(newEnterprise.getCep());
        if(newEnterprise.getTradeName() != null && oldEnterprise.getTradeName() != newEnterprise.getTradeName())
            oldEnterprise.setTradeName(newEnterprise.getTradeName());
        if(newEnterprise.getSuppliers() != null && oldEnterprise.getSuppliers() != newEnterprise.getSuppliers())
            oldEnterprise.setSuppliers(newEnterprise.getSuppliers());
        if(newEnterprise.getCnpj() != null && oldEnterprise.getCnpj() != newEnterprise.getCnpj())
            oldEnterprise.setCnpj(newEnterprise.getCnpj());
        enterpriseRepository.save(oldEnterprise);
    }

    public void removeEnterprise(Long id){
        enterpriseRepository.deleteById(id);
    }
}

package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.domain.entity.Supplier;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFound;
import br.com.desafio.tecnico.desafio.infraestructure.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllEnterprise(){
        return supplierRepository.findAll();
    }

    public Supplier getEnterpriseById(Long id){
        return supplierRepository.findById(id)
                .orElseThrow(EnterpriseNotFound::new);
    }

    public void saveEnterprise(Supplier supplier){

        supplierRepository.save(supplier);
    }

    public void updateEnterprise(Supplier newSupplier){
        var oldSupplier = getEnterpriseById(newSupplier.getSupplierId());
        if(newSupplier.getCep() != null && newSupplier.getCep() != newSupplier.getCep())
            oldSupplier.setCep(newSupplier.getCep());
        supplierRepository.save(oldSupplier);
    }

    public void removeEnterprise(Long id){
        supplierRepository.deleteById(id);
    }
}

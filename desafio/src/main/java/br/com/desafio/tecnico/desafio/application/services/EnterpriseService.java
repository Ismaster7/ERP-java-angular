package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.mapper.EnterpriseMapper;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.EnterpriseRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFound;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import br.com.desafio.tecnico.desafio.infraestructure.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EnterpriseService {
    private final CepService cepService;
    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;
    private final SupplierRepository supplierRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository, CepService cepService, EnterpriseMapper enterpriseMapper, SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
        this.enterpriseMapper = enterpriseMapper;
        this.enterpriseRepository = enterpriseRepository;
        this.cepService = cepService;
    }

    public List<Enterprise> getAllEnterprise() {
        return enterpriseRepository.findAll();
    }

    public Enterprise getEnterpriseById(Long id) {
        return enterpriseRepository.findById(id)
                .orElseThrow(EnterpriseNotFound::new);
    }

    @Transactional
    public Enterprise saveEnterprise(EnterpriseRequestDto enterpriseRequestDto) {
        var cnpj = new Cnpj(enterpriseRequestDto.cnpj());
        boolean exists = enterpriseRepository.existsByCnpj(cnpj);
        if (exists) {
            throw new IllegalArgumentException("Enterprise with this CNPJ already exists");
        }
        String state = cepService.consultCep(enterpriseRequestDto.cep()).cep();

        Enterprise enterprise = enterpriseMapper.toEntity(enterpriseRequestDto);

        if (enterpriseRequestDto.suppliers() != null) {
            Set<Supplier> suppliers = validateAndFetchSuppliersForEnterprise(enterpriseRequestDto.suppliers(), enterprise, state);
            enterprise.setSuppliers(suppliers);
        }
        // regra 1: CNPJ deve ser único


        return enterpriseRepository.save(enterprise);
    }

    @Transactional
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

    @Transactional
    public void removeEnterprise(Long id) {
        enterpriseRepository.deleteById(id);
    }

    /**
     * Regras de negócio de vinculação de fornecedor
     */
    public Set<Supplier> validateAndFetchSuppliersForEnterprise(Set<Long> suppliersId, Enterprise enterprise, String state) {
        boolean isFromParana = cepService.isCepFromSpecificStates(new String[]{"parana", "paraná", "pr"}, state);

        Set<Supplier> suppliers = (Set<Supplier>) supplierRepository.findAllById(suppliersId);

        if (isFromParana) {
            LocalDate actualDate = LocalDate.now();
            for (Supplier supplier : suppliers) {
                if (supplier.getType().equals(SupplierType.fromInt(1)) &&
                        supplier.getBirthDate() != null &&
                        supplier.getBirthDate().plusYears(18).isAfter(actualDate)) {

                    throw new IllegalArgumentException(
                            "Não é permitido cadastrar fornecedor pessoa física menor de idade para empresas do " + state
                    );
                }
            }
        }

        return suppliers;
    }
}

package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.mapper.EnterpriseMapper;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestCreateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFoundException;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseRuleException;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import br.com.desafio.tecnico.desafio.infraestructure.repository.SupplierRepository;
import br.com.desafio.tecnico.desafio.presentation.serializer.view.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class EnterpriseService {
    private final CepService cepService;
    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;
    private final SupplierRepository supplierRepository;
    private final Set<String>
            prohibitedStatesForUnderageSuppliers = Set.of("parana", "paraná", "pr");

    public EnterpriseService(EnterpriseRepository enterpriseRepository, CepService cepService, EnterpriseMapper enterpriseMapper, SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
        this.enterpriseMapper = enterpriseMapper;
        this.enterpriseRepository = enterpriseRepository;
        this.cepService = cepService;
    }
    @JsonView(JsonViews.Default.class)
    public Set<EnterpriseResponseDto> getAllEnterprise() {
        Set<Enterprise> enterprises = enterpriseRepository.findAll()
                .stream()
                .collect(Collectors.toSet());

        return enterpriseMapper.toDto(enterprises);
    }

    public EnterpriseResponseDto getEnterpriseById(Long id) {
        return enterpriseMapper.toDto(
                enterpriseRepository.findById(id)
                        .orElseThrow(EnterpriseNotFoundException::new)
        );
    }

    @Transactional
    public EnterpriseResponseDto saveEnterprise(EnterpriseRequestCreateDto enterpriseRequestCreateDto) {
        validateExistsDocument(enterpriseRequestCreateDto.cnpj());

        Enterprise enterprise = enterpriseMapper.toEntity(enterpriseRequestCreateDto);
        var state = cepService.consultCep(enterpriseRequestCreateDto.cep());


        if (enterpriseRequestCreateDto.suppliers() != null && !enterpriseRequestCreateDto.suppliers().isEmpty()) {
            Set<Supplier> suppliers = StreamSupport
                    .stream(supplierRepository.findAllById(enterpriseRequestCreateDto.suppliers()).spliterator(), false)
                    .collect(Collectors.toSet());

            boolean result = cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, state.cep());
            if(result){
                validateAndFetchSuppliersForEnterprise(suppliers, state.cep());
            }

            for (Supplier supplier : suppliers) {
                supplier.getEnterprises().add(enterprise); // atualiza lado dono
            }
            enterprise.setSuppliers(suppliers); // atualiza lado inverso
        }

        Enterprise saved = enterpriseRepository.save(enterprise);
        return enterpriseMapper.toDto(saved);
    }

    @Transactional
    public EnterpriseResponseDto updateEnterprise(EnterpriseRequestUpdateDto enterpriseRequestUpdateDto) {
        // busca a empresa antiga
        var oldEnterprise = enterpriseRepository.findById(enterpriseRequestUpdateDto.enterpriseId())
                .orElseThrow(EnterpriseNotFoundException::new);

        // Consulta e valida CEP
        var state = cepService.consultCep(enterpriseRequestUpdateDto.cep());


        // Converte request para entidade temporária
        var newEnterprise = enterpriseMapper.toEntity(enterpriseRequestUpdateDto);

        // Atualiza CEP, tradeName e CNPJ
        if (newEnterprise.getCep() != null && !newEnterprise.getCep().equals(oldEnterprise.getCep())) {
            oldEnterprise.setCep(newEnterprise.getCep());
        }
        if (newEnterprise.getTradeName() != null && !newEnterprise.getTradeName().equals(oldEnterprise.getTradeName())) {
            oldEnterprise.setTradeName(newEnterprise.getTradeName());
        }
        if (newEnterprise.getCnpj() != null && !newEnterprise.getCnpj().equals(oldEnterprise.getCnpj())) {
            oldEnterprise.setCnpj(newEnterprise.getCnpj());
        }

        if (enterpriseRequestUpdateDto.suppliers() != null) {

            Set<Supplier> newSuppliers = StreamSupport
                    .stream(supplierRepository.findAllById(enterpriseRequestUpdateDto.suppliers()).spliterator(), false)
                    .collect(Collectors.toSet());

            boolean result = cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, state.cep());
            if (result) {
                validateAndFetchSuppliersForEnterprise(newSuppliers, state.cep());
            }

            oldEnterprise.getSuppliers().stream()
                    .filter(s -> !newSuppliers.contains(s))
                    .forEach(oldEnterprise::removeSupplier);

            // Adiciona novos fornecedores
            newSuppliers.stream()
                    .filter(s -> !oldEnterprise.getSuppliers().contains(s))
                    .forEach(oldEnterprise::addSupplier);
        }

        return enterpriseMapper.toDto(enterpriseRepository.save(oldEnterprise));
    }



    public void validateAndFetchSuppliersForEnterprise(Set<Supplier> suppliers, String state) {
            LocalDate actualDate = LocalDate.now();
            for (Supplier supplier : suppliers) {
                if (supplier.getType().equals(SupplierType.fromInt(0)) &&
                        supplier.getBirthDate() != null &&
                        supplier.getBirthDate().plusYears(18).isAfter(actualDate)) {

                    throw new EnterpriseRuleException();
                }
            }


    }

    public void validateExistsDocument(String cnpjStr){
        var cnpj = new Cnpj(cnpjStr);
        boolean exists = enterpriseRepository.existsByCnpj(cnpj);
        if (exists) {
            throw new IllegalArgumentException("Enterprise with this CNPJ already exists");
        }
    }


    public void deleteEnterprise(Long id) {
        if(!enterpriseRepository.existsById(id)){
            throw new EnterpriseNotFoundException();
        }
        enterpriseRepository.deleteById(id);
    }
}

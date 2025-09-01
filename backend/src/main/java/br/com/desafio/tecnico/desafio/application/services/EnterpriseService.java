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
import br.com.desafio.tecnico.desafio.infraestructure.repository.specifications.EnterpriseSpecifications;
import br.com.desafio.tecnico.desafio.presentation.serializer.view.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

        validateExistsDocument(enterpriseRequestCreateDto.cnpj(), enterpriseRequestCreateDto.tradeName());

        Enterprise enterprise = enterpriseMapper.toEntity(enterpriseRequestCreateDto);
        var state = cepService.consultCep(enterpriseRequestCreateDto.cep());
        enterprise.setState(state.estado());
        if (enterpriseRequestCreateDto.suppliers() != null && !enterpriseRequestCreateDto.suppliers().isEmpty()) {
            Set<Supplier> suppliers = StreamSupport
                    .stream(supplierRepository.findAllById(enterpriseRequestCreateDto.suppliers()).spliterator(), false)
                    .collect(Collectors.toSet());

            boolean result = cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, enterprise.getState());
            if(result){
                validateAndFetchSuppliersForEnterprise(suppliers);
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
        var oldEnterprise = enterpriseRepository.findById(enterpriseRequestUpdateDto.enterpriseId())
                .orElseThrow(EnterpriseNotFoundException::new);


        String state = "";
        if(enterpriseRequestUpdateDto.cep() != null){
            state = cepService.consultCep(enterpriseRequestUpdateDto.cep()).estado();
        }

        var newEnterprise = enterpriseMapper.toEntity(enterpriseRequestUpdateDto);

        if (newEnterprise.getCep() != null && !newEnterprise.getCep().equals(oldEnterprise.getCep())) {
            if(state.isEmpty()) throw new RuntimeException("Houve um problema com a requisição de cep");
            oldEnterprise.setCep(newEnterprise.getCep());
            oldEnterprise.setState(state);
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

            boolean result = cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, state);
            if (result) {
                validateAndFetchSuppliersForEnterprise(newSuppliers);
            }

            Set<Supplier> suppliersToRemove = new HashSet<>(oldEnterprise.getSuppliers());
            suppliersToRemove.removeAll(newSuppliers);
            for (Supplier supplier : suppliersToRemove) {
                oldEnterprise.removeSupplier(supplier);
            }

            Set<Supplier> suppliersToAdd = new HashSet<>(newSuppliers);
            suppliersToAdd.removeAll(oldEnterprise.getSuppliers());
            for (Supplier supplier : suppliersToAdd) {
                oldEnterprise.addSupplier(supplier);
            }
        }

        return enterpriseMapper.toDto(enterpriseRepository.save(oldEnterprise));
    }




    public void validateAndFetchSuppliersForEnterprise(Set<Supplier> suppliers) {
            LocalDate actualDate = LocalDate.now();
            for (Supplier supplier : suppliers) {
                if (supplier.getType().equals(SupplierType.FISICA) &&
                        supplier.getBirthDate() != null &&
                        supplier.getBirthDate().plusYears(18).isAfter(actualDate)) {

                    throw new EnterpriseRuleException();
                }
            }


    }

    public void validateExistsDocument(String cnpjStr, String tradeName){
        var cnpj = new Cnpj(cnpjStr);
        boolean exists = enterpriseRepository.existsByCnpj(cnpj);
        if (exists) {
            throw new IllegalArgumentException("CNPJ já cadastrado!");
        }
        boolean exists2 = enterpriseRepository.existsByTradeName(tradeName);
        if (exists2) {
            throw new IllegalArgumentException("Nome Fantasia já cadastrado!");
        }

    }

    @Transactional
    public void deleteEnterprise(Long id) {
        if(!enterpriseRepository.existsById(id)){
            throw new EnterpriseNotFoundException();

        }
        Enterprise enterprise = enterpriseRepository.findById(id)
                .orElseThrow(EnterpriseNotFoundException::new);
        enterprise.clearSuppliers(); // Limpa todas as relações
        enterpriseRepository.delete(enterprise);
    }

    public Set<EnterpriseResponseDto> searchEnterprises(String tradeName, String cnpj) {
        Specification<Enterprise> spec = Specification.where(EnterpriseSpecifications.tradeNameLike(tradeName))
                .and(EnterpriseSpecifications.cnpjLike(cnpj));

        return enterpriseMapper.toDto(new HashSet<>(enterpriseRepository.findAll(spec)));

    }


}

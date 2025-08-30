package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.mapper.EnterpriseMapper;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestCreateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFound;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import br.com.desafio.tecnico.desafio.infraestructure.repository.SupplierRepository;
import br.com.desafio.tecnico.desafio.presentation.serializer.view.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
                        .orElseThrow(EnterpriseNotFound::new)
        );
    }

    @Transactional
    public EnterpriseResponseDto saveEnterprise(EnterpriseRequestCreateDto enterpriseRequestCreateDto) {
        validateExistsDocument(enterpriseRequestCreateDto.cnpj());
        var state = cepService.consultCep(enterpriseRequestCreateDto.cep());
        validateExistsCep(state.cep());
        Enterprise enterprise = enterpriseMapper.toEntity(enterpriseRequestCreateDto);

        if (enterpriseRequestCreateDto.suppliers() != null) { // se a lista tiver algo, vamos validar os fornecedores
            // criamos a lista de fornecedores
            Set<Supplier> suppliers = StreamSupport
                    .stream(supplierRepository.findAllById(enterpriseRequestCreateDto.suppliers()).spliterator(), false)
                    .collect(Collectors.toSet());/* se o cep for do parana, chamaremos a função validateSuppliersForEnterprise, para verificar se algum dos
             fornecedores é divergente em nossas verificações */
            boolean result = cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, state.cep());
            if(result){
                validateAndFetchSuppliersForEnterprise(suppliers, state.cep());
            }
            // se tudo ocorrer bem, passamos nossa lista de fornecedores para a empresa.
            enterprise.setSuppliers(suppliers);
        }

        return enterpriseMapper.toDto(enterpriseRepository.save(enterprise));
    }

    @Transactional
    public EnterpriseResponseDto updateEnterprise(EnterpriseRequestUpdateDto enterpriseRequestUpdateDto) {
        // Validações
        var oldEnterprise = enterpriseRepository.findById(enterpriseRequestUpdateDto.enterpriseId())
                .orElseThrow(EnterpriseNotFound::new);
        var state = cepService.consultCep(enterpriseRequestUpdateDto.cep());
        validateExistsCep(state.cep());
        var newEnterprise = enterpriseMapper.toEntity(enterpriseRequestUpdateDto);
        if (enterpriseRequestUpdateDto.suppliers() != null) { // se a lista tiver algo, vamos validar os fornecedores
            // criamos a lista de fornecedores
            Set<Supplier> suppliers = StreamSupport
                    .stream(supplierRepository.findAllById(enterpriseRequestUpdateDto.suppliers()).spliterator(), false)
                    .collect(Collectors.toSet());/* se o cep for do parana, chamaremos a função validateSuppliersForEnterprise, para verificar se algum dos
             fornecedores é divergente em nossas verificações */
            boolean result = cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, state.cep());
            if(result){
                validateAndFetchSuppliersForEnterprise(suppliers, state.cep());
            }
            // se tudo ocorrer bem, passamos nossa lista de fornecedores para a empresa.
            newEnterprise.setSuppliers(suppliers);
        }

        // atualização

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

        return enterpriseMapper.toDto(enterpriseRepository.save(oldEnterprise));
    }

    @Transactional
    public void removeEnterprise(Long id) {
        enterpriseRepository.deleteById(id);
    }

    /**
     * Regras de negócio de vinculação de fornecedor
     */
    public void validateAndFetchSuppliersForEnterprise(Set<Supplier> suppliers, String state) {
            LocalDate actualDate = LocalDate.now();
            for (Supplier supplier : suppliers) {
                if (supplier.getType().equals(SupplierType.fromInt(0)) &&
                        supplier.getBirthDate() != null &&
                        supplier.getBirthDate().plusYears(18).isAfter(actualDate)) {

                    throw new IllegalArgumentException(
                            "Não é permitido cadastrar fornecedor pessoa física menor de idade para empresas do Paraná"
                    );
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

    public void validateExistsCep(String state){
        if(state == null){
            throw new RuntimeException("Este Cep não corresponde a nenhum lugar válido");
        }
    }

    public void deleteEnterprise(Long id) {
        enterpriseRepository.findById(id).orElseThrow(EnterpriseNotFound::new);
        enterpriseRepository.deleteById(id);
    }
}

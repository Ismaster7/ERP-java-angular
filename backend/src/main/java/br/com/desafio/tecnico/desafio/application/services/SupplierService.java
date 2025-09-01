package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.mapper.SupplierMapper;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierResponseDto;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.*;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import br.com.desafio.tecnico.desafio.infraestructure.repository.SupplierRepository;
import br.com.desafio.tecnico.desafio.infraestructure.repository.specifications.SupplierSpecifications;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static br.com.desafio.tecnico.desafio.infraestructure.repository.specifications.SupplierSpecifications.documentLike;
import static br.com.desafio.tecnico.desafio.infraestructure.repository.specifications.SupplierSpecifications.nameLike;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final SupplierMapper supplierMapper;
    private final CepService cepService;
    private final Set<String>
            prohibitedStatesForUnderageSuppliers = Set.of("parana", "paraná", "pr");
    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper,
                           EnterpriseRepository enterpriseRepository, CepService cepService){
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
        this.enterpriseRepository = enterpriseRepository;
        this.cepService = cepService;
    }

    public List<Supplier> getAllEnterprise(){
        return supplierRepository.findAll();
    }

    public SupplierResponseDto getEnterpriseById(Long id){
        return supplierMapper.toDto(supplierRepository.findById(id)
                .orElseThrow(EnterpriseNotFoundException::new));
    }
    public Set<SupplierResponseDto> getAllSuppliers() {
        Set<Supplier> suppliers = supplierRepository.findAll()
                .stream()
                .collect(Collectors.toSet());

        return supplierMapper.toDto(suppliers);
    }
    @Transactional
    public SupplierResponseDto saveSupplier(SupplierRequestDto supplierRequestDto){
        validateDocument(supplierRequestDto.document());
        Supplier supplier = supplierMapper.toEntity(supplierRequestDto);
        supplier.setType(validateLegalDocumentType(supplier));
        validateSupplierCanPersist(supplier);
        if(supplierRequestDto.enterprises() != null) {
            Set<Enterprise> enterprises = StreamSupport
                    .stream(enterpriseRepository.findAllById(supplierRequestDto.enterprises()).spliterator(), false)
                    .collect(Collectors.toSet());
            if(supplier.getType() == SupplierType.FISICA
                    && supplier.getBirthDate().plusYears(18).isAfter(LocalDate.now())) {
                validateEnterpriseForSuppliers(enterprises, supplier);
            }
            supplier.setEnterprises(enterprises);
        }

       return supplierMapper.toDto(supplierRepository.save(supplier));
    }

    @Transactional
    public SupplierResponseDto updateSupplier(SupplierRequestUpdateDto newSupplier) {
        Supplier existingSupplier = supplierRepository.findById(newSupplier.supplierId())
                .orElseThrow(() -> new SupplierNotFoundException("Supplier não encontrado com id: " + newSupplier.supplierId()));

        if (newSupplier.document() != null && !newSupplier.document().isBlank()) {
            existingSupplier.setDocument(new Document(newSupplier.document()));
        }
        if (newSupplier.name() != null && !newSupplier.name().isBlank()) {
            existingSupplier.setName(newSupplier.name());
        }
        if (newSupplier.email() != null && !newSupplier.email().isBlank()) {
            existingSupplier.setEmail(newSupplier.email());
        }
        if (newSupplier.cep() != null && !newSupplier.cep().isBlank()) {
            existingSupplier.setCep(new Cep(newSupplier.cep()));
        }
        if (newSupplier.rg() != null && !newSupplier.rg().isBlank()) {
            existingSupplier.setRg(newSupplier.rg());
        }
        if (newSupplier.birthDate() != null) {
            existingSupplier.setBirthDate(newSupplier.birthDate());
        }

        existingSupplier.setType(validateLegalDocumentType(existingSupplier));
        if ((existingSupplier.getBirthDate() == null || existingSupplier.getRg() == null)
                && (newSupplier.rg() == null || newSupplier.birthDate() == null)
                && (existingSupplier.getType().equals(SupplierType.FISICA))) {
            throw new EnterpriseRuleException("Você não pode se tornar fornecedor pessoa física sem cadastrar rg e data de aniversário");
        }
        validateSupplierCanPersist(existingSupplier);

        if (newSupplier.enterprises() != null) {
            Set<Enterprise> newEnterprises = StreamSupport
                    .stream(enterpriseRepository.findAllById(newSupplier.enterprises()).spliterator(), false)
                    .collect(Collectors.toSet());
        newEnterprises.forEach(enterprise -> System.out.println("Enterprise: " + enterprise.getEnterpriseId()));
            if (existingSupplier.getType() == SupplierType.FISICA
                    && existingSupplier.getBirthDate().plusYears(18).isAfter(LocalDate.now())) {
                validateEnterpriseForSuppliers(newEnterprises, existingSupplier);
            }

            for (Enterprise oldEnterprise : new HashSet<>(existingSupplier.getEnterprises())) {
                existingSupplier.removeEnterprise(oldEnterprise);
            }

            for (Enterprise enterprise : newEnterprises) {
                existingSupplier.addEnterprise(enterprise);
            }
        }

        return supplierMapper.toDto(supplierRepository.save(existingSupplier));
    }



    public void validateSupplierCanPersist(Supplier supplier){
        if(supplier.getType().equals(SupplierType.FISICA)
                && (supplier.getRg() == null || supplier.getBirthDate() == null)){
            throw new IllegalArgumentException("Fornecedor pessoa física também precisa cadastrar rg e data de nascimento");
        }

        var state = cepService.consultCep(supplier.getCep().getValue());
        if(state == null){
            throw new InvalidCepException();
        }

    }

    public Set<SupplierResponseDto> searchSuppliers(String name, String document) {
        Specification<Supplier> spec = Specification.where(nameLike(name))
                .and(documentLike(document));


        return supplierMapper.toDto(new HashSet<>(supplierRepository.findAll(spec)));
    }

    public void validateDocument(String value){
        var document = new Document(value);
        boolean exists = supplierRepository.existsByDocument(document);
        if (exists) {
            throw new IllegalArgumentException("Ja tem um fornecedor com este CNPJ/CPF");
        }
    }



    public void removeEnterprise(Long id){
        supplierRepository.deleteById(id);
    }

    public void validateEnterpriseForSuppliers(Set<Enterprise> enterprises, Supplier supplier) {

            for (Enterprise enterprise : enterprises) {
                if (cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, enterprise.getState()))
                    throw new EnterpriseRuleException();

        }
    }

    private SupplierType validateLegalDocumentType(Supplier supplier){
        if(supplier.getDocument().isValidCnpj(supplier.getDocument().getDocument())){
           return SupplierType.JURIDICA;
        } else if (supplier.getDocument().isValidCpf(supplier.getDocument().getDocument())) {
            return SupplierType.FISICA;
        }else {
            throw new InvalidDocumentExceptionException("Documento de fornecedor inválido");
        }
    }
    @Transactional
    public void deleteById(Long id) {
        if(!supplierRepository.existsById(id)){
            throw new SupplierNotFoundException();
        };
        supplierRepository.deleteById(id);
    }
}

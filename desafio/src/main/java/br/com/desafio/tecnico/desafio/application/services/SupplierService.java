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
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFoundException;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseRuleException;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.InvalidCepException;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.SupplierNotFoundException;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import br.com.desafio.tecnico.desafio.infraestructure.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public SupplierResponseDto saveSupplier(SupplierRequestDto supplierRequestDto){
        validateCnpj(supplierRequestDto.document());
        Supplier supplier = supplierMapper.toEntity(supplierRequestDto);
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

    public SupplierResponseDto updateSupplier(SupplierRequestUpdateDto newSupplier){
        Supplier existingSupplier = supplierRepository.findById(newSupplier.supplierId())
                .orElseThrow(() -> new SupplierNotFoundException("Supplier não encontrado com id: " + newSupplier.supplierId()));

        // se ele tentar trocar de tipo, então vamos verificar se está adequado as outras regras
        if((existingSupplier.getBirthDate() == null || existingSupplier.getRg() == null)
                &&(newSupplier.rg() == null || newSupplier.birthDate() == null)
                && (newSupplier.type().equals(SupplierType.FISICA.getCod()))){
            throw new EnterpriseRuleException("Você não pode se tornar fornecedor pessoa física sem cadastrar rg e data de aniversário");
        }

        Supplier supplier = supplierMapper.toEntityPut(newSupplier);
        validateSupplierCanPersist(supplier);

        if(newSupplier.enterprises() != null) {
            Set<Enterprise> enterprises = StreamSupport
                    .stream(enterpriseRepository.findAllById(newSupplier.enterprises()).spliterator(), false)
                    .collect(Collectors.toSet());
            if(supplier.getType() == SupplierType.FISICA
                    && supplier.getBirthDate().plusYears(18).isAfter(LocalDate.now())) {
                validateEnterpriseForSuppliers(enterprises, supplier);
            }
            supplier.setEnterprises(enterprises);
        }

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

        if (newSupplier.type() != null) {
            existingSupplier.setType(SupplierType.values()[newSupplier.type()]);
        }

        if (newSupplier.rg() != null && !newSupplier.rg().isBlank()) {
            existingSupplier.setRg(newSupplier.rg());
        }

        if (newSupplier.birthDate() != null) {
            existingSupplier.setBirthDate(newSupplier.birthDate());
        }

        return supplierMapper.toDto(supplierRepository.save(existingSupplier));
    }

    public void validateSupplierCanPersist(Supplier supplier){
        // validação de se o fornecedor é pessoa física tendo rg e data de aniversário na requisição
        if(supplier.getType().equals(SupplierType.FISICA)
                && (supplier.getRg() == null || supplier.getBirthDate() == null)){
            throw new IllegalArgumentException("Fornecedor pessoa física também precisa cadastrar rg e data de nascimento");
        }

        // validação do cep do fornecedor
        var state = cepService.consultCep(supplier.getCep().getValue());
        if(state == null){
            throw new InvalidCepException();
        }

    }

    public void validateCnpj(String value){
        // validação de Documento existente no banco de dados
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

        /*  Infelizmente teremos de realizar uam requisição para cada enterprise, verificando se são do Paraná,
           PORÉM só faremos isso se o fornecedor for menor de 18 anos.
        */

            for (Enterprise enterprise : enterprises) {
                if (cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, enterprise.getCep().getValue()))
                    throw new EnterpriseRuleException();

        }
    }

    public void deleteById(Long id) {
        if(!supplierRepository.existsById(id)){
            throw new SupplierNotFoundException();
        };
        supplierRepository.deleteById(id);
    }
}

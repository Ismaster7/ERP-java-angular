package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.mapper.SupplierMapper;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierResponseDto;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFound;
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
                .orElseThrow(EnterpriseNotFound::new));
    }

    public Supplier saveSupplier(SupplierRequestDto supplierRequestDto){
        validateSupplierCanPersist(supplierRequestDto);
        Supplier supplier = supplierMapper.toEntity(supplierRequestDto);
        if(supplierRequestDto.enterprises() != null) {
            Set<Enterprise> enterprises = StreamSupport
                    .stream(enterpriseRepository.findAllById(supplierRequestDto.enterprises()).spliterator(), false)
                    .collect(Collectors.toSet());
            validateEnterpriseForSuppliers(enterprises, supplier);
            supplier.setEnterprises(enterprises);
        }

       return supplierRepository.save(supplier);
    }

    public void validateSupplierCanPersist(SupplierRequestDto supplierRequestDto){
        // validação de Documento existente no banco de dados
        var document = new Document(supplierRequestDto.document());
        boolean exists = supplierRepository.existsByDocument(document);
        if (exists) {
            throw new IllegalArgumentException("Ja tem um fornecedor com este CNPJ/CPF");

        }
        // validação de se o fornecedor é pessoa física tendo rg e data de aniversário na requisição
        if(SupplierType.fromInt(supplierRequestDto.type()) == SupplierType.FISICA
                && (supplierRequestDto.rg() == null || supplierRequestDto.birthDate() == null)){
            throw new IllegalArgumentException("Fornecedor pessoa física também precisa cadastrar rg e data de nascimento");
        }

        // validação do cep do fornecedor
        var state = cepService.consultCep(supplierRequestDto.cep());
        if(state == null){
            throw new RuntimeException("Este Cep não corresponde a nenhum lugar válido");
        }

    }

    public void updateSupplier(Supplier newSupplier){
//        var oldSupplier = getEnterpriseById(newSupplier.getSupplierId());
//        if(newSupplier.getCep() != null && newSupplier.getCep() != newSupplier.getCep())
//            oldSupplier.setCep(newSupplier.getCep());
//        supplierRepository.save(oldSupplier);
    }

    public void removeEnterprise(Long id){
        supplierRepository.deleteById(id);
    }

    public void validateEnterpriseForSuppliers(Set<Enterprise> enterprises, Supplier supplier) {

        /*  Infelizmente teremos de realizar uam requisição para cada enterprise, verificando se são do Paraná,
           PORÉM só faremos isso se o fornecedor for menor de 18 anos.
        */
        if(supplier.getBirthDate().plusYears(18).isAfter(LocalDate.now())) {
            String[] states = new String[]{"parana", "paraná", "pr"};

            for (Enterprise enterprise : enterprises) {
                if (cepService.isCepFromSpecificStates(prohibitedStatesForUnderageSuppliers, enterprise.getCep().getValue()))
                    throw new IllegalArgumentException("Não é permitido cadastrar fornecedor pessoa física menor de idade para empresas do Paraná");
            }
        }
    }

}

package br.com.desafio.tecnico.desafio.application.mapper;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierResponseDto;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SupplierMapper {

    private final EnterpriseMapper enterpriseMapper;

    public SupplierMapper(EnterpriseMapper enterpriseMapper){
        this.enterpriseMapper = enterpriseMapper;
    }

    public Supplier toEntityPut(SupplierRequestUpdateDto supplierRequestUpdateDto){
        if(supplierRequestUpdateDto == null)
        {
            throw new IllegalArgumentException("Parâmetros nulos");
        }
        var entity = new Supplier();

        if(supplierRequestUpdateDto.cep() != null){
            entity.setCep(new Cep(supplierRequestUpdateDto.cep()));
        }
        if(supplierRequestUpdateDto.document() != null){
            entity.setDocument(new Document(supplierRequestUpdateDto.document().toString()));
        }

        if(supplierRequestUpdateDto.name() != null){
            entity.setName(supplierRequestUpdateDto.name());
        }
        if(supplierRequestUpdateDto.email() != null){
            entity.setEmail(supplierRequestUpdateDto.email());
        }

        if(supplierRequestUpdateDto.rg() != null){
            entity.setRg(supplierRequestUpdateDto.rg());
        }
        if(supplierRequestUpdateDto.birthDate() != null){
            entity.setBirthDate(supplierRequestUpdateDto.birthDate());
        }
        return entity;
    }

    public Supplier toEntity(SupplierRequestDto supplierRequestDto){
        if(supplierRequestDto == null)
        {
            throw new IllegalArgumentException("Parâmetros nulos");
        }
        var entity = new Supplier();

        if(supplierRequestDto.cep() != null){
            entity.setCep(new Cep(supplierRequestDto.cep()));
        }
        if(supplierRequestDto.document() != null){
            entity.setDocument(new Document(supplierRequestDto.document().toString()));
        }

        if(supplierRequestDto.name() != null){
            entity.setName(supplierRequestDto.name());
        }
        if(supplierRequestDto.email() != null){
            entity.setEmail(supplierRequestDto.email());
        }
        if(supplierRequestDto.rg() != null){
            entity.setRg(supplierRequestDto.rg());
        }
        if(supplierRequestDto.birthDate() != null){
            entity.setBirthDate(supplierRequestDto.birthDate());
        }
        return entity;
    }

    public SupplierResponseDto toDto(Supplier supplier) {
        if (supplier == null) {
            return null;
        }


        // Converte as Enterprises associadas
        Set<EnterpriseResponseDto> enterprises = enterpriseMapper.toDtoWList(supplier.getEnterprises());

        return new SupplierResponseDto(
                supplier.getSupplierId(), // id do fornecedor
                supplier.getDocument() != null ? supplier.getDocument().getDocument() : null, // CPF/CNPJ
                supplier.getName() != null ? supplier.getName(): null, // nome
                supplier.getEmail() != null ? supplier.getEmail(): null, // email
                supplier.getCep() != null ? supplier.getCep().getValue() : null, // cep
                supplier.getType() != null ? supplier.getType().getCod() : null, // tipo do supplier (enum)
                supplier.getRg() != null ? supplier.getRg() : null, // rg
                supplier.getBirthDate() != null ? supplier.getBirthDate() : null, // data de nascimento
                enterprises // lista de enterprises associadas
        );
    }
    public Set<SupplierResponseDto> toDto(Set<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return Set.of();
        }

        return suppliers.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public SupplierResponseDto toDtoWithoutList(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        return new SupplierResponseDto(
                supplier.getSupplierId(),
                supplier.getDocument() != null ? supplier.getDocument().getDocument() : null,
                supplier.getName()!= null ? supplier.getName() : null,
                supplier.getEmail() != null ? supplier.getEmail() : null,
                supplier.getCep() != null ? supplier.getCep().getValue() : null,
                supplier.getType() != null ? supplier.getType().ordinal() : null,
                supplier.getRg() != null ? supplier.getRg() : null,
                supplier.getBirthDate() != null ? supplier.getBirthDate() : null,
                new HashSet<>()
        );
    }

    public Set<SupplierResponseDto> toDtoWList(Set<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return Set.of();
        }

        return suppliers.stream()
                .map(this::toDtoWithoutList)
                .collect(Collectors.toSet());
    }
}

package br.com.desafio.tecnico.desafio.application.mapper;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestCreateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierResponseDto;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EnterpriseMapper {


    public Enterprise toEntity(EnterpriseRequestCreateDto enterpriseRequestCreateDto){
        if(enterpriseRequestCreateDto == null)
        {
            throw new IllegalArgumentException("Parâmetros nulos");
        }
        var entity = new Enterprise();

        if(enterpriseRequestCreateDto.cep() != null){
            entity.setCep(new Cep(enterpriseRequestCreateDto.cep().toString()));
        }
        if(enterpriseRequestCreateDto.cnpj() != null){
            entity.setCnpj(new Cnpj(enterpriseRequestCreateDto.cnpj().toString()));
        }

        if(enterpriseRequestCreateDto.tradeName() != null){
            entity.setTradeName(enterpriseRequestCreateDto.tradeName());
        }

        return entity;
    }

    public Enterprise toEntity(EnterpriseRequestUpdateDto enterpriseUpdateCreateDto){
        if(enterpriseUpdateCreateDto == null)
        {
            throw new IllegalArgumentException("Parâmetros nulos");
        }
        var entity = new Enterprise();

        if(enterpriseUpdateCreateDto.cep() != null){
            entity.setCep(new Cep(enterpriseUpdateCreateDto.cep().toString()));
        }
        if(enterpriseUpdateCreateDto.cnpj() != null){
            entity.setCnpj(new Cnpj(enterpriseUpdateCreateDto.cnpj().toString()));
        }

        if(enterpriseUpdateCreateDto.tradeName() != null){
            entity.setTradeName(enterpriseUpdateCreateDto.tradeName());
        }

        return entity;
    }

    public EnterpriseResponseDto toDto(Enterprise enterprise) {
        if (enterprise == null) {
            return null;
        }

        // Mapeamento manual de suppliers
        Set<SupplierResponseDto> suppliers = null;
        if (enterprise.getSuppliers() != null) {
            suppliers = enterprise.getSuppliers().stream()
                    .map(supplier -> new SupplierResponseDto(
                            supplier.getSupplierId(),
                            supplier.getDocument() != null ? supplier.getDocument().getDocument() : null,
                            supplier.getName() != null ? supplier.getName() : null,
                            supplier.getEmail() != null ? supplier.getEmail() : null,
                            supplier.getCep() != null ? String.valueOf(supplier.getCep().getValue()) : null,
                            supplier.getType().getCod(),
                            supplier.getRg(),
                            supplier.getBirthDate(),
                            new HashSet<>()
                    ))
                    .collect(Collectors.toSet());
        }

        return new EnterpriseResponseDto(
                enterprise.getEnterpriseId(),
                enterprise.getCnpj() != null ? enterprise.getCnpj().getDocument() : null,
                enterprise.getTradeName() != null ? enterprise.getTradeName() : null,
                enterprise.getState() != null ? enterprise.getState() : null,
                enterprise.getCep() != null ? String.valueOf(enterprise.getCep().getValue()) : null,
                suppliers
        );
    }

    public Set<EnterpriseResponseDto> toDto(Set<Enterprise> enterprises) {
        if (enterprises == null || enterprises.isEmpty()) {
            return Set.of();
        }

        return enterprises.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
    public Set<EnterpriseResponseDto> toDtoWList(Set<Enterprise> enterprises) {
        if (enterprises == null || enterprises.isEmpty()) {
            return Set.of();
        }

        return enterprises.stream()
                .map(this::toDtoWithoutList)
                .collect(Collectors.toSet());
    }
    public EnterpriseResponseDto toDtoWithoutList(Enterprise enterprise) {
        if (enterprise == null) {
            return null;
        }

        return new EnterpriseResponseDto(
                enterprise.getEnterpriseId(),
                enterprise.getCnpj() != null ? enterprise.getCnpj().getDocument() : null,
                enterprise.getTradeName()!= null ? enterprise.getTradeName() : null,
                enterprise.getState() != null ? enterprise.getState() : null,
                enterprise.getCep() != null ? String.valueOf(enterprise.getCep().getValue()) : null,
                new HashSet<>()
        );
    }
}





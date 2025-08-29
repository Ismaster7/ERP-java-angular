package br.com.desafio.tecnico.desafio.application.mapper;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.EnterpriseRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseMapper {

    public Enterprise toEntity(EnterpriseRequestDto enterpriseRequestDto){
        if(enterpriseRequestDto == null)
        {
            throw new IllegalArgumentException("Parâmetros nulos");
        }
        var entity = new Enterprise();

        if(enterpriseRequestDto.cep() != null){
            entity.setCep(new Cep(enterpriseRequestDto.cep().toString()));
        }
        if(enterpriseRequestDto.cnpj() != null){
            entity.setCnpj(new Cnpj(enterpriseRequestDto.cnpj().toString()));
        }

        if(enterpriseRequestDto.tradeName() != null){
            entity.setTradeName(enterpriseRequestDto.tradeName());
        }

        return entity;
    }

}

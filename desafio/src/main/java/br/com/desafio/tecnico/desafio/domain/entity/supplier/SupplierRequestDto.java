package br.com.desafio.tecnico.desafio.domain.entity.supplier;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record SupplierRequestDto(
         Long supplierId,
 Document document,
 String name,
 String email,
 Cep cep,
 SupplierType type,
 String rg,
 LocalDate birthDate,
 Set<Enterprise> enterprises
) {
}

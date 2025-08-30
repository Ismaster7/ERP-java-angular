package br.com.desafio.tecnico.desafio.application.services.mocks;


import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestCreateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnterpriseMock {

    public static EnterpriseRequestCreateDto createRequestDto() {
        Set<Long> supplierIds = Set.of(1L, 2L);
        return new EnterpriseRequestCreateDto(
                1234567L,
                "12332258525852",
                "Empresa",
                "0207110",
                supplierIds
        );
    }

    public static Enterprise createEnterpriseEntity() {
        return new Enterprise();
    }

    public static Enterprise createSavedEnterpriseEntity() {
        Enterprise savedEnterprise = new Enterprise();
        savedEnterprise.setEnterpriseId(10L);
        return savedEnterprise;
    }

    public static List<Supplier> createSuppliers() {
        Supplier supplier1 = new Supplier();
        supplier1.setType(SupplierType.FISICA);
        supplier1.setBirthDate(LocalDate.now().minusYears(20));

        Supplier supplier2 = new Supplier();
        supplier2.setType(SupplierType.JURIDICA);

        return List.of(supplier1, supplier2);
    }

    public static EnterpriseResponseDto createResponseDto() {
        return new EnterpriseResponseDto(
                10L,
                "123456780001",
                "Empresa Teste",
                "0207110",
                new HashSet<>()
        );
    }
}


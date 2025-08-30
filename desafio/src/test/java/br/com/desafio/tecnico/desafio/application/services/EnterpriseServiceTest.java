package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.mapper.EnterpriseMapper;
import br.com.desafio.tecnico.desafio.application.services.CepService;
import br.com.desafio.tecnico.desafio.application.services.EnterpriseService;
import br.com.desafio.tecnico.desafio.application.services.dto.CepResponse;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestCreateDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.enums.SupplierType;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseRuleException;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.InvalidDocumentExceptionException;
import br.com.desafio.tecnico.desafio.infraestructure.repository.EnterpriseRepository;
import br.com.desafio.tecnico.desafio.infraestructure.repository.SupplierRepository;
import br.com.desafio.tecnico.desafio.application.services.mocks.EnterpriseMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EnterpriseServiceTest {

    @Mock
    private EnterpriseRepository enterpriseRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private CepService cepService;

    @Mock
    private EnterpriseMapper enterpriseMapper;

    @InjectMocks
    private EnterpriseService enterpriseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void shouldSaveEnterprise() {
        var requestDto = EnterpriseMock.createRequestDto();
        var enterprise = EnterpriseMock.createEnterpriseEntity();
        var savedEnterprise = EnterpriseMock.createSavedEnterpriseEntity();
        List<Supplier> suppliers = EnterpriseMock.createSuppliers();
        var responseDto = EnterpriseMock.createResponseDto();

        when(enterpriseMapper.toEntity(requestDto)).thenReturn(enterprise);
        when(cepService.consultCep(requestDto.cep())).thenReturn(new CepResponse("08526-654","parana", "pr"));
        when(supplierRepository.findAllById(requestDto.suppliers())).thenReturn(suppliers);
        when(cepService.isCepFromSpecificStates(anySet(), any())).thenReturn(true);
        when(enterpriseRepository.save(enterprise)).thenReturn(savedEnterprise);
        when(enterpriseMapper.toDto(savedEnterprise)).thenReturn(responseDto);

        var result = enterpriseService.saveEnterprise(requestDto);

        verify(enterpriseMapper).toEntity(requestDto);
        verify(cepService).consultCep(requestDto.cep());
        verify(supplierRepository).findAllById(requestDto.suppliers());
        verify(cepService).isCepFromSpecificStates(anySet(), any());
        verify(enterpriseRepository).save(enterprise);
        verify(enterpriseMapper).toDto(any(br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise.class));

        assertEquals(new HashSet<>(suppliers), enterprise.getSuppliers());
        assertEquals(responseDto, result);
    }

    @Test
    void shouldThrowEnterpriseRuleExceptionWhenSupplierUnderageAndCEPProhibited() {
        // Usando os mocks
        EnterpriseRequestCreateDto dto = EnterpriseMock.createRequestDto();
        Enterprise enterprise = EnterpriseMock.createEnterpriseEntity();
        List<Supplier> suppliers = EnterpriseMock.createSuppliers();

        // Ajustando para que um supplier seja menor de 18 anos
        Supplier underageSupplier = suppliers.get(0);
        underageSupplier.setBirthDate(LocalDate.now().minusYears(15));
        underageSupplier.setDocument(new Document("12345678901"));
        underageSupplier.setCep(new Cep("80000-000"));

        // Mock do mapper
        when(enterpriseMapper.toEntity(dto)).thenReturn(enterprise);

        // Mock do CEP Service
        when(cepService.consultCep(dto.cep()))
                .thenReturn(new CepResponse(dto.cep(), "Rua Teste", "PR"));
        when(cepService.isCepFromSpecificStates(anySet(), anyString())).thenReturn(true);

        // Mock do SupplierRepository para retornar o supplier menor de idade
        when(supplierRepository.findAllById(eq(dto.suppliers()))).thenReturn(suppliers);

        // Executa e verifica a exceção
        assertThrows(EnterpriseRuleException.class, () -> enterpriseService.saveEnterprise(dto));

        // Verifica que a empresa não foi salva
        verify(enterpriseRepository, never()).save(any());
    }





}

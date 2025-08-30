package br.com.desafio.tecnico.desafio.presentation.controller;

import br.com.desafio.tecnico.desafio.application.services.EnterpriseService;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestCreateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/enterprise/v1")
public class EnterpriseController {
    private final EnterpriseService enterpriseService;
    public EnterpriseController(EnterpriseService enterpriseService){
        this.enterpriseService = enterpriseService;
    }

    @GetMapping
    public ResponseEntity<Set<EnterpriseResponseDto>>getAllEnterprises(){
        return ResponseEntity.status(HttpStatus.FOUND).body(enterpriseService.getAllEnterprise());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnterpriseResponseDto> getEnterpise(@PathVariable(name="id") Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(enterpriseService.getEnterpriseById(id));
    }


    @PostMapping
    public ResponseEntity<EnterpriseResponseDto> createNewEnterprise(@RequestBody @Valid EnterpriseRequestCreateDto enterprise){
        return ResponseEntity.status(HttpStatus.CREATED).body( enterpriseService.saveEnterprise(enterprise));
    }

    @PutMapping
    public ResponseEntity<EnterpriseResponseDto> updateEnterprise(@RequestBody @Valid EnterpriseRequestUpdateDto enterprise){
        return ResponseEntity.status(HttpStatus.OK).body( enterpriseService.updateEnterprise(enterprise));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable(name = "id") Long id){
        enterpriseService.deleteEnterprise(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

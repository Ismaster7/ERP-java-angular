package br.com.desafio.tecnico.desafio.presentation.controller;

import br.com.desafio.tecnico.desafio.application.mapper.EnterpriseMapper;
import br.com.desafio.tecnico.desafio.application.services.EnterpriseService;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.EnterpriseRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/enterprise/v1")
public class EnterpriseController {
    private final EnterpriseService enterpriseService;
    public EnterpriseController(EnterpriseService enterpriseService){
        this.enterpriseService = enterpriseService;
    }

    @GetMapping
    public ResponseEntity<List<Enterprise>>getAllEnterprises(){
        return ResponseEntity.status(HttpStatus.FOUND).body(enterpriseService.getAllEnterprise());
    }

    @PostMapping
    public ResponseEntity<Enterprise> createNewEnterprise(@RequestBody @Valid EnterpriseRequestDto enterprise){
        return ResponseEntity.status(HttpStatus.CREATED).body( enterpriseService.saveEnterprise(enterprise));
    }
}

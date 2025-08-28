package br.com.desafio.tecnico.desafio.presentation.controller;

import br.com.desafio.tecnico.desafio.application.services.EnterpriseService;
import br.com.desafio.tecnico.desafio.domain.entity.Enterprise;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Enterprise> createNewEnterprise(Enterprise enterprise){
        return ResponseEntity.status(HttpStatus.CREATED).body(enterpriseService.saveEnterprise(enterprise));
    }
}

package br.com.desafio.tecnico.desafio.presentation.controller;

import br.com.desafio.tecnico.desafio.application.services.SupplierService;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/supplier/v1")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService){
        this.supplierService = supplierService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> getSupplier(@PathVariable(name="id") Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(supplierService.getEnterpriseById(id));
    }

    @GetMapping
    public ResponseEntity<Set<SupplierResponseDto>> getAllSuppliers(){
        return ResponseEntity.status(HttpStatus.FOUND).body(supplierService.getAllSuppliers());
    }

    @PostMapping
    public ResponseEntity<SupplierResponseDto> saveSupplier(@RequestBody SupplierRequestDto supplierRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body( supplierService.saveSupplier(supplierRequestDto));
    }

    @PutMapping
    public ResponseEntity<SupplierResponseDto> updateSupplier(@RequestBody SupplierRequestUpdateDto supplierRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body( supplierService.updateSupplier(supplierRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable(name = "id")Long id){
        supplierService.deleteById(id);
        return ResponseEntity.status((HttpStatus.NO_CONTENT)).build();
    }

}

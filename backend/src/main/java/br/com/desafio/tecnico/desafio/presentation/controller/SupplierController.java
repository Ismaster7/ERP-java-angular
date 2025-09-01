package br.com.desafio.tecnico.desafio.presentation.controller;

import br.com.desafio.tecnico.desafio.application.services.SupplierService;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/{id}", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<SupplierResponseDto> getSupplier(@PathVariable(name="id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getEnterpriseById(id));
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Set<SupplierResponseDto>> getAllSuppliers(){
        return ResponseEntity.status(HttpStatus.OK).body(supplierService.getAllSuppliers());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<SupplierResponseDto> saveSupplier(@RequestBody @Valid  SupplierRequestDto supplierRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body( supplierService.saveSupplier(supplierRequestDto));
    }

    @PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<SupplierResponseDto> updateSupplier(@RequestBody @Valid SupplierRequestUpdateDto supplierRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body( supplierService.updateSupplier(supplierRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable(name = "id")Long id){
        supplierService.deleteById(id);
        return ResponseEntity.status((HttpStatus.NO_CONTENT)).build();
    }

    @GetMapping("/search")
    public ResponseEntity<Set<SupplierResponseDto>> searchSuppliers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String document) {

        return ResponseEntity.ok(supplierService.searchSuppliers(name, document));
    }


}

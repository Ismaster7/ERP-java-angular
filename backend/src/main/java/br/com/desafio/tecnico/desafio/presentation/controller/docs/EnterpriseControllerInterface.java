package br.com.desafio.tecnico.desafio.presentation.controller.docs;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestCreateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseRequestUpdateDto;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/enterprise/v1")
public interface EnterpriseControllerInterface {


    @Operation(
            summary = "Find all enterprises",
            tags = {"Enterprise"},
            responses = {
                    @ApiResponse(
                            description = "Success - list of enterprises retrieved",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = EnterpriseResponseDto.class))
                                    ),
                                    @Content(
                                            mediaType = MediaType.APPLICATION_XML_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = EnterpriseResponseDto.class))
                                    )
                            }),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Set<EnterpriseResponseDto>> getAllEnterprises();


    @Operation(
            summary = "Find enterprise by ID",
            tags = {"Enterprise"},
            responses = {
                    @ApiResponse(
                            description = "Success - enterprise retrieved",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = EnterpriseResponseDto.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EnterpriseResponseDto> getEnterprise(@PathVariable(name = "id") Long id);


    @Operation(
            summary = "Create new enterprise",
            tags = {"Enterprise"},
            responses = {
                    @ApiResponse(
                            description = "Created - enterprise successfully created",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = EnterpriseResponseDto.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    @PostMapping
    ResponseEntity<EnterpriseResponseDto> createNewEnterprise(@RequestBody @Valid EnterpriseRequestCreateDto enterprise);

    @Operation(
            summary = "Update enterprise",
            tags = {"Enterprise"},
            responses = {
                    @ApiResponse(
                            description = "Success - enterprise updated",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = EnterpriseResponseDto.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    @PutMapping
    ResponseEntity<EnterpriseResponseDto> updateEnterprise(@RequestBody @Valid EnterpriseRequestUpdateDto enterprise);


    @Operation(
            summary = "Delete enterprise by ID",
            tags = {"Enterprise"},
            responses = {
                    @ApiResponse(description = "No content - enterprise deleted", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEnterprise(@PathVariable(name = "id") Long id);
}

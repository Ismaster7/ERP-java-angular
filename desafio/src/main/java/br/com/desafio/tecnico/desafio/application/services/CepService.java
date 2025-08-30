package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.services.dto.CepResponse;
import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.InvalidCepException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class CepService {

    @Value("${cep.api}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CepResponse consultCep(String cep) {
        String url = apiUrl.replace("{cep}", cep);
        try {
            // RestTemplate já faz o mapeamento para o DTO CepResponse
            var result = restTemplate.getForObject(url, CepResponse.class);
            if(result.cep() == null){
                throw new InvalidCepException();
            }
        return result;
        } catch (Exception e) {
            throw new InvalidCepException();
        }
    }

    public boolean isCepFromSpecificStates(Set<String> states, String enterpriseCep) {
        var enterpriseLocation = consultCep(enterpriseCep);
        for (String state : states) {
            if (enterpriseLocation.uf().equalsIgnoreCase(state) ||
                    enterpriseLocation.estado().equalsIgnoreCase(state) ) {
                return true;
            }
        }
        return false;
    }
}

package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.services.dto.CepResponse;
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
            return restTemplate.getForObject(url, CepResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro na requisição ao ViaCEP: " + e.getMessage(), e);
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

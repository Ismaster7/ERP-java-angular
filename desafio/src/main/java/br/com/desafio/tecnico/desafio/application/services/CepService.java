package br.com.desafio.tecnico.desafio.application.services;

import br.com.desafio.tecnico.desafio.application.services.dto.CepResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<CepResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CepResponse.class
        );

        return response.getBody();
    }


    public boolean isFromSpecificState(String[] states, String cep){
        var cepApi = consultCep(cep);
        System.out.print(cepApi);

        return true;

    }


}

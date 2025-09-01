package br.com.desafio.tecnico.desafio.presentation.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CepSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null && value.matches("\\d{8}")) {
            String formatted = value.replaceFirst("(\\d{5})(\\d{3})", "$1-$2");
            gen.writeString(formatted);
        } else {
            gen.writeString(value);
        }
    }
}

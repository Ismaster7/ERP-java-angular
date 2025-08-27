package br.com.desafio.tecnico.desafio.domain.enums;

public enum SupplierType {
    FISICA(1),
    JURIDICA(2);

    private final int cod;

    SupplierType(int cod){
        this.cod = cod;
    }

    public int getCod() {
        return cod;
    }

    public static SupplierType fromInt(int i) {
        for (SupplierType type : SupplierType.values()) {
            if (type.getCod() == i) {
                return type;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + i);
    }
}

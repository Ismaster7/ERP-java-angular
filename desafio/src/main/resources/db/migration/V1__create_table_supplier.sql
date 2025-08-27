

CREATE TABLE enterprise (
    enterprise_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    trade_name VARCHAR(255) NOT NULL UNIQUE,
    cep_value VARCHAR(10) NOT NULL
);

CREATE TABLE supplier (
    supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    document VARCHAR(14) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    cep VARCHAR(10),
    type INT NOT NULL,
    rg VARCHAR(20),
    birthday_date DATE
);

CREATE TABLE enterprise_supplier (
    enterprise_id BIGINT NOT NULL,
    supplier_id BIGINT NOT NULL,
    PRIMARY KEY (enterprise_id, supplier_id),
    FOREIGN KEY (enterprise_id) REFERENCES enterprise(enterprise_id) ON DELETE CASCADE,
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON DELETE CASCADE
);

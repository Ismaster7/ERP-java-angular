# ERP SYSTEM

Um sistema simples de ERP desenvolvido com Java (Spring Boot) no backend e Angular no frontend, proposto como desafio técnico.

---

## 🛠 Requisitos

- Nenhuma aplicação rodando nas portas **4999** (frontend) e **8999** (backend).
- Docker e Docker Compose instalados.
- Conexão com a internet para baixar as imagens necessárias.

---

## 🚀 Instalação

Clone este repositório em uma pasta local. Crie nela um arquivo com nome ".env" e adicione:
```bash
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=desafio
MYSQL_USER=backend
MYSQL_PASSWORD=root
````
ou use outras credenciais, se você quiser.
Com Docker e Docker Compose instalados, abra a pasta dentro de algum terminal e execute:
obs: Se não conseguir rodar via docker por algum motivo, será necessário alterar o arquivo application-docker.yaml no java com os dados do seu banco de dados local. O front-end pode ser executado com ng serve.
```bash
docker compose up --build -d
# (use sudo no início se estiver em uma máquina Linux)
````

Agora, basta acessar http://localhost:4999 para ver o projeto
---
## 🛠 Features
- ✅ API disponível em JSON e XML (Content Negociation) (veja seção API).
- 🔄 Testes unitários .  (veja seção API)
- 📚 Documentação completa da API via Swagger (veja seção API).
- 🔍 Filtros dinâmicos tanto para fornecedores quanto para empresas.
- Tela responsiva para diferentes dispositivos
- 🔄 Sistema de adição e remoção de entidades com relacionamento Many-to-Many.
- 🌐 Consumo de API externa para validação de CEP:
- No frontend e no backend.
- Com validação interna em ambos os lados também.
---
## 📚 API

- Para acessar a documentação da api, basta rodar o docker-compose como nas instruções de instalação e depois acessar o link:
http://localhost:8999/swagger-ui/index.html

- Para ver as resposta da API com XML, é necessário alguma plataforma de testes de api, como o postman. Usando ele como exemplo:

 
1. Abra o Postman e vá até a aba Headers da requisição.
 
2. Adicione a chave Accept com o valor `application/xml` (para receber resposta em XML).

   
- A Api possui 2 testes unitários em src/test/java/br.com.desafio.tecnico.desafio. Lá tem um mock de dados tambem.

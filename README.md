# 🕶️ E-Commerce de Óculos de Sol - API REST

Este projeto é uma API REST desenvolvida em **Java 21** com o **Quarkus**, voltada para gerenciamento de óculos de sol em um sistema de e-commerce.

## 📊 Diagrama de Classes (UML)

Abaixo está o diagrama de classes que representa toda a estrutura do projeto:

![Diagrama UML](oculos-de-sol-tp1\docs\modelagem.png)



## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Quarkus**
- **JPA/Hibernate**
- **RESTEasy**
- **MapStruct (DTOs)**
- **PostgreSQL**
- **JUnit**

## 🧩 Estrutura do Projeto

```
src
├── model             # Entidades JPA
├── dto               # Objetos de transferência de dados
├── repository        # Interfaces JPA para persistência
├── service           # Interfaces de regras de negócio
├── resource          # Endpoints REST
└── exceptions        # Exceptions personalizadas
```

## 🏗️ Como Rodar o Projeto

Execute os comandos abaixo no terminal para rodar o projeto em modo de desenvolvimento:

Clona o repositório do GitHub para sua máquina:
```bash
git clone https://github.com/seu-usuario/oculos-ecommerce-api.git
```

Entra na pasta do projeto:
```bash
cd oculos-ecommerce-api
```
Inicia o Quarkus em modo de desenvolvimento com hot reload?
```bash
./mvnw quarkus:dev
```
Após isso, o projeto estará rodando em:
```bash
http://localhost:8080
```

Você pode acessar a documentação da API (Swagger) em:  
```bash
http://localhost:8080/q/swagger-ui
```


## 👨‍💻 Desenvolvedor

Desenvolvido por **Joás**  
Curso: **Sistemas de Informação**

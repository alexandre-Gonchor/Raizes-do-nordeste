# 🌵 Raízes do Nordeste - API Backend
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)
![JWT](https://img.shields.io/badge/Security-JWT-black.svg)

> API RESTful robusta desenvolvida para o gerenciamento de uma rede de restaurantes de culinária nordestina. O sistema resolve desafios complexos do food service, como controle Multi-Tenant (múltiplas filiais), estoques distribuídos, regras de precificação dinâmica (cupons e fidelidade) e segurança de rotas.

---

## 📋 Sumário
- [Visão Geral e Arquitetura](#-visão-geral-e-arquitetura)
- [Regras de Negócio Implementadas](#-regras-de-negócio-implementadas)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Como Executar o Projeto Localmente](#-como-executar-o-projeto-localmente)
- [Testando a API (Swagger e Insomnia)](#-testando-a-api)

---

## 🏛️ Visão Geral e Arquitetura

O "Raízes do Nordeste" é um sistema de retaguarda desenhado para a escalabilidade. Diferente de sistemas de loja única, sua estrutura de banco de dados suporta uma matriz e infinitas filiais.

O projeto foi construído seguindo a **Arquitetura em Camadas (Layered Architecture)** para garantir manutenibilidade:
* **Dominio:** Entidades JPA que representam as tabelas do banco.
* **Infra:** Interfaces do Spring Data JPA (Repositórios) responsáveis pela persistência.
* **Aplicação (Serviço):** O "cérebro" do sistema, contendo regras matemáticas e de validação.
* **API (Controle):** Exposição dos endpoints REST e DTOs.

---

## 🧠 Regras de Negócio Implementadas

Para atender aos requisitos de um e-commerce real, o backend processa as seguintes lógicas de forma automatizada:

1. **Catálogo Inteligente (Vitrine):** Realiza um *Join* com o estoque, retornando ao cliente apenas produtos com quantidade disponível (> 0) na filial selecionada.
2. **Prioridade de Descontos (Checkout):** Aplica primeiro o desconto de Cupons Promocionais (%) sobre o valor bruto e, somente depois, abate o saldo de Pontos de Fidelidade, garantindo a matemática financeira correta.
3. **Gestão de Estoque:** Baixa instantânea (Saída) de ingredientes na filial específica ao aprovar um pedido, e registro de movimentações de (Entrada) por gerentes.
4. **Acúmulo de Fidelidade:** O cliente ganha pontos baseados no valor *final pago* da compra.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**: Versão LTS da linguagem.
- **Spring Boot**: Framework principal para a construção da API.
- **PostgreSQL**: Banco de dados relacional.
- **Spring Security + JWT**: Para controle de acesso, RBAC e autenticação stateless.
- **Spring Data JPA / Hibernate**: Mapeamento Objeto-Relacional (ORM).
- **Lombok**: Redução de código boilerplate.
- **SpringDoc OpenAPI (Swagger)**: Documentação interativa.

---

## ⚙️ Como Executar o Projeto Localmente

### 1. Pré-requisitos
* **JDK 21** instalado.
* **Maven** instalado.
* **PostgreSQL** rodando localmente (porta padrão `5432`).
* **Git** instalado.

### 2. Clonando o Repositório
Abra o seu terminal e execute os comandos abaixo para baixar o código e entrar na pasta do projeto:

```bash
git clone https://github.com/alexandre-Gonchor/Raizes-do-nordeste.git
cd Raizes-do-nordeste

```
### 3. Build e Execução
Abra um terminal na pasta raiz do projeto e execute:

```bash
# Baixa as dependências e compila
mvn clean install

# Roda o servidor
mvn spring-boot:run

```
## 🧪 Testando a API

### Opção 1: Swagger UI (Recomendado)
Acesse **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**.

1. Crie um usuário na rota `POST /auth/register`.
2. Faça login em `POST /auth/login` e copie o token gerado.
3. Clique no botão **Authorize** (Cadeado no topo da página), cole o token e teste as rotas protegidas.

### Opção 2: Insomnia
Na raiz do projeto, está disponível o arquivo `insomnia_collection.json`. Importe este arquivo no seu cliente Insomnia para ter acesso a todas as requisições já pré-configuradas.

---

### 📝 Exemplos de Requisições

**1. Criar um Pedido (`POST /pedidos`):**
```json
{
  "itens": [
    {
      "produtoID": 1,
      "quantidade": 2
    }
  ],
  "canalPedido": "APP",
  "ClienteId": 1,
  "usarPontos": false,
  "cupom": "SAOJOAO20"
}

```


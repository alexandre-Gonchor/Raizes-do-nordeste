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

🗄️ Como criar o banco de dados no PostgreSQL

Para que a aplicação funcione, o banco de dados deve existir antes do primeiro "Run". Escolha uma das opções abaixo:
Opção 1: Via Linha de Comando (psql ou Terminal)

Abra o terminal do seu PostgreSQL e execute o comando abaixo:
SQL

CREATE DATABASE raizes_nordeste_db;

Opção 2: Via Interface Gráfica (pgAdmin ou DBeaver)

    Conecte-se ao seu servidor local do PostgreSQL.

    Clique com o botão direito em Databases (Bancos de Dados).

    Selecione Create > Database....

    No campo "Database", digite exatamente: raizes_nordeste_db.

    Clique em Save (Salvar).

    Importante: Após criar o banco, não é necessário criar tabelas. O Hibernate (JPA) lerá as classes Java e gerará toda a estrutura de tabelas, chaves primárias e relacionamentos automaticamente assim que você iniciar a API pela primeira vez.


## 🧪 Testando a API

### Opção 1: Swagger UI (Recomendado)
Acesse **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**.

1. Crie um usuário na rota `POST /auth/register`.
2. Faça login em `POST /auth/login` e copie o token gerado.
3. Clique no botão **Authorize** (Cadeado no topo da página), cole o token e teste as rotas protegidas.

### Opção 2: Insomnia
Na raiz do projeto, está disponível o arquivo `insomnia_collection.json`. Importe este arquivo no seu cliente Insomnia para ter acesso a todas as requisições já pré-configuradas.

---


---

## 🔄 Fluxos Principais Implementados 

O projeto contempla a implementação completa "End-to-End" dos dois principais fluxos operacionais exigidos, garantindo que o ciclo de vida dos dados seja fechado e testável.

### 🛒 Fluxo A: Pedido → Pagamento (Mock) → Atualização de Status
Este fluxo simula a jornada completa do cliente no Totem de autoatendimento, desde a escolha do produto até a entrega no balcão.

1. **Criar Pedido (`POST /pedidos`):** * Valida a existência dos itens e se há estoque disponível.
    * Aplica regras de negócio matemáticas (validação de Cupons Promocionais e uso de Pontos de Fidelidade).
    * Realiza a baixa (Saída) imediata no estoque logístico da unidade.
2. **Registro de Pagamento Mock (`POST /pedidos/{id}/pagamento`):** * Simula a comunicação com um Gateway de Pagamento (ex: Stone/Cielo).
    * Possui uma lógica randômica para aprovar ou recusar a transação, alterando o status financeiro do pedido.
3. **Atualização de Status Logístico (`PUT /pedidos/{id}/status`):** * Permite que o gerente da filial avance a esteira de produção (de `PAGO` para `EM_PREPARO` e, finalmente, `ENTREGUE`).

### 📦 Fluxo B: Estoque Distribuído por Unidade (Multi-Tenant)
Este fluxo garante o isolamento físico e lógico das mercadorias entre a Matriz e as Filiais.

1. **Cadastrar/Consultar Produto (`GET /cardapio/vitrine/{unidadeId}`):** * Realiza uma busca inteligente (Join) que só exibe para o cliente os produtos que possuem saldo positivo na filial em que ele se encontra.
2. **Movimentar Estoque (`POST /estoque/entrada`):** * Permite o reabastecimento logístico. O gerente insere as mercadorias que chegaram do fornecedor diretamente no saldo da sua unidade correspondente.
3. **Consultar Saldo e Auditoria (`GET /relatorios/estoque/{unidadeId}`):** * Gera um relatório completo de todas as movimentações de Entrada (reabastecimento) e Saída (vendas) ocorridas na unidade, garantindo rastreabilidade (Auditoria).

---



## 📡 Endpoints da API

A documentação interativa e completa está disponível via **Swagger UI** acessando `http://localhost:8080/swagger-ui.html` enquanto a aplicação estiver rodando.

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/auth/register` | Cadastra um novo usuário/cliente no sistema (com aceite LGPD). | ❌ |
| `POST` | `/auth/login` | Valida as credenciais e retorna o Token JWT de acesso. | ❌ |
| `POST` | `/unidades` | Cadastra uma nova unidade física (Matriz ou Filiais). | ✅ |
| `GET` | `/unidades` | Lista todas as unidades cadastradas no sistema. | ✅ |
| `GET` | `/unidades/{id}` | Retorna os detalhes de uma unidade específica. | ✅ |
| `GET` | `/cardapio/vitrine/{unidadeId}` | Lista produtos disponíveis (estoque > 0) para os clientes. | ❌ |
| `POST` | `/estoque/entrada` | Registra administrativamente o reabastecimento de produtos. | ✅ |
| `POST` | `/promocoes` | Cria uma nova campanha/cupom de desconto no sistema. | ✅ |
| `POST` | `/pedidos` | Cria um pedido, aplica descontos matemáticos e dá baixa no estoque. | ✅ |
| `POST` | `/pedidos/{id}/pagamento` | Processa o Gateway de Pagamento Simulado (Mock). | ✅ |
| `GET` | `/pedidos` | Lista o histórico de pedidos (suporta filtros, ex: `?canal=APP`). | ✅ |
| `GET` | `/pedidos/{id}` | Consulta os detalhes e itens de um pedido específico. | ✅ |
| `PUT` | `/pedidos/{id}/status` | Atualiza o status logístico do pedido (ex: `EM_PREPARO`). | ✅ |
| `GET` | `/relatorios/estoque/{unidadeId}`| Retorna a auditoria de movimentações logísticas da filial. | ✅ |
| `GET` | `/relatorios/vendas/{unidadeId}` | Retorna as métricas de faturamento mensal da filial. | ✅ |

> **Nota sobre Autenticação (✅):** Endpoints marcados como protegidos exigem o envio do token no Header da requisição: `Authorization: Bearer <seu_token_aqui>`.


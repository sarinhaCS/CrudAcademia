# Sistema de Gestão de Academia

Sistema desktop em Java para gerenciar alunos, instrutores, planos, modalidades e matrículas de uma academia.
Projeto da disciplina de **Programação Orientada a Objetos** — Universidade Católica de Brasília.

A rotina principal do sistema é a **matrícula**: ela relaciona um aluno a um plano, uma modalidade e, opcionalmente, um instrutor.

## Tecnologias

- Java (JDK 17 ou superior)
- Java Swing (JFrame Form, criado no designer do NetBeans)
- MySQL 8 / InnoDB
- JDBC (MySQL Connector/J, já incluído na pasta `lib/`)
- Padrão arquitetural MVC + DAO
- NetBeans (projeto Ant)

## Funcionalidades

- CRUD de **Aluno**
- CRUD de **Instrutor**
- CRUD de **Plano**
- CRUD de **Modalidade**
- CRUD de **Matrícula** (CRUD principal)
- Consulta com `JOIN` (matrícula + aluno + plano + modalidade + instrutor)
- Relatório de matrículas
- Menu principal

## Estrutura do projeto

```
CrudAcademia/
├── src/
│   ├── app/          Main (ponto de entrada)
│   ├── model/        Classes de domínio (Aluno, Instrutor, Plano, ...)
│   ├── view/         Telas Swing (JFrame Form)
│   ├── controller/   Validações e ligação entre View e DAO
│   └── dao/          Acesso ao banco (JDBC) e ConexaoMySQL
├── sql/              Scripts do banco (criação, carga e consultas)
├── lib/              Driver MySQL Connector/J
└── nbproject/        Configuração do projeto NetBeans
```

### Como o MVC funciona aqui

```
View (tela)  →  Controller (valida)  →  DAO (SQL/JDBC)  →  MySQL
     ↑                  |
     └──────── resultado ┘
```

- **Model:** classes simples com atributos, construtores, getters e setters.
- **View:** apenas interface gráfica. Não tem regra de negócio nem SQL.
- **Controller:** valida os campos, chama o DAO e informa a View do resultado.
- **DAO:** único lugar com SQL. Herda de `ConexaoMySQL`, que abre a conexão com o banco.

## Banco de dados

O banco se chama `bd_academia` e tem 5 tabelas: **Aluno**, **Instrutor**, **Plano**, **Modalidade** e **Matrícula**.

## Como executar

### 1. Pré-requisitos

- JDK 17 ou superior
- MySQL 8 em execução
- NetBeans

### 2. Criar o banco

Execute, nesta ordem, no MySQL Workbench (ou outro cliente):

1. `sql/academia_DDL.sql` — cria o banco e as tabelas
2. `sql/academia_carga.sql` — insere os dados de teste

### 3. Configurar a conexão

Abra `src/dao/ConexaoMySQL.java` e ajuste, **apenas no seu computador**:

- `user`: seu usuário do MySQL (padrão `root`)
- `senha`: sua senha do MySQL

> ⚠️ **Não faça commit da sua senha.** Antes de enviar alterações, confira com `git status` e, se `ConexaoMySQL.java` aparecer modificado, desfaça com `git restore src/dao/ConexaoMySQL.java`.

### 4. Abrir e rodar

1. No NetBeans: **File → Open Project** e selecione a pasta do projeto.
2. Se a biblioteca do MySQL aparecer com aviso, clique com o botão direito no projeto → **Resolve Reference Problems** e aponte para `lib/mysql-connector-j-26.10.0.jar`.
3. Execute com **Run** (F6). A classe principal é `app.Main`.

## Validações implementadas

| Cadastro | Regras |
|---|---|
| Aluno | Nome obrigatório; CPF com 11 dígitos e único; data `dd/mm/aaaa`, válida e não futura; telefone opcional |
| Instrutor | Nome obrigatório; CREF obrigatório (máx. 20 caracteres) e único |
| Plano | Nome obrigatório; valor maior que zero com até 2 casas decimais; duração entre 1 e 120 meses |

## Conceitos de POO aplicados

- **Encapsulamento:** atributos privados com getters e setters nas classes do model.
- **Herança:** os DAOs herdam de `ConexaoMySQL`.
- **Abstração e responsabilidade única:** cada camada (Model, View, Controller, DAO) tem uma função definida.
- **Polimorfismo:** `toString()` sobrescrito nas classes do model, usado para exibir nomes em listas.

## Equipe

Sarah Cruz Serpa
Maria Eduarda Gomes da Silva
Victor Gabriel Andrade
Vitor Lustosa Leão Pinheiro

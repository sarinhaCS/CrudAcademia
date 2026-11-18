# Sistema de Gestão de Academia

Sistema desktop em Java para gerenciar alunos, instrutores, planos, modalidades e matrículas de uma academia, com relatório de matrículas e faturamento.

Projeto da disciplina de **Programação Orientada a Objetos** — Universidade Católica de Brasília.

A rotina principal do sistema é a **matrícula**: ela relaciona um aluno a um plano, a uma modalidade e, opcionalmente, a um instrutor.

## Sumário

- [Tecnologias](#tecnologias)
- [Funcionalidades](#funcionalidades)
- [Relatório de matrículas e faturamento](#relatório-de-matrículas-e-faturamento)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Banco de dados](#banco-de-dados)
- [Como executar](#como-executar)
- [Validações implementadas](#validações-implementadas)
- [Conceitos de POO aplicados](#conceitos-de-poo-aplicados)
- [Equipe](#equipe)

## Tecnologias

- Java (JDK 17 ou superior)
- Java Swing (JFrame Form, criado no designer do NetBeans)
- MySQL 8 / InnoDB
- JDBC (MySQL Connector/J, já incluído na pasta `lib/`)
- Padrão arquitetural MVC + DAO
- NetBeans (projeto Ant)

## Funcionalidades

- **Menu principal** com acesso a todas as telas
- CRUD de **Aluno**, **Instrutor**, **Plano** e **Modalidade**, com busca por nome
- CRUD de **Matrícula** (rotina principal), com **instrutor opcional**
- Consulta com `JOIN` (matrícula + aluno + plano + modalidade + instrutor), mostrando nomes em vez de códigos
- **Relatório de matrículas e faturamento** com filtros por período e por situação

## Relatório de matrículas e faturamento

A tela de relatório lista todas as matrículas com os **nomes** do aluno, do plano, da modalidade e do instrutor (ou "Sem instrutor"), além da data, da situação e do valor do plano.

**Filtros**

- Período: data inicial e final da matrícula, no formato `dd/mm/aaaa` (campo vazio = sem limite)
- Situação: Todas, Ativa, Trancada ou Cancelada

**Faturamento** (calculado sobre as matrículas que aparecem no filtro)

| Indicador | Como é calculado |
|---|---|
| Faturamento | Soma do valor do plano das matrículas **Ativas** |
| Receita mensal estimada | Para cada matrícula ativa, valor do plano ÷ duração em meses; depois soma |
| Trancadas | Quantidade e valor somado das matrículas trancadas |
| Canceladas | Quantidade e valor somado das matrículas canceladas |
| Por plano / por modalidade | Faturamento e quantidade de matrículas ativas agrupados |

Os cálculos ficam na classe `model/ResumoFaturamento`, que percorre a lista de matrículas uma única vez. Os valores monetários usam `BigDecimal`.

## Estrutura do projeto

```
academia/
├── src/
│   ├── app/          Main (ponto de entrada: abre o menu principal)
│   ├── model/        Classes de domínio (Aluno, Instrutor, Plano, Matricula, ResumoFaturamento, ...)
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
- **Controller:** valida os campos, chama o DAO e informa a View do resultado ou do erro.
- **DAO:** único lugar com SQL. Herda de `ConexaoMySQL`, que abre a conexão com o banco.

## Banco de dados

O banco se chama `bd_academia` e tem 5 tabelas: `aluno`, `instrutor`, `plano`, `modalidade` e `matricula`.

| Relacionamento | Cardinalidade | Observação |
|---|---|---|
| aluno → matrícula | 1:N | Obrigatório |
| plano → matrícula | 1:N | Obrigatório; exclusão restrita (`ON DELETE RESTRICT`) |
| modalidade → matrícula | 1:N | Obrigatório |
| instrutor → matrícula | 1:N | **Opcional** (`id_tutor` aceita `NULL`) |

A tabela `matricula` é a tabela central e liga as demais por chaves estrangeiras. A situação da matrícula só aceita `Ativa`, `Trancada` ou `Cancelada` (restrição `CHECK`).

Scripts na pasta `sql/`:

| Arquivo | Conteúdo |
|---|---|
| `academia_DDL.sql` | Cria o banco, as tabelas, as chaves primárias e estrangeiras |
| `academia_carga.sql` | Dados de demonstração |
| `academia_consultas.sql` | Consultas de exemplo (inclui o `JOIN`) |

## Como executar

### 1. Pré-requisitos

- JDK 17 ou superior
- MySQL 8 em execução
- NetBeans

### 2. Criar o banco

Execute, nesta ordem, no MySQL Workbench (ou outro cliente):

1. `sql/academia_DDL.sql` — cria o banco e as tabelas
2. `sql/academia_carga.sql` — insere os dados de teste

> O `academia_DDL.sql` começa apagando o banco (`DROP DATABASE IF EXISTS`). Se você rodá-lo de novo, execute a carga outra vez.

### 3. Configurar a conexão

Abra `src/dao/ConexaoMySQL.java` e ajuste, **apenas no seu computador**:

- `user`: seu usuário do MySQL (padrão `root`)
- `senha`: sua senha do MySQL

> ⚠️ **Não faça commit da sua senha.** Antes de enviar alterações, confira com `git status` e, se `ConexaoMySQL.java` aparecer modificado, desfaça com `git restore src/dao/ConexaoMySQL.java`.

### 4. Abrir e rodar

1. No NetBeans: **File → Open Project** e selecione a pasta do projeto.
2. Se a biblioteca do MySQL aparecer com aviso, clique com o botão direito no projeto → **Resolve Reference Problems** e aponte para `lib/mysql-connector-j-26.10.0.jar`.
3. Execute **Clean and Build** (Shift+F11) e depois **Run** (F6). A classe principal é `app.Main`, que abre o menu principal.


## Validações implementadas

| Cadastro | Regras |
|---|---|
| Aluno | Nome obrigatório; CPF com 11 dígitos e único; data `dd/mm/aaaa`, válida e não futura; telefone opcional |
| Instrutor | Nome obrigatório; CREF obrigatório (máx. 20 caracteres) e único |
| Plano | Nome obrigatório e único; valor maior que zero com até 2 casas decimais; duração entre 1 e 120 meses |
| Modalidade | Nome obrigatório |
| Matrícula | Aluno, plano e modalidade obrigatórios; instrutor opcional |
| Relatório | Datas válidas no formato `dd/mm/aaaa`; data inicial não pode ser posterior à final |

Erros do banco são traduzidos pelo controller em mensagens amigáveis (por exemplo, CPF repetido ou plano em uso por matrículas).

## Conceitos de POO aplicados

- **Encapsulamento:** atributos privados com getters e setters nas classes do model.
- **Herança:** os DAOs herdam de `ConexaoMySQL`.
- **Abstração e responsabilidade única:** cada camada (Model, View, Controller, DAO) tem uma função definida.
- **Polimorfismo:** `toString()` sobrescrito nas classes do model, usado para exibir nomes em listas; coleções declaradas como `List<...>` e criadas como `ArrayList`.

## Equipe

- Maria Eduarda Gomes da Silva
- Sarah Cruz Serpa
- Victor Gabriel Andrade
- Vitor Lustosa Leão Pinheiro

-- ======================================================
-- Projeto : Sistema de Gestão de Academia
-- Objetivo : Criação da estrutura do banco (MySQL / InnoDB)
-- Arquivo : academia_DDL.sql
-- Descrição: Script da criação da estrutura do banco da academia
-- Disciplina: Programação Orientada a Objeto
-- ======================================================

DROP DATABASE IF EXISTS bd_academia;
CREATE DATABASE bd_academia CHARACTER SET utf8mb4;
USE bd_academia;

CREATE TABLE aluno(
	id_aluno INT AUTO_INCREMENT,
    nome VARCHAR(200) NOT NULL,
    cpf CHAR(11) NOT NULL,
    telefone VARCHAR(20), 
    data_nascimento DATE NOT NULL,
    
    CONSTRAINT pk_aluno PRIMARY KEY(id_aluno),
    CONSTRAINT uq_cpf UNIQUE(cpf)
)ENGINE = InnoDB;

CREATE TABLE instrutor(
	id_tutor INT AUTO_INCREMENT,
    nome VARCHAR(200) NOT NULL,
    cref VARCHAR(20) NOT NULL,
    
    CONSTRAINT pk_instrutor PRIMARY KEY(id_tutor),
    CONSTRAINT uq_cref UNIQUE(cref)
)ENGINE = InnoDB;

CREATE TABLE modalidade(
	id_mod INT AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    
    CONSTRAINT pk_modalidade PRIMARY KEY(id_mod)
)ENGINE = InnoDB;

CREATE TABLE plano(
	id_plano INT AUTO_INCREMENT,
    nome_plano VARCHAR(50) NOT NULL,
    valor DECIMAL(8,2) NOT NULL,
    duracao_meses INT NOT NULL,
    
    CONSTRAINT pk_plano PRIMARY KEY(id_plano)
)ENGINE = InnoDB;

CREATE TABLE matricula(
	id_mat INT AUTO_INCREMENT,
    id_aluno INT NOT NULL,
    id_plano INT NOT NULL,
    id_mod INT NOT NULL,
    id_tutor INT,
    data_matricula DATE DEFAULT (CURRENT_DATE),
    status_mat VARCHAR(50) NOT NULL DEFAULT 'Ativa',
    
    CONSTRAINT pk_matricula PRIMARY KEY(id_mat),
    CONSTRAINT ck_status CHECK (status_mat IN 
		('Ativa', 'Trancada', 'Cancelada')),
	CONSTRAINT fk_aluno FOREIGN KEY(id_aluno) REFERENCES aluno(id_aluno),
    CONSTRAINT fk_plano FOREIGN KEY(id_plano) REFERENCES plano(id_plano)
		ON DELETE RESTRICT,
    CONSTRAINT fk_modalidade FOREIGN KEY(id_mod) REFERENCES modalidade(id_mod),
    CONSTRAINT fk_instrutor FOREIGN KEY(id_tutor) REFERENCES instrutor(id_tutor)
)ENGINE = InnoDB;

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
    situacao VARCHAR(50) NOT NULL DEFAULT 'Ativa',
    
    CONSTRAINT pk_matricula PRIMARY KEY(id_mat),
    CONSTRAINT ck_situacao CHECK (situacao IN 
		('Ativa', 'Trancada', 'Cancelada')),
	CONSTRAINT fk_aluno FOREIGN KEY(id_aluno) REFERENCES aluno(id_aluno),
    CONSTRAINT fk_plano FOREIGN KEY(id_plano) REFERENCES plano(id_plano)
		ON DELETE RESTRICT,
    CONSTRAINT fk_modalidade FOREIGN KEY(id_mod) REFERENCES modalidade(id_mod),
    CONSTRAINT fk_instrutor FOREIGN KEY(id_tutor) REFERENCES instrutor(id_tutor)
)ENGINE = InnoDB;

INSERT INTO aluno (nome, cpf, telefone, data_nascimento) VALUES 
	('João Lima','22222222222','61988880000', '1985-03-12'),
    ('Rebheca Cruz','12021001222','77984000040', '2010-09-06');
    
INSERT INTO instrutor (nome, cref) VALUES ('Ana Costa','CREF-DF 5678');

INSERT INTO modalidade (nome) VALUES 
	('Musculação'), ('Spinning'), ('Pilates'), ('Dança'), ('Boxe');
    
INSERT INTO plano (nome_plano, valor, duracao_meses) VALUES ('Trimestral', 270.00, 3);

INSERT INTO matricula (id_aluno, id_plano, id_mod, id_tutor)
  VALUES (1, 1, 1, 1), (2, 1, 1, null);
  
SELECT m.id_mat, a.nome AS aluno, p.nome_plano AS plano, p.valor,
       mo.nome AS modalidade, IFNULL(i.nome, 'Sem instrutor') AS instrutor,
       m.data_matricula, m.situacao
FROM matricula AS m
INNER JOIN aluno AS a      ON m.id_aluno = a.id_aluno
INNER JOIN plano AS p      ON m.id_plano = p.id_plano
INNER JOIN modalidade AS mo ON m.id_mod = mo.id_mod
LEFT JOIN instrutor AS i  ON m.id_tutor = i.id_tutor;

SELECT * FROM aluno;
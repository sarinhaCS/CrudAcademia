-- ======================================================
-- Projeto : Sistema de Gestão de Academia
-- Objetivo : Carga de dados de teste (MySQL / InnoDB)
-- Arquivo : academia_carga.sql
-- Descrição: Script de carga de dados de teste para o banco da academia
-- Disciplina: Programação Orientada a Objeto
-- ======================================================

-- VOLUME : 20 alunos | 8 instrutores | 5 modalidades | 4 planos | 24 matrículas

-- =========================
-- PLANOS
-- =========================
INSERT INTO plano (nome_plano, valor, duracao_meses) VALUES
('Mensal',     119.90,  1),
('Trimestral', 319.90,  3),
('Semestral',  599.90,  6),
('Anual',     1099.90, 12);

-- =========================
-- MODALIDADES
-- =========================
INSERT INTO modalidade (nome) VALUES
('Musculação'),
('Crossfit'),
('Zumba'),
('Muay Thai'),
('Pilates');

-- =========================
-- INSTRUTORES
-- =========================
INSERT INTO instrutor (nome, cref) VALUES
('Carlos Eduardo Menezes', '012345-G/DF'),
('Fernanda Lima Barbosa',  '023456-G/DF'),
('Rafael Augusto Teixeira','034567-G/DF'),
('Juliana Prado Nogueira', '045678-G/DF'),
('Marcelo Henrique Dias',  '056789-G/DF'),
('Patrícia Souza Andrade', '067890-G/DF'),
('Thiago Ramos Oliveira',  '078901-G/DF'),
('Camila Ferreira Rocha',  '089012-G/DF');

-- =========================
-- ALUNOS
-- =========================
INSERT INTO aluno (nome, cpf, telefone, data_nascimento) VALUES
('Ana Beatriz Santos',      '11122233301', '(61) 99111-0001', '1998-03-12'),
('Bruno Henrique Costa',    '11122233302', '(61) 99111-0002', '2006-07-25'),
('Carla Mendes Ribeiro',    '11122233303', '(61) 99111-0003', '2001-11-04'),
('Daniel Alves Pereira',    '11122233304', '(61) 99111-0004', '1990-01-30'),
('Eduarda Martins Lopes',   '11122233305', '(62) 99111-0005', '2003-05-18'),
('Felipe Gomes Carvalho',   '11122233306', '(61) 99111-0006', '1988-09-09'),
('Gabriela Nunes Araújo',   '11122233307', '(61) 99111-0007', '2008-12-21'),
('Henrique Barros Silva',   '11122233308', '(16) 99111-0008', '1993-04-02'),
('Isabela Cardoso Freitas', '11122233309', '(61) 99111-0009', '2000-08-14'),
('João Pedro Moreira',      '11122233310', '(61) 99111-0010', '1997-02-27'),
('Karina Duarte Campos',    '11122233311', NULL,              '1985-10-06'),
('Lucas Vieira Machado',    '11122233312', '(62) 99111-0012', '2002-06-19'),
('Mariana Rezende Pinto',   '11122233313', '(62) 99111-0013', '1994-03-08'),
('Nicolas Batista Souza',   '11122233314', '(62) 99111-0014', '1991-11-23'),
('Olívia Castro Monteiro',  '11122233315', '(62) 99111-0015', '1996-01-15'),
('Paulo Roberto Farias',    '11122233316', '(61) 99111-0016', '1983-07-31'),
('Quésia Lima Tavares',     '11122233317', NULL,              '2004-09-03'),
('Rodrigo Fonseca Lima',    '11122233318', '(61) 99111-0018', '2000-05-11'),
('Sabrina Azevedo Cunha',   '11122233319', '(77) 99111-0019', '1992-12-29'),
('Vinícius Teles Borges',   '11122233320', '(61) 99111-0020', '1999-04-17');

-- =========================
-- MATRÍCULAS
-- Colunas: id_aluno, id_plano, id_mod, id_tutor, data_matricula, situacao
-- Planos:      1=Mensal, 2=Trimestral, 3=Semestral, 4=Anual
-- Modalidades: 1=Musculação, 2=Crossfit, 3=Zumba, 4=Muay Thai, 5=Pilates
-- =========================
INSERT INTO matricula (id_aluno, id_plano, id_mod, id_tutor, data_matricula, status_mat) VALUES
-- Uma matrícula para cada um dos 20 alunos
( 1, 4, 1, 1,    '2026-01-10', 'Ativa'),
( 2, 3, 2, 2,    '2026-02-05', 'Ativa'),
( 3, 2, 5, 4,    '2026-03-15', 'Ativa'),
( 4, 1, 4, 5,    '2026-04-02', 'Ativa'),
( 5, 2, 3, NULL,    '2026-05-20', 'Ativa'),
( 6, 4, 1, 7,    '2026-01-22', 'Ativa'),
( 7, 3, 5, 6,    '2026-02-18', 'Trancada'),
( 8, 1, 2, 2,    '2026-06-01', 'Cancelada'),
( 9, 2, 3, 8,    '2026-07-07', 'Ativa'),
(10, 3, 4, 5,    '2026-03-03', 'Ativa'),
(11, 1, 1, NULL, '2026-08-12', 'Ativa'),      -- sem instrutor
(12, 2, 2, 7,    '2026-09-01', 'Ativa'),
(13, 4, 5, 4,    '2026-01-30', 'Ativa'),
(14, 1, 1, NULL, '2026-10-05', 'Ativa'),      -- sem instrutor
(15, 3, 3, NULL,    '2026-04-14', 'Trancada'),
(16, 2, 4, 5,    '2026-05-09', 'Cancelada'),
(17, 1, 5, 6,    '2026-10-20', 'Ativa'),
(18, 4, 2, 2,    '2026-02-11', 'Ativa'),
(19, 3, 1, 1,    '2026-06-25', 'Ativa'),
(20, 1, 3, NULL, '2026-11-02', 'Ativa'),      -- sem instrutor

-- Alunos com mais de uma matrícula
( 1, 4, 3, NULL,    '2026-03-01', 'Ativa'),      -- aluno 1 também faz Zumba
( 4, 1, 1, 1,    '2026-09-10', 'Ativa'),      -- aluno 4 também faz Musculação
( 8, 2, 1, 7,    '2026-09-15', 'Ativa'),      -- aluno 8 voltou, em outra modalidade
(13, 2, 1, NULL, '2026-05-04', 'Trancada');   -- aluno 13 também em Musculação, sem instrutor

-- ======================================================
-- Projeto : Sistema de Gestão de Academia
-- Objetivo : Demonstração dos registros do banco (MySQL / InnoDB)
-- Arquivo : academia_consultas.sql
-- Descrição: Script de consulta do banco da academia
-- Disciplina: Programação Orientada a Objeto
-- ======================================================

-- Matrículas com nome do aluno, plano, modalidade e instrutor
SELECT m.id_mat, a.nome AS aluno, p.nome_plano, md.nome AS modalidade,
       IFNULL(i.nome, 'Sem instrutor') AS instrutor, m.data_matricula, m.status_mat
FROM matricula AS m
JOIN aluno AS a       	 ON a.id_aluno  = m.id_aluno
JOIN plano AS p       	 ON p.id_plano  = m.id_plano
JOIN modalidade AS md 	 ON md.id_mod   = m.id_mod
LEFT JOIN instrutor AS i ON i.id_tutor = m.id_tutor   -- LEFT JOIN por causa do instrutor opcional
ORDER BY m.id_mat;

-- Quantidade de matrículas por situação
SELECT status_mat, COUNT(*) AS total
FROM matricula
GROUP BY status_mat;

SELECT * FROM aluno;
SELECT * FROM matricula;

DROP DATABASE cabeleireiro;
CREATE DATABASE cabeleireiro;
USE cabeleireiro;

CREATE TABLE tbl_login(
	id_login INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(100) NOT NULL,
    senha VARCHAR(100) NOT NULL,
    acesso_adm BOOLEAN NOT NULL DEFAULT FALSE,
    estatus CHAR NOT NULL DEFAULT 'A'
);

CREATE TABLE tbl_funcionario(
	id_funcionario INT PRIMARY KEY AUTO_INCREMENT,
    nome_funcionario VARCHAR(100) NOT NULL,
    cpf_funcionario VARCHAR(20) NOT NULL,
    email_funcionario VARCHAR(100) DEFAULT NULL,
    telefone_funcionario VARCHAR(45) NOT NULL,
    FK_id_login INT NOT NULL,
    estatus CHAR NOT NULL DEFAULT 'A',
    
    CONSTRAINT FK_id_login1 FOREIGN KEY(FK_id_login) REFERENCES tbl_login(id_login)
);

CREATE TABLE tbl_servico(
	id_servico INT PRIMARY KEY AUTO_INCREMENT,
    nome_servico VARCHAR(100) NOT NULL,
    preco_servico DECIMAL(7,2),
    descricao_servico VARCHAR(500),
    estatus CHAR NOT NULL DEFAULT 'A'
);

CREATE TABLE tbl_cliente(
	id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nome_cliente VARCHAR(100) NOT NULL,
    cpf_cliente VARCHAR(20) NOT NULL,
    dt_nasc_cliente DATE NOT NULL,
    email_cliente VARCHAR(100) DEFAULT NULL,
    telefone_cliente VARCHAR(20) NOT NULL,
    FK_id_login INT NOT NULL,
    estatus CHAR NOT NULL DEFAULT 'A',
    
    CONSTRAINT FK_id_login2 FOREIGN KEY(FK_id_login) REFERENCES tbl_login(id_login)
);

CREATE TABLE tbl_produto(
	id_produto INT PRIMARY KEY AUTO_INCREMENT,
    nome_produto VARCHAR(100) NOT NULL,
    valor_produto DECIMAL(7, 2) NOT NULL,
    validade_produto DATE DEFAULT NULL,
    estatus CHAR NOT NULL DEFAULT 'A'
);

CREATE TABLE tbl_agendamento(
	id_agendamento INT PRIMARY KEY AUTO_INCREMENT,
    data_agendamento DATE NOT NULL,
    horario_agendamento TIME NOT NULL,
    FK_id_cliente INT NOT NULL,
    estatus CHAR NOT NULL DEFAULT 'A',
    
    CONSTRAINT FK_id_cliente FOREIGN KEY(FK_id_cliente) REFERENCES tbl_cliente(id_cliente)
);

CREATE TABLE tbl_venda(
	id_venda INT PRIMARY KEY AUTO_INCREMENT,
    total_venda DECIMAL(7, 2) NOT NULL,
    FK_id_agendamento INT NOT NULL,
    estatus CHAR NOT NULL DEFAULT 'A',
    
    CONSTRAINT FK_id_agendamento1 FOREIGN KEY(FK_id_agendamento) REFERENCES tbl_agendamento(id_agendamento)
);

################ CONEXÕES ###############
CREATE TABLE funcionario_servico(    
    FK_id_servico INT NOT NULL,
    FK_id_funcionario INT NOT NULL,
    
    PRIMARY KEY(FK_id_servico, FK_id_funcionario),
    
    CONSTRAINT FK_id_servico FOREIGN KEY(FK_id_servico) REFERENCES tbl_servico(id_servico),
    CONSTRAINT FK_id_funcionario FOREIGN KEY(FK_id_funcionario) REFERENCES tbl_funcionario(id_funcionario)
);

CREATE TABLE servico_agendamento(
	FK_id_servico INT NOT NULL,
    FK_id_agendamento INT NOT NULL,
    
    PRIMARY KEY(FK_id_servico, FK_id_agendamento),
    
    CONSTRAINT FK_id_servico2 FOREIGN KEY(FK_id_servico) REFERENCES tbl_servico(id_servico),
    CONSTRAINT FK_id_agendamento2 FOREIGN KEY(FK_id_agendamento) REFERENCES tbl_agendamento(id_agendamento)
);

CREATE TABLE venda_produto(
	FK_id_venda INT NOT NULL,
	FK_id_produto INT NOT NULL,
    
    PRIMARY KEY(FK_id_venda, FK_id_produto),
    
    CONSTRAINT FK_id_venda FOREIGN KEY(FK_id_venda) REFERENCES tbl_venda(id_venda),
    CONSTRAINT FK_id_produto FOREIGN KEY(FK_id_produto) REFERENCES tbl_produto(id_produto)
);

################################################################################################################################
#INSERTS - Serviço
INSERT INTO tbl_servico(nome_servico, preco_servico, descricao_servico) VALUES('Corte masculino', '20', 'Corte de cabelo masculino.');
INSERT INTO tbl_servico(nome_servico, preco_servico, descricao_servico) VALUES('Corte feminino', '20', 'Corte de cabelo feminino.');
INSERT INTO tbl_servico(nome_servico, preco_servico, descricao_servico) VALUES('Progressiva', '30', 'Progressiva no cabelo.');
INSERT INTO tbl_servico(nome_servico, preco_servico, descricao_servico) VALUES('Alisamento', '25', 'Alisamento de cabelo.');
INSERT INTO tbl_servico(nome_servico, preco_servico, descricao_servico) VALUES('Manicure', '25', 'Tratamento nas mãos.');
INSERT INTO tbl_servico(nome_servico, preco_servico, descricao_servico) VALUES('Pedicure', '25', 'Tratamento nos pés.');

SELECT * FROM tbl_servico;

#INSERTS - Login
INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('dani', 'dani123', false);
INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('biel', 'biel123', false);
INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('drica', 'drica123', false);
INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('lana', 'lana123', false);

INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('doug', 'doug123', false);
INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('bruno', 'bruno123', false);
INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('henri', 'henri123', false);
INSERT INTO tbl_login (usuario, senha, acesso_adm) VALUES ('giih', 'giih123', false);

select * from tbl_login;

#INSERTS - Cliente
INSERT INTO tbl_cliente(nome_cliente, cpf_cliente, dt_nasc_cliente, email_cliente, telefone_cliente, FK_id_login) VALUES('Danielle', '558.559.123-11', '2001-02-02', '', '4002-8922', '1');
INSERT INTO tbl_cliente(nome_cliente, cpf_cliente, dt_nasc_cliente, email_cliente, telefone_cliente, FK_id_login) VALUES('Gabriel', '558.559.123-30', '2000-02-02', '', '4002-8822', '2');
INSERT INTO tbl_cliente(nome_cliente, cpf_cliente, dt_nasc_cliente, email_cliente, telefone_cliente, FK_id_login) VALUES('Adriana', '558.559.123-15', '1999-02-20', '', '4002-8926', '3');
INSERT INTO tbl_cliente(nome_cliente, cpf_cliente, dt_nasc_cliente, email_cliente, telefone_cliente, FK_id_login) VALUES('Elaine', '558.559.123-16', '2000-05-27', '', '4002-8927', '4');

SELECT * FROM tbl_cliente;

#INSERTS - Funcionário
INSERT INTO tbl_funcionario(nome_funcionario, cpf_funcionario, email_funcionario, telefone_funcionario, FK_id_login) VALUES('Douglas', '558.559.123-10', 'douglas@email.com', '4002-8922', '5');
INSERT INTO tbl_funcionario(nome_funcionario, cpf_funcionario, email_funcionario, telefone_funcionario, FK_id_login) VALUES('Bruno', '558.559.123-12', 'bruno@email.com', '4002-8923', '6');
INSERT INTO tbl_funcionario(nome_funcionario, cpf_funcionario, email_funcionario, telefone_funcionario, FK_id_login) VALUES('Henrique', '558.559.123-13', '', '4002-8924', '7');
INSERT INTO tbl_funcionario(nome_funcionario, cpf_funcionario, email_funcionario, telefone_funcionario, FK_id_login) VALUES('Giovanna', '558.559.123-14', 'giovanna@email.com', '4002-8925', '8');

SELECT * FROM tbl_funcionario;

#INSERTS - Funcionário_Servico
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(1, 2);
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(1, 6);
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(2, 1);
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(2, 3);
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(2, 5);
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(3, 4);
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(4, 2);
INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) VALUES(4, 4);

SELECT * FROM funcionario_servico;

#INSERTS - Produto
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Gel', '10', '2021-02-03');
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Shampoo', '10', '2021-02-03');
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Condicionador', '10', '2021-02-03');
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Creme', '10', '2021-02-03');
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Mascara', '10', '2021-02-03');
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Esmalte', '10', '2021-02-03');
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Pente', '10', null);
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Escova', '10', null);
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Toalha', '10', null);
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Tesoura', '10', null);
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Gilete', '10', null);
INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES('Lamina', '10', null);

SELECT * FROM tbl_produto;

#INSERTS - Agendamento
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-07-25', '14:30', 1);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-07-25', '16:30', 2);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-07-26', '13:30', 2);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-07-26', '15:00', 3);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-07-26', '17:30', 4);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-07-28', '09:30', 1);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-08-01', '09:30', 1);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-08-01', '12:30', 3);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-08-01', '14:30', 4);
INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES ('2020-08-02', '09:30', 2);

SELECT * FROM tbl_agendamento;

#INSERTS - Servico_Agendamento
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (1, 1);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (1, 2);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (2, 3);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (3, 4);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (3, 5);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (3, 6);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (4, 1);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (4, 6);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (5, 6);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (6, 6);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (7, 6);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (8, 6);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (9, 6);
INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (10, 6);

SELECT * FROM servico_agendamento;

##########################################################################################################
-- Limpa as tabelas na ordem correta para evitar erros de chave estrangeira ao reiniciar
DELETE FROM historico_status_pedido;
DELETE FROM item_pedido;
DELETE FROM pedido;
DELETE FROM endereco_entrega;
DELETE FROM oculos;
DELETE FROM marca;
DELETE FROM cartao;
DELETE FROM endereco;
DELETE FROM telefone;
DELETE FROM cliente;
DELETE FROM funcionario;
DELETE FROM usuario_perfis;
DELETE FROM usuario;
DELETE FROM cidade;
DELETE FROM estado;


-- Estados
INSERT INTO estado (id, nome, sigla) VALUES(1, 'Tocantins', 'TO');
INSERT INTO estado (id, nome, sigla) VALUES(2, 'Goiás', 'GO');
INSERT INTO estado (id, nome, sigla) VALUES(3, 'São Paulo', 'SP');

-- Cidades
INSERT INTO cidade (id, nome, estado_id) VALUES(1, 'Palmas', 1);
INSERT INTO cidade (id, nome, estado_id) VALUES(2, 'Goiânia', 2);
INSERT INTO cidade (id, nome, estado_id) VALUES(3, 'São Paulo', 3);

-- Marcas
INSERT INTO marca (id, nome) VALUES(1, 'Ray-Ban');
INSERT INTO marca (id, nome) VALUES(2, 'Oakley');
INSERT INTO marca (id, nome) VALUES(3, 'Prada');

-- Oculos
INSERT INTO oculos (id, nome, preco, quantidadeEstoque, corArmacao, genero, modelo, id_marca) VALUES(1, 'Aviator Classic', 650.00, 50, 'DOURADO', 'UNISEX', 'AVIADOR', 1);
INSERT INTO oculos (id, nome, preco, quantidadeEstoque, corArmacao, genero, modelo, id_marca) VALUES(2, 'Holbrook', 800.00, 30, 'PRETO', 'MASCULINO', 'ESPORTIVO', 2);
INSERT INTO oculos (id, nome, preco, quantidadeEstoque, corArmacao, genero, modelo, id_marca) VALUES(3, 'Prada Runway', 1200.00, 15, 'PRETO', 'FEMININO', 'RETRO', 3);

-- Usuários
-- Senha para todos é "123" (o hash abaixo corresponde a "123" com seu HashService)
INSERT INTO usuario(id, nome, cpf, dataNascimento, email, senha) VALUES (1, 'Admin Joas', '11122233344', '1990-01-01', 'adm@email.com', 'l/d3I4h/tVO0S5SDBveYtZr4c4s2i3jL8KzW5c5yIA+2iR84vWMVv4A5T++8sPhx/aN2dYwM+rJpl0i4Jw3lVg==');
INSERT INTO usuario(id, nome, cpf, dataNascimento, email, senha) VALUES (2, 'Cliente Fyllipe', '55566677788', '2000-05-10', 'cliente@email.com', 'l/d3I4h/tVO0S5SDBveYtZr4c4s2i3jL8KzW5c5yIA+2iR84vWMVv4A5T++8sPhx/aN2dYwM+rJpl0i4Jw3lVg==');
INSERT INTO usuario(id, nome, cpf, dataNascimento, email, senha) VALUES (3, 'Funcionario Silva', '99988877766', '1995-03-20', 'employee@email.com', 'l/d3I4h/tVO0S5SDBveYtZr4c4s2i3jL8KzW5c5yIA+2iR84vWMVv4A5T++8sPhx/aN2dYwM+rJpl0i4Jw3lVg==');

-- Perfis dos Usuários (ADM=0, EMPLOYE=1, USER=2 - baseado no seu enum Perfil, ajuste se necessário)
INSERT INTO usuario_perfis(usuario_id, perfil) VALUES (1, 0); -- admin@email.com é ADM
INSERT INTO usuario_perfis(usuario_id, perfil) VALUES (2, 2); -- cliente@email.com é USER
INSERT INTO usuario_perfis(usuario_id, perfil) VALUES (3, 1); -- employee@email.com é EMPLOYE

-- Clientes
INSERT INTO cliente(id, dataCadastro, usuario_id) VALUES (1, '2025-01-10', 1); -- Admin também é cliente
INSERT INTO cliente(id, dataCadastro, usuario_id) VALUES (2, '2025-02-15', 2); -- Cliente principal

-- Funcionários
INSERT INTO funcionario(id, cargo, dataContratacao, salario, usuario_id) VALUES (1, 'Gerente de TI', '2023-01-01', 9500.00, 1); -- Admin é funcionário
INSERT INTO funcionario(id, cargo, dataContratacao, salario, usuario_id) VALUES (2, 'Vendedor', '2024-03-01', 3200.00, 3); -- Funcionário

-- Endereços dos usuários
INSERT INTO endereco(id, logradouro, numero, bairro, cep, complemento, cidade_id, usuario_id) VALUES (1, 'Quadra 104 Sul, Rua SE 03', '10', 'Plano Diretor Sul', '77020014', 'Apto 101', 1, 2);
INSERT INTO endereco(id, logradouro, numero, bairro, cep, complemento, cidade_id, usuario_id) VALUES (2, 'Avenida Paulista', '1578', 'Bela Vista', '01310200', '10º andar', 3, 2);

-- Cartões dos usuários
INSERT INTO cartao(id, titular, cpfCartao, numero, dataValidade, cvc, modalidadeCartao, usuario_id) VALUES (1, 'Cliente Fyllipe', '55566677788', '4012888877776666', '2029-12-31', 'hash_do_cvv', 'CREDITO', 2);

-- Pedido de Exemplo para o cliente@email.com (Cliente ID 2)
-- 1. Snapshot do Endereço de Entrega
INSERT INTO endereco_entrega(id, logradouro, numero, complemento, bairro, cidadeNome, estadoNome, estadoSigla, cep) VALUES (1, 'Quadra 104 Sul, Rua SE 03', '10', 'Apto 101', 'Plano Diretor Sul', 'Palmas', 'Tocantins', 'TO', '77020014');

-- 2. O Pedido em si
INSERT INTO pedido(id, data, valorTotal, situacao_atual, cliente_id, id_endereco_entrega) VALUES (1, '2025-06-10 10:30:00', 1450.00, 'ENTREGUE', 2, 1);

-- 3. Itens do Pedido
INSERT INTO item_pedido(id, quantidade, precoUnitario, id_oculos, id_pedido) VALUES (1, 1, 650.00, 1, 1);
INSERT INTO item_pedido(id, quantidade, precoUnitario, id_oculos, id_pedido) VALUES (2, 1, 800.00, 2, 1);

-- 4. Histórico do Pedido
INSERT INTO historico_status_pedido(id, dataHora, observacao, situacao, id_pedido) VALUES (1, '2025-06-10 10:30:00', 'Pedido realizado e pagamento autorizado.', 'PAGAMENTO_AUTORIZADO', 1);
INSERT INTO historico_status_pedido(id, dataHora, observacao, situacao, id_pedido) VALUES (2, '2025-06-11 09:00:00', 'Pedido enviado para a transportadora.', 'ENVIADO', 1);
INSERT INTO historico_status_pedido(id, dataHora, observacao, situacao, id_pedido) VALUES (3, '2025-06-14 14:00:00', 'Entrega concluída com sucesso.', 'ENTREGUE', 1);


SELECT setval('usuario_id_seq', (SELECT MAX(id) FROM usuario));
SELECT setval('estado_id_seq', (SELECT MAX(id) FROM estado));
SELECT setval('cidade_id_seq', (SELECT MAX(id) FROM cidade));
SELECT setval('telefone_id_seq', (SELECT MAX(id) FROM telefone));
SELECT setval('endereco_id_seq', (SELECT MAX(id) FROM endereco));
SELECT setval('cartao_id_seq', (SELECT MAX(id) FROM cartao));
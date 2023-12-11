DROP TABLE IF EXISTS eventos;
DROP TABLE IF EXISTS utlizadores;
DROP TABLE IF EXISTS codigos_registo;
DROP TABLE IF EXISTS eventos_utlizadores;

INSERT INTO utlizadores (username, password, nome) VALUES ('a2020133653@isec.pt', 'password12345', 'diogo');
INSERT INTO utlizadores (username, password, nome) VALUES ('admin', 'admin', 'administrator');

INSERT INTO eventos (designacao, place, datetime) VALUES ('Aula PD T1', 'ISEC', '2019-12-12 12:12:12');
INSERT INTO eventos (designacao, place, datetime) VALUES ('Aula PD P1', 'ISEC', '2021-12-12 12:12:12');

INSERT INTO eventos_utlizadores (id_evento, username) VALUES (1, 'a2020133653@isec.pt');
INSERT INTO eventos_utlizadores (id_evento, username) VALUES (2, 'a2020133653@isec.pt');

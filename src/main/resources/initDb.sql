DROP TABLE IF EXISTS client, account, card;

CREATE TABLE client (
    _id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    date_of_birth DATE NOT NULL,
    passport_num VARCHAR(255) NOT NULL
 );

CREATE TABLE account (
    _id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    number VARCHAR(20) NOT NULL UNIQUE,
    balance DECIMAL NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(_id)
);

CREATE TABLE card (
    _id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    daily_limit DECIMAL DEFAULT NULL,
    FOREIGN KEY (account_id) REFERENCES account(_id)
);

CREATE INDEX clients_last_name_index ON client(last_name);
CREATE INDEX accounts_client_id_index ON account(client_id);
CREATE INDEX accounts_number_index ON account(number);
CREATE INDEX cards_account_id_index ON card(account_id);
CREATE INDEX cards_number_index ON card(card_number);

INSERT INTO client (last_name, first_name, middle_name, date_of_birth, passport_num) VALUES
('Нестеров', 'Ипполит', 'Александрович', '1957-09-23', 4879821667),
('Кошелев', 'Юлиан', 'Эдуардович', '2002-01-22', 4945560768),
('Панфилов', 'Виталий', '', '1971-04-24', 4366207326),
('Горбунова', 'Дионисия', 'Вениаминовна', '1970-12-08', 4925969757),
('Захарова', 'Берта', 'Геннадиевна', '1986-05-06', 4290421116),
('Моисеева', 'Клара', 'Якововна', '2003-11-28', 4746565266),
('Ситников', 'Алексей', 'Агафонович', '1951-03-10', 4087399807),
('Блинова', 'Валерия', '', '1956-11-10', 4363356897),
('Максимов', 'Григорий', 'Гордеевич', '1972-04-23', 4659583557),
('Кулагина', 'Жасмин', 'Ильяовна', '1961-12-23', 4673708867);

INSERT INTO account (client_id, number, balance) VALUES
(1, '40729944900000001766', 970.18),
(2, '40545104200000001272', 12000.00),
(2, '40261494800000008428', 39076.09),
(3, '40912102700000008827', 860098.00),
(4, '40305477100000006821', 32131.12),
(5, '40187781400000001227', 230.87),
(6, '40388647900000002618', 95432.45),
(7, '50443335500000009240', 326780.40),
(8, '50975169400000007279', 82350.74),
(9, '40712715800000008626', 76320.00),
(10,'40654984100000009523', 560.00),
(10,'50945260200000001986', 340086.59),
(10,'40139892500000001635', 870920.00);

INSERT INTO card (account_id, card_number, daily_limit) VALUES
(1, '5419706295307412', 0.00),
(2, '4762541783266988', 1000.00),
(5, '4876898958703626', 999.99),
(6, '5421814722123000', 0.00),
(7, '5386745744863876', 0.00),
(8, '5470487992351756', 0.00),
(9, '5508500028018421', 0.00),
(10, '4834142517299649', 0.00),
(11, '5810508661245030', 0.00),
(12, '4342839231641273', 0.00),
(13, '5800850144293158', 0.00),
(13, '4973986170896476', 3000.00),
(13, '5205147940914498', 500.00);
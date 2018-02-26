BEGIN;

INSERT INTO vat_rate(id, rate, start_date) VALUES (24, 20, '2011-01-04');

INSERT INTO expense(id, date, amount, vat_id, reason) VALUES (16, '2018-02-15', 21.75, 24, 'Some test reason');
INSERT INTO expense(id, date, amount, vat_id, reason) VALUES (17, '2018-02-25', 17.89, 24, 'Test reason');
INSERT INTO expense(id, date, amount, vat_id, reason) VALUES (18, '2018-02-12', 56.42, 24, 'Another test reason');

COMMIT;
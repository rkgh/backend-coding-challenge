BEGIN;

INSERT INTO vat_rate(rate, start_date, finish_date) VALUES (15, '2008-12-01', '2009-12-31');
INSERT INTO vat_rate(rate, start_date, finish_date) VALUES (17.5, '2010-01-01', '2011-01-03');
INSERT INTO vat_rate(rate, start_date) VALUES (20, '2011-01-04');

COMMIT;
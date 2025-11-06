CREATE TABLE t_date (
    id VARCHAR PRIMARY KEY,
    date DATE NOT NULL
);

INSERT INTO t_date(id, date) VALUES
('positive', '2024-12-30'::date),
('negative', '2025-12-30 BC'::date);

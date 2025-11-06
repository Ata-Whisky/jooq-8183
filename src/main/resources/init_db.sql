CREATE TABLE t_date (
    id VARCHAR PRIMARY KEY,
    date DATE NOT NULL,
    local_date DATE NOT NULL
);

INSERT INTO t_date(id, date, local_date) VALUES
('positive', '2024-12-30'::date, '2024-12-30'::date),
('negative', '2025-12-30 BC'::date, '2025-12-30 BC'::date);

-- ============================================================
-- V2: Seed de categorías basadas en la planilla real
-- ============================================================

INSERT INTO categoria (nombre, tipo) VALUES
    ('Alquiler',      'FIJO'),
    ('Claro',         'FIJO'),
    ('Netflix',       'FIJO'),
    ('Agua',          'FIJO'),
    ('Luz',           'FIJO'),
    ('Gas',           'FIJO'),
    ('Internet',      'FIJO'),
    ('Auto/Nafta',    'VARIABLE'),
    ('Comida/Super',  'VARIABLE'),
    ('Farmacia',      'VARIABLE'),
    ('Otros',         'VARIABLE');

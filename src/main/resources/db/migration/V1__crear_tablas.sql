-- ============================================================
-- V1: Creación de tablas principales
-- ============================================================

CREATE TABLE categoria (
    id      BIGSERIAL    PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    tipo    VARCHAR(20)  NOT NULL CHECK (tipo IN ('FIJO', 'VARIABLE'))
);

CREATE TABLE gasto (
    id           BIGSERIAL      PRIMARY KEY,
    categoria_id BIGINT         NOT NULL REFERENCES categoria(id),
    descripcion  VARCHAR(255),
    monto        NUMERIC(15, 2) NOT NULL,
    fecha        DATE           NOT NULL,
    mes          VARCHAR(7)     NOT NULL,   -- formato YYYY-MM
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE TABLE ingreso (
    id          BIGSERIAL      PRIMARY KEY,
    descripcion VARCHAR(255)   NOT NULL,
    monto       NUMERIC(15, 2) NOT NULL,
    fecha       DATE           NOT NULL,
    mes         VARCHAR(7)     NOT NULL,    -- formato YYYY-MM
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

-- Índices para las consultas más comunes (por mes)
CREATE INDEX idx_gasto_mes          ON gasto(mes);
CREATE INDEX idx_gasto_categoria_id ON gasto(categoria_id);
CREATE INDEX idx_ingreso_mes        ON ingreso(mes);

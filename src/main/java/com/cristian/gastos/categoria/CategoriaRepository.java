package com.cristian.gastos.categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Categoria.
 * Spring Data genera la implementación automáticamente en tiempo de arranque.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /** Devuelve todas las categorías de un tipo (FIJO o VARIABLE). */
    List<Categoria> findByTipo(TipoCategoria tipo);

    /** Busca una categoría por nombre exacto. */
    Optional<Categoria> findByNombre(String nombre);

    /** Verifica si ya existe una categoría con ese nombre. */
    boolean existsByNombre(String nombre);
}

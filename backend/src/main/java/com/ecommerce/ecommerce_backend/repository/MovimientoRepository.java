package com.ecommerce.ecommerce_backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ecommerce.ecommerce_backend.model.MovimientoInventario;

@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoInventario, Long> {
    // Query para cuando se necesita el stock de UN solo producto
    @Query("SELECT SUM(m.cantidad) FROM MovimientoInventario m WHERE m.producto.id = :productoId")
    Integer getStockActual(@Param("productoId")  Long productoId);

    // Query que trae el stock de TODOS los productos en una sola consulta a la BD
    @Query("SELECT m.producto.id, SUM(m.cantidad) FROM MovimientoInventario m GROUP BY m.producto.id")
    List<Object[]> getStockTodosLosProductos();
}

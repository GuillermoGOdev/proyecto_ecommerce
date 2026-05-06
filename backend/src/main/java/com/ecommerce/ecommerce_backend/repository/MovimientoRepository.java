package com.ecommerce.ecommerce_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ecommerce.ecommerce_backend.model.MovimientoInventario;

@Repository
public interface MovimientoRepository extends JpaRepository<MovimientoInventario, Long> {
    @Query("SELECT SUM(m.cantidad) FROM MovimientoInventario m WHERE m.producto.id = :productoId")
    Integer getStockActual(@Param("productoId") Long productoId);
}

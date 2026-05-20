// Sincronización de Rol (Singleton)
let rolActual = localStorage.getItem('rolActual') || 'CLIENTE';

async function cambiarRolDeSesion(rol) {
    rolActual = rol;
    localStorage.setItem('rolActual', rol);
    
    // Si se cambia a CLIENTE desde esta pantalla del admin, redirigir a index.html
    if (rol === 'CLIENTE') {
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/sesion/cambiar-rol?rol=${rol}`, {
            method: 'POST'
        });
        if (response.ok) {
            const select = document.getElementById("rol-select");
            if (select) select.value = rolActual;
        }
    } catch (err) {
        console.error("Error al sincronizar rol con el backend:", err);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    // Seguridad UX: Si un comprador intenta ver el historial de stock, lo redirigimos a productos
    if (rolActual === 'CLIENTE') {
        window.location.href = 'index.html';
        return;
    }

    // Sincronizar rol activo
    cambiarRolDeSesion(rolActual);

    // Lógica normal de carga de movimientos...
    fetch("http://localhost:8080/producto/movimientos")
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("lista-historial");
            
            // Ordenar por fecha descendente
            data.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
            
            tbody.innerHTML = data.map((m, index) => {
                // Lógica de colores según la cantidad
                const claseCantidad = m.cantidad > 0 ? 'cantidad-positiva' : 'cantidad-negativa';
                
                return `
                    <tr>
                        <td>#${data.length - index}</td> <td>${new Date(m.fecha).toLocaleString()}</td>
                        <td><strong>${m.producto}</strong></td>
                        <td>
                            <span class="badge ${m.tipo.toLowerCase()}">
                                ${m.tipo.toUpperCase()}
                            </span>
                        </td>
                        <td class="${claseCantidad}">
                            ${m.cantidad > 0 ? '+' : ''}${m.cantidad}
                        </td>
                        <td>${m.obs}</td>
                    </tr>
                `;
            }).join('');
        })
        .catch(err => console.error("Error al cargar movimientos:", err));
});
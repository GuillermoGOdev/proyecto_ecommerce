document.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:8080/producto/movimientos")
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById("lista-historial");
            // Ordenar por fecha descendente (lo más nuevo arriba)
            data.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
            
            tbody.innerHTML = data.map((m, index) => `
                <tr>
                    <td>#${index + 1}</td>
                    <td>${new Date(m.fecha).toLocaleString()}</td>
                    <td><strong>${m.producto}</strong></td>
                    <td><span class="badge ${m.tipo.toLowerCase()}">${m.tipo}</span></td>
                    <td>${m.cantidad > 0 ? '+' : ''}${m.cantidad}</td>
                    <td>${m.obs}</td>
                </tr>
            `).join('');
        })
        .catch(err => console.error("Error al cargar movimientos:", err));
});
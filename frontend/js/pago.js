document.addEventListener('DOMContentLoaded', () => {
    // 1. Recuperar el pedido del localStorage
    const pedido = JSON.parse(localStorage.getItem('pedidoTemporal'));

    if (!pedido) {
        alert("Tu sesión de pago ha expirado o no hay productos seleccionados.");
        window.location.href = 'index.html';
        return;
    }

    // 2. Cargar los datos visuales en el resumen
    const nombreProd = document.getElementById('prod-nombre');
    const totalProd = document.getElementById('prod-total');

    if (nombreProd) nombreProd.innerText = pedido.nombre;
    if (totalProd) totalProd.innerText = `S/ ${parseFloat(pedido.precio).toFixed(2)}`;

    // 3. ÚNICO MANEJADOR del formulario de pago
    const checkoutForm = document.getElementById('checkout-form');
    
    checkoutForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const overlay = document.getElementById('overlay');
        const btnFinalizar = document.getElementById('btn-finalizar');
        
        const cliente = {
            nombre: document.getElementById('nombre-cliente').value,
            email: document.getElementById('email-cliente').value
        };

        // UI: Bloquear botón y mostrar Spinner
        btnFinalizar.disabled = true;
        overlay.style.display = 'flex'; 

        // Simulación de delay (opcional, para que se vea el proceso)
        setTimeout(async () => {
            try {
                // Llamada a tu API de Spring Boot
                const response = await fetch(`http://localhost:8080/producto/${pedido.id}/movimiento`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        cantidad: 1,
                        tipo: 'SALIDA',
                        observacion: `Venta Online - Cliente: ${cliente.email}`
                    })
                });

                if (response.ok) {
                    // ÉXITO: 1. Generar PDF
                    await descargarPDF(pedido, cliente);
                    
                    // 2. Mostrar Toast
                    showToast("¡Pago exitoso! Se ha descargado su recibo.", "success");
                    
                    // 3. Redirigir
                    setTimeout(() => {
                        localStorage.removeItem('pedidoTemporal');
                        window.location.href = 'index.html'; 
                    }, 3000);

                } else {
                    // ERROR de Backend (ej. Stock insuficiente)
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

            } catch (err) {
                // MANEJO DE ERROR
                overlay.style.display = 'none'; // Quitar spinner para dejar al usuario corregir
                btnFinalizar.disabled = false;
                btnFinalizar.innerText = "Confirmar Pago";
                showToast(err.message, "error");
            }
        }, 1500); 
    });
});

// --- FUNCIONES DE APOYO (Fuera del DOMContentLoaded para orden) ---

function showToast(mensaje, tipo = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = `toast ${tipo}`;
    toast.textContent = mensaje;
    container.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

async function descargarPDF(pedido, cliente) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    
    doc.setFontSize(22);
    doc.setTextColor(0, 123, 255);
    doc.text("COMPROBANTE DE COMPRA", 20, 30);
    
    doc.setFontSize(12);
    doc.setTextColor(100);
    doc.text(`Cliente: ${cliente.nombre}`, 20, 50);
    doc.text(`Email: ${cliente.email}`, 20, 60);
    doc.text(`Fecha: ${new Date().toLocaleString()}`, 20, 70);
    
    doc.line(20, 75, 190, 75);
    doc.setFont("helvetica", "bold");
    doc.text("Producto", 20, 85);
    doc.text("Total", 160, 85);
    
    doc.setFont("helvetica", "normal");
    doc.text(pedido.nombre, 20, 95);
    doc.text(`S/ ${pedido.precio.toFixed(2)}`, 160, 95);
    
    doc.line(20, 105, 190, 105);
    doc.text("Gracias por su compra.", 20, 115);

    doc.save(`Recibo_${pedido.id}_${Date.now()}.pdf`);
}
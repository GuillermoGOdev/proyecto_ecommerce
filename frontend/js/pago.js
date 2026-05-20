// Alternar entre Tarjeta y PayPal (Patrón: Adapter)
function alternarMetodoPago(metodo) {
    const seccionTarjeta = document.getElementById('seccion-tarjeta');
    const seccionPaypal = document.getElementById('seccion-paypal');
    
    const numTarjeta = document.getElementById('num-tarjeta');
    const expTarjeta = document.getElementById('exp-tarjeta');
    const cvcTarjeta = document.getElementById('cvc-tarjeta');
    const btnFinalizar = document.getElementById('btn-finalizar');
    
    if (metodo === 'PAYPAL') {
        if (seccionTarjeta) seccionTarjeta.style.display = 'none';
        if (seccionPaypal) seccionPaypal.style.display = 'block';
        
        // Quitar 'required' para evitar validación de campos invisibles
        if (numTarjeta) numTarjeta.removeAttribute('required');
        if (expTarjeta) expTarjeta.removeAttribute('required');
        if (cvcTarjeta) cvcTarjeta.removeAttribute('required');
        
        if (btnFinalizar) btnFinalizar.innerText = "Pagar con PayPal";
    } else {
        if (seccionTarjeta) seccionTarjeta.style.display = 'block';
        if (seccionPaypal) seccionPaypal.style.display = 'none';
        
        // Restaurar 'required'
        if (numTarjeta) numTarjeta.setAttribute('required', '');
        if (expTarjeta) expTarjeta.setAttribute('required', '');
        if (cvcTarjeta) cvcTarjeta.setAttribute('required', '');
        
        if (btnFinalizar) btnFinalizar.innerText = "Confirmar Pago";
    }
}
window.alternarMetodoPago = alternarMetodoPago;

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
        const overlayMsg = document.getElementById('overlay-msg');
        const btnFinalizar = document.getElementById('btn-finalizar');
        
        // Obtener el método seleccionado
        const metodoSeleccionado = document.querySelector('input[name="metodo-pago"]:checked').value;
        
        const cliente = {
            nombre: document.getElementById('nombre-cliente').value,
            email: document.getElementById('email-cliente').value
        };

        // UI: Bloquear botón y mostrar Spinner
        btnFinalizar.disabled = true;
        
        if (metodoSeleccionado === 'PAYPAL') {
            overlayMsg.innerHTML = `Conectando con servidores de PayPal...<br><span style='font-size:0.85rem; font-weight:normal; color:#666;'>Procesando pago seguro</span>`;
        } else {
            overlayMsg.innerText = "Procesando su pago seguro con Tarjeta...";
        }
        
        overlay.style.display = 'flex'; 

        // Simulación de delay (para simular comunicación con la pasarela)
        setTimeout(async () => {
            try {
                // Llamada a tu API de Spring Boot (ejecuta el CompraFacade y adapta la pasarela)
                const response = await fetch(`http://localhost:8080/producto/${pedido.id}/movimiento`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        cantidad: 1,
                        tipo: 'SALIDA',
                        observacion: `Venta Online (${metodoSeleccionado}) - Cliente: ${cliente.email}`
                    })
                });

                if (response.ok) {
                    // ÉXITO: 1. Generar PDF
                    await descargarPDF(pedido, cliente, metodoSeleccionado);
                    
                    // 2. Mostrar Toast
                    showToast("¡Pago exitoso! Se ha descargado su recibo.", "success");
                    
                    // 3. Redirigir
                    setTimeout(() => {
                        localStorage.removeItem('pedidoTemporal');
                        window.location.href = 'index.html'; 
                    }, 3000);

                } else {
                    // ERROR de Backend (ej. Stock insuficiente, rol inválido, etc.)
                    const errorMsg = await response.text();
                    throw new Error(errorMsg);
                }

            } catch (err) {
                // MANEJO DE ERROR
                overlay.style.display = 'none'; // Quitar spinner para dejar al usuario corregir
                btnFinalizar.disabled = false;
                btnFinalizar.innerText = metodoSeleccionado === 'PAYPAL' ? "Pagar con PayPal" : "Confirmar Pago";
                showToast(err.message, "error");
            }
        }, 1800); 
    });
});

// --- FUNCIONES DE APOYO ---

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

async function descargarPDF(pedido, cliente, metodoPago) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    
    doc.setFontSize(22);
    doc.setTextColor(0, 123, 255);
    doc.text("COMPROBANTE DE COMPRA", 20, 30);
    
    doc.setFontSize(12);
    doc.setTextColor(100);
    doc.text(`Cliente: ${cliente.nombre}`, 20, 50);
    doc.text(`Email: ${cliente.email}`, 20, 60);
    doc.text(`Método de Pago: ${metodoPago}`, 20, 70);
    doc.text(`Fecha: ${new Date().toLocaleString()}`, 20, 80);
    
    doc.line(20, 85, 190, 85);
    doc.setFont("helvetica", "bold");
    doc.text("Producto", 20, 95);
    doc.text("Total", 160, 95);
    
    doc.setFont("helvetica", "normal");
    doc.text(pedido.nombre, 20, 105);
    doc.text(`S/ ${pedido.precio.toFixed(2)}`, 160, 105);
    
    doc.line(20, 115, 190, 115);
    doc.text(`Comprobante generado por sistema. Gracias por su compra con ${metodoPago}.`, 20, 125);

    doc.save(`Recibo_${pedido.id}_${Date.now()}.pdf`);
}
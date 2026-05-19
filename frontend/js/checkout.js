const API = "http://localhost:8080/api/carrito";

async function cargarCheckout(){

    const response = await fetch(API);

    const carrito = await response.json();

    const resumen = document.getElementById("resumen");

    let html = "";

    let subtotal = 0;

    carrito.forEach(item => {

        subtotal += item.subtotal;

        html += `
        
            <p>
                ${item.producto.nombre}
                x${item.cantidad}
                - S/${item.subtotal}
            </p>

        `;
    });

    const distrito =
        document.getElementById("distrito").value;

    const flete = calcularFlete(distrito);

    const total = subtotal + flete;

    html += `
    
        <hr>

        <h3>Subtotal: S/${subtotal}</h3>

        <h3>Flete: S/${flete}</h3>

        <h2>Total: S/${total}</h2>

    `;

    resumen.innerHTML = html;
}

function calcularFlete(distrito){

    switch(distrito){

        case "Piura":
            return 10;

        case "Castilla":
            return 8;

        case "Sullana":
            return 15;

        default:
            return 20;
    }
}

document
.getElementById("distrito")
.addEventListener("change", cargarCheckout);

async function finalizarCompra(){

    alert("🎉 Compra realizada correctamente");

    // Vaciar carrito
    await fetch(
        "http://localhost:8080/api/carrito/vaciar",
        {
            method: "DELETE"
        }
    );

    // Redireccionar al inicio
    window.location.href = "index.html";
}
cargarCheckout();
const API = "http://localhost:8080/api/carrito";

async function cargarCarrito(){

    const response = await fetch(API);

    const carrito = await response.json();

    const container = document.getElementById("carrito-container");

    let html = "";

    let total = 0;

    carrito.forEach(item => {

        total += item.subtotal;

        html += `
        
            <div class="card">

                <div class="info">

                   <img src="${item.producto.imagenURL}" alt="">

                    <div>
                        <h2>${item.producto.nombre}</h2>

                        <p>Cantidad: ${item.cantidad}</p>

                        <p>Precio: S/${item.producto.precio}</p>

                        <h3>Subtotal: S/${item.subtotal}</h3>
                    </div>

                </div>

                <button 
                    class="btn-eliminar"
                    onclick="eliminarProducto(${item.producto.id})"
                >
                    Eliminar
                </button>

            </div>

        `;
    });

    container.innerHTML = html;

    document.getElementById("total").innerText =
        `Total: S/${total}`;
}

async function eliminarProducto(id){

    await fetch(`${API}/eliminar/${id}`, {
        method: "DELETE"
    });

    cargarCarrito();
}

cargarCarrito();
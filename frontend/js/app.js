function cargarProductos() {
    fetch("http://localhost:8080/producto")
            .then(res => res.json())
            .then(data => {
                let html = "";
    
                for(let p of data) {
                    html += `
                        <div class="card">
                            <h3>${p.nombre}</h3>
                            <p>Precio: S/${p.precio}</p>
                            <button>Comprar</button>
                            <button id="editar" onclick="editarProducto(${p.id})"><img src="assets/icons/lapiz-blanco.png" alt="editar"></button>
                            <button id="eliminar" onclick="eliminarProducto(${p.id})"><img src="assets/icons/basura-blanco.png" alt="eliminar"></button>
                        </div>
                    `;
                };
    
                document.getElementById("contenedor-productos").innerHTML = html;
            });
}

function agregarProducto() {

    let nombre = document.getElementById("nombre").value;
    let precio = document.getElementById("precio").value;

    fetch("http://localhost:8080/producto", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            nombre: nombre,
            precio: precio
        })
    })
    .then(res => res.json())
    .then(data => {
        console.log("Producto guardado: ", data);
        cargarProductos();
    })
}

function editarProducto(id) {
    fetch(`http://localhost:8080/producto/${id}`)
        .then(res => res.json())
        .then(producto => {
            let nuevoNombre = prompt("Nuevo nombre: ", producto.nombre);
            let nuevoPrecio = prompt("Nuevo precio: ", producto.precio);

            if (nuevoNombre && nuevoPrecio) {
                fetch(`http://localhost:8080/producto/${id}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        nombre: nuevoNombre,
                        precio: nuevoPrecio
                    })
                })
                .then(() => cargarProductos());
            }
        })
        .catch(error => console.error('Error al obtener producto:', error));
}

function eliminarProducto(id) {
    if (confirm("¿Estás seguro de que quieres eliminar este producto?")) {
        fetch(`http://localhost:8080/producto/${id}`, {
            method: "DELETE",
        })
        .then(() => cargarProductos())
        .catch(error => console.error('Error al eliminar producto:', error));
    }
}

cargarProductos();
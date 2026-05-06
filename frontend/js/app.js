function cargarCategorias(data = null) {
    if (data === null) {
        fetch("http://localhost:8080/categoria")
            .then(res => res.json())
            .then(actualizarInterfazCategorias);
    } else {
        actualizarInterfazCategorias(data);
    }
}


function actualizarInterfazCategorias(data) {
    let select = document.getElementById("categoria");
    if (!select) return;
    
    if (!Array.isArray(data)) {
        console.error("Los datos recibidos no son una lista válida:", data);
        return; 
    }

    select.innerHTML = '<option value="">Seleccione una categoría</option>';
    data.forEach(cat => {
        let option = document.createElement("option");
        option.value = cat.id;
        option.textContent = cat.nombre;
        select.appendChild(option);
    });
}


function cargarProductos() {
    fetch("http://localhost:8080/producto")
            .then(res => res.json())
            .then(data => {
                let html = "";
    
                for(let p of data) {
                    let imgPath = p.imagenURL ? p.imagenURL : 'https://placehold.co/150';

                    html += `
                        <div class="card">
                            <img src="${imgPath}" alt="${p.nombre}" onerror="this.onerror=null; this.src='https://placehold.co/150';">
                        <div class="card-body">
                        <h3>${p.nombre}</h3>
                        <p class="categoria"><span>${p.categoria ? p.categoria.nombre : 'General'}</span></p>
                        <p class="precio">S/${p.precio}</p>
                        </div>
        
                        <div class="card-footer">
                            <button class="btn-comprar" onclick="comprar()">Comprar</button>
                            <div class="acciones-admin">
                                <button class="btn-icon btn-editar" onclick="editarProducto(${p.id})">
                                    <img src="assets/icons/lapiz-blanco.png">
                                </button>
                                <button class="btn-icon btn-eliminar" onclick="eliminarProducto(${p.id})">
                                    <img src="assets/icons/basura-blanco.png">
                                </button>
                            </div>
                        </div>
                    </div>
                    `;
                };
    
                document.getElementById("contenedor-productos").innerHTML = html;
            });
}

function agregarProducto() {

    let nombre = document.getElementById("nombre").value;
    let precio = document.getElementById("precio").value;
    let imagenURL = document.getElementById("imagenURL").value;
    let categoriaId = document.getElementById("categoria").value;

    if (!categoriaId) {
        alert("Por favor, selecciona una categoría");
        return;
    }

    fetch("http://localhost:8080/producto", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            nombre: nombre,
            precio: precio,
            imagenURL: imagenURL,
            categoria: {
                id: categoriaId 
            }
        })
    })
    .then(res => res.json())
    .then(data => {
        console.log("Producto guardado: ", data);
        document.getElementById("nombre").value = "";
        document.getElementById("precio").value = "";
        document.getElementById("imagenURL").value = "";
        document.getElementById("categoria").value = "";
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
                        precio: nuevoPrecio,
                        imagenURL: producto.imagenURL,
                        categoria: producto.categoria
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


document.addEventListener("DOMContentLoaded", () => {
    console.log("Cargando datos iniciales...");
    
    Promise.all([
        fetch("http://localhost:8080/categoria").then(res => res.json()),
        fetch("http://localhost:8080/producto").then(res => res.json())
    ])
    .then(([categorias, productos]) => {
       
        cargarCategorias(categorias); 
        
        cargarProductos(); 
        
        console.log("Carga completada con éxito");
    })
    .catch(err => {
        console.error("Error en la conexión:", err);
        alert("Error al conectar con el backend. Revisa si Spring Boot está activo.");
    });
});
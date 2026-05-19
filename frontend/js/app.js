const API_CONFIG = {
    BASE_URL: "http://localhost:8080",
    ENDPOINTS: {
        PRODUCTOS: "/producto",
        CATEGORIAS: "/categoria"
    }
};



function cargarCategorias(data = null) {
    if (data === null) {
        fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.CATEGORIAS}`)
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

// Variable global para guardar los productos cargados
let productosCargados = [];

function cargarProductos() {
    fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}`)
        .then(res => res.json())
        .then(data => {
            productosCargados = data; // Guardamos la copia
            mostrarProductos(productosCargados); // Llamamos a una nueva función que pinta
        });
}

// Nueva función separada para pintar en el HTML (Principio de Responsabilidad Única)
function mostrarProductos(lista) {
    let html = "";
    for (let p of lista) {
        let imgPath = p.imagenURL ? p.imagenURL : 'https://placehold.co/150';
        html += `
            <div class="card">
                <img src="${imgPath}" alt="${p.nombre}" onerror="this.onerror=null; this.src='https://placehold.co/150';">
                <div class="card-body">
                    <h3>${p.nombre}</h3>
                    <p class="stock">Disponible: <strong>${p.stockActual}</strong></p>

                    <p class="categoria"><span>${p.categoria ? p.categoria.nombre : 'General'}</span></p>
                    <p class="precio">S/${p.precio}</p>
                </div>
                <div class="card-footer">
                    <button 
                            class="btn-comprar" 
                            onclick="agregarAlCarrito(${p.id})"> Agregar al carrito</button>
                    <div class="acciones-admin">
                        <button class="btn-icon btn-editar" onclick="editarProducto(${p.id})">
                            <img src="assets/icons/lapiz-blanco.png">
                        </button>
                        <button class="btn-icon btn-eliminar" onclick="eliminarProducto(${p.id})">
                            <img src="assets/icons/basura-blanco.png">
                        </button>
                    </div>
                </div>
            </div>`;
    }
    
    const contenedor = document.getElementById("contenedor-productos");
    if (lista.length === 0) {
        contenedor.innerHTML = '<p class="mensaje-vacio">No se encontraron productos.</p>';
    } else {
        contenedor.innerHTML = html;
    }
}

// LA FUNCIÓN DE FILTRO (Lo que ocurre al escribir)
function filtrarProductos() {
    const texto = document.getElementById("buscador").value.toLowerCase();
    
    // Filtramos el array global
    const filtrados = productosCargados.filter(p => {
        const nombre = p.nombre.toLowerCase();
        const categoria = p.categoria ? p.categoria.nombre.toLowerCase() : 'general';
        return nombre.includes(texto) || categoria.includes(texto);
    });

    // Mostramos solo los resultados que coinciden
    mostrarProductos(filtrados);
}


async function agregarProducto() {
    // 1. Referencias a los elementos del DOM
    const btnGuardar = document.querySelector(".form-container button");
    const inputNombre = document.getElementById("nombre");
    const inputPrecio = document.getElementById("precio");
    const inputImagen = document.getElementById("imagenURL"); 
    const inputCategoria = document.getElementById("categoria");

    // 2. Guardar el estado original del botón
    const textoOriginal = btnGuardar.textContent;

    // Resetear estilos antes de validar
    resaltarError(inputNombre, false);
    resaltarError(inputPrecio, false);
    resaltarError(inputCategoria, false);

    let hayError = false;

    // Validar Nombre
    if (!inputNombre.value.trim()) {
        resaltarError(inputNombre);
        hayError = true;
    }

    // Validar Precio
    if (!inputPrecio.value || parseFloat(inputPrecio.value) <= 0) {
        resaltarError(inputPrecio);
        hayError = true;
    }

    // Validar Categoría
    if (!inputCategoria.value) {
        resaltarError(inputCategoria);
        hayError = true;
    }

    if (hayError) {
        showToast("Por favor, completa los campos marcados en rojo", "warning");
        return;
    }

    // Desactivar botón y cambiar texto
    btnGuardar.disabled = true;
    btnGuardar.innerHTML = 'Guardando... <span class="spinner"></span>';

    try {
        // 5. Petición al servidor
        const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                nombre: inputNombre.value,
                precio: inputPrecio.value,
                imagenURL: inputImagen.value,
                categoria: { id: inputCategoria.value }
            })
        });

        if (response.ok) {
            // 6. FEEDBACK DE ÉXITO
            btnGuardar.textContent = "¡Producto Guardado!";
            showToast("¡Producto guardado con éxito!", "success");

            // Limpiar campos
            inputNombre.value = "";
            inputPrecio.value = "";
            inputImagen.value = "";
            inputCategoria.value = "";

            // Recargar la lista de productos
            cargarProductos();
        } else {
            throw new Error("Error en la respuesta del servidor");
        }

    } catch (error) {
        // 7. FEEDBACK DE ERROR
        console.error("Error al guardar:", error);
        showToast("No se pudo guardar el producto. Revisa la conexión.", "error");
    } finally {
        // 8. RESTAURAR BOTÓN después de 2 segundos
        setTimeout(() => {
            btnGuardar.disabled = false;
            btnGuardar.textContent = textoOriginal;
            btnGuardar.style.backgroundColor = ""; // Vuelve al color del CSS
        }, 2000);
    }
}

async function editarProducto(id) {
    try {
        const res = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}/${id}`);
        const producto = await res.json();

        let nuevoNombre = prompt("Nuevo nombre: ", producto.nombre);
        let nuevoPrecio = prompt("Nuevo precio: ", producto.precio);

        if (nuevoNombre && nuevoPrecio) {
            const response = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}/${id}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    nombre: nuevoNombre,
                    precio: nuevoPrecio,
                    imagenURL: producto.imagenURL,
                    categoria: producto.categoria
                })
            });

            if (response.ok) {
                showToast("Producto actualizado con éxito", "success");
                cargarProductos();
            } else {
                throw new Error();
            }
        }
    } catch (error) {
        showToast("Error al editar el producto", "error");
    }
}

async function eliminarProducto(id) {
    if (confirm("¿Estás seguro de que quieres eliminar este producto?")) {
        try {
            const res = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}/${id}`, {
                method: "DELETE",
            });

            if (res.ok) {
                showToast("Producto eliminado correctamente", "success");
                cargarProductos(); // Recarga la lista
            } else {
                throw new Error();
            }
        } catch (error) {
            showToast("No se pudo eliminar el producto", "error");
        }
    }
}


function resaltarError(elemento, error = true) {
    if (error) {
        elemento.style.border = "2px solid #dc3545"; 
        elemento.style.backgroundColor = "#fff8f8";  
    } else {
        elemento.style.border = "1px solid #ccc";    
        elemento.style.backgroundColor = "#fff";
    }
}




document.addEventListener("DOMContentLoaded", () => {
    console.log("Cargando datos iniciales...");
    
    Promise.all([
        fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.CATEGORIAS}`).then(res => res.json()),
        fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}`).then(res => res.json())
    ])
    .then(([categorias, productos]) => {
       
        cargarCategorias(categorias); 
        
        cargarProductos(); 

        actualizarContadorCarrito();
        
        console.log("Carga completada con éxito");
    })
    .catch(err => {
        console.error("Error en la conexión:", err);
        alert("Error al conectar con el backend. Revisa si Spring Boot está activo.");
    });
});

function showToast(mensaje, tipo = 'success') {
    // 1. Crear el contenedor si no existe
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }

    // 2. Crear la notificación
    const toast = document.createElement('div');
    toast.className = `toast ${tipo}`;
    toast.innerHTML = `<div class="toast-text">${mensaje}</div>`;

    // 3. Agregar al contenedor
    container.appendChild(toast);

    // 4. Eliminar del DOM automáticamente después de 3 segundos
    setTimeout(() => {
        toast.remove();
    }, 3000);
}


// ==========================
// CARRITO
// ==========================

async function agregarAlCarrito(idProducto) {

    try {

        const response = await fetch(
            `http://localhost:8080/api/carrito/agregar/${idProducto}?cantidad=1`,
            {
                method: "POST"
            }
        );

        if(response.ok){

            showToast("SE AGREGO AL CARRITO CON EXITO...", "success");

            actualizarContadorCarrito();

        }else{
            throw new Error();
        }

    } catch (error) {

        console.error(error);

        showToast("No se pudo agregar al carrito", "error");
    }
}


// ==========================
// CONTADOR CARRITO
// ==========================

async function actualizarContadorCarrito(){

    try {

        const response = await fetch(
            "http://localhost:8080/api/carrito"
        );

        const carrito = await response.json();

        let cantidad = 0;

        carrito.forEach(item => {
            cantidad += item.cantidad;
        });

        const contador =
            document.getElementById("contador-carrito");

        if(contador){
            contador.textContent = cantidad;
        }

    } catch(error){

        console.error(error);
    }
}
const API_CONFIG = {
    BASE_URL: "http://localhost:8080",
    ENDPOINTS: {
        PRODUCTOS: "/producto",
        CATEGORIAS: "/categoria"
    }
};

// --- Caching Utilities ---
const CACHE_KEYS = {
    PRODUCTOS: "productos_cache",
    CATEGORIAS: "categorias_cache"
};

let productosCargados = [];
let categoriaSeleccionada = 'TODOS';

function obtenerCache(key) {
    try {
        const data = localStorage.getItem(key);
        return data ? JSON.parse(data) : null;
    } catch (e) {
        console.error(`Error al leer de la caché para la clave ${key}:`, e);
        return null;
    }
}

function guardarCache(key, data) {
    try {
        localStorage.setItem(key, JSON.stringify(data));
    } catch (e) {
        console.error(`Error al guardar en la caché para la clave ${key}:`, e);
    }
}

function cargarCategorias(data = null) {
    if (data === null) {
        fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.CATEGORIAS}`)
            .then(async res => {
                if (!res.ok) throw new Error(`Error ${res.status}`);
                return res.json();
            })
            .then(dataCategorias => {
                guardarCache(CACHE_KEYS.CATEGORIAS, dataCategorias);
                actualizarInterfazCategorias(dataCategorias);
            })
            .catch(err => console.error("Error al cargar categorías:", err));
    } else {
        actualizarInterfazCategorias(data);
    }
}

function actualizarInterfazCategorias(data) {
    const select = document.getElementById("categoria");
    const filtroContenedor = document.getElementById("categorias-filtros");
    const listaProductos = Array.isArray(productosCargados) ? productosCargados : [];

    if (!select) return;

    if (!Array.isArray(data)) {
        console.error("Los datos recibidos no son una lista válida:", data);
        return;
    }

    select.innerHTML = '<option value="">Seleccione una categoría</option>';
    if (filtroContenedor) filtroContenedor.innerHTML = '';

    if (filtroContenedor) {
        const btnTodos = document.createElement("button");
        btnTodos.className = "filter-btn active";
        btnTodos.innerHTML = `Todos <span>${listaProductos.length}</span>`;
        btnTodos.onclick = (e) => filtrarPorCategoria('TODOS', e.currentTarget);
        filtroContenedor.appendChild(btnTodos);
    }

    data.forEach(cat => {
        const option = document.createElement("option");
        option.value = cat.id;
        option.textContent = cat.nombre;
        select.appendChild(option);

        const cantidad = listaProductos.filter(p => p.categoria && p.categoria.id === cat.id).length;

        if (filtroContenedor) {
            const btn = document.createElement("button");
            btn.className = "filter-btn";
            btn.innerHTML = `${cat.nombre} <span>${cantidad}</span>`;
            btn.onclick = (e) => filtrarPorCategoria(cat.nombre, e.currentTarget);
            filtroContenedor.appendChild(btn);
        }
    });
}

async function refrescarInterfazCompleta() {
    try {
        const [categorias, productos] = await Promise.all([
            fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.CATEGORIAS}`).then(async res => {
                if (!res.ok) throw new Error(`Error ${res.status}`);
                return res.json();
            }),
            fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}`).then(async res => {
                if (!res.ok) throw new Error(`Error ${res.status}`);
                return res.json();
            })
        ]);

        guardarCache(CACHE_KEYS.CATEGORIAS, categorias);
        guardarCache(CACHE_KEYS.PRODUCTOS, productos);

        productosCargados = productos;
        actualizarInterfazCategorias(categorias);
        filtrarProductos();
    } catch (error) {
        console.error("Error al refrescar la interfaz:", error);
    }
}

function filtrarPorCategoria(nombreCategoria, elemento) {
    document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
    elemento.classList.add('active');

    categoriaSeleccionada = nombreCategoria;
    filtrarProductos();
}

function cargarProductos() {
    fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}`)
        .then(async res => {
            if (!res.ok) throw new Error(`Error ${res.status}`);
            return res.json();
        })
        .then(data => {
            guardarCache(CACHE_KEYS.PRODUCTOS, data);
            productosCargados = data;
            mostrarProductos(productosCargados);
        })
        .catch(err => console.error("Error al cargar productos:", err));
}

function mostrarProductos(lista) {
    const contenedor = document.getElementById("contenedor-productos");
    if (!contenedor) return;

    const productosAMostrar = Array.isArray(lista) ? lista : [];
    let html = "";

    for (let p of productosAMostrar) {
        const imgPath = p.imagenURL ? p.imagenURL : 'https://placehold.co/150';
        const stockActual = (p.stock !== undefined) ? p.stock : 0;
        const claseStock = stockActual < 5 ? 'stock-bajo' : 'stock-normal';

        html += `
            <div class="card">
                <img src="${imgPath}" alt="${p.nombre}" onerror="this.onerror=null; this.src='https://placehold.co/150';">
                <div class="card-body">
                    <h3>${p.nombre}</h3>
                    <p class="stock">
                        Disponible:
                        <strong class="${claseStock}">${stockActual}</strong>
                    </p>
                    <p class="categoria">
                        <span>${p.categoria ? p.categoria.nombre : 'General'}</span>
                    </p>
                    <p class="precio">
                        ${p.precioOriginal ? `
                            <span style="text-decoration:line-through;color:#999;font-size:14px;">S/${p.precioOriginal}</span>
                            <br>
                            <span style="color:#16a34a;font-size:22px;font-weight:bold;">S/${p.precio}</span>
                        ` : `<span style="font-size:22px;font-weight:bold;">S/${p.precio}</span>`}
                    </p>
                </div>
                <div class="card-footer">
                    <button class="btn-comprar" onclick="irAPagar(${p.id}, '${p.nombre}', ${p.precio})">Comprar</button>
                    <div class="acciones-admin">
                        <button class="btn-icon btn-surtir" onclick="surtirStock(${p.id})" title="Surtir Stock">
                            <span style="font-size:1.2rem;">📦</span>
                        </button>
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
    }

    contenedor.innerHTML = productosAMostrar.length === 0
        ? '<p class="mensaje-vacio">No se encontraron productos.</p>'
        : html;

    aplicarControlesDeRol();
}

function filtrarProductos() {
    const buscador = document.getElementById("buscador");
    const precioRange = document.getElementById("precioRange");
    const precioMaxLabel = document.getElementById("precioMaxLabel");

    const texto = buscador ? buscador.value.toLowerCase() : "";
    const precioMax = precioRange ? parseFloat(precioRange.value) : Infinity;

    if (precioMaxLabel) {
        precioMaxLabel.textContent = `S/${precioMax}`;
    }

    const filtrados = (Array.isArray(productosCargados) ? productosCargados : []).filter(p => {
        const coincideTexto = p.nombre.toLowerCase().includes(texto) ||
            (p.categoria && p.categoria.nombre.toLowerCase().includes(texto));
        const coincidePrecio = p.precio <= precioMax;
        const coincideCategoria = (categoriaSeleccionada === 'TODOS') ||
            (p.categoria && p.categoria.nombre === categoriaSeleccionada);
        return coincideTexto && coincidePrecio && coincideCategoria;
    });

    mostrarProductos(filtrados);
}


async function agregarProducto() {
    // 1. Referencias a los elementos del DOM
    const btnGuardar = document.querySelector(".btn-guardar");
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

            cerrarModal(); // Cerrar el modal después de guardar
            refrescarInterfazCompleta(); // Recarga completa para actualizar categorías y filtros
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
                refrescarInterfazCompleta(); // Recarga completa para actualizar categorías y filtros
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

/*async function comprar(id, nombre) {
    const cantidad = prompt(`¿Cuántas unidades de ${nombre} desea comprar?`, "1");

    // Validaciones básicas de cliente
    if (cantidad === null) return; 
    const cantNum = parseInt(cantidad);
    
    if (isNaN(cantNum) || cantNum <= 0) {
        showToast("Por favor, ingresa una cantidad válida", "warning");
        return;
    }

    try {
        // Usamos el endpoint que configuraste en tu InventarioServiceImpl
        const url = `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}/${id}/movimiento`;
        
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                cantidad: cantNum,
                tipo: "SALIDA",
                observacion: "Venta directa desde panel"
            })
        });

        // Si el servidor lanza el RuntimeException (Stock insuficiente)
        if (!res.ok) {
            // Intentamos obtener el mensaje de error del backend
            const errorData = await res.json().catch(() => ({ message: "Error desconocido" }));
            throw new Error(errorData.message || "No hay stock suficiente.");
        }

        showToast(`✅ Venta de ${nombre} exitosa`, "success");
        refrescarInterfazCompleta(); // Esto actualizará los números en los botones y las cards

    } catch (err) {
        // Aquí es donde el administrador ve que no puede vender lo que no tiene
        console.error("Error en la venta:", err.message);
        showToast(err.message, "error");
    }
}*/

function irAPagar(id, nombre, precio) {
    const pedido = { id, nombre, precio, cantidad: 1 };
    localStorage.setItem('pedidoTemporal', JSON.stringify(pedido));
    window.location.href = 'pago.html'; // Te lleva a la nueva página
}

document.addEventListener("DOMContentLoaded", async () => {
    aplicarControlesDeRol();

    const cachedCategorias = obtenerCache(CACHE_KEYS.CATEGORIAS);
    const cachedProductos = obtenerCache(CACHE_KEYS.PRODUCTOS);

    if (cachedCategorias && cachedProductos) {
        productosCargados = cachedProductos;
        actualizarInterfazCategorias(cachedCategorias);
        mostrarProductos(productosCargados);
    }

    try {
        const [categorias, productos] = await Promise.all([
            fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.CATEGORIAS}`).then(async res => {
                if (!res.ok) throw new Error(`Error ${res.status}`);
                return res.json();
            }),
            fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}`).then(async res => {
                if (!res.ok) throw new Error(`Error ${res.status}`);
                return res.json();
            })
        ]);

        guardarCache(CACHE_KEYS.CATEGORIAS, categorias);
        guardarCache(CACHE_KEYS.PRODUCTOS, productos);

        productosCargados = productos;
        actualizarInterfazCategorias(categorias);
        filtrarProductos();
    } catch (err) {
        console.error("Error en la conexión con el backend:", err);
        if (!cachedCategorias || !cachedProductos) {
            alert("Error al conectar con el backend. Revisa si Spring Boot está activo.");
        }
    }

    try {
        await cambiarRolDeSesion(rolActual);
    } catch (err) {
        console.error("No se pudo sincronizar el rol inicial:", err);
    }
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
    toast.textContent = mensaje;

    // 3. Agregar al contenedor
    container.appendChild(toast);

    // 4. Eliminar del DOM automáticamente después de 3 segundos
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

async function surtirStock(id) {
    const cantidad = prompt("¿Cuántas unidades ingresan?");
    if (!cantidad || isNaN(cantidad)) return;

    try {
        const res = await fetch(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.PRODUCTOS}/${id}/movimiento`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                cantidad: parseInt(cantidad),
                tipo: "ENTRADA",
                observacion: "Ingreso manual de mercadería"
            })
        });

        if (res.ok) {
            showToast("Stock actualizado", "success");
            cargarProductos(); // Recarga para ver el nuevo número
        }
    } catch (error) {
        showToast("Error al actualizar stock", "error");
    }
}

// Abrir el modal
function abrirModal() {
    const modal = document.getElementById("modalProducto");
    modal.style.display = "block";
    document.body.style.overflow = "hidden"; // Evita scroll al estar abierto
}

// Cerrar el modal
function cerrarModal() {
    const modal = document.getElementById("modalProducto");
    modal.style.display = "none";
    document.body.style.overflow = "auto"; // Habilita el scroll de nuevo
}

// Cerrar si el usuario hace clic fuera del contenido blanco
window.onclick = function(event) {
    const modal = document.getElementById("modalProducto");
    if (event.target == modal) {
        cerrarModal();
    }
}

// Sincronización de Rol (Singleton)
let rolActual = localStorage.getItem('rolActual') || 'CLIENTE';

async function cambiarRolDeSesion(rol) {
    rolActual = rol;
    localStorage.setItem('rolActual', rol);

    try {
        const response = await fetch(`${API_CONFIG.BASE_URL}/api/sesion/cambiar-rol?rol=${rol}`, {
            method: 'POST'
        });
        if (response.ok) {
            const data = await response.json();
            if (document.getElementById("rol-select")) {
                showToast(`Rol actualizado: ${data.rol}`, "success");
            }
        }
    } catch (err) {
        console.error("Error al sincronizar rol con el backend:", err);
    }

    aplicarControlesDeRol();
}

function aplicarControlesDeRol() {
    const select = document.getElementById("rol-select");
    if (select) select.value = rolActual;

    const adminActions = document.querySelector(".admin-actions");
    const accionesAdmin = document.querySelectorAll(".acciones-admin");
    const btnComprar = document.querySelectorAll(".btn-comprar");
    const navHistorial = document.getElementById("nav-historial");

    if (rolActual === 'CLIENTE') {
        if (adminActions) adminActions.style.display = "none";
        accionesAdmin.forEach(el => el.style.display = "none");
        btnComprar.forEach(el => el.style.display = "block");
        if (navHistorial) navHistorial.style.display = "none";
    } else {
        if (adminActions) adminActions.style.display = "block";
        accionesAdmin.forEach(el => el.style.display = "flex");
        btnComprar.forEach(el => el.style.display = "none");
        if (navHistorial) navHistorial.style.display = "";
    }
}
async function calcularDescuento() {

    const tipo = document.getElementById("tipoCliente").value;

    try {

        const response = await fetch(
            `${API_CONFIG.BASE_URL}/producto/descuento?tipo=${tipo}`
        );

        productosCargados = await response.json();

        mostrarProductos(productosCargados);

        showToast("Descuento aplicado correctamente", "success");

    } catch (error) {

        console.error(error);


    }

}
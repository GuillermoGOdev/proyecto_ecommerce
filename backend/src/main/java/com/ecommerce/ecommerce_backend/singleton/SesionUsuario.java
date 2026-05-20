package com.ecommerce.ecommerce_backend.singleton;

// Patrón: Singleton
public class SesionUsuario {

    private static volatile SesionUsuario instancia;

    private String nombre;
    private String email;
    private String rol; // "CLIENTE" o "ADMIN"

    private SesionUsuario() {
        this.nombre = "Cliente Invitado";
        this.email = "comprador@correo.com";
        this.rol = "CLIENTE";
    }

    public static SesionUsuario getInstance() {
        if (instancia == null) {
            synchronized (SesionUsuario.class) {
                if (instancia == null) {
                    instancia = new SesionUsuario();
                }
            }
        }
        return instancia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void cerrarSesion() {
        this.nombre = "Cliente Invitado";
        this.email = "comprador@correo.com";
        this.rol = "CLIENTE";
    }
}

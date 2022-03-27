
package com.alexander.modelo;

public class Cliente {
    int id;
    String dni;
    String Nombres;
    String email;
    String pass;
    String direccion;

    public Cliente() {
    }

    public Cliente(int id, String dni, String Nombres, String email, String pass, String direccion) {
        this.id = id;
        this.dni = dni;
        this.Nombres = Nombres;
        this.email = email;
        this.pass = pass;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String Nombres) {
        this.Nombres = Nombres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    
}

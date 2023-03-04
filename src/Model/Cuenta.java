/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author USER
 */
public class Cuenta {
    
    private String nombre, apellido, cedula;
    private int clave, numeroCuenta;
    private double deposito;

    public Cuenta() {
    }

    public Cuenta(String nombre, String apellido, String cedula, int clave, double deposito, int numeroCuenta) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.clave = clave;
        this.deposito = deposito;
        this.numeroCuenta = numeroCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public int getClave() {
        return clave;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getDeposito() {
        return deposito;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setDeposito(double deposito) {
        this.deposito = deposito;
    }
    
    
}

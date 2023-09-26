/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Back.Modelo;

import java.util.HashMap;
import java.util.Map;

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
    
    public String toString(){
        return numeroCuenta+","+nombre+","+apellido+","+cedula+","+deposito+","+cifrarTexto(""+clave,4);
    }
    
    public String cifrarTexto(String texto, int clave) {
        Map<Character, Character> tablaCifrado = crearTablaCifrado(clave);
        StringBuilder textoCifrado = new StringBuilder();

        for (char caracter : texto.toCharArray()) {
            // Verificar si el carácter está en la tabla de cifrado
            if (tablaCifrado.containsKey(caracter)) {
                textoCifrado.append(tablaCifrado.get(caracter));
            } else {
                // Si el carácter no está en la tabla, mantenerlo sin cambios
                textoCifrado.append(caracter);
            }
        }

        return textoCifrado.toString();
    }

    private Map<Character, Character> crearTablaCifrado(int clave) {
        Map<Character, Character> tablaCifrado = new HashMap<>();
        String caracteresPermitidos = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 áéíóúÁÉÍÓÚ";

        // Asegurarse de que la clave esté en el rango adecuado
        clave = clave % caracteresPermitidos.length();

        // Crear la tabla de cifrado con desplazamiento según la clave
        for (int i = 0; i < caracteresPermitidos.length(); i++) {
            char caracterOriginal = caracteresPermitidos.charAt(i);
            int indiceCifrado = (i + clave + caracteresPermitidos.length()) % caracteresPermitidos.length();
            char caracterCifrado = caracteresPermitidos.charAt(indiceCifrado);
            tablaCifrado.put(caracterOriginal, caracterCifrado);
        }

        return tablaCifrado;
    }
    
}

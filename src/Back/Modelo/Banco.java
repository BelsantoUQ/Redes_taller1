/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Back.Modelo;

import Back.Modelo.Cuenta;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class Banco {

    public static ArrayList<Cuenta> cuentasUsuarios;

    public Banco() throws IOException {
        cuentasUsuarios = new ArrayList<>(); //NOMBRE, APELLIDO, CEDULA, CLAVE, MONTO EN LA CUENTA, NUMERO DE CUENTA
        cuentasUsuarios.add(new Cuenta("Santiago", "Velanida Gallo", "1094971551", 1234, 8000, 1));
        cuentasUsuarios.add(new Cuenta("Estefania", "Arroyave Bermudez", "1094953679", 4321, 400000, 2));
        escribirEnArchivo();

    }

    public static void escribirEnArchivo() throws IOException {
        // Crea una instancia de FileWriter para escribir en el archivo de texto
        FileWriter fileWriter = new FileWriter("src/Recursos_Datos/banco_Cuentas.txt");

        // Crea una instancia de BufferedWriter para escribir en el archivo de texto
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        // Itera sobre el arreglo de objetos y escribe cada objeto en una línea separada en el archivo de texto
        bufferedWriter.write("numCuenta,nombre,apellido,cedula,deposito,clave");
        bufferedWriter.newLine();
        for (Cuenta usuario : cuentasUsuarios) {
            bufferedWriter.write(usuario.toString() + ";");
            bufferedWriter.newLine();
        }

        // Cierra el BufferedWriter para asegurarte de que todos los datos se han escrito en el archivo de texto
        bufferedWriter.close();
    }

    public String getUsuariosToString() {
        String usuarios = "";
        for (Cuenta usuario : cuentasUsuarios) {
            usuarios = usuarios + usuario.getNombre() + "&" + usuario.getApellido()
                    + "&" + usuario.getCedula() + "&" + usuario.getNumeroCuenta() + "&"
                    + usuario.getDeposito() + "%";
        }
        return usuarios;
    }

    public boolean ingresar(String cedula, int clave) {
        for (Cuenta usuario : cuentasUsuarios) {
            if (usuario.getCedula().equals(cedula) && usuario.getClave() == clave) {
                return true;
            }
        }
        return false;
    }

    public int abrirCuenta(String nombre, String apellido, String cedula, int clave, double deposito) {
        try {

            Cuenta nuevoUsuario = new Cuenta(nombre, apellido, cedula, clave, deposito, cuentasUsuarios.size() + 101);
            cuentasUsuarios.add(nuevoUsuario);
            return nuevoUsuario.getNumeroCuenta();
        } catch (Exception e) {
            System.out.println("Erro al registrar cuenta: " + e);
            return -1;
        }
    }

    public boolean modificarCuenta(int clave, String cedula, String cambio, String nuevoValor) {
        int posCuenta = getPosicionPorCedula(cedula);
        if (posCuenta >= 0) {
            switch (cambio) {
                case "Nombre":
                    cuentasUsuarios.get(posCuenta).setNombre(nuevoValor);
                    return true;
                case "Apellido":
                    cuentasUsuarios.get(posCuenta).setApellido(nuevoValor);
                    return true;
            }
        }
        return false;
    }

    public boolean cerrarCuenta(int cuenta, String motivo, int clave) {
        int posCuenta = getPosicionPorCuenta(cuenta);
        if (posCuenta >= 0) {
            if (cuentasUsuarios.get(posCuenta).getClave() == clave) {
                cuentasUsuarios.remove(posCuenta);
                return true;
            }
        }
        return false;
    }

    public double consignarDinero(int cuenta, String cedula, double deposito) {
        int posCuenta = getPosicionPorCuenta(cuenta);
        if (posCuenta >= 0) {
            Cuenta user = cuentasUsuarios.get(posCuenta);
            if (cedula.equals(user.getCedula())) {
                double dineroActual = user.getDeposito();
                user.setDeposito(dineroActual + deposito);
                return user.getDeposito();
            } else {
                return -2;
            }
        }
        return -1;
    }

    public double transferirDinero(int cuentaDestino, double transferencia, int cuentaOrigen, int clave) {
        int posCuentaDestino = getPosicionPorCuenta(cuentaDestino);
        int posCuentaOrigen = getPosicionPorCuenta(cuentaOrigen);
        if (posCuentaDestino >= 0 && posCuentaOrigen >= 0 && posCuentaDestino != posCuentaOrigen) {
            Cuenta userD = cuentasUsuarios.get(posCuentaDestino);
            Cuenta userO = cuentasUsuarios.get(posCuentaOrigen);
            if (clave == userO.getClave()) {

                if (transferencia <= userO.getDeposito()) {
                    double dineroActualUserO = userO.getDeposito();
                    double dineroActualUserD = userD.getDeposito();
                    userO.setDeposito(dineroActualUserO - transferencia);
                    userD.setDeposito(dineroActualUserD + transferencia);

                    return dineroActualUserO - transferencia;
                } else {
                    return -3;
                }
            }else{
                return -2;
            }
        }
        return -1;
    }

    public double retirarDinero(int cuenta, String cedula, double retiro, int clave) {
        int posCuenta = getPosicionPorCuenta(cuenta);
        if (posCuenta >= 0) {
            Cuenta user = cuentasUsuarios.get(posCuenta);
            if (cedula.equals(user.getCedula()) && clave == user.getClave()) {
                double dineroActual = user.getDeposito();
                if (dineroActual >= retiro) {
                    user.setDeposito(dineroActual - retiro);
                    return user.getDeposito();
                }else{
                    return -3;
                }
            }else{
                return -2;
            }
        }
        return -1;
    }

    private int getPosicionPorCuenta(int cuenta) {
        for (int i = 0; i < cuentasUsuarios.size(); i++) {
            if (cuenta == cuentasUsuarios.get(i).getNumeroCuenta()) {
                return i;
            }
        }
        return -1;
    }

    private int getPosicionPorCedula(String cedula) {
        for (int i = 0; i < cuentasUsuarios.size(); i++) {
            if (cedula.equals(cuentasUsuarios.get(i).getCedula())) {
                return i;
            }
        }
        return -1;
    }

}

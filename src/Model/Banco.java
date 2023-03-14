/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author USER
 */
public class Banco {

    public static ArrayList<Cuenta> cuentasUsuarios;

    public Banco() {
        cuentasUsuarios = new ArrayList<>();
        cuentasUsuarios.add(new Cuenta("Santiago", "Velanida Gallo", "1094971551", 1234, 80000, 1));
        cuentasUsuarios.add(new Cuenta("Estefania", "Arroyave Bermudez", "1094953679", 4321, 400000, 2));
    }

    public String getUsuariosToString() {
        String usuarios = "";
        for (Cuenta cuentasUsuario : cuentasUsuarios) {
            usuarios = usuarios + cuentasUsuario.getNombre() + "&" + cuentasUsuario.getApellido() + "&"
                    + cuentasUsuario.getCedula() + "&" + cuentasUsuario.getNumeroCuenta() + "&"
                    + cuentasUsuario.getDeposito() + "%";
        }
        return usuarios;
    }

    public boolean ingresar(String cedula, int clave) {
        for (Cuenta cuentasUsuario : cuentasUsuarios) {
            if (cuentasUsuario.getCedula().equals(cedula) && clave == cuentasUsuario.getClave()) {
                return true;
            }
        }
        return false;
    }

    public boolean abrirCuenta(String nombre, String apellido, String cedula, int clave, double deposito) {
        try {

            cuentasUsuarios.add(new Cuenta(nombre, apellido, cedula, clave, deposito, cuentasUsuarios.size() + 101));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean modificarCuenta(int clave, int numeroCuenta, int cambio, String nuevoValor) {
        try {

            int posCuenta = getPosicionPorCuenta(numeroCuenta);
            if (posCuenta >= 0) {
                switch (cambio) {
                    case 1:
                        cuentasUsuarios.get(posCuenta).setNombre(nuevoValor);
                        break;
                    case 2:
                        cuentasUsuarios.get(posCuenta).setApellido(nuevoValor);
                        break;
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean cerrarCuenta(int cuenta, String motivo, int clave) {
        try {

            int posCuenta = getPosicionPorCuenta(cuenta);
            if (posCuenta >= 0) {
                if (cuentasUsuarios.get(posCuenta).getClave() == clave) {
                    cuentasUsuarios.remove(posCuenta);
                }
            }
        } catch (Exception e) {
            return false;
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
            if (transferencia < userO.getDeposito() && clave == userO.getClave()) {
                double dineroActualUserO = userO.getDeposito();
                double dineroActualUserD = userO.getDeposito();
                if (dineroActualUserO >= transferencia) {

                    dineroActualUserO = userO.getDeposito() - transferencia;
                    dineroActualUserD = userO.getDeposito() + transferencia;
                    userD.setDeposito(dineroActualUserO);
                    userO.setDeposito(dineroActualUserD);

                }
                return dineroActualUserD;
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
                }
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

}

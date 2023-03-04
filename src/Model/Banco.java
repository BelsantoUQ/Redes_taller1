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
    
    public boolean abrirCuenta(String nombre, String apellido, String cedula, int clave, double deposito){
        cuentasUsuarios.add(new Cuenta(nombre, apellido, cedula, clave, deposito, cuentasUsuarios.size()+101));
        return true;
    }
    
    public boolean modificarCuenta(int clave, int numeroCuenta, int cambio, String nuevoValor){
        int posCuenta = getPosicionPorCuenta(numeroCuenta);
        if (posCuenta>=0) {
            switch (cambio){
                case 1:cuentasUsuarios.get(posCuenta).setNombre(nuevoValor);
                    break;
                case 2:cuentasUsuarios.get(posCuenta).setApellido(nuevoValor);
                    break;
            }
            return true;
        }
        return false;
    }
    
    public boolean cerrarCuenta(int cuenta, String motivo, int clave){
        int posCuenta = getPosicionPorCuenta(cuenta);
        if (posCuenta>=0) {
            if (cuentasUsuarios.get(posCuenta).getClave()==clave)
                cuentasUsuarios.remove(posCuenta);
        }
        return false;
    }
    
    public double consignarDinero(int cuenta, String cedula, double deposito){
        int posCuenta = getPosicionPorCuenta(cuenta);
        if (posCuenta>=0) {
            Cuenta user = cuentasUsuarios.get(posCuenta);
            if (cedula.equals(user.getCedula())) {
                double dineroActual = user.getDeposito();
                user.setDeposito(dineroActual+deposito);
                return user.getDeposito();
            }
        }
        return -1;
    }
    
    public double transferirDinero(int cuentaDestino, double transferencia, int cuentaOrigen, int clave){
        int posCuentaDestino = getPosicionPorCuenta(cuentaDestino);
        int posCuentaOrigen = getPosicionPorCuenta(cuentaOrigen);
        if (posCuentaDestino>=0 && posCuentaOrigen>=0 && posCuentaDestino!=posCuentaOrigen) {
            Cuenta userD = cuentasUsuarios.get(posCuentaDestino);
            Cuenta userO = cuentasUsuarios.get(posCuentaOrigen);
            if (transferencia<userO.getDeposito() && clave==userO.getClave()) {
                double dineroActualUserO = userO.getDeposito()-transferencia;
                double dineroActualUserD = userO.getDeposito()+transferencia;
                userD.setDeposito(dineroActualUserO);
                userO.setDeposito(dineroActualUserD);
                return dineroActualUserD;
            }
        }
        return -1;
    }
    
    public double retirarDinero(int cuenta, String cedula, double retiro, int clave){
        int posCuenta = getPosicionPorCuenta(cuenta);
        if (posCuenta>=0) {
            Cuenta user = cuentasUsuarios.get(posCuenta);
            if (cedula.equals(user.getCedula()) && clave==user.getClave()) {
                double dineroActual = user.getDeposito();
                user.setDeposito(dineroActual-retiro);
                return user.getDeposito();
            }
        }
        return -1;
    }
    
    private int getPosicionPorCuenta(int cuenta){
        for (int i = 0; i < cuentasUsuarios.size(); i++) {
            if (cuenta == cuentasUsuarios.get(i).getNumeroCuenta()) {
                return i;
            }
        }
        return -1;
    }
    
}

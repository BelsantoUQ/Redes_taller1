/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Back.Controlador;

import Back.Modelo.Banco;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class Servidor {

    public static void main(String[] args) {

        ServerSocket listener = null;
        Socket serverSideSocket = null;
        DataInputStream in;
        DataOutputStream out;

        //puerto de nuestro servidor
        final int PUERTO = 5000;

        try {
            //Creamos el socket del servidor
            listener = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
            Banco miBanco = new Banco();
            //Siempre estara escuchando peticiones
            while (true) {

                //Espero a que un cliente se conecte
                serverSideSocket = listener.accept();

                System.out.println("Cliente conectado");
                in = new DataInputStream(serverSideSocket.getInputStream());
                out = new DataOutputStream(serverSideSocket.getOutputStream());

                //Leo el mensaje que me envia
                String mensaje = in.readUTF();
                System.out.println(mensaje);
                String[] solicitud = mensaje.split("&");
                double resultado;

                switch (solicitud[0]) {
                    case "0":
                        try {
                            out.writeUTF(miBanco.ingresar(solicitud[1], Integer.parseInt(solicitud[2])) ? "200" : "400");
                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "1":
                        try {
                            System.out.println("opcion 1: registrar cuenta");
                            out.writeUTF("Número de cuenta asignado: #" + miBanco.abrirCuenta(solicitud[1], solicitud[2], solicitud[3], Integer.parseInt(solicitud[4]), Double.parseDouble(solicitud[5])));

                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "2":
                        try {

                            System.out.println("opcion 2: modificar cuenta");
                            out.writeUTF(miBanco.modificarCuenta(Integer.parseInt(solicitud[1]), solicitud[2], solicitud[3], solicitud[4]) ? "Modificación exitosa" : "Error no se ha encontrado el usuario");
                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "3":
                        try {

                            out.writeUTF(miBanco.cerrarCuenta(Integer.parseInt(solicitud[1]), solicitud[2], Integer.parseInt(solicitud[3])) ? "Cancelación de cuenta exitosa" : "Error no se ha encontrado el usuario");
                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e + "\n contacte con el administrador");
                        }
                        break;

                    case "4":
                        try {

                            resultado = miBanco.consignarDinero(Integer.parseInt(solicitud[1]), solicitud[2], Double.parseDouble(solicitud[3]));
                            out.writeUTF((resultado > -1) ? "Consignación exitosa \n su saldo actual es: " + resultado : ""+resultado);
                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "5":
                        try {

                            resultado = miBanco.transferirDinero(Integer.parseInt(solicitud[1]), Double.parseDouble(solicitud[2]), Integer.parseInt(solicitud[3]), Integer.parseInt(solicitud[4]));
                            out.writeUTF((resultado > -1) ? "Tranferencia exitosa \n ahora el remitente tiene: " + resultado : ""+resultado);
                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "6":
                        try {

                            resultado = miBanco.retirarDinero(Integer.parseInt(solicitud[1]), solicitud[2], Double.parseDouble(solicitud[3]), Integer.parseInt(solicitud[4]));
                            out.writeUTF((resultado > -1) ? "Transacción exitosa \n su saldo actual es: " + resultado : ""+resultado);
                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "7":
                        try {

                            out.writeUTF("" + miBanco.getUsuariosToString());
                        } catch (Exception e) {
                            out.writeUTF("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;
                }
                try {
                    if (Integer.parseInt(solicitud[0]) > 6) {
                        out.writeUTF("¡Error! no se ha interpretado la solicitud"
                                + "\n debido a que no es una opción válida");
                    }

                } catch (NullPointerException e) {
                    out.writeUTF("¡Error! no se ha interpretado la solicitud \n"
                            + "Debido a que ha ingresado una opcion no valida: \n" + e
                            + "\n contacte con el administrador");
                }
                //Cierro el socket
                serverSideSocket.close();
                System.out.println("Cliente desconectado");
                miBanco.escribirEnArchivo();
                System.out.println("Información guardada");

            }

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

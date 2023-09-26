/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Back.Controlador;

import Back.Modelo.Banco;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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
        BufferedReader in;
        PrintWriter out;

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
                in = new BufferedReader(new InputStreamReader(serverSideSocket.getInputStream()));
                out = new PrintWriter(serverSideSocket.getOutputStream(), true);

                //Leo el mensaje que me envia
                String mensaje = in.readLine();
                System.out.println(mensaje);
                String[] solicitud = mensaje.split("&");
                double resultado;

                switch (solicitud[0]) {
                    case "0":
                        try {
                            out.println(miBanco.ingresar(descifrarTexto(solicitud[1],4), Integer.parseInt(descifrarTexto(solicitud[2], 4))) ? "200" : "400");
                        } catch (Exception e) {
                            out.println("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "1":
                        try {
                            System.out.println("opcion 1: registrar cuenta");
                            out.println("Número de cuenta asignado: #" + miBanco.abrirCuenta(descifrarTexto(solicitud[1], 4), descifrarTexto(solicitud[2], 4), descifrarTexto(solicitud[3], 4), Integer.parseInt(descifrarTexto(solicitud[4], 4)), Double.parseDouble(descifrarTexto(solicitud[5], 4))));

                        } catch (Exception e) {
                            out.println("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "2":
                        try {

                            System.out.println("opcion 2: modificar cuenta");
                            out.println(miBanco.modificarCuenta(Integer.parseInt(descifrarTexto(solicitud[1], 4)), descifrarTexto(solicitud[2], 4), descifrarTexto(solicitud[3], 4), descifrarTexto(solicitud[4], 4)) ? "Modificación exitosa" : "Error no se ha encontrado el usuario");
                        } catch (Exception e) {
                            out.println("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "3":
                        try {

                            out.println(miBanco.cerrarCuenta(Integer.parseInt(descifrarTexto(solicitud[1], 4)), descifrarTexto(solicitud[2], 4), Integer.parseInt(descifrarTexto(solicitud[3], 4))) ? "Cancelación de cuenta exitosa" : "Error no se ha encontrado el usuario");
                        } catch (Exception e) {
                            out.println("Error --> " + e + "\n contacte con el administrador");
                        }
                        break;

                    case "4":
                        try {

                            resultado = miBanco.consignarDinero(Integer.parseInt(descifrarTexto(solicitud[1], 4)), descifrarTexto(solicitud[2], 4), Double.parseDouble(descifrarTexto(solicitud[3], 4)));
                            out.println((resultado > -1) ? "Consignación exitosa \n su saldo actual es: " + resultado : ""+resultado);
                        } catch (Exception e) {
                            out.println("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "5":
                        try {

                            resultado = miBanco.transferirDinero(Integer.parseInt(descifrarTexto(solicitud[1], 4)), Double.parseDouble(descifrarTexto(solicitud[2], 4)), Integer.parseInt(descifrarTexto(solicitud[3], 4)), Integer.parseInt(descifrarTexto(solicitud[4], 4)));
                            out.println((resultado > -1) ? "Tranferencia exitosa \n ahora el remitente tiene: " + resultado : ""+resultado);
                        } catch (Exception e) {
                            out.println("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "6":
                        try {

                            resultado = miBanco.retirarDinero(Integer.parseInt(descifrarTexto(solicitud[1], 4)), descifrarTexto(solicitud[2], 4), Double.parseDouble(descifrarTexto(solicitud[3], 4)), Integer.parseInt(descifrarTexto(solicitud[4], 4)));
                            out.println((resultado > -1) ? "Transacción exitosa \n su saldo actual es: " + resultado : ""+resultado);
                        } catch (Exception e) {
                            out.println("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;

                    case "7":
                        try {

                            out.println("" + miBanco.getUsuariosToString());
                        } catch (Exception e) {
                            out.println("Error --> " + e+ "\n contacte con el administrador");
                        }
                        break;
                }
                try {
                    if (Integer.parseInt(solicitud[0]) > 6) {
                        out.println("¡Error! no se ha interpretado la solicitud"
                                + "\n debido a que no es una opción válida");
                    }

                } catch (NullPointerException e) {
                    out.println("¡Error! no se ha interpretado la solicitud \n"
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
    
    public static String descifrarTexto(String textoCifrado, int clave) {
        Map<Character, Character> tablaCifrado = crearTablaCifrado(clave);
        Map<Character, Character> tablaDescifrado = new HashMap<>();

        // Crear la tabla de descifrado invirtiendo la tabla de cifrado
        for (Map.Entry<Character, Character> entry : tablaCifrado.entrySet()) {
            tablaDescifrado.put(entry.getValue(), entry.getKey());
        }

        StringBuilder textoDescifrado = new StringBuilder();

        for (char caracter : textoCifrado.toCharArray()) {
            // Verificar si el carácter está en la tabla de descifrado
            if (tablaDescifrado.containsKey(caracter)) {
                textoDescifrado.append(tablaDescifrado.get(caracter));
            } else {
                // Si el carácter no está en la tabla, mantenerlo sin cambios
                textoDescifrado.append(caracter);
            }
        }

        return textoDescifrado.toString();
    }

    private static Map<Character, Character> crearTablaCifrado(int clave) {
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

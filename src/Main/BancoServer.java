/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.Banco;
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
public class BancoServer {
    public static void main(String[] args) {
        
        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in;
        DataOutputStream out;
        
        //puerto de nuestro servidor
        final int PUERTO = 5000;

        try {
            //Creamos el socket del servidor
            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
            Banco miBanco= new Banco();
            //Siempre estara escuchando peticiones
            while (true) {

                //Espero a que un cliente se conecte
                sc = servidor.accept();

                System.out.println("Cliente conectado");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());

                //Leo el mensaje que me envia
                String mensaje = in.readUTF();
                System.out.println(mensaje);
                String[] solicitud = mensaje.split("&");
                
                switch (solicitud[0]){
                    case "0": out.writeUTF(miBanco.ingresar(solicitud[1], Integer.parseInt( solicitud[2] ))?"200":"400");
                        break;
                    
                    case "1": out.writeUTF(miBanco.abrirCuenta(solicitud[1], solicitud[2], solicitud[3], Integer.parseInt( solicitud[4] ), Double.parseDouble(solicitud[5]))?"200":"No se pudo crear la cuenta");
                        break;
                    
                    case "2": out.writeUTF(miBanco.modificarCuenta(Integer.parseInt(solicitud[1]), Integer.parseInt(solicitud[2]), Integer.parseInt(solicitud[3]), solicitud[4])?"200":"No se pudo modificar la cuenta");
                        break;
                    
                    case "3": out.writeUTF(miBanco.cerrarCuenta(Integer.parseInt( solicitud[1] ), solicitud[2], Integer.parseInt( solicitud[3] ))?"200":"No se pudo cerrar la cuenta");
                        break;
                    
                    case "4": out.writeUTF(miBanco.consignarDinero(Integer.parseInt( solicitud[1] ), solicitud[2], Double.parseDouble(solicitud[3]))!=-1?"200":"No se pudo realizar el deposito");
                        break;
                    
                    case "5": out.writeUTF(miBanco.transferirDinero(Integer.parseInt( solicitud[1] ), Double.parseDouble(solicitud[2]), Integer.parseInt( solicitud[3] ), Integer.parseInt( solicitud[4] ))!=-1?"200":"No se pudo transferir dinero");
                        break;
                    
                    case "6": out.writeUTF(miBanco.retirarDinero(Integer.parseInt( solicitud[1] ), solicitud[2], Double.parseDouble(solicitud[3]), Integer.parseInt( solicitud[4] ))!=-1?"200":"No se pudo realizar el retiro");
                        break;
                        
                    case "7": out.writeUTF(miBanco.getUsuariosToString());
                        break;
                    
                }
                
                //Cierro el socket
                sc.close();
                System.out.println("Cliente desconectado");

            }

        } catch (IOException ex) {
            Logger.getLogger(BancoServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

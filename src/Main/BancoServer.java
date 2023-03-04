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
                    case "1": miBanco.abrirCuenta(solicitud[1], solicitud[2], solicitud[3], Integer.parseInt( solicitud[4] ), Double.parseDouble(solicitud[5]));
                        break;
                    
                    case "2": miBanco.modificarCuenta(Integer.parseInt(solicitud[1]), Integer.parseInt(solicitud[2]), Integer.parseInt(solicitud[3]), solicitud[4]);
                        break;
                    
                    case "3": miBanco.cerrarCuenta(Integer.parseInt( solicitud[1] ), solicitud[2], Integer.parseInt( solicitud[3] ));
                        break;
                    
                    case "4": miBanco.consignarDinero(Integer.parseInt( solicitud[1] ), solicitud[2], Double.parseDouble(solicitud[3]));
                        break;
                    
                    case "5": miBanco.transferirDinero(Integer.parseInt( solicitud[1] ), Double.parseDouble(solicitud[2]), Integer.parseInt( solicitud[3] ), Integer.parseInt( solicitud[4] ));
                        break;
                    
                    case "6": miBanco.retirarDinero(Integer.parseInt( solicitud[1] ), solicitud[2], Double.parseDouble(solicitud[3]), Integer.parseInt( solicitud[4] ));
                        break;
                    
                    default:
                        out.writeUTF("¡Error! no se ha interpretado la solicitud");                
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

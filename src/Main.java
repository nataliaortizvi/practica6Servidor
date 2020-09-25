
import processing.core.PApplet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

import java.net.UnknownHostException;
import java.net.InetAddress;

import com.google.gson.Gson;

import model.Usuario;


public class Main extends PApplet {
	
	String ingresarDatos, mal, bien;
	int pantalla = 0;
	Usuario[] userR;
	
	//variables tcp
	ServerSocket server;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("Main");
		
	}
	
	public void settings() {
		size(500,500);
	}
	
	public void setup() {
		
		//saber cual es mi ip para colocarla en el socket del cliente
		 try {
	            InetAddress n = InetAddress.getLocalHost();
	            String ip = n.getHostAddress();
	            System.out.println(ip);

	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }

		
		iniciarServer();
		
		ingresarDatos = "Ingrese su usuario\n y contraseña desde\n su teléfono móvil";
		bien = "Bienvenido";
		
	}
	
	public void draw() {
		background(250,230,180);
		
		switch(pantalla) {
		case 0:
			//primera pantalla
			fill(250,120,95);
			text (ingresarDatos, 250,200);
			textSize(35);
			textAlign(CENTER);
			break;
		case 1:
			//pantalla bienvenido
			fill(250,120,95);
			text (bien, 250,230);
			textSize(35);
			textAlign(CENTER);
			break;
		}
	
	}
	
	public void iniciarServer() {
		
		new Thread(
				() -> {
					
					try {
						server = new ServerSocket(5000);
						System.out.println("Esperando conexion");
						socket = server.accept();
						System.out.println("Cliente conectado");
						
						//emisor
						OutputStream os = socket.getOutputStream();
						writer = new BufferedWriter (new OutputStreamWriter(os));
						
						//receptor
						InputStream is = socket.getInputStream();
						reader = new BufferedReader (new InputStreamReader(is));
						
						while(true) {
							//linea recibida (json de usuario)
							String line = reader.readLine();
							
							Gson gson = new Gson();
							//creo un usuario con el json que recibi del usuario de cliente
							Usuario user = gson.fromJson(line, Usuario.class);
							
			//////////////////////////////creo 3 usuarios inventados que ya estan registrados//////////////////////////////
							Usuario[] userR = new Usuario[3];
							userR[0] = new Usuario("nataliaortiz4","apaches99");
							userR[1] = new Usuario("jotica22","lagartija01");
							userR[2] = new Usuario("ladiabla81","campanita00");
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////			
							
			
							
							System.out.println("el que llega   "+user.getUsuario());
							
							
							//recorro el arreglo para verificar si el usuario que llego coincide con alguno ya registrado
							for(int i = 0; i< 3; i++) {
								if(userR[i].getUsuario().equals(user.getUsuario())){
									System.out.println("EXITO");
									
									//si coincide el usuario, ahora se valida con la contraseña
									if(userR[i].getContraseña().equals(user.getContraseña())){
										//cambio a la pantalla de bienvenido
										pantalla = 1;
									}
									
								}else {
									System.out.println("FALLIDO");
								}
							}
					
							
							//mando mensajes con el metodo sendMessage
							
							//si la pantalla esta en bienvenido entonces mando un mensaje de bueno
							if(pantalla == 1) {
								sendMessage("good");
							}
							
							//si la pantalla esta en inicio entonces mando un mensaje de malo
							if(pantalla == 0) {
								sendMessage("bad");
							}
						}
						
						
						
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			).start();
		
	}
	
	public void sendMessage(String mensaje) {
		
		new Thread(
				() -> {
					try {
						writer.write(mensaje + "\n");
						writer.flush();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			).start();
	}

}

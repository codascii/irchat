package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class TheServer {
	
	private static final Logger LOG = Logger.getLogger(TheServer.class.getName());
	private boolean isStarted = false;
	
	public void demarer() {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		Socket socket = null;
		OutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		ServerSocket server = null;
		
		try {
			server = new ServerSocket(1997,100);
		
			while(true) {
				try {
		
					Socket clientSocket = server.accept(); // méthode bloquante : le programme arrête de s'exécuter
					
					in = clientSocket.getInputStream();
					isr = new InputStreamReader(in);
					br = new BufferedReader(isr);
					
					String s = br.readLine();
					
					while (s != null) {
						
						s = br.readLine();
					}
					
				} catch (IOException e) {
					LOG.error("Error during socket Manipulation",e);
					break;
				} finally {
					Utils.close(br);
					Utils.close(isr);
					Utils.close(in);
				}
			}
		} catch (IOException e1) {
			LOG.error("Error during socket manipulation",e1);
		} finally {
			Utils.close(server);
		}
	}
	
	public void arreter() {
		System.exit(0);
	}

}

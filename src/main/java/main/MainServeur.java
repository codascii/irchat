package main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class MainServeur extends Thread {
	private static final Logger LOG = Logger.getLogger(MainServeur.class.getName());
	public static final List<CChatteur> CLIENT_LIST = new LinkedList<>();
	
	private ServerSocket server = null;
	private Socket clientSocket = null;
	public static boolean isStarted;
	
	public MainServeur() {
		try {
			Drapeau.s.acquire();
			isStarted = false;
			Drapeau.s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	public void arreterServeur() {
		isStarted = false;
		this.interrupt();
	}
	
	public void lancerServeur() {
		try {
			Drapeau.s.acquire();
			isStarted = true;
			this.start();
			Drapeau.s.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String msg, Socket s) {
		  
		  OutputStream os = null;
		  OutputStreamWriter osw = null;
		  BufferedWriter bw = null;		  
		  
		  try {
		   os = s.getOutputStream();
		   osw = new OutputStreamWriter(os, "UTF-8");
		   bw = new BufferedWriter(osw, 8192);
		   
		   bw.write(msg);
		   bw.flush();
		   
		  } catch (IOException e) {
		   
		   e.printStackTrace();
		  } 
	}

	@Override
	public void run() {

		try {
			server = new ServerSocket(6667);
			LOG.info("Start Socket Server on port : " + 6667);
			while (isStarted) {
				clientSocket = server.accept();
				new ClientThread(clientSocket).start();
			}

		} catch (IOException e) {
			LOG.error("Error during socket execution.", e);
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				LOG.error("Error during socket stream closing.", e);
			}
		}

		System.exit(0);
	}
}

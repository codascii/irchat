package main;

import java.io.*;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ClientThread extends Thread {
	private static final Logger LOG = Logger.getLogger(ClientThread.class.getName());
	private final Socket s;
	private Interpreteur i;

	public ClientThread(Socket s) {
		this.s = s;
		this.i= new Interpreteur();
	}

	@Override
	public void run() {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			in = s.getInputStream();
			isr = new InputStreamReader(in, "UTF-8");
			br = new BufferedReader(isr);
			
			while (MainServeur.isStarted) {
				try {
					Drapeau.s.acquire();
					if(MainServeur.isStarted) {
						this.i.interpreter(br.readLine(), this.s);
						//System.out.println(br.readLine());
					}
					Drapeau.s.release();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			LOG.error("Error during IO execution.", e);
		}

	}
}

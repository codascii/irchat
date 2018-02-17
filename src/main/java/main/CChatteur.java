package main;

import java.net.Socket;

public class CChatteur {
	private String pseudo;
	private String chan;
	private Socket sock;
	
	public CChatteur(String pseudo, Socket sock) {
		this.pseudo = pseudo;
		this.sock = sock;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getChan() {
		return chan;
	}
	public void setChan(String chan) {
		this.chan = chan;
	}
	public Socket getSock() {
		return sock;
	}
	public void setSock(Socket sock) {
		this.sock = sock;
	}

	

}

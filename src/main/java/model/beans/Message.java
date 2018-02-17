package model.beans;

import java.util.Date;

import main.Interpreteur;

public class Message {
	private int id;
	private int user_id;
	private String pseudo;
	private Date date;
	private Date dateLastCo;

	private String content;
	private String chan;
	
	public Message() {};
	public Message(int id, int user_id, Date date, String content, String chan) {
		this.id = id;
		this.user_id = user_id;
		this.date = date;
		this.content = content;
		this.chan = chan;
	}
	
	@Override
	public String toString() { 
		return this.id + " " + this.user_id + " " + this.date + " " + this.content + " " + this.chan;
	}
	
	public int getId() 	{ 
		return this.id; 
	}
	public void setId(int id) { 
		this.id = id; 
	}
	
	public int getUser_id() { 
		return this.user_id; 
	}
	
	public void setUser_id(int user_id) { 
		this.user_id = user_id; 
	}
	
	public Date getDate() 							{ 
		return this.date; 
	}
	public Message setDate(Date date) 					{ 
		this.date = date;
		return this;
	}
	public String getContent() 							{ 
		return this.content; 
	}
	public Message setContent(String content) 				{ 
		this.content = content; 
		return this;
	}
	
	public String getChan() {
		return this.chan;
	}
	
	public Message setChan(String chan) {
		this.chan = chan;
		return this;
	}
	public String getPseudo() {
		return pseudo;
	}
	public Message setPseudo(String pseudo) {
		this.pseudo = pseudo;
		return this;
	}

	public Date getDateLastCo() {
		return dateLastCo;
	}
	public Message setDateLastCo(Date dateLastCo) {
		this.dateLastCo = dateLastCo;
		return this;
	}
	
	public StringBuilder toJSON(String cmd) {
		StringBuilder json = new StringBuilder(" ");
		if(cmd.equals(Interpreteur.CMD_CONNECT)) {
			json.append("{\"connect\" : {\"pseudo\" : \"");
			json.append(this.pseudo);
			json.append("\",\"date\": \"");
			json.append(this.date);
			json.append("\",\"lastconnect\": \"");
			json.append(this.dateLastCo);
			json.append("\"}}");
		} else if(cmd.equals(Interpreteur.CMD_QUIT)) {
			json.append("{\"discochannel\":{\"pseudo\":\"");
			json.append(this.pseudo);
			json.append("\",\"channel\":\"");
			json.append(this.chan);
			json.append("\",\"date\":\"");
			json.append(this.date);
			json.append("\",\"content\":\"");
			json.append(this.content);
			json.append("\"}}");
		} else if(cmd.equals(Interpreteur.CMD_MSG)) {
			json.append("{\"message\":{\"pseudo\":\"");
			json.append(this.pseudo);
			json.append("\",\"date\":\"");
			json.append(this.date);
			json.append("\",\"content\":\"");
			json.append(this.content);
			json.append("\"}}");
			
		}
		
		json.append("\r\n");
		return json;
	}
}

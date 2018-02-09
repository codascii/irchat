package model.beans;

import java.sql.Date;

public class Message {
	private int id;
	private int user_id;
	private String pseudo;
	private Date date;
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
	public void setId(int id) 							{ 
		this.id = id; 
	}
	public int getUser_id() 							{ 
		return this.user_id; 
	}
	public void setUser_id(int user_id) 				{ 
		this.user_id = user_id; 
	}
	public Date getDate() 							{ 
		return this.date; 
	}
	public void setDate(Date date) 					{ 
		this.date = date; 
	}
	public String getContent() 							{ 
		return this.content; 
	}
	public void setContent(String content) 				{ 
		this.content = content; 
	}
	
	public String getChan() {
		return this.chan;
	}
	
	public void setChan(String chan) {
		this.chan = chan;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
}

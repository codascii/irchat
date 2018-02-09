package model.beans;

import java.sql.Date;

public class User {
	private int id;
	private String pseudo;
	private Date first_co;
	private Date last_co;
	
	public User() {}
	public User(int id) {
		this.id = id;
	}
	
	public User(String p) {
		this.pseudo = p;
	}
	
	public User(int id, String pseudo, Date first_co, Date last_co) {
		this.id = id; 
		this.pseudo = pseudo;
		this.first_co = first_co;
		this.last_co = last_co;
	}
	
	@Override
	public String toString() 						{ 
		return this.id + " " + this.pseudo + " " + this.first_co + " " + this.last_co; 
		}
	
	
	public void setId(int id) 						{ 
		this.id = id; 
		}
	public int getId()								{ 
		return this.id; 
		}
	public void setPseudo(String pseudo)			{ 
		this.pseudo = pseudo; 
		}
	public String getPseudo()						{ 
		return this.pseudo; 
		}
	public void setFirst_co(Date first_co)			{ 
		this.first_co = first_co; 
		}
	public Date getFirst_co()						{ 
		return this.first_co; 
		}
	public void setLast_co(Date last_co)			{ 
		this.last_co = last_co; 
		}
	public Date getLast_co()						{ 
		return this.last_co; 
		}

}

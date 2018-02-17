package main;

import java.net.Socket;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.beans.User;
import model.dao.DAOFactory;
import model.dao.DAOMessage;
import model.dao.DAOUser;
import model.dao.exceptions.DAOException;

public class Interpreteur {
	
	//private static Pattern pattern;
	//private static Matcher matcher;
	
	public static String CMD_CONNECT;
	public static String CMD_QUIT;
	public static String CMD_JOIN;
	public static String CMD_MSG;
	public static String CMD_EXIT;
	public static String CMD_CHANNELS;	
	public static Connection c = null;
	
	private String commandeSend;
	private DAOUser daou = null;
	private DAOMessage daom = null;
	
	
	static {
		CMD_CONNECT = "#CON";
		CMD_QUIT = "#QUI";
		CMD_JOIN = "#JOI";
		CMD_MSG = "#MSG";
		CMD_EXIT = "#EXI";
		CMD_CHANNELS = "#CHA";
		
		try {
			c = DAOFactory.getConnection();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public Interpreteur() {
		if(c != null) {
			daou = DAOFactory.getDAOUser(c);
			daom = DAOFactory.getDAOMessage(c);
		}
		commandeSend = new String("DEFAULT COMMANDE");
	}
	
	public static boolean commandFind(String cmd) {
		
		if(cmd != null) {
			final Pattern pattern = Pattern.compile("^(((#CONNECT|#JOIN)\\s[\\w]+)|((#QUIT|#EXIT)((\\s){1}[\\w\\W]*)?)|(#MSG(\\s){1}[\\w\\W]+)|#CHANNELS)$");
			final Matcher matcher = pattern.matcher(cmd);
			
			return (matcher.find()) ? true : false;
		} else {
			return false;
		}
	}
	
	/*public void interpreter(String message, Socket s) {
		if(message != null) {
			pattern = Pattern.compile("^(((#CONNECT|#JOIN)\\s[\\w]+)|((#QUIT|#EXIT)((\\s){1}[\\w\\W]*)?)|(#MSG(\\s){1}[\\w\\W]+)|#CHANNELS)$");
			matcher = pattern.matcher(message);
			
			if(matcher.find()) {
				commandeSend = message.substring(0, 4);
					
				if(commandeSend.equals(CMD_CONNECT) ) {
					
					//	Prend tout les caractères à partir du neuvième.
					final String pseudo = message.substring(9);
					User user = new User(pseudo);

					if(daou != null) {
						try {
							user = daou.findByUser(user);
						} catch (DAOException e) {

							try {
								user = daou.create(user);
								System.out.println(user);
							} catch (DAOException e1) {
								e1.printStackTrace();
							}
						}
						
						final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						
						// TODO : Envoyer le JSON au client
						final String messageToSend = "{\"connect\" : {\"pseudo\" : \""+user.getPseudo()+"\",\"date\": \""+date+"\",\"lastconnect\": \""+user.getLast_co()+"\"}}";
						MainServeur.sendMessage(messageToSend, s);
						
						// Gestion de la mise de la date de dernière connexion
						java.sql.Date curdtae = new java.sql.Date(Calendar.getInstance().getTime().getTime());
						user.setLast_co(curdtae);
						try {
							daou.update(user);
						} catch (DAOException e) {
							e.printStackTrace();
						}
					}

				} else if (commandeSend.equals(CMD_QUIT)) {
					MainServeur.sendMessage("T'as envoyé la commande #QUIT", s);
				} else if (commandeSend.equals(CMD_JOIN)) {
					MainServeur.sendMessage("T'as envoyé la commande #JOIN", s);
				} else if (commandeSend.equals(CMD_MSG)) {
					MainServeur.sendMessage("T'as envoyé la commande #MSG", s);
				} else if (commandeSend.equals(CMD_EXIT)) {
					MainServeur.sendMessage("T'as envoyé la commande #EXIT", s);
				} else if (commandeSend.equals(CMD_CHANNELS)) {
					try {
						List<String> channels = daom.getChannels();						
						String messageToSend = "{\"channels\" : [\"";//#Dota2\",\"#Minecraft\",\"#LoL\"]}";
						
						for(String ss : channels) {
							if(!channels.get(channels.size() - 1).equals(ss)) {
								messageToSend = messageToSend + ss + "\",\"";
							} else {
								messageToSend = messageToSend + ss + "\"";
							}
						}
						
						messageToSend += "]}";

						// Elever la dernière virgule avant d'envoyer les channels
						
						MainServeur.sendMessage(messageToSend, s);
					} catch (DAOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("Command error !!");
				}			
			} else {
				System.out.println("Erreur de commande !!");
			}
		}
	}*/
	
}

package main;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import model.beans.Message;
import model.beans.User;
import model.dao.DAOFactory;
import model.dao.DAOMessage;
import model.dao.DAOUser;
import model.dao.exceptions.DAOException;

public class ClientThread extends Thread {
	private static final Logger LOG = Logger.getLogger(ClientThread.class.getName());
	//private Socket s;
	private final Socket s;
	private Interpreteur i;
	private String messageRecu;
	private String command;
	
	public static Connection c = null;
	private DAOUser daou = null;
	private DAOMessage daom = null;
	
	static {		
		try {
			c = DAOFactory.getConnection();
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	public ClientThread(Socket ircSocket) {
		if(c != null) {
			daou = DAOFactory.getDAOUser(c);
			daom = DAOFactory.getDAOMessage(c);
		}
		
		this.s = ircSocket;
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
				//try {
					//Drapeau.s.acquire();
					if(MainServeur.isStarted) {
						//this.i.interpreter(br.readLine(), this.s);
						//System.out.println(br.readLine());
						messageRecu = br.readLine();
						
						if(Interpreteur.commandFind(messageRecu)) {
							//	Extraction de la commande envoyés
							command = messageRecu.substring(0, 4);
							
							if(command.equals(Interpreteur.CMD_CONNECT) ) {
//								Prend tout les caractères à partir du neuvième.
								final String pseudo = messageRecu.substring(9);
								/*final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								final String date = sdf.format(new Date());*/

								final long millis= java.lang.System.currentTimeMillis();  
								final java.sql.Date d = new java.sql.Date(millis);
								
								User user = new User(pseudo);
								final Message message = new Message();

								if(daou != null) {
									try {
										user = daou.findByUser(user);
									} catch (DAOException e) {

										try {
											user = daou.create(user);
								
										} catch (DAOException e1) {
											e1.printStackTrace();
										}
									}
									
									
									message.setPseudo(user.getPseudo())
										   .setDate(d)
										   .setDateLastCo(user.getLast_co());
									
																		
									// TODO : Envoyer le JSON au client
									//final String messageToSend = "{\"connect\" : {\"pseudo\" : \""+user.getPseudo()+"\",\"date\": \""+date+"\",\"lastconnect\": \""+user.getLast_co()+"\"}}";
									final CChatteur chat = new CChatteur(user.getPseudo(), s);
									MainServeur.CLIENT_LIST.add(chat);
									
									
									// Gestion de la mise de la date de dernière connexion
									//java.sql.Date curdtae = new java.sql.Date(Calendar.getInstance().getTime().getTime());
									//user.setLast_co(curdtae);
									user.setLast_co(d);

									try {
										daou.update(user);
									} catch (DAOException e) {
										e.printStackTrace();
									}
									
									// Envoie du message
									for(CChatteur chatteur: MainServeur.CLIENT_LIST) {
										//On averti tout le monde de l'arrivé de notre ami le chatteur.
										//MainServeur.sendMessage(messageToSend, chatteur.getSock());
										MainServeur.sendMessage(message.toJSON(Interpreteur.CMD_CONNECT).toString(), chatteur.getSock());
									}
								}
								
							}  else if (command.equals(Interpreteur.CMD_QUIT)) {
								
								for(CChatteur chatteur: MainServeur.CLIENT_LIST) {
									if(s.equals(chatteur.getSock())) {
										// Le client a envoyé un message
										final String actualChatteur = chatteur.getPseudo();
										final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
										String content;
										if(messageRecu.length() > 6) {
											content = messageRecu.substring(6);
										} else {
											content = "";
										}
										
										final long millis= java.lang.System.currentTimeMillis();  
										final java.sql.Date d = new java.sql.Date(millis);
										
										final Message message = new Message();
										message.setPseudo(actualChatteur)
											   .setChan(chatteur.getChan())
											   .setDate(d)
											   .setContent(content);
										
										for(CChatteur cc: MainServeur.CLIENT_LIST) {
												
											final Socket c = cc.getSock();
											//final String json = "{\"discochannel\":{\"pseudo\":\""+actualChatteur+"\",\"channel\":\""+chatteur.getChan()+"\",\"date\":\""+date+"\",\"content\":\""+content+"\"}}";
											if(chatteur.getChan().equals(cc.getChan()) && !chatteur.getSock().equals(c)) {
												//MainServeur.sendMessage(json, cc.getSock());
												MainServeur.sendMessage(message.toJSON(Interpreteur.CMD_QUIT).toString(), chatteur.getSock());
											}
										}
										
										
										int index = MainServeur.CLIENT_LIST.indexOf(chatteur);
										MainServeur.CLIENT_LIST.get(index).setChan(null);
										
									}
								}
								
							} else if (command.equals(Interpreteur.CMD_JOIN)) {
								for(CChatteur chatteur : MainServeur.CLIENT_LIST) {
									if(s.equals(chatteur.getSock())) {
										final String chan = messageRecu.substring(6);
										chatteur.setChan(chan);
										MainServeur.sendMessage("Vous avez rejoins le chan : "+chan+"\r\n", s);
									}
								}
							} else if (command.equals(Interpreteur.CMD_MSG)) {
								final String content = messageRecu.substring(5);
								
								final Message message = new Message();
								
								
								for(CChatteur chatteur: MainServeur.CLIENT_LIST) {
									if(s.equals(chatteur.getSock()) && chatteur.getChan() != null) {
										// Le client a envoyé un message
										final String actualChatteur = chatteur.getPseudo();								
										final long millis= java.lang.System.currentTimeMillis();  
										final java.sql.Date d = new java.sql.Date(millis);
										
										message.setPseudo(actualChatteur)
										   .setDate(d)
										   .setContent(content)
										   .setChan(chatteur.getChan());
										User u = new User();
										u.setPseudo(actualChatteur);
										try {
											message.setUser_id(daou.findIdByUser(u));
											daom.create(message);
										} catch (DAOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										for(CChatteur cc: MainServeur.CLIENT_LIST) {
												
											//final String json = "{\"message\":{\"pseudo\":\""+actualChatteur+"\",\"date\":\""+date+"\",\"content\":\""+message+"\"}}";
											if(chatteur.getChan().equals(cc.getChan())) {
												//MainServeur.sendMessage(json, cc.getSock());
												MainServeur.sendMessage(message.toJSON(Interpreteur.CMD_MSG).toString(), cc.getSock());
											}
										}
										
									}
								}
								
							} else if (command.equals(Interpreteur.CMD_EXIT)) {
								for(CChatteur chatteur: MainServeur.CLIENT_LIST) {
									if(s.equals(chatteur.getSock())) {
										// Le client a envoyé un message
										final String actualChatteur = chatteur.getPseudo();
										final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
										String content;
										if(messageRecu.length() > 6) {
											content = messageRecu.substring(6);
										} else {
											content = "";
										}
										User u = new User();
										u.setPseudo(actualChatteur);
										try {
											daou.updateLast_co(u);
										} catch (DAOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										for(CChatteur cc: MainServeur.CLIENT_LIST) {
												
											final Socket c = cc.getSock();
											final String json = "{\"discoserver\":{\"pseudo\":\""+actualChatteur+"\",\"date\":\""+date+"\",\"content\":\""+content+"\"}}\r\n";
											if(!chatteur.getSock().equals(c)) {
												MainServeur.sendMessage(json, cc.getSock());
											}
										}
										
										
										int index = MainServeur.CLIENT_LIST.indexOf(chatteur);
										MainServeur.CLIENT_LIST.remove(index);
										
									}
								}
								
							} else if (command.equals(Interpreteur.CMD_CHANNELS)) {
								List<String> channels = new LinkedList<>();
								String messageToSend = "{\"channels\" : [";//#Dota2\",\"#Minecraft\",\"#LoL\"]}";
								boolean flag = false;
								for(CChatteur chatteur : MainServeur.CLIENT_LIST) {
									if(chatteur.getChan() != null) {
										if(!channels.contains(chatteur.getChan())) {
											channels.add(chatteur.getChan());
											messageToSend += "\""+chatteur.getChan()+"\",";
											flag = true;
										}
									}
								}
								if(flag == true) {
									messageToSend = messageToSend.substring(0,messageToSend.length()-1);
								}
								messageToSend += "]}\r\n";

								// Elever la dernière virgule avant d'envoyer les channels
								
								MainServeur.sendMessage(messageToSend, s);
							} else {
								// Ce else ne sert à rien
							}
						} else {
							// Envoyer : Erreur de commande
						}
					}
					/*Drapeau.s.release();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			
		} catch (IOException e) {
			LOG.error("Error during IO execution.", e);
		}

	}
}

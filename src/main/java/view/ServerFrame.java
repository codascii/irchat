package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.MainServeur;
import model.beans.Message;
import model.beans.User;
import model.dao.DAOFactory;
import model.dao.DAOMessage;
import model.dao.exceptions.DAOException;

public class ServerFrame extends JFrame {

	private static final long serialVersionUID = -7319784823079373838L;
	private static final String ID_TEXTFIELD_PLACEHOLDER;
	private static Connection con;
	private Container app_container;	
	private static final JLabel labelListChannels;
	private static DAOMessage daom;
	private JComboBox<String> cbChannels;
	private JTextField jtfIdUser;
	private JScrollPane logsAreaScrollPane = null;
	private JTextArea logsArea;
	private JButton btStart;
	private JButton btStop;
	private List<Message> logs;
	private MainServeur server = null;
	
	static {
		ID_TEXTFIELD_PLACEHOLDER = "Saisir l'id user";
		labelListChannels = new JLabel("Liste des channels :");
		labelListChannels.setBounds(10, 10, 120, 25);
		
		try {
			con = DAOFactory.getConnection();
			daom = DAOFactory.getDAOMessage(con);
		} catch (DAOException e) {
			// Traiter l'exception
			e.printStackTrace();
		}
	}

	// MOH 2017/11/25
	public ServerFrame() {
		super("NFP-121 : Interface serveur");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.initializeServerFrame();
		this.addAllActionListener();
		
		try {
			//	Récupération de la liste des channels et enregistrement dans le combobox
			for(String s : daom.getChannels()) {
				cbChannels.addItem(s);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		this.server = new MainServeur();
		this.showLogs();	
		this.setSize(640, 480);
		this.setResizable(false);	
		this.setVisible(true);		
	}
	
	// MOH 2018/01/29
	private void initializeServerFrame() {

		// Récupération et initialisation du conteuneur
		this.app_container = this.getContentPane();
		this.app_container.setLayout(null);
		
		// Ajout du label : "Liste des channels :" sur l'interface
		this.app_container.add(ServerFrame.labelListChannels);
		
		// Ajout du combo box sur l'interface
		this.cbChannels = new JComboBox<String>();
		this.cbChannels.setBounds(130, 10, 120, 25);
		this.app_container.add(this.cbChannels);
		
		// Ajout de la zone de saisie de l'identifiant du user auquel on veut voir les logs
		this.jtfIdUser = new JTextField(ServerFrame.ID_TEXTFIELD_PLACEHOLDER);
		this.jtfIdUser.setBounds(260, 10, 100, 25);
		this.jtfIdUser.setForeground(Color.GRAY);
		this.app_container.add(this.jtfIdUser);
		
		// Ajout de la zone d'affichage des logs dans un scroll pane
		this.logsArea = new JTextArea();
		this.logsArea.setEditable(false); 	//	On interdit la modification sur ce champ
		this.logsAreaScrollPane = new JScrollPane(this.logsArea);		
		this.logsAreaScrollPane.setBounds(10, 50, 610, 340);
		this.app_container.add(this.logsAreaScrollPane);
		
		// Ajout du bouton "Lancer" sur l'interface
		this.btStart = new JButton("Lancer");
		this.btStart.setBounds(355, 410, 120, 25);
		this.app_container.add(this.btStart);
		
		// Ajout du bouton "Arrêter" sur l'interface
		this.btStop = new JButton("Arrêter");
		this.btStop.setBounds(500, 410, 120, 25);
		this.app_container.add(this.btStop);
	}
	
	// MOH 2018/01/29
	private void addAllActionListener() {
		/**************************************************************/
		/** - Listener sur le combo box des channels :
		/** - Lorsque l'on sélectionne un channel, le système récupère 
		/**   les logs assosiés à ce channel
		/*************************************************************/
		this.cbChannels.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final String itemSelected = (String) cbChannels.getSelectedItem();
					
				try {
					logs = ServerFrame.daom.findByChan(itemSelected);
					showLogs();
				} catch (DAOException e1) {
					e1.printStackTrace();
				}
						
			}
		});
		
		/**************************************************************/
		/** -
		/*************************************************************/
		this.jtfIdUser.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (jtfIdUser.getText().equals(ID_TEXTFIELD_PLACEHOLDER)) {
		        	jtfIdUser.setText("");
		        	jtfIdUser.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (jtfIdUser.getText().isEmpty()) {
		        	jtfIdUser.setForeground(Color.GRAY);
		        	jtfIdUser.setText(ID_TEXTFIELD_PLACEHOLDER);
		        }
		    }
		});
		
		/**************************************************************/
		/** -
		/*************************************************************/
		this.jtfIdUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final String idUser = jtfIdUser.getText();
				
				try {
					final int id = Integer.parseInt(idUser);

					logs = ServerFrame.daom.findByUser(new User(id));
					showLogs();
					//for(Message log : daom.findByUser(new User(id))) {
						//cbChannels.addItem(s);
					//}
				} catch(NumberFormatException ex) {
					JOptionPane.showMessageDialog(new JFrame(), "Veuillez saisir un numérique svp.", 
							"IRC : Erreur de type de données", JOptionPane.ERROR_MESSAGE);
				} catch(DAOException ex) {
					JOptionPane.showMessageDialog(new JFrame(), "Aucune trace d'un message avec l'identifiant utilisateur saisie.", 
							"IRC : Erreur de type de données", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
	
		/**************************************************************/
		/** -
		/*************************************************************/
		this.btStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Lancement du serveur				
				if(!server.isStarted()) {
					server.lancerServeur();
				}
			}
		});
		
		/**************************************************************/
		/** -
		/*************************************************************/
		this.btStop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				// Arrêt du serveur				
				if(server.isStarted()) {
					System.out.println("Stoper le serveur.");
					server.arreterServeur();// Ça ne fonctionne pas ça.
				}
			}
		});
	}
	
	// MOH 2018/01/28
	private void showLogs() {
		this.logsArea.setText("");
		for(Message m : this.logs) {
			logsArea.setText(logsArea.getText() + m.getId()+ "\t"+ m.getPseudo() + "\t"+m.getDate().toString()+"\t"+m.getChan()+"\t"+m.getContent() + "\n");
		}
	}

}

package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.dao.exceptions.DAOException;

public class DAOFactory {

	private DAOFactory() {
		// Useless
	}

	public static Connection getConnection() throws DAOException {
		Connection c;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/irc_bdd?useSSL=false", "root", "");

			return c;

		} catch (ClassNotFoundException e) {
			throw new DAOException("Erreur durant le pilote est introuvable.", e);
		} catch (SQLException e) {
			throw new DAOException("Erreur durant l'utilisation de SQL.", e);
		}
	}
	
	public static DAOUser getDAOUser(Connection c) {
		return new DAOUser(c);
	}
	
	public static DAOAddress getDAOAddress(Connection c) {
		return new DAOAddress(c);
	}
	
	public static DAOMessage getDAOMessage(Connection c) {
		return new DAOMessage(c);
	}

}

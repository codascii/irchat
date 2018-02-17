package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import model.beans.User;
import model.dao.exceptions.DAOException;
import model.dao.utils.DAOUtils;
import model.dao.DAO;

public class DAOUser extends DAO<User>{

	protected DAOUser (Connection c) { 
		super(c); 
		}
	
	//public List<User> findByUser(User us) throws DAOException  {
	public User findByUser(User us) throws DAOException  {
		final String sql = "SELECT * FROM `user` WHERE `username` = ?";
		PreparedStatement st = null;
		ResultSet r = null;
		//List<User> listu = null;
		
		try {
			st = connect.prepareStatement(sql);
			st.setString(1, us.getPseudo());
			r = st.executeQuery();
			//listu = new LinkedList<User>();
			if(r.next()) {
				final User u = new User();
				u.setId(r.getInt("id"));
				u.setPseudo(r.getString("username"));
				u.setFirst_co(r.getDate("first_co"));
				u.setLast_co(r.getDate("last_co"));
				//listu.add(u);
				return u;
				
			}
			
			throw new DAOException("User not found");
			
		} catch (SQLException e) {
			throw new DAOException("Error during USER dao manipulation.");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	public int findIdByUser(User us) throws DAOException {
		final String sql = "SELECT `id` FROM `user` WHERE `username` = ?";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setString(1, us.getPseudo());
			r = st.executeQuery();
			if(r.next())
				return r.getInt("id");
			throw new DAOException("User not found");
		} catch (SQLException e) {
			throw new DAOException("Error during USER dao manipulation.");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	@Override
	public User find(Object id) throws DAOException {
		final String sql = "SELECT * FROM `user` WHERE `id` = ?";
		PreparedStatement st = null;
		ResultSet r = null;
		if(!(id instanceof Integer)) 
			throw new DAOException("Error ID not supported.");
		
		try {
			st = connect.prepareStatement(sql);
			st.setInt(1,(int)id);
			r = st.executeQuery();
			if(r.next()) {
				final User u = new User();
				u.setId(r.getInt("id"));
				u.setPseudo(r.getString("username"));
				u.setFirst_co(r.getDate("first_co"));
				u.setLast_co(r.getDate("last_co"));
				return u;
			}
			throw new DAOException("User not found.");
		} catch (SQLException e) {
			throw new DAOException("Error during USER dao manipulation.");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
		
	}
	
	@Override
	public User create(User us) throws DAOException {
		final String sql = "INSERT INTO `user` VALUES(NULL,?,NOW(),NOW())";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, us.getPseudo());
			st.executeUpdate();
			
			r = st.getGeneratedKeys();
			if(r.next()) {
				us = this.find(r.getInt(1));
				return us;
			}
			throw new DAOException("Error during insertion.");
		} catch(SQLException e) {
			throw new DAOException("Error during DAO User manipulation.", e);
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	@Override
	public User update(User us) throws DAOException {
		final String sql = "UPDATE `user` SET `username`= ? , `first_co`= ? , `last_co`= ? WHERE `id`= ?";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setString(1, us.getPseudo());
			st.setDate(2, us.getFirst_co());
			st.setDate(3, us.getLast_co());
			st.setInt(4, us.getId());
			st.executeUpdate();
			return us;
		} catch(SQLException e) {
			throw new DAOException("Error during DAO User manipulation", e);
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	public User updateLast_co(User us) throws DAOException {
		final String sql ="UPDATE `user` SET `last_co` = NOW() WHERE `username`=?";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setString(1, us.getPseudo());
			st.executeUpdate();
			return us;
		} catch(SQLException e) {
			throw new DAOException("Error during dao manip'");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	@Override
	public void delete(User us) throws DAOException {
		final String sql = "DELETE FROM `user` WHERE `id`= ? ";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setInt(1, us.getId());
			st.executeUpdate();
		} catch(SQLException e) {
			throw new DAOException("Error during DAO user manipulation", e);
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	public void deleteByUser(User us) throws DAOException {
		final String sql = "DELETE FROM `user` WHERE `username` = ?";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setString(1, us.getPseudo());
			st.executeUpdate();
		} catch(SQLException e) {
			throw new DAOException("Error during DAO user manipulation", e);
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
		
	}

	@Override
	public void exporter(String filePath) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> importer(String filePath) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}

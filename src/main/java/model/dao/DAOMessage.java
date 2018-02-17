package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.sql.Date;

import model.beans.Message;
import model.beans.User;
import model.dao.DAO;
import model.dao.exceptions.DAOException;
import model.dao.utils.DAOUtils;

public class DAOMessage extends DAO<Message> {
	protected DAOMessage(Connection c) { 
		super(c); 
	}
	
	// MOH 2018/01/27
	public List<String> getChannels() throws DAOException {
		final String sql = "SELECT DISTINCT `chan` FROM `messages`";
		PreparedStatement st = null;
		ResultSet r = null;
		List<String> channels = new LinkedList<>();
		
		try {
			st = connect.prepareStatement(sql);
			r = st.executeQuery();
			while(r.next()) {
				channels.add(r.getString("chan"));
			}

			return channels;
			
		} catch (SQLException e) {
			throw new DAOException("Error during USER dao manipulation.");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	public List<Message> findByChan(String chan) throws DAOException {
		final String sql = "SELECT `m`.`id`, `m`.`date`, `m`.`content`, `m`.`chan`, `u`.`username` FROM `messages` AS `m`, `user` AS `u` WHERE `m`.`chan` = ? AND `m`.`user_id` = `u`.`id`";
		
		PreparedStatement st = null;
		ResultSet r = null;
		List<Message> listm = null;
		boolean flag = false;
		try {
			st = connect.prepareStatement(sql);
			st.setString(1, chan);
			r = st.executeQuery();
			listm = new LinkedList<Message>();			

			while(r.next()) {
				if(!flag)
					flag = true;
				
				final Message m = new Message();
				//User u = new User();
				
				m.setId(r.getInt("id"));
				//m.setUser_id(r.getInt("user_id"));
				m.setDate(r.getDate("date"));
				m.setContent(r.getString("content"));
				m.setChan(r.getString("chan"));
				m.setPseudo(r.getString("username"));
				
				//u = daou.find(m.getId());
				//m.setPseudo(u.getPseudo());
				
				listm.add(m);
			}
			if(flag)
				return listm;
			throw new DAOException("Channel : "+chan+" not found.");
		} catch (SQLException e) {
			throw new DAOException("Error during DAOMessage manipulation, findBychan(:= "+chan+").");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}
	
	public List<Message> findByDate(Date d) throws DAOException  {
		final String sql = "SELECT `m`.`id`, `m`.`date`, `m`.`content`, `m`.`chan`, `u`.`username` FROM `messages` AS `m`, `user` AS `u` WHERE `m`.`date` = ? AND `m`.`user_id` = `u`.`id`";
		PreparedStatement st = null;
		ResultSet r = null;
		List<Message> listm = null;
		
		boolean flag = false;
		try {
			st = connect.prepareStatement(sql);
			st.setDate(1,d);
			r = st.executeQuery();
			listm = new LinkedList<Message>();
			
			while(r.next()) {
				if(!false)
					flag = true;
				
				final Message m = new Message();
				m.setId(r.getInt("id"));
				m.setDate(r.getDate("date"));
				m.setContent(r.getString("content"));
				m.setChan(r.getString("chan"));
				m.setPseudo(r.getString("username"));
				listm.add(m);
			}
			if(flag)
				return listm;
			throw new DAOException("Date not found.");
		} catch (SQLException e) {
			throw new DAOException("Error during USER dao manipulation.");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
			
		}
	}
	
	public List<Message> findByUser(User u) throws DAOException {
		//final String sql = "SELECT * FROM `messages` WHERE user_id = ?";
		final String sql = "SELECT `m`.`id`, `m`.`date`, `m`.`content`, `m`.`chan`, `u`.`username` FROM `messages` AS `m`, `user` AS `u` WHERE `m`.`user_id` = ? AND `m`.`user_id` = `u`.`id`";
		PreparedStatement st = null;
		ResultSet r = null;
		List<Message> listm = null;
		boolean flag = false;
		try  {
			st = connect.prepareStatement(sql);
			st.setInt(1, u.getId());
			r = st.executeQuery();
			listm = new LinkedList<Message>();
			
			while(r.next()) {
				if(!flag)
					flag = true;
				
				final Message m = new Message();
				m.setId(r.getInt("id"));
				m.setDate(r.getDate("date"));
				m.setChan(r.getString("chan"));
				m.setContent(r.getString("content"));
				m.setPseudo(r.getString("username"));
				listm.add(m);
			}
			if(flag)
				return listm;
			throw new DAOException("User not found.");
		} catch (SQLException e) {
			throw new DAOException("Error during USER dao manipulation.");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}

	@Override
	public Message find(Object id) throws DAOException {
		final String sql = "SELECT * FROM `messages` WHERE id = ?";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setInt(1,(int)id);
			r = st.executeQuery();
			if(r.next()) {
				final Message m = new Message();
				m.setId(r.getInt("id"));
				m.setUser_id(r.getInt("user_id"));
				m.setDate(r.getDate("date"));
				m.setContent(r.getString("content"));
				m.setChan(r.getString("chan"));
				
				return m;
			}
			throw new DAOException("Message not found.");
		} catch (SQLException e) {
			throw new DAOException("Error during USER dao manipulation.");
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}

	@Override
	public Message create(Message obj) throws DAOException {
		final String sql = "INSERT INTO `messages` VALUES(NULL,?,?,?,?)";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, obj.getUser_id());
			st.setDate(2, (java.sql.Date)obj.getDate());
			st.setString(3, obj.getChan());
			st.setString(4, obj.getContent());
			st.executeUpdate();
			
			r = st.getGeneratedKeys();
			if(r.next()) {
				obj.setId(r.getInt(1));
				return obj;
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
	public Message update(Message obj) throws DAOException {
		final String sql = "UPDATE `messages` SET `user_id`= ?, `date` = ?, `content` = ?, `chan` = ? WHERE `id` = ?";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setInt(1, obj.getUser_id());
			st.setDate(2, (java.sql.Date)obj.getDate());
			st.setString(3,obj.getContent());
			st.setString(4, obj.getChan());
			st.setInt(5, obj.getId());
			st.executeUpdate();
			return obj;
		} catch(SQLException e) {
			throw new DAOException("Error during DAO User manipulation", e);
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}

	@Override
	public void delete(Message obj) throws DAOException {
		final String sql = "DELETE FROM `messages` WHERE `id` = ?";
		PreparedStatement st = null;
		ResultSet r = null;
		try {
			st = connect.prepareStatement(sql);
			st.setInt(1, obj.getId());
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
	public List<Message> importer(String filePath) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}
	
}

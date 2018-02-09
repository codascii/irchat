package model.dao;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import model.beans.Address;
import model.beans.User;
import model.dao.exceptions.DAOException;
import model.dao.utils.DAOUtils;

public class DAOAddress extends DAO<Address> {

	protected DAOAddress(Connection c) {
		super(c);
	}
	
	public List<Address> listAddressFromUser(User u) throws DAOException {
		final String sql = "SELECT `id` FROM `addresses` WHERE `id_user` = ?";
		final List<Address> addressList = new LinkedList<>();
		
		PreparedStatement st = null;
		ResultSet r = null;
		
		try {
			st = connect.prepareStatement(sql);
			st.setInt(1, u.getId());
			r = st.executeQuery();
			
			while (r.next()) {
				addressList.add(find(r.getInt(1)));				
			}
			return addressList;
		} catch (SQLException e) {
			throw new DAOException("Error during DAO address listning", e);
		} finally {
			DAOUtils.close(st);
			DAOUtils.close(r);
		}
	}

	@Override
	public Address find(Object id) throws DAOException {
		final String sql = "SELECT * FROM `addresses` WHERE `id`= ? ";
		final Address a = new Address();

		PreparedStatement st = null;
		ResultSet r = null;

		if (!(id instanceof Integer)) {
			throw new DAOException("Error in id format.");
		}

		try {
			st = connect.prepareStatement(sql);
			st.setInt(1, (int) id);
			r = st.executeQuery();

			if (r.next()) {
				a.setId(r.getInt("id"));
				a.setIdUser(r.getInt("id_user"));
				a.setStreet(r.getString("street"));
				a.setPostalCode(r.getString("postale_code"));
				a.setCity(r.getString("city"));

				return a;
			}

			throw new DAOException("Address not found in the database.");

		} catch (SQLException e) {
			throw new DAOException("Error during DAO address finding.", e);
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}

	@Override
	public Address create(Address obj) throws DAOException {
		final String sql = "INSERT INTO `addresses` VALUES(NULL, ? , ? , ? , ?)";

		PreparedStatement st = null;
		ResultSet r = null;

		try {
			st = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, obj.getIdUser());
			st.setString(2, obj.getStreet());
			st.setString(3, obj.getPostalCode());
			st.setString(4, obj.getCity());
			st.executeUpdate();

			r = st.getGeneratedKeys();

			if (r.next()) {
				obj.setId(r.getInt(1));
				return obj;
			}

			throw new DAOException("Error during DAO creation.");

		} catch (SQLException e) {
			throw new DAOException("Error during DAO Address creation.", e);
		} finally {
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
	}

	@Override
	public Address update(Address obj) throws DAOException {
		final String sql = "UPDATE `addresses` SET `id_user`= ? , `street`= ? , `postale_code`= ? , `city`= ? WHERE `id`= ? ";

		PreparedStatement st = null;

		try {
			st = connect.prepareStatement(sql);
			st.setInt(1, obj.getIdUser());
			st.setString(2, obj.getStreet());
			st.setString(3, obj.getPostalCode());
			st.setString(4, obj.getCity());
			st.setInt(5, obj.getId());
			st.executeUpdate();
			
			return obj;

		} catch (SQLException e) {
			throw new DAOException("Error during DAO Address update.", e);
		} finally {
			DAOUtils.close(st);
		}
	}

	@Override
	public void delete(Address obj) throws DAOException {
		final String sql = "DELETE FROM `addresses` WHERE `id`= ? ";
		
		PreparedStatement st = null;
		try {
			st = connect.prepareStatement(sql);
			st.setInt(1, obj.getId());
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DAOException("Error during DAO Address update.", e);
		} finally {
			DAOUtils.close(st);
		}
	}

	@Override
	public void exporter(String filePath) throws DAOException {
		PreparedStatement st = null;
		ResultSet r = null;
		PrintWriter pw = null;

		try {
			st = connect.prepareStatement("SELECT * FROM `addresses`");
			r = st.executeQuery();
			pw = new PrintWriter(filePath);

			while (r.next()) {
				final Address a = new Address();
				a.setId(r.getInt("id"));
				a.setIdUser(r.getInt("id_user"));
				a.setStreet(r.getString("street"));
				a.setPostalCode(r.getString("postale_code"));
				a.setCity(r.getString("city"));

				pw.println(a);
			}

		} catch (FileNotFoundException e) {
			throw new DAOException("Fichier : " + filePath + " introuvable", e);
		} catch (SQLException e) {
			throw new DAOException("Error when exporting Addresses", e);
		}finally {
			if(pw != null) pw.close();
			DAOUtils.close(r);
			DAOUtils.close(st);
		}
		
	}

	@Override
	public List<Address> importer(String filePath) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}
}

package model.dao;

import java.sql.Connection;
import java.util.List;

import model.dao.exceptions.DAOException;

public abstract class DAO<T> {
	protected Connection connect;

	protected DAO(Connection c) {
		this.connect = c;
	}
	
	public abstract T find(Object id) throws DAOException;
	
	public abstract T create(T obj) throws DAOException;
	
	public abstract T update(T obj) throws DAOException;
	
	public abstract void delete(T obj) throws DAOException;
	
	public abstract void exporter(String filePath) throws DAOException;
	
	public abstract List<T> importer(String filePath) throws DAOException;

}

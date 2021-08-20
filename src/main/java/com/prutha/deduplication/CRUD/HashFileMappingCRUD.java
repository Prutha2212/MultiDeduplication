package com.prutha.deduplication.CRUD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.prutha.deduplication.DAO.HashFileMapping;
import com.prutha.deduplication.database.ConnectionDB;

public class HashFileMappingCRUD {
	private ConnectionDB connectionDB;

	public HashFileMappingCRUD(ConnectionDB connectionDB) {
		this.connectionDB = connectionDB;
	}

	public Boolean create(HashFileMapping hashFileMapping) {

		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement(
					"INSERT INTO `hashfilemapping` (`fileID`, `hashid`,`chunk`) VALUES (?, ?,?)");
			statement.setInt(1, hashFileMapping.getFileID());
			statement.setInt(2, hashFileMapping.getHashID());
			statement.setInt(3, hashFileMapping.getChunk());

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}



	public List<HashFileMapping> getAll() {
		List<HashFileMapping> hashFileMappingList = new ArrayList<HashFileMapping>();
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement("SELECT * FROM  `hashfilemapping`");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				HashFileMapping hashFileMapping = new HashFileMapping();
				hashFileMapping.setHashID(result.getInt(1));
				hashFileMapping.setFileID(result.getInt(2));
				hashFileMapping.setChunk(result.getInt(3));

				hashFileMappingList.add(hashFileMapping);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashFileMappingList;
	}

	public List<HashFileMapping> getMap(Integer fileID, Integer hashID) {
		List<HashFileMapping> hashFileMappingList = new ArrayList<HashFileMapping>();
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement("SELECT * FROM  `hashfilemapping` where `fileID`=? and `hashid`=?");
			statement.setInt(1, fileID);
			statement.setInt(2, hashID);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				HashFileMapping hashFileMapping = new HashFileMapping();
				hashFileMapping.setHashID(result.getInt(1));
				hashFileMapping.setFileID(result.getInt(2));
				hashFileMapping.setChunk(result.getInt(3));

				hashFileMappingList.add(hashFileMapping);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashFileMappingList;
	}
	
}

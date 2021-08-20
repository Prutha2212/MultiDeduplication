package com.prutha.deduplication.CRUD;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.prutha.deduplication.DAO.FileHashValues;
import com.prutha.deduplication.database.ConnectionDB;

public class FileHashCRUD {

	private ConnectionDB connectionDB;

	public FileHashCRUD(ConnectionDB connectionDB) {
		this.connectionDB = connectionDB;

	}

	public Integer create(FileHashValues fileHash) throws IOException {
		Integer oid = null;
		try {

			PreparedStatement statement = connectionDB.getCon().prepareStatement(
					"INSERT INTO `filehashvalues` (`hash`, `fileData`,`fileSize`) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, fileHash.getHash());
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(fileHash.getFile());
				statement.setBinaryStream(2, fis);
				statement.setLong(3, fileHash.getFileSize());
				statement.executeUpdate();
				ResultSet rs = statement.getGeneratedKeys();
				if (rs.next()) {
					int newId = rs.getInt(1);
					oid = newId;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (fis != null)
					fis.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return oid;
	}

	public FileHashValues get(Integer hashID) {
		FileHashValues fileHashValues = new FileHashValues();
		try {
			PreparedStatement statement = connectionDB.getCon()
					.prepareStatement("SELECT * FROM deduplication.filehashvalues where hashid = ?");
			statement.setInt(1, hashID);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				fileHashValues.setHashID(result.getInt(1));
				fileHashValues.setHash(result.getString(2));
				fileHashValues.setFileData(result.getBlob(3));
				fileHashValues.setFileSize(result.getLong(4));
				return fileHashValues;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public FileHashValues getbyHash(String hash) {
		FileHashValues fileHashValues = new FileHashValues();
		try {
			PreparedStatement statement = connectionDB.getCon()
					.prepareStatement("SELECT * FROM deduplication.filehashvalues where hash = ?");
			statement.setString(1, hash);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				fileHashValues.setHashID(result.getInt(1));
				fileHashValues.setHash(result.getString(2));
				fileHashValues.setFileData(result.getBlob(3));
				fileHashValues.setFileSize(result.getLong(4));
				return fileHashValues;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<FileHashValues> getbyFileID(Integer fileID) {
		List<FileHashValues> fileHashValuesList = new ArrayList<FileHashValues>();
		
		try {
			PreparedStatement statement = connectionDB.getCon()
					.prepareStatement("SELECT fh.hashid,fh.hash,fh.fileSize FROM deduplication.filehashvalues as fh\r\n" + 
							"inner join deduplication.hashfilemapping  hfm\r\n" + 
							"on fh.hashid= hfm.hashid\r\n" + 
							"and hfm.fileID= ?");
			statement.setInt(1, fileID);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				FileHashValues fileHashValues = new FileHashValues();
				fileHashValues.setHashID(result.getInt(1));
				fileHashValues.setHash(result.getString(2));
				fileHashValues.setFileSize(result.getLong(3));
				fileHashValuesList.add(fileHashValues);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fileHashValuesList;
	}

	public List<FileHashValues> getAll() {
		List<FileHashValues> fileHashValuesList = new ArrayList<FileHashValues>();
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement("SELECT * FROM filehashvalues");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				FileHashValues fileHashValues = new FileHashValues();
				fileHashValues.setHashID(result.getInt(1));
				fileHashValues.setHash(result.getString(2));
				fileHashValues.setFileData(result.getBlob(3));
				fileHashValues.setFileSize(result.getLong(4));
				fileHashValuesList.add(fileHashValues);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fileHashValuesList;
	}

	public Boolean update(FileHashValues fileHashValues) {

		try {

			PreparedStatement statement = connectionDB.getCon()
					.prepareStatement("UPDATE `filehashvalues` SET `hash` = ?, `fileData` = ? WHERE (`hashid` = ?)");
			statement.setString(1, fileHashValues.getHash());
			statement.setBlob(2, fileHashValues.getFileData());
			statement.setLong(3, fileHashValues.getHashID());
			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Boolean delete(Integer hashId) {

		PreparedStatement statement;
		try {
			statement = connectionDB.getCon().prepareStatement("DELETE FROM `filehashvalues` WHERE (`hashid` = ?)");
			statement.setInt(1, hashId);

			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Set<String> getAllHash() {
		Set<String> fileHashValuesSet = new HashSet<String>();
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement("SELECT hash FROM filehashvalues");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				fileHashValuesSet.add(result.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fileHashValuesSet;
	}
	
	public Long getHashSize() {
		Long hashSize =0L;
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement("SELECT fileSize FROM filehashvalues");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				hashSize += result.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashSize;
	}

}

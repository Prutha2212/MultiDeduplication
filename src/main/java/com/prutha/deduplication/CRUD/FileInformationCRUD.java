package com.prutha.deduplication.CRUD;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.prutha.deduplication.DAO.FileInformation;
import com.prutha.deduplication.algorithm.ChunkingAlgorithmType;
import com.prutha.deduplication.database.ConnectionDB;

public class FileInformationCRUD {
	private ConnectionDB connectionDB;

	public FileInformationCRUD(ConnectionDB connectionDB) {
		this.connectionDB = connectionDB;

	}

	public Integer create(FileInformation fileInformation) {
		Integer oid = null;
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement(
					"INSERT INTO `deduplication`.`fileinformation` (`fileName`, `fileType`, `fileLocation`, `chunkSize`,`fileSize`,`chunkingAlgorithmType`) VALUES (?, ?, ?, ?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, fileInformation.getFileName());
			statement.setString(2, fileInformation.getFileType());
			statement.setString(3, fileInformation.getFileLocation());
			statement.setLong(4, fileInformation.getChunkSize());
			statement.setLong(5, fileInformation.getFileSize());
			statement.setString(6, fileInformation.getChunkingAlgorithmType().toString());
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				int newId = rs.getInt(1);
				oid = newId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return oid;
	}

	public FileInformation get(Integer fileID) {
		FileInformation fileInformation = new FileInformation();
		try {
			PreparedStatement statement = connectionDB.getCon()
					.prepareStatement("SELECT * FROM fileinformation where fileID =?");
			statement.setInt(1, fileID);
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				fileInformation.setFileID(result.getInt(1));
				fileInformation.setFileName(result.getString(2));
				fileInformation.setFileType(result.getString(3));
				fileInformation.setFileLocation(result.getString(4));
				fileInformation.setChunkSize(result.getLong(5));
				fileInformation.setFileSize(result.getLong(6));
				fileInformation.setChunkingAlgorithmType(ChunkingAlgorithmType.valueOf(result.getString(7)));

				return fileInformation;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<FileInformation> getAll() {
		List<FileInformation> fileInformationList = new ArrayList<FileInformation>();
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement("SELECT * FROM fileinformation");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				FileInformation fileInformation = new FileInformation();
				fileInformation.setFileID(result.getInt(1));
				fileInformation.setFileName(result.getString(2));
				fileInformation.setFileType(result.getString(3));
				fileInformation.setFileLocation(result.getString(4));
				fileInformation.setChunkSize(result.getLong(5));
				fileInformation.setFileSize(result.getLong(6));
				fileInformation.setChunkingAlgorithmType(ChunkingAlgorithmType.valueOf(result.getString(7)));
				fileInformation.setProcessingTime(result.getTimestamp(8));
				fileInformationList.add(fileInformation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fileInformationList;
	}

	public Boolean update(FileInformation fileInformation) {

		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement(
					"UPDATE fileinformation SET `fileName` = ?, `fileType` = ?, `fileLocation` = ?, `chunkSize` = ? WHERE (`fileID` = ?)");
			statement.setString(1, fileInformation.getFileName());
			statement.setString(2, fileInformation.getFileType());
			statement.setString(3, fileInformation.getFileLocation());
			statement.setLong(4, fileInformation.getChunkSize());
			statement.setInt(5, fileInformation.getFileID());
			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Boolean delete(Integer fileID) {

		PreparedStatement statement;
		try {
			statement = connectionDB.getCon().prepareStatement("DELETE FROM `fileinformation` WHERE (`fileID` = ?)");
			statement.setInt(1, fileID);

			int rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Boolean updateChunkSize(Integer fileID) {
		try {
			PreparedStatement statement = connectionDB.getCon()
					.prepareStatement("select count(*) from deduplication.hashfilemapping where fileID =?");
			statement.setInt(1, fileID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				Integer chunkCount = result.getInt(1);
				PreparedStatement statement2 = connectionDB.getCon()
						.prepareStatement("UPDATE fileinformation SET `chunkSize` = ? WHERE (`fileID` = ?)");
				statement2.setInt(1, chunkCount);
				statement2.setInt(2, fileID);
				int rowsUpdated = statement2.executeUpdate();
				if (rowsUpdated > 0)
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean setProcessingTime(Integer fileID, Long timeInterval) {

		try {
			PreparedStatement statement = connectionDB.getCon()
					.prepareStatement("UPDATE fileinformation SET `processingTime` = ? WHERE (`fileID` = ?)");
			statement.setTimestamp(1, new Timestamp(timeInterval));
			statement.setInt(2, fileID);
			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0)
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public FileInformation getbyFile(File file) {
		FileInformation fileInformation = new FileInformation();
		try {
			PreparedStatement statement = connectionDB.getCon().prepareStatement(
					"SELECT * FROM fileinformation where fileName =? and fileLocation=? and fileType=?");
			statement.setString(1, FilenameUtils.removeExtension(file.getName()));
			statement.setString(2, file.getParent());
			statement.setString(3, FilenameUtils.getExtension(file.getName()));
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				fileInformation.setFileID(result.getInt(1));
				fileInformation.setFileName(result.getString(2));
				fileInformation.setFileType(result.getString(3));
				fileInformation.setFileLocation(result.getString(4));
				fileInformation.setChunkSize(result.getLong(5));

				return fileInformation;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

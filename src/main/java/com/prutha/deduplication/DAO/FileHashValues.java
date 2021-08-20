package com.prutha.deduplication.DAO;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.io.FileUtils;

public class FileHashValues {
	private Integer hashID;
	private String hash;
	private Long fileSize;
	private Blob fileData;
	private File file; 
	public FileHashValues() {
	}

	public FileHashValues(String hash, File file) {
		this.hash = hash;
		this.file = file;
		try {
			this.fileData = new SerialBlob(FileUtils.readFileToByteArray(file));
			this.fileSize = this.fileData.length();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Blob getFileData() {
		return fileData;
	}

	public void setFileData(Blob fileData) {
		this.fileData = fileData;
	}

	public Integer getHashID() {
		return hashID;
	}

	public void setHashID(Integer hashID) {
		this.hashID = hashID;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}

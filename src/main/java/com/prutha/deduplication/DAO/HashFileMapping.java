package com.prutha.deduplication.DAO;

public class HashFileMapping {
	private Integer fileID;
	private Integer hashID;
	private Integer chunk;
	
	
	public HashFileMapping() {
		
	}
	public HashFileMapping(Integer fileID, Integer hashID, Integer chunk) {
		super();
		this.fileID = fileID;
		this.hashID = hashID;
		this.chunk = chunk;
	}
	public Integer getFileID() {
		return fileID;
	}
	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}
	public Integer getHashID() {
		return hashID;
	}
	public void setHashID(Integer hashID) {
		this.hashID = hashID;
	}
	public Integer getChunk() {
		return chunk;
	}
	public void setChunk(Integer chunk) {
		this.chunk = chunk;
	}
}

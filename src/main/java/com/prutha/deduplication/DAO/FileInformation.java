package com.prutha.deduplication.DAO;

import java.sql.Timestamp;
import java.util.List;

import com.prutha.deduplication.algorithm.ChunkingAlgorithmType;

public class FileInformation {
	private Integer fileID;
	private String fileName;
	private String fileType;
	private String fileLocation;
	private Long chunkSize;
	private Long fileSize;
	private ChunkingAlgorithmType chunkingAlgorithmType;
	private List<FileHashValues> fileHashValuesList;
	private Timestamp processingTime;
		

	public FileInformation() {
		
	}
	public FileInformation(String fileName, String fileType, String fileLocation, Long chunkSize,Long fileSize,ChunkingAlgorithmType chunkingAlgorithmType) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileLocation = fileLocation;
		this.chunkSize = chunkSize;
		this.fileSize=fileSize;
		this.chunkingAlgorithmType= chunkingAlgorithmType;
	}

	public Integer getFileID() {
		return fileID;
	}

	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public List<FileHashValues> getFileHashValuesList() {
		return fileHashValuesList;
	}

	public void setFileHashValuesList(List<FileHashValues> fileHashValuesList) {
		this.fileHashValuesList = fileHashValuesList;
	}

	public Long getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(Long chunkSize) {
		this.chunkSize = chunkSize;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public ChunkingAlgorithmType getChunkingAlgorithmType() {
		return chunkingAlgorithmType;
	}
	public void setChunkingAlgorithmType(ChunkingAlgorithmType chunkingAlgorithmType) {
		this.chunkingAlgorithmType = chunkingAlgorithmType;
	}
	public Timestamp getProcessingTime() {
		return processingTime;
	}
	public void setProcessingTime(Timestamp processingTime) {
		this.processingTime = processingTime;
	}
}

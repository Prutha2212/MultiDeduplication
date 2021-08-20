package com.prutha.deduplication.action;

import java.util.List;
import java.util.stream.Collectors;

import com.prutha.deduplication.CRUD.FileHashCRUD;
import com.prutha.deduplication.CRUD.FileInformationCRUD;
import com.prutha.deduplication.CRUD.HashFileMappingCRUD;
import com.prutha.deduplication.DAO.FileInformation;
import com.prutha.deduplication.database.ConnectionDB;

public class ReportGeneration {
	private ConnectionDB connectionDB;
	private FileHashCRUD fileHashCRUD;
	private FileInformationCRUD fileInformationCRUD;
	@SuppressWarnings("unused")
	private HashFileMappingCRUD hashFileMappingCRUD;
	private List<FileInformation> fileInformations;

	private Float dedupRatio;
	private Float dirSize;
	private Float throughPut;
	private Float hashSize;
	private Integer totalFiles;
	private Integer totalHashFiles;

	public ReportGeneration() {
		connectionDB = new ConnectionDB();
		fileHashCRUD = new FileHashCRUD(connectionDB);
		fileInformationCRUD = new FileInformationCRUD(connectionDB);
		hashFileMappingCRUD = new HashFileMappingCRUD(connectionDB);
	}

	public void run() {
		fileInformations = getFileInformation();
		dirSize = (float) fileInformations.stream().collect(Collectors.summingLong(report -> report.getFileSize()));
		hashSize = (float) fileHashCRUD.getHashSize();
		setDedupRatio( 1 - (hashSize / dirSize));

		Long totalProcessingTime = fileInformations.stream()
				.collect(Collectors.summingLong(report -> report.getProcessingTime().getTime()));
		setThroughPut((dirSize / (float) totalProcessingTime));
		totalFiles= getFileInformations().size();
		totalHashFiles= fileHashCRUD.getAllHash().size();
	}

	private List<FileInformation> getFileInformation() {
		List<FileInformation> fileInformations = fileInformationCRUD.getAll();
		fileInformations.forEach(fileInfo -> {
			fileInfo.setFileHashValuesList(fileHashCRUD.getbyFileID(fileInfo.getFileID()));
		});
		return fileInformations;
	}

	public ConnectionDB getConnectionDB() {
		return connectionDB;
	}

	public void setConnectionDB(ConnectionDB connectionDB) {
		this.connectionDB = connectionDB;
	}

	public void setFileHashCRUD(FileHashCRUD fileHashCRUD) {
		this.fileHashCRUD = fileHashCRUD;
	}

	public void setFileInformationCRUD(FileInformationCRUD fileInformationCRUD) {
		this.fileInformationCRUD = fileInformationCRUD;
	}

	public void setHashFileMappingCRUD(HashFileMappingCRUD hashFileMappingCRUD) {
		this.hashFileMappingCRUD = hashFileMappingCRUD;
	}

	public Float getDedupRatio() {
		return dedupRatio;
	}

	public void setDedupRatio(Float dedupRatio) {
		this.dedupRatio = dedupRatio;
	}

	public Float getThroughPut() {
		return throughPut;
	}

	public void setThroughPut(Float throughPut) {
		this.throughPut = throughPut;
	}

	public Float getDirSize() {
		return dirSize;
	}

	public void setDirSize(Float dirSize) {
		this.dirSize = dirSize;
	}

	public Float getHashSize() {
		return hashSize;
	}

	public void setHashSize(Float hashSize) {
		this.hashSize = hashSize;
	}

	public List<FileInformation> getFileInformations() {
		return fileInformations;
	}

	public void setFileInformations(List<FileInformation> fileInformations) {
		this.fileInformations = fileInformations;
	}

	public Integer getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(Integer totalFiles) {
		this.totalFiles = totalFiles;
	}

	public Integer getTotalHashFiles() {
		return totalHashFiles;
	}

	public void setTotalHashFiles(Integer totalHashFiles) {
		this.totalHashFiles = totalHashFiles;
	}
}

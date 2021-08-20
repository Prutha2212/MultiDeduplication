package com.prutha.deduplication.action;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.prutha.deduplication.CRUD.FileHashCRUD;
import com.prutha.deduplication.CRUD.FileInformationCRUD;
import com.prutha.deduplication.CRUD.HashFileMappingCRUD;
import com.prutha.deduplication.algorithm.Chunking;
import com.prutha.deduplication.algorithm.FileBasedChunking;
import com.prutha.deduplication.algorithm.FixedSizeChunking;
import com.prutha.deduplication.algorithm.RabinKarpRollingHashChunking;
import com.prutha.deduplication.chunkingTools.FileList;
import com.prutha.deduplication.database.ConnectionDB;

public class DeduplicatioRun {
	private ConnectionDB connectionDB;
	private FileHashCRUD fileHashCRUD;
	private FileInformationCRUD fileInformationCRUD;
	private HashFileMappingCRUD hashFileMappingCRUD;
	
	public DeduplicatioRun() {
		connectionDB = new ConnectionDB();
		fileHashCRUD = new FileHashCRUD(connectionDB);
		fileInformationCRUD = new FileInformationCRUD(connectionDB);
		hashFileMappingCRUD = new HashFileMappingCRUD(connectionDB);
	}

	private Chunking selectAlgorithm(Long chunksize, String directory) {
		return chunksize <= Chunking.ONE_MB ? new FileBasedChunking(directory)
				: chunksize <= 2 * Chunking.ONE_MB ? new FixedSizeChunking(directory)
						: new RabinKarpRollingHashChunking(directory);

	}

	public void applyAlgorithm(String directory) {
		for (File file : new FileList(directory).filelist) {
			Chunking chunkingAlgorithm = selectAlgorithm(file.length(), directory);
			try {
				long startTime = System.currentTimeMillis();
				Integer fileID = chunkingAlgorithm.setAll(file,fileHashCRUD,fileInformationCRUD,hashFileMappingCRUD);
				if(fileID==0)
					continue;
				long endTime = System.currentTimeMillis();
				fileInformationCRUD.setProcessingTime(fileID, endTime-startTime);
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}

	}
	
}

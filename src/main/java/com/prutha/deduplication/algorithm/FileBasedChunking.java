package com.prutha.deduplication.algorithm;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.io.FilenameUtils;

import com.prutha.deduplication.CRUD.FileHashCRUD;
import com.prutha.deduplication.CRUD.FileInformationCRUD;
import com.prutha.deduplication.CRUD.HashFileMappingCRUD;
import com.prutha.deduplication.DAO.FileHashValues;
import com.prutha.deduplication.DAO.FileInformation;
import com.prutha.deduplication.DAO.HashFileMapping;
import com.prutha.deduplication.chunkingTools.Checksum;

public class FileBasedChunking implements Chunking {
	public Set<String> indexTable;
	public Checksum sum;
	public int count;

	public FileBasedChunking(String directory) {
		indexTable = new HashSet<String>();
		sum = new Checksum();
		// list = new FileList(Config.DIRECTORY);
		count = 0;
	}

	public Integer setAll(File file, FileHashCRUD fileHashCRUD, FileInformationCRUD fileInformationCRUD,
			HashFileMappingCRUD hashFileMappingCRUD) throws SerialException, SQLException, IOException {
		indexTable = fileHashCRUD.getAllHash();
		count = 0;
		FileInformation fileInformationDB = null;
		if (file.isFile() && !file.isHidden()) {
			String checksum = sum.generateChecksum(file.getAbsolutePath(), file.getName());

			// check if file exists in db : file name, location and extension, if not skip
			fileInformationDB = fileInformationCRUD.getbyFile(file);
			if (fileInformationDB == null) {
				// save fileinfo and map
				fileInformationDB = new FileInformation(FilenameUtils.removeExtension(file.getName()),
						FilenameUtils.getExtension(file.getName()), file.getParent(), 1L, file.length(),
						ChunkingAlgorithmType.FILE_BASED_CHUNKING);
				fileInformationDB.setFileID(fileInformationCRUD.create(fileInformationDB));

				if (duplicateDetection(checksum)) {
					FileHashValues fileHashValues = fileHashCRUD.getbyHash(checksum);
					hashFileMappingCRUD.create(new HashFileMapping(fileInformationDB.getFileID(), fileHashValues.getHashID(), 1));
				} else {
					FileHashValues fileHashValues = new FileHashValues(checksum, file);
					Integer fileHashId = null;
					try {
						fileHashId = fileHashCRUD.create(fileHashValues);
						hashFileMappingCRUD.create(new HashFileMapping(fileInformationDB.getFileID(), fileHashId, 1));
						indexTable.add(checksum);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
			else
				return 0;

		}
		return fileInformationDB!=null?fileInformationDB.getFileID():0;
	}

	public boolean duplicateDetection(String hashvalue) {
		return indexTable.contains(hashvalue);
	}
}

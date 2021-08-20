package com.prutha.deduplication.algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.prutha.deduplication.chunkingTools.Config;

public class FixedSizeChunking implements Chunking {
	public Set<String> indexTable;
	public Checksum sum;
	public Long count;
	private FileInputStream fis;

	public FixedSizeChunking(String directory) {
		indexTable = new HashSet<String>();
		sum = new Checksum();

	}

	public Integer setAll(File file, FileHashCRUD fileHashCRUD, FileInformationCRUD fileInformationCRUD,
			HashFileMappingCRUD hashFileMappingCRUD) throws SerialException, SQLException, IOException {
		// Read each file and perform fixed-size chunking
		indexTable = fileHashCRUD.getAllHash();
		FileInformation fileInformationDB = null;
		byte[] chunk = new byte[Config.FIXED_CHUNKING];
		count = 1L;
		if (file.isFile() && !file.isHidden()) {
			
			if (fileInformationDB == null) {
				// save fileinfo and map
				fileInformationDB = new FileInformation(FilenameUtils.removeExtension(file.getName()),
						FilenameUtils.getExtension(file.getName()), file.getParent(), 1L, file.length(),
						ChunkingAlgorithmType.FIXED_SIZE_CHUNKING);
				fileInformationDB.setFileID(fileInformationCRUD.create(fileInformationDB));
				
				
				try {
					fis = new FileInputStream(file.getAbsolutePath());
					while (fis.read(chunk) != -1) {
						// perform the hash on the chunk
						String hashvalue = sum.chunking(chunk);
							if (duplicateDetection(hashvalue)) {
								FileHashValues fileHashValues = fileHashCRUD.getbyHash(hashvalue);
								hashFileMappingCRUD.create(new HashFileMapping(fileInformationDB.getFileID(),
										fileHashValues.getHashID(), count.intValue()));
							} else {

								FileHashValues fileHashValues = new FileHashValues(hashvalue, file);
								Integer fileHashId = fileHashCRUD.create(fileHashValues);
								hashFileMappingCRUD.create(
										new HashFileMapping(fileInformationDB.getFileID(), fileHashId, count.intValue()));
								indexTable.add(hashvalue);
							}
						
					}
					}
				 catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// Read file into a byte array and use SHA-1 hash the chunk
		
		}
		return fileInformationDB != null ? fileInformationDB.getFileID() : 0;
	}

	public boolean duplicateDetection(String hashvalue) {
		return indexTable.contains(hashvalue);
	}

}

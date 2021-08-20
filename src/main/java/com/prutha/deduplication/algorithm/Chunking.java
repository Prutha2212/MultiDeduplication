package com.prutha.deduplication.algorithm;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

import com.prutha.deduplication.CRUD.FileHashCRUD;
import com.prutha.deduplication.CRUD.FileInformationCRUD;
import com.prutha.deduplication.CRUD.HashFileMappingCRUD;

public interface Chunking {
	static final Integer ONE_MB = 1000000;
	public Integer setAll(File fileList, FileHashCRUD fileHashCRUD, FileInformationCRUD fileInformationCRUD, HashFileMappingCRUD hashFileMappingCRUD) throws SerialException, SQLException, IOException;
}

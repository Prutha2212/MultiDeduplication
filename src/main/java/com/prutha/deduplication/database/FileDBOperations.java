package com.prutha.deduplication.database;

import com.prutha.deduplication.CRUD.FileHashCRUD;
import com.prutha.deduplication.CRUD.FileInformationCRUD;
import com.prutha.deduplication.CRUD.HashFileMappingCRUD;

public class FileDBOperations {
	private ConnectionDB connectionDB;
	private FileHashCRUD fileHashCRUD;
	private FileInformationCRUD fileInformationCRUD;
	private HashFileMappingCRUD hashFileMappingCRUD;
}

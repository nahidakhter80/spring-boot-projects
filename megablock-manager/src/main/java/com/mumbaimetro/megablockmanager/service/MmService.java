package com.mumbaimetro.megablockmanager.service;

import java.io.IOException;
import java.util.List;

public interface MmService {
	public void writeExcelToDb() throws IOException;	
	public void writeResultToExcel()  throws IOException;	
	public List<String> getCancelledTrainList() throws IOException;
}

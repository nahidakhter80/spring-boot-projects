package com.mumbaimetro.megablockmanager;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mumbaimetro.megablockmanager.service.MmService;

@Component
public class MmBean implements CommandLineRunner {

	@Autowired
	private MmService mmService;
	
    public void run(String... args) {
    	System.out.println("\n\n\nWelcome!!!");
    	try {
    		mmService.writeExcelToDb();
    		mmService.writeResultToExcel(); 
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try {
    	    Thread.sleep(30000);
    	} catch(InterruptedException ex) {
    	    Thread.currentThread().interrupt();
    	}
    }
}

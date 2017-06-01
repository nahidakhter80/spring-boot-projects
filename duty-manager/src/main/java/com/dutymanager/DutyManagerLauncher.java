package com.dutymanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DutyManagerLauncher implements CommandLineRunner {

	@Override
	public void run(String... arg0) throws Exception {
		 System.out.println("\n\n\nHello");
		 Runtime rt = Runtime.getRuntime();
		 String url = "http://localhost:8080/dutymanager/";
		 rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
	}
}

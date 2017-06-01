package com.schedule.generator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WebPageLauncher implements CommandLineRunner {

	@Override
	public void run(String... arg0) throws Exception {
		 Runtime rt = Runtime.getRuntime();
		 String url = "http://localhost:8080/schedule/index.html";
		 rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
	}
}

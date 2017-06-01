package com.schedule.generator.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.schedule.generator.dto.Response;
import com.schedule.generator.entity.ScheduledTrain;
import com.schedule.generator.service.ScheduleService;
import com.schedule.generator.util.Constants;

@RestController
public class ScheduleController {
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping(path="/uploadperforma", method=RequestMethod.POST) 
	public Response processPerforma(@RequestParam("file") MultipartFile file, 
			@RequestParam("isNumericSet") Boolean isNumericSet) {
		InputStream is;
		try {
			is = file.getInputStream();
			return Response.successResponse(scheduleService.processProfarma(is, isNumericSet));
		} catch (Exception e) {
			e.printStackTrace();
			return Response.errorResponse(e.getMessage());
		}        
	}
	
	@RequestMapping(path="/generateExcel", method=RequestMethod.POST) 
	public Response generateExcel(@RequestBody List<ScheduledTrain> list) {
		scheduleService.updateEtyRemark(list);
		try {
			scheduleService.generateExcel();
			return Response.successResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.errorResponse(e.getMessage());			
		}
		
	}
	
	@RequestMapping(path="/download", method=RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<byte[]> downloadOutputfile() throws IOException {
		Path path = Paths.get(Constants.OUTPUT_FILE);
		byte[] contents = Files.readAllBytes(path); 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        String filename = Constants.OUTPUT_FILE;
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
        
        //new File(Constants.OUTPUT_FILE).delete();
        return response;
	}
	
	
}

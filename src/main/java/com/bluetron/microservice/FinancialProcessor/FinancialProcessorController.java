package com.bluetron.microservice.FinancialProcessor; 

import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.RestController;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.core.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.apache.poi.ss.usermodel.*;
import java.io.*;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONObject;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController  
public class FinancialProcessorController   
{  
	@CrossOrigin(origins = "http://localhost:8080,http://localhost:8081")
	@GetMapping(path = "/getNumofSheets/{filename}" , produces=MediaType.APPLICATION_JSON_VALUE)  
	public ResponseEntity<Object> retriveNumofSheets(@PathVariable("filename") String filename)  
	{  
		try
		{

			File myFile = new File("/mnt/data/input/"+filename);
			Workbook wb = WorkbookFactory.create (myFile); 
			int sheet = wb.getNumberOfSheets(); 
			//List<JSONObject> entities = new ArrayList<JSONObject>();
   
            JSONObject entity = new JSONObject();
            entity.put("valid", "true");
            entity.put("Sheets", sheet);
            
            Process process;
			process = Runtime.getRuntime().exec(String.format("bash /init_acyclic_step_workflow.sh -i %s", filename));
	
            
            //entities.add(entity);
      
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			//List<JSONObject> entities = new ArrayList<JSONObject>();
			   JSONObject entity = new JSONObject();
            	entity.put("valid", "false");
            	entity.put("Sheets", 0);
            
           	// entities.add(entity);
     		 return new ResponseEntity<Object>(entity, HttpStatus.OK);
		}
  
	}
	
	@GetMapping(value = "/getcsvfile", produces = "text/csv")
	public ResponseEntity getcsvfile(@RequestParam(name="csvfilename", required = true) String csvfilename) {
    try {
    
    	System.out.println(csvfilename);
        
		File file = new File("/mnt/data/output/"+csvfilename);
		//File file = new File("C:/Bluetron/FinancialProcessor/"+csvfilename);
		
			if (file.exists()) {
			
		        return ResponseEntity.ok()
		                .header("Content-Disposition", "attachment; filename=" + csvfilename) 
		                .header("Access-Control-Allow-Origin", "*") 
		                .contentLength(file.length())
		                .contentType(MediaType.parseMediaType("text/csv"))
		                .body(new org.springframework.core.io.FileSystemResource("/mnt/data/output/"+csvfilename));
		                //.body(new org.springframework.core.io.FileSystemResource("C:/Bluetron/FinancialProcessor/"+csvfilename));
					
	  	
	  		} else	  
	  		{
	       		 return new ResponseEntity(HttpStatus.NOT_FOUND);
	        }
	   }
	   catch(Exception e)
	   {
	    	return new ResponseEntity(HttpStatus.NOT_FOUND);
	   } 
}  
}  
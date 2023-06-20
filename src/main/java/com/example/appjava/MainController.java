package com.example.appjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.appjava.bo.Connect;

@Controller
@RequestMapping(path = "")
@CrossOrigin(maxAge = 3600)
public class MainController {
	
	@Autowired
	Connect connect;

	@GetMapping(path = "")
	   public ResponseEntity<String> getDroneMedications() {
	      
			//connect.connectSftp();
			connect.transferSftpS3();
			//connect.getListFile();
			
			//connect.connectS3();
			return new ResponseEntity<String>("Hello World!", HttpStatus.OK);
	      
	      
	   }

}

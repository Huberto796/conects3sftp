package com.example.appjava.bo;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.RemoteResourceInfo;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;

@Service
public class Connect {
	
	private static final Logger logger = Logger.getLogger( Connect.class );
	
	public void connectSftp() {
		
		logger.info("inicio");
		System.out.println("inicio");
		JSch jsch = new JSch();
		try {
			logger.info("process");
			System.out.println("process");
		    Session jschSession = jsch.getSession("procesosbo-stage", "sftp19.sapsf.com", 22);
		    jschSession.setPassword("Db4B3Hti");
		    jschSession.setConfig("StrictHostKeyChecking", "no");
		    jschSession.connect();
		    logger.info("end process");
		    System.out.println("end process");
		    ChannelSftp chanel = (ChannelSftp) jschSession.openChannel("sftp");
		    chanel.connect();
		    
		    
		    InputStream inputStream = chanel.get("/FEED/UPLOAD/currentAprovalstest.csv");
		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		    
		    File remoteFile = new File( "tsts" );
		    
		    Stream<String> content = reader.lines();
		    content.forEach(a -> System.out.println( a ));
		    
		    
		    chanel.exit();
		    jschSession.disconnect();	
		    
		} catch (JSchException | SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("end method");
	}
	
	public void getListFile() {
		
		// create a instance of SSHClient
        SSHClient client = new SSHClient();
  
        // add host key verifier
        client.addHostKeyVerifier(new PromiscuousVerifier());
  
        // connect to the sftp server
        try {
			client.connect("sftp19.sapsf.com");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        // authenticate by username and password.
        try {
			client.authPassword("procesosbo-stage", "Db4B3Hti");
		} catch (UserAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        // get new sftpClient.
        SFTPClient sftpClient = null;
		try {
			sftpClient = client.newSFTPClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        // Give the path to the directory from which
        // you want to get a list of all the files.
        String remoteDir = "/FEED/UPLOAD";
        List<RemoteResourceInfo> resourceInfoList = null;
        
        RemoteResourceInfo remoteResourceInfo = null;
        
		try {
			resourceInfoList = sftpClient.ls(remoteDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
        for (RemoteResourceInfo file : resourceInfoList) {
            System.out.println("File name is " + file.getName());
            if( file.getName().equals("currentAprovalstest.csv") ) {
            	
            	System.out.println("file download " + file.getName());
            	//file.
        		AWSCredentials credentials = new BasicAWSCredentials(
      				  "AKIAVNLA6PWAL4STIYPF", 
      				  "QBMJySaZlB9Zj7xyUIDXlFYA8+4V/+0sPdUega58"
        				);
      		
        		System.out.println("inicio  client s3");
        		AmazonS3 s3client = AmazonS3ClientBuilder
      				  .standard()
      				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
      				  .withRegion(Regions.US_EAST_1)
      				  .build();
      		
      		//	file.getPath().
        		
        		File remoteFile = new File( "tsts" + file.getName() );
        		try {
					FileWriter fileWriter = new FileWriter("algo.csv");
					//fileWriter.write( file.getAttributes().toBytes() );
					fileWriter.write(file.toString() );
					//fileWriter.
					
					
					
					//remoteFile.
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		System.out.println("create ar errtestsss");
        		s3client.putObject(
      				  "hcp-6c2e47b7-c78a-40ca-95be-e97495d2bdd3", 
      				  "Document/errtestsss.txt", 
      				  //new File("E:\\Users\\HSOLORZANO\\Documents\\err190623.csv")
      				//new File( file.getAttributes().toBytes() )
      				remoteFile
      				);
            	
            }
            System.out.println("");
        }
        
        
	}
	
	
	public void connectS3() {
		
		System.out.println("inicio  connectS3");
		AWSCredentials credentials = new BasicAWSCredentials(
				  "AKIAVNLA6PWAL4STIYPF", 
				  "QBMJySaZlB9Zj7xyUIDXlFYA8+4V/+0sPdUega58"
		);
		
		System.out.println("inicio  client");
		AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();
		
		
		
		s3client.putObject(
				  "hcp-6c2e47b7-c78a-40ca-95be-e97495d2bdd3", 
				  "Document/err190623.txt", 
				  new File("E:\\Users\\HSOLORZANO\\Documents\\err190623.csv")
				);
		
		//s3client
	
		
		System.out.println("fin  connectS3");
		
	}
	
public void transferSftpS3() {
		
		logger.info("inicio");
		System.out.println("inicio");
		JSch jsch = new JSch();
		try {
			logger.info("process");
			System.out.println("process");
		    Session jschSession = jsch.getSession("procesosbo-stage", "sftp19.sapsf.com", 22);
		    jschSession.setPassword("Db4B3Hti");
		    jschSession.setConfig("StrictHostKeyChecking", "no");
		    jschSession.connect();
		    logger.info("end process");
		    System.out.println("end process");
		    ChannelSftp chanel = (ChannelSftp) jschSession.openChannel("sftp");
		    chanel.connect();
		    
		    
		    InputStream inputStream = chanel.get("/FEED/UPLOAD/currentAprovalstest.csv");
		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		    
		    String lContent = new String(  );
		    

		    String lineContent = new String(  );
		    while ((lineContent = reader.readLine()) != null) {
		    	lContent += lineContent + "\n";
	        }
		    
		    
		    System.out.println("inicio  connect S3");
			AWSCredentials credentials = new BasicAWSCredentials(
					  "AKIAVNLA6PWAL4STIYPF", 
					  "QBMJySaZlB9Zj7xyUIDXlFYA8+4V/+0sPdUega58"
			);
			
			System.out.println("inicio  s3client");
			AmazonS3 s3client = AmazonS3ClientBuilder
					  .standard()
					  .withCredentials(new AWSStaticCredentialsProvider(credentials))
					  .withRegion(Regions.US_EAST_1)
					  .build();
			
			
			
			s3client.putObject(
					  "hcp-6c2e47b7-c78a-40ca-95be-e97495d2bdd3", 
					  "Document/TEST.txt", 
					  lContent
					);
			
			
			System.out.println("fin ");
			
			
		    chanel.exit();
		    jschSession.disconnect();	
		    
		} catch (JSchException | SftpException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		logger.info("end method");
	}
	
	

}

package client_server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class log {
	public void write(String data) {
		try{
			File file =new File("log.txt");
		      //if file doesnt exists, then create it
		   	if(!file.exists()){
		   		file.createNewFile();
			 }
			 //true = append file
		   	FileWriter fileWritter = new FileWriter(file.getName(),true);
		   	fileWritter.write(data+"\r\n");
		   	fileWritter.close();
		     }catch(IOException e){
		      e.printStackTrace();
		     }
	}
}

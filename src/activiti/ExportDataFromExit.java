package activiti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.fasterxml.jackson.databind.ObjectMapper;

import ubidotsCode.ubidots;

public class ExportDataFromExit implements JavaDelegate {
	public void execute(DelegateExecution execution) 
	{
		
		String file="C:/Program Files/Apache Software Foundation/Tomcat 9.0/bin/activiti_data.json";
		String apiKeyUbidots=ubidots.returnJSONvalue("API key",file);
		String variableID=ubidots.returnJSONvalue("Temperatura da Casa ID",file);
		
		String temperatiraexterior=execution.getVariable("temperaturaexterior").toString();
		String tempInterior=execution.getVariable("temperaturainterior").toString();
		String nomeDoUtilizador=execution.getVariable("nomeutilizador").toString();
		String temperaturaDeConforto=execution.getVariable("temperaturadeconforto").toString();
		
		
		JSONObject user = new JSONObject();
		//Inserting key-value pairs into the json object
	    JSONObject jsonfiledata=new JSONObject();
	    ObjectMapper mapper = new ObjectMapper();
	    
	    user.put("Temperatura de Conforto", temperaturaDeConforto);
	    user.put("Temperatura Interior", tempInterior);
	    user.put("Temperatura Exterior", temperatiraexterior);
	    user.put("Em Casa", "Negativo");
	    try {
	    	jsonfiledata = mapper.readValue(new File("C:/Users/Pedro Capelo/Desktop/activiti_data.json"), JSONObject.class);
	    	if(jsonfiledata.get(nomeDoUtilizador)!=null) {//user exists
	    		Object user2=jsonfiledata.get(nomeDoUtilizador);
				JSONObject jsonuser = (JSONObject) JSONValue.parse(new ObjectMapper().writeValueAsString(user2));
				
				jsonuser.put("Temperatura Interior", tempInterior);
				jsonuser.put("Temperatura Exterior", temperatiraexterior);
				jsonuser.put("Em Casa", "Negativo");
				
				jsonfiledata.put(nomeDoUtilizador,jsonuser);
	    	}else {//user does not exist
	    		jsonfiledata.put(nomeDoUtilizador,user);
	    	}
	    	//Write into the file
	        FileWriter jsonfile = new FileWriter("C:/Users/Pedro Capelo/Desktop/activiti_data.json");      
	        jsonfile.write(jsonfiledata.toString());
	        jsonfile.close();
	        System.out.println("Successfully updated json object to file...!!");
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		
	}
}
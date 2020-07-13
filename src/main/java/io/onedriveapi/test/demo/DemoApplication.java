package io.onedriveapi.test.demo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	static String clientid = "06e1316b-fa4e-4fb7-9d9c-44188155c4b8";
	static String clientsecret = "L5_Q~_.61.uic_Kpqtze4pwg-Ohk~.we6l";
	static String baseUrl = "https://login.microsoft.com/common/oauth2/v2.0/";
	static String authorizationCode = "authorization_code";
	static String redirectUri = "http://localhost:8088/onedrivetest";
	static String scope = "onedrive.readwrite";
	static String readLine = null;
	static String accesstoken = null ;
	static int responseCode = 0;
	
	public static void main(String[] args) throws IOException{
		SpringApplication.run(DemoApplication.class, args);
		URL urlForCode = new URL(baseUrl+"authorize?"+"client_id="+clientid+"&scope="+scope+"&response_type=code"+"&redirect_uri="+redirectUri);
		Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler " + urlForCode);
	}
	
	public static void testapi(String code) throws IOException, JSONException {	       
		       File file = new File("D:\\CV.pdf");

		       //get access token
		       accesstoken =  getAccessToken(baseUrl,clientid,redirectUri,authorizationCode, code,clientsecret);
		       //upload api
		       URL urlforfileupload = new URL("https://api.onedrive.com/v1.0/drive/root:/Test/cv.pdf:/content");
		       HttpURLConnection conection2 = (HttpURLConnection) urlforfileupload.openConnection();
		       System.out.println(conection2);
		       conection2.setRequestMethod("PUT");
		       conection2.setRequestProperty("Authorization", "Bearer " + accesstoken);
		       conection2.setDoOutput(true);
		       //send the above info in the body
		       byte[] outputInBytes = readFileToByteArray(file);
		       OutputStream os = conection2.getOutputStream();
		       os.write( outputInBytes );    
		       os.close();
		       responseCode = conection2.getResponseCode();
		       if (responseCode == HttpURLConnection.HTTP_OK) {
		           BufferedReader in = new BufferedReader(
		               new InputStreamReader(conection2.getInputStream()));
		           StringBuffer response = new StringBuffer();
		           while ((readLine = in .readLine()) != null) {
		               response.append(readLine);
		           } in .close();
		           // print result
		           System.out.println("JSON String Result " + response);
		       }else {
		    	   System.out.println("Bad Request");
		       }
		    		 
		}
		
	private static byte[] readFileToByteArray(File file){
		    FileInputStream fis = null;
		    // Creating a byte array using the length of the file
		    byte[] bArray = new byte[(int) file.length()];
		    try{
		      fis = new FileInputStream(file);
		      fis.read(bArray);
		      fis.close();                   
		    }catch(IOException ioExp){
		      ioExp.printStackTrace();
		    }
		    return bArray;
		  } 
	
	private static String getAccessToken(String baseUrl, String clientid, String redirectUri, String authorizationCode, String code, String clientsecret) throws IOException, JSONException{
		URL urlForAccessToken = new URL(baseUrl+"token");
	       String readLine = null;
	       int responseCode = 0;
	       String accesstoken = null;
	       HttpURLConnection conection = (HttpURLConnection) urlForAccessToken.openConnection();
	       System.out.println(conection);
	       conection.setRequestMethod("POST");
	       conection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	       conection.setDoOutput(true);
	       String str =  "client_id="+clientid+"&redirect_uri="+redirectUri+"&client_secret="+clientsecret+"&code="+code+"&grant_type="+authorizationCode;

	       //send the above info in the body
	       byte[] outputInBytes = str.getBytes("UTF-8");
	       OutputStream os = conection.getOutputStream();
	       os.write( outputInBytes );    
	       os.close();
	       responseCode = conection.getResponseCode();
	       if (responseCode == HttpURLConnection.HTTP_OK) {
	           BufferedReader in = new BufferedReader(
	               new InputStreamReader(conection.getInputStream()));
	           StringBuffer response = new StringBuffer();
	           while ((readLine = in .readLine()) != null) {
	        	   response.append(readLine);
	           } in .close();
	           // print result
	           JSONObject obj = new JSONObject(response.toString());
		       System.out.println(obj);
		       accesstoken =(String) obj.get("access_token");
		       System.out.println(accesstoken);
	       } else {
	           System.out.println("Bad Request");
	       }
	     
		return accesstoken;
	}
}

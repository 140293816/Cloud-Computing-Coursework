package point;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

 











import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

 

public class POS {
 private static final String storageConnectionString =
		        "DefaultEndpointsProtocol=http;"
		        + "AccountName=portalvhdsyr89wrycr3hld;"
		        + "AccountKey=90kFLTt8sMK0iDn8KLtQjj30jQq8BmzDpeyNSDCunQ6A8xw+vexzdSL1xTNNgAlx/La5gdxGxfXRwGFvRPpmaw==";

 	
 private static final Logger log = Logger.getLogger(POS.class.getName());
 
 private Options options = new Options();
 
 private final String country;

 

 public POS(String country) { 

  this.country = country;

  options.addOption("h", "help", false, "show help.");

  options.addOption("query1", "type1", true, "type in the query in JSON format");
  
  options.addOption("query2", "type2", true, "type in the query in JSON format");
  
  options.addOption("createCustomer", "customer", true, "type in the customer details in JSON format");
  
  options.addOption("createOrder", "order", true, "type in the order details in JSON format");
  
  checkPool();
 

 }
 
 
 public void parse(String[] args)
 {
	 boolean connection=isInternetReachable();
	 if(connection){sendMessage();}
	 
	 String[] str=process(args);
	 
	 if(str!=null)
	 {
		 if(connection)
		 {
		 for(int i=0;i<str.length;i++)
		 {
			 addMessage(str[i]);
		 }
		 }
		 else
		 { 
		 for(int i=0;i<str.length;i++)
		 {
			 reserveMessage(str[i]);
		 }
		 }
	 }
 }
 

 public String[] process(String[] args) {

  CommandLineParser parser = new BasicParser();

  CommandLine cmd = null;
  
  try {

   cmd = parser.parse(options, args);
   
  
   
  if (cmd.hasOption("h"))
   {	   
    help();
    return null;
   }
   
   else if(cmd.hasOption("query1"))
   {
	   String str=cmd.getOptionValue("query1");
	   if(isJSONString(str))
	   {
		   
		   return new String[]{createId(str)};
		   
		   
	   }
	   else 
	   {
		   log.log(Level.SEVERE, "Please type in your commands in JSON format");
		   help();
		   return null;
	   }
   }
  
   else if(cmd.hasOption("query2"))
   {
	   String str=cmd.getOptionValue("query2");
	   if(isJSONString(str))
	   {		   
		  return new String[]{changeMessage(str)};
		   
	   }
	   else 
	   {
		   log.log(Level.SEVERE, "Please type in your commands in JSON format");
		   help();
		   return null;
	   }
   }
  
  
   else if(cmd.hasOption("createCustomer"))
   {
	   String str=cmd.getOptionValue("createCustomer");
	   if(isJSONString(str))
	   {		   
		  return customer(str);
		   
	   }
	   else 
	   {
		   log.log(Level.SEVERE, "Please type in your commands in JSON format");
		   help();
		   return null;
	   }
   }
  
   else if(cmd.hasOption("createOrder"))
   {
	   String str=cmd.getOptionValue("createOrder");
	   if(isJSONString(str))
	   {		   
		   return order(str);
		   
	   }
	   else 
	   {
		   log.log(Level.SEVERE, "Please type in your commands in JSON format");
		   help();
		   return null;
	   }
   }
  
   

  
   
   else 
   {
    log.log(Level.SEVERE, "Illegal command");
    help();
    return null;
   }
   
   


  } catch (ParseException e) {

   log.log(Level.SEVERE, "Failed to parse comand line properties", e);

   help();return null;

  }

 }
 public boolean isInternetReachable()
 {
     try {
         //make a URL to a known source
         URL url = new URL("https://manage.windowsazure.com/");

         //open a connection to that source
         HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

         //trying to retrieve data from the source. If there
         //is no connection, this line will fail
         Object objData = urlConnect.getContent();

     } catch (UnknownHostException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return false;
     }
     catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return false;
     }
     return true;
 }

 private Boolean isJSONString(String str)
	{
		try
		{
			new JSONObject(str);
		}
		catch(Exception e){return false;}
		return true;
	} 
 
 private void help() {

  // This prints out some help

	 HelpFormatter formater = new HelpFormatter();	  
	
	 formater.printHelp("Main", options);


 }
 
 private String changeMessage(String str)
 {
	 try{
	 JSONObject json=new JSONObject(str);
	 json.put("country", country);	 
	 json.put("messageId", UUID.randomUUID().toString());
	 return json.toString();
	 }
	 catch( Exception e)
	 {
		 log.log(Level.SEVERE, "unknown error", e);
		 return "";
	 }
 }
 
 private String createId(String str)
 {
	 try {
		JSONObject json=new JSONObject(str);
		json.put("messageId", UUID.randomUUID().toString());
		
		return json.toString();
		
	} catch (JSONException e) {
		
		e.printStackTrace();
		return "";
	}
 }
 
 private String[] customer(String str)
 {
	 try {
		    String id = UUID.randomUUID().toString();
		    
			JSONObject json=new JSONObject(str);
			json.put("messageId", UUID.randomUUID().toString());
			json.put("customerId", id);
			json.put("country", country);
			json.put("database","NoSQL");
			json.put("type", "createCustomer");
			
			JSONObject json1=new JSONObject(str);
			json1.put("messageId", UUID.randomUUID().toString());
			json1.put("customerId", id);
			json1.put("country", country);
			json1.put("database","MSSQL");
			json1.put("type", "createCustomer");
			
			return new String[]{json.toString(),json1.toString()};
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
 }
 
 private String[] order(String str)
 {
	 try {
			JSONObject json=new JSONObject(str);
			String id=UUID.randomUUID().toString();
			String number =UUID.randomUUID().toString();
			
			json.put("messageId", UUID.randomUUID().toString());
			json.put("orderId", id);
			json.put("number", number);
			json.put("database","NoSQL");
			json.put("type", "createOrder");
			
			JSONObject json1=new JSONObject(str);
			json1.put("messageId", UUID.randomUUID().toString());
			json1.put("orderId", id);
			json1.put("number", number);
			json1.put("database","MSSQL");
			json1.put("type", "createOrder");
			
			return new String[]{json.toString(),json1.toString()};
			
		} catch (JSONException e) {
			
			e.printStackTrace();
			return null;
		}
 }
 
 
 private void addMessage(String str)
 {
	 try
 	{
 			 	   
 	    // Retrieve storage account from connection-string.
 	    CloudStorageAccount storageAccount = 
 	       CloudStorageAccount.parse(storageConnectionString);

 	    // Create the queue client.
 	    CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

 	    // Retrieve a reference to a queue.
 	    CloudQueue queue = queueClient.getQueueReference("queue0");

 	    // Create the queue if it doesn't already exist.
 	    queue.createIfNotExists();

 	    // Create a message and add it to the queue.
 	    CloudQueueMessage message = new CloudQueueMessage(str);
 	    queue.addMessage(message);
 	    	    
 	}
 	catch (Exception e)
 	{
 	    // Output the stack trace.
 	    e.printStackTrace();
 	}
 
 }
 
 private void checkPool()
 {
	 try
	 {
		 ObjectInputStream inb =new ObjectInputStream(new FileInputStream("example0"));
	 }
	 catch(Exception e)
	 {
		 createPool();
	 }
 }
 
 private void createPool()
 {
	 try
	 {
		 ObjectOutputStream obj = new ObjectOutputStream (new FileOutputStream("example0"));
		 obj.writeObject(new LinkedList<String>());
		 obj.close();
	 }
	 catch(Exception e){}
 }
 
 private void reserveMessage(String str)
 {
	 try{
		   ObjectInputStream inb =new ObjectInputStream(new FileInputStream("example0"));
		   Queue<String> q = (Queue<String>)inb.readObject();
		   q.add(str);
		   ObjectOutputStream obj = new ObjectOutputStream (new FileOutputStream("example0"));
		   obj.writeObject(q);
		   obj.close();
		   } 
	   catch (Exception e) {
		e.printStackTrace();}
 }
 
 private void sendMessage()
 {
	 try{
		   ObjectInputStream inb =new ObjectInputStream(new FileInputStream("example0"));
		   Queue<String> q = (Queue<String>)inb.readObject();
		   while(!q.isEmpty())
		   {
			   addMessage(q.poll());
		   }
		   ObjectOutputStream obj = new ObjectOutputStream (new FileOutputStream("example0"));
		   obj.writeObject(q);
		   obj.close();
		   } 
	   catch (Exception e) {
		e.printStackTrace();}
 }

}

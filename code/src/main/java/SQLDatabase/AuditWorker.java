package SQLDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import tableStorage.NoSQL;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

public class AuditWorker {
	public static final String storageConnectionString =
	        "DefaultEndpointsProtocol=http;"
	        + "AccountName=portalvhdsyr89wrycr3hld;"
	        + "AccountKey=90kFLTt8sMK0iDn8KLtQjj30jQq8BmzDpeyNSDCunQ6A8xw+vexzdSL1xTNNgAlx/La5gdxGxfXRwGFvRPpmaw==";
	private static MSSQL service=new MSSQL();
	private static NoSQL serviceN=new NoSQL();
	
 
	 
	private static void process(String str) 
	{
		try{
			JSONObject json=new JSONObject(str);
			String type=json.getString("type");
		
		if(type.equals("createCustomer"))
		{
			service.createCustomer(json.getString("customerId"),json.getString("name"), json.getString("country"));
		}
		
		else if(type.equals("createOrder"))
		{
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			service.createOrder(json.getString("orderId"),json.getString("number"),json.getString("customer"), formatter.parse(json.getString("date")), json.getInt("price"));
		}
		
		else if(type.equals("findCustomerById"))
		{
			service.findCustomer(json.getString("id"));
		}
		
		else if(type.equals("findOrderById"))
		{
			service.findOrder(json.getString("id"));
		}
				
		else if(type.equals("showAllCustomers"))
		{
			service.showAllCustomers();			
		}
		
		else if(type.equals("showAllOrders"))
		{
			service.showAllOrders();
		}
		
		else {throw new IllegalStateException("illegal type");}
		
		}
		catch(Exception e)
		{
			 e.printStackTrace();
		}
	}
	private static boolean checkMessage(String str)
	{
		try
		{
			JSONObject json=new JSONObject(str);
			return !serviceN.checkMessage(json.getString("messageId"));
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	private static void storeMessage(String str)
	{
		try
		{
			JSONObject json=new JSONObject(str);
			serviceN.receivedMessage(json.getString("messageId"), json.toString());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private static String checkSQL(String str) 
	{
		try
		{
			JSONObject json=new JSONObject(str);
			return json.getString("database");
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	
	public static void main(String[] args) {
		while(true){
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount = 
		        CloudStorageAccount.parse(storageConnectionString);

		    // Create the queue client.
		    CloudQueueClient queueClient = storageAccount.createCloudQueueClient();
		    
		    CloudQueue queue = queueClient.getQueueReference("queue0");

		    CloudQueueMessage peekedMessage = queue.peekMessage();
		    
		    if(peekedMessage!=null){
		    String message=peekedMessage.getMessageContentAsString();
		    
		    if(!checkMessage(message))
		    {
		    	CloudQueueMessage retrievedMessage = queue.retrieveMessage();
		    	queue.deleteMessage(retrievedMessage);
		    }
		    else
		    {
//		    	System.out.println(message);
		    	String sqlType=checkSQL(message);
		    	
		    	
		    	if(sqlType.equals("NoSQL"))
		    	{
		    		System.out.println("messages for NoSQL");
		    	}
		    	
		    	
		    	else if(sqlType.equals("MSSQL"))
		    	{
		    		CloudQueueMessage retrievedMessage = queue.retrieveMessage();
		    		storeMessage(message);
		    		process(message);
		    		queue.deleteMessage(retrievedMessage);
		    	}
		    	
		    	
		    	else
		    	{
		    		CloudQueueMessage retrievedMessage = queue.retrieveMessage();
		    		System.out.println("illegal messages");
		    		queue.deleteMessage(retrievedMessage);
		    		
		    	}
		    }	
		    }	    
		    Thread.sleep(2000);
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();		    
		}
	    	
	    }
	}

}

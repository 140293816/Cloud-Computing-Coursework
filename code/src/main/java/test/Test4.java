package test;

import org.json.JSONObject;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.queue.*;


public class Test4 {
	 public static final String storageConnectionString =
		        "DefaultEndpointsProtocol=http;"
		        + "AccountName=portalvhdsyr89wrycr3hld;"
		        + "AccountKey=90kFLTt8sMK0iDn8KLtQjj30jQq8BmzDpeyNSDCunQ6A8xw+vexzdSL1xTNNgAlx/La5gdxGxfXRwGFvRPpmaw==";

		    public static void main(String[] args) {
		    	
		    	try
		    	{
		    		JSONObject json=new JSONObject();
				 	   json.put("type", "average");
//				 	   json.put("name", "Gina");
				 	   json.put("country","NCL");
				 	   
				 	   
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
		    	    CloudQueueMessage message = new CloudQueueMessage(json.toString());
		    	    queue.addMessage(message);
		    	    
		    	   

		    	    // Peek at the next message.
		    	    CloudQueueMessage peekedMessage = queue.peekMessage();

		    	    // Output the message value.
		    	    if (peekedMessage != null)
		    	    {
		    	    	
		    	    	System.out.println(peekedMessage.getMessageContentAsString());
		    	   }
		    	}
		    	catch (Exception e)
		    	{
		    	    // Output the stack trace.
		    	    e.printStackTrace();
		    	}
		    }

}

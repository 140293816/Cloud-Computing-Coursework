package test;
import org.json.JSONObject;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.queue.*;


public class Test6 {
	 public static final String storageConnectionString =
		        "DefaultEndpointsProtocol=http;"
		        + "AccountName=portalvhdsyr89wrycr3hld;"
		        + "AccountKey=90kFLTt8sMK0iDn8KLtQjj30jQq8BmzDpeyNSDCunQ6A8xw+vexzdSL1xTNNgAlx/La5gdxGxfXRwGFvRPpmaw==";

		    public static void main(String[] args) {
		    	
		    	try
		    	{
		    	    // Retrieve storage account from connection-string.
		    	    CloudStorageAccount storageAccount = 
		    	        CloudStorageAccount.parse(storageConnectionString);

		    	    // Create the queue client.
		    	    CloudQueueClient queueClient = storageAccount.createCloudQueueClient();

		    	    // Retrieve a reference to a queue.
		    	    CloudQueue queue = queueClient.getQueueReference("queue0");

		    	    // Retrieve the first visible message in the queue.
		    	    CloudQueueMessage retrievedMessage = queue.retrieveMessage();

		    	    if (retrievedMessage != null)
		    	    {
		    	        // Process the message in less than 30 seconds, and then delete the message.
		    	    	System.out.println(retrievedMessage.getMessageContentAsString());
		    	        queue.deleteMessage(retrievedMessage);
		    	    }
		    	}
		    	catch (Exception e)
		    	{
		    	   
		    	    e.printStackTrace();
		    	}
		    	    	
		    	
		    }

}

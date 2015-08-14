package test;
import java.io.*;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;

public class Test2 {
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

    	    // Create the table client.
    	    CloudTableClient tableClient = storageAccount.createCloudTableClient();

    	    // Delete the table and all its data if it exists.
    	    CloudTable cloudTable = new CloudTable("Message",tableClient);
    	    cloudTable.deleteIfExists();
    	    
    	    CloudTable cloudTable1 = new CloudTable("Customer",tableClient);
    	    cloudTable1.deleteIfExists();
    	}
    	catch (Exception e)
    	{
    	    // Output the stack trace.
    	    e.printStackTrace();
    	}
    }
}
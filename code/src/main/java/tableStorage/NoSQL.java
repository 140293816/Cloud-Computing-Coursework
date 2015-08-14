package tableStorage;
import java.io.*;
import java.util.Date;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;

public class NoSQL {
	public static final String storageConnectionString =
	        "DefaultEndpointsProtocol=http;"
	        + "AccountName=portalvhdsyr89wrycr3hld;"
	        + "AccountKey=90kFLTt8sMK0iDn8KLtQjj30jQq8BmzDpeyNSDCunQ6A8xw+vexzdSL1xTNNgAlx/La5gdxGxfXRwGFvRPpmaw==";


	public NoSQL(){}
	
	public void createTable(String name)
	{
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		   // Create the table client.
		   CloudTableClient tableClient = storageAccount.createCloudTableClient();

		   // Create the table if it doesn't exist.		   
		   CloudTable cloudTable = new CloudTable(name,tableClient);
		   cloudTable.createIfNotExists();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public void listTables()
	{
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		    // Loop through the collection of table names.
		    for (String table : tableClient.listTables())
		    {
		      // Output each table name.
		      System.out.println(table);
		   }
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public void createCustomer(String id, String name, String country)
	{
		try
		{
			CustomerEntity cus=new CustomerEntity(id,name,country);
			System.out.println(cus.getRowKey() +
		            " " + cus.getPartitionKey() + 
		            "\t" + cus.getName());
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		    // Create a cloud table object for the table.
		    CloudTable cloudTable = tableClient.getTableReference("Customer");

		   // Create an operation to add the new customer to the people table.
		    TableOperation insertCustomer = TableOperation.insertOrReplace(cus);

		    // Submit the operation to the table service.
		    cloudTable.execute(insertCustomer);
		    System.out.println("customer generated successfully");
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public Boolean findCustomerByid(String id)
	{
		try
		{
		    // Define constants for filters.
		    final String PARTITION_KEY = "PartitionKey";
		    final String ROW_KEY = "RowKey";
		    final String TIMESTAMP = "Timestamp";

		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		   // Create a cloud table object for the table.
		   CloudTable cloudTable = tableClient.getTableReference("Customer");

		    // Create a filter condition where the partition key is "Smith".
		    String partitionFilter = TableQuery.generateFilterCondition(
		       ROW_KEY, 
		       QueryComparisons.EQUAL,
		       id);

		   // Specify a partition query, using "Smith" as the partition key filter.
		   TableQuery<CustomerEntity> partitionQuery =
		       TableQuery.from(CustomerEntity.class)
		       .where(partitionFilter);

		    // Loop through the results, displaying information about the entity.
		    for (CustomerEntity entity : cloudTable.execute(partitionQuery)) {
		        System.out.println(entity.getPartitionKey() +
		            " " + entity.getRowKey() + 
		            "\t" + entity.getName());
		        System.out.println("customer is found");
		    }
		    return cloudTable.execute(partitionQuery).iterator().hasNext();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		    return false;
		}
	}
	
	
	
	public void createOrder(String id, String number,String customer, Date date, int price)
	{
		try
		{
			if(!findCustomerByid(customer)){throw new IllegalArgumentException("Customer dose not exist");}
			OrderEntity order=new OrderEntity(id, number,customer,date,price);
			System.out.println(order.getPartitionKey() +
		            "  " + order.getRowKey() + 
		            "\t" + order.getNumber() +
		            "\t" + order.getDate() +
		            "\t" + order.getPrice());
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		    // Create a cloud table object for the table.
		    CloudTable cloudTable = tableClient.getTableReference("Order");

		   // Create an operation to add the new customer to the people table.
		    TableOperation insertOrder = TableOperation.insertOrReplace(order);

		    // Submit the operation to the table service.
		    cloudTable.execute(insertOrder);
		    System.out.println("order generated successfully");
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public Boolean findOrderById(String id)
	{
		try
		{
		    // Define constants for filters.
		    final String PARTITION_KEY = "PartitionKey";
		    final String ROW_KEY = "RowKey";
		    final String TIMESTAMP = "Timestamp";

		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		   // Create a cloud table object for the table.
		   CloudTable cloudTable = tableClient.getTableReference("Order");

		    // Create a filter condition where the partition key is "Smith".
		    String partitionFilter = TableQuery.generateFilterCondition(
		       ROW_KEY, 
		       QueryComparisons.EQUAL,
		       id);

		   // Specify a partition query, using "Smith" as the partition key filter.
		   TableQuery<OrderEntity> partitionQuery =
		       TableQuery.from(OrderEntity.class)
		       .where(partitionFilter);

		    // Loop through the results, displaying information about the entity.
		    for (OrderEntity entity : cloudTable.execute(partitionQuery)) {
		        System.out.println(entity.getPartitionKey() +
		            " " + entity.getRowKey() + 
		            "\t" + entity.getNumber() +
		            "\t" + entity.getDate() +
		            "\t" + entity.getPrice());
		        System.out.println("order is found");
		            
		   }
		   return cloudTable.execute(partitionQuery).iterator().hasNext();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		    return false;
		}
	}	
	
	
	

	
	public int total(String country)
	{
		try
		{
		    // Define constants for filters.
		    final String PARTITION_KEY = "PartitionKey";
		    final String ROW_KEY = "RowKey";
		  
		    final String TIMESTAMP = "Timestamp";
		    int sum=0;

		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		   // Create a cloud table object for the table.
		   CloudTable cloudTable = tableClient.getTableReference("Customer");
		   CloudTable cloudTable1 = tableClient.getTableReference("Order");
		   
		    // Create a filter condition where the partition key is "Smith".
		    String partitionFilter = TableQuery.generateFilterCondition(
		    		PARTITION_KEY, 
		       QueryComparisons.EQUAL,
		       country);

		   // Specify a partition query, using "Smith" as the partition key filter.
		   TableQuery<CustomerEntity> partitionQuery =
		       TableQuery.from(CustomerEntity.class)
		       .where(partitionFilter);

		    // Loop through the results, displaying information about the entity.
		    for (CustomerEntity entity : cloudTable.execute(partitionQuery)) {
		    	;

			    // Create a filter condition where the partition key is "Smith".
			    String partitionFilter1 = TableQuery.generateFilterCondition(
			       PARTITION_KEY, 
			       QueryComparisons.EQUAL,
			       entity.getRowKey());

			   // Specify a partition query, using "Smith" as the partition key filter.
			   TableQuery<OrderEntity> partitionQuery1 =
			       TableQuery.from(OrderEntity.class)
			       .where(partitionFilter1);

			    // Loop through the results, displaying information about the entity.
			    for (OrderEntity entity1 : cloudTable1.execute(partitionQuery1)) {
			        sum+=entity1.getPrice();
			            
			   }
		    }
		    System.out.println("total price: "+sum);
		   return sum;
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		    return 0;
		}
	}
	
	public int average(String country)
	{
		try
		{
		    // Define constants for filters.
		    final String PARTITION_KEY = "PartitionKey";
		    final String ROW_KEY = "RowKey";
		    final String TIMESTAMP = "Timestamp";
		    int sum=0;
		    int number=0;

		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		   // Create a cloud table object for the table.
		   CloudTable cloudTable = tableClient.getTableReference("Customer");
		   CloudTable cloudTable1 = tableClient.getTableReference("Order");

		    // Create a filter condition where the partition key is "Smith".
		    String partitionFilter = TableQuery.generateFilterCondition(
		       PARTITION_KEY, 
		       QueryComparisons.EQUAL,
		       country);

		   // Specify a partition query, using "Smith" as the partition key filter.
		   TableQuery<CustomerEntity> partitionQuery =
		       TableQuery.from(CustomerEntity.class)
		       .where(partitionFilter);

		    // Loop through the results, displaying information about the entity.
		    for (CustomerEntity entity : cloudTable.execute(partitionQuery)) {	    	

				    // Create a filter condition where the partition key is "Smith".
				    String partitionFilter1 = TableQuery.generateFilterCondition(
				       PARTITION_KEY, 
				       QueryComparisons.EQUAL,
				       entity.getRowKey());

				   // Specify a partition query, using "Smith" as the partition key filter.
				   TableQuery<OrderEntity> partitionQuery1 =
				       TableQuery.from(OrderEntity.class)
				       .where(partitionFilter1);

				    // Loop through the results, displaying information about the entity.
				    for (OrderEntity entity1 : cloudTable1.execute(partitionQuery1)) {
				        sum+=entity1.getPrice();
				        number++;
		    }
		    }
		    System.out.println("average price: "+sum/number);
		   return sum/number;
		
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		    return 0;
		}
	}
	

	
	
	public void ListRecentOrders(String country)
	{
		try
		{
		    // Define constants for filters.
		    final String PARTITION_KEY = "PartitionKey";
		    final String ROW_KEY = "RowKey";
		    final String TIMESTAMP = "Timestamp";
		    final long DAY_IN_MS = 1000 * 60 * 60 * 24;
	    			  

		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		   // Create a cloud table object for the table.
		   CloudTable cloudTable = tableClient.getTableReference("Customer");
		   CloudTable cloudTable1 = tableClient.getTableReference("Order");

		    // Create a filter condition where the partition key is "Smith".
		    String partitionFilter = TableQuery.generateFilterCondition(
		       PARTITION_KEY, 
		       QueryComparisons.EQUAL,
		       country);

		   // Specify a partition query, using "Smith" as the partition key filter.
		   TableQuery<CustomerEntity> partitionQuery =
		       TableQuery.from(CustomerEntity.class)
		       .where(partitionFilter);

		    // Loop through the results, displaying information about the entity.
		   System.out.println("recent orders:");
		    for (CustomerEntity entity : cloudTable.execute(partitionQuery)) {
		    	

			    // Create a filter condition where the partition key is "Smith".
			    String partitionFilter1 = TableQuery.generateFilterCondition(
			       PARTITION_KEY, 
			       QueryComparisons.EQUAL,
			       entity.getRowKey());

			   // Specify a partition query, using "Smith" as the partition key filter.
			   TableQuery<OrderEntity> partitionQuery1 =
			       TableQuery.from(OrderEntity.class)
			       .where(partitionFilter1);

			    // Loop through the results, displaying information about the entity.
			   
			    for (OrderEntity entity1 : cloudTable1.execute(partitionQuery1)) {	
			    	Date date=entity1.getDate();
			    	if(date.after(new Date(System.currentTimeMillis() - (7 * DAY_IN_MS)))&&date.before(new Date()))
			    	{
			    		 System.out.println(entity1.getPartitionKey() +
			 		            " " + entity1.getRowKey() + 
			 		            "\t" + entity1.getNumber() +
			 		            "\t" + date +
			 		            "\t" + entity1.getPrice());
			    	}
			       
			            
			   }
		    }
		  
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		   
		}
	}
	
	
	public void receivedMessage(String id,String message)
	{
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		    // Create a cloud table object for the table.
		    CloudTable cloudTable = tableClient.getTableReference("Message");

		    // Create a new customer entity.
		   Message entity=new Message(id,message);

		    // Create an operation to add the new customer to the people table.
		    TableOperation insertEntity = TableOperation.insertOrReplace(entity);

		    // Submit the operation to the table service.
		    cloudTable.execute(insertEntity);
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}
	
	public boolean checkMessage(String id)
	{
		try
		{
		    // Define constants for filters.
		    final String PARTITION_KEY = "PartitionKey";
		    final String ROW_KEY = "RowKey";
		    final String TIMESTAMP = "Timestamp";

		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		       CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		   // Create a cloud table object for the table.
		   CloudTable cloudTable = tableClient.getTableReference("Message");

		    // Create a filter condition where the partition key is "Smith".
		    String partitionFilter = TableQuery.generateFilterCondition(
		       ROW_KEY, 
		       QueryComparisons.EQUAL,
		       id);

		   // Specify a partition query, using "Smith" as the partition key filter.
		   TableQuery<Message> partitionQuery =
		       TableQuery.from(Message.class)
		       .where(partitionFilter);

		    
		    
		    return cloudTable.execute(partitionQuery).iterator().hasNext();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		    return false;
		}
	}
}

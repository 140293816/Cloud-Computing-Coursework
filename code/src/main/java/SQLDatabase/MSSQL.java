package SQLDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


import tableStorage.CustomerEntity;
import tableStorage.OrderEntity;

public class MSSQL {
	
    public static final String connectionString = 
        "jdbc:sqlserver://pubys26z9f.database.windows.net:1433" + ";" +  
            "database=POS" + ";" + 
            "user=ZZH140293816@pubys26z9f" + ";" +  
            "password=Zzh19921116";

    
    
    public MSSQL(){}
    
    public void operation(String sqlString)
    {
    	// The types for the following variables are
        // defined in the java.sql library.
        Connection connection = null;  // For making the connection
        Statement statement = null;    // For the SQL statement
        ResultSet resultSet = null;    // For the result set, if applicable

        try
        {
            // Ensure the SQL Server driver class is available.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establish the connection.
            connection = DriverManager.getConnection(connectionString);
           

            // Use the connection to create the SQL statement.
            statement = connection.createStatement();

            // Execute the statement.
            statement.executeUpdate(sqlString);

            // Provide a message when processing is complete.
            System.out.println("Processing complete.");

        }
        // Exception handling
        catch (ClassNotFoundException cnfe)  
        {

            System.out.println("ClassNotFoundException " +
                               cnfe.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close resources.
                if (null != connection) connection.close();
                if (null != statement) statement.close();
                if (null != resultSet) resultSet.close();
            }
            catch (SQLException sqlException)
            {
                // No additional action if close() statements fail.
            }
        } 
      
        
        
    }
    
    public void createCustomerTable()
    {
    	String str="CREATE TABLE Customer00 (" + 
                "[ID] [nvarchar](50) NOT NULL," +
                "[Name] [nvarchar](50) NOT NULL," + 
                "[Country] [nvarchar](50) NOT NULL)";
    	operation(str);
    	
    	String index="CREATE CLUSTERED INDEX index1 " + "ON Customer00 (ID)";
    	operation(index);
    }
    
    public void createOrderTable()
    {
    	String str="CREATE TABLE Order00 (" + 
                "[ID] [nvarchar](50) NOT NULL," +
                "[Customer] [nvarchar](50) NOT NULL," + 
                "[Number] [nvarchar](50) NOT NULL," + 
                "[Date] [date] NOT NULL," + 
                "[price] [int] NOT NULL)";
    	operation(str);
    	
    	String index="CREATE CLUSTERED INDEX index1 " + "ON Order00 (ID)";
    	operation(index);
    }
    
    public void createCustomer(String id,String name, String country)
    {
    	CustomerEntity cus=new CustomerEntity(id,name,country);
    	System.out.println(cus.getPartitionKey() +
	            " " + cus.getRowKey() + 
	            "\t" + cus.getName());
    	String str=
                "INSERT INTO Customer00 " + 
                "(ID, Name, Country) " + 
                "VALUES('"+cus.getRowKey()+"', '"+cus.getName()+"', '"+cus.getPartitionKey()+"')";
    	operation(str);
    }
    
    public Boolean findCustomer(String id)
    {
    	Connection connection = null;  // For making the connection
    	Statement statement = null;    // For the SQL statement
    	ResultSet resultSet = null;    // For the result set, if applicable

    	try
    	{
    	    // Ensure the SQL Server driver class is available.
    	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    	    // Establish the connection.
    	    connection = DriverManager.getConnection(connectionString);

    	    // Define the SQL string.
    	    String sqlString = "SELECT * FROM Customer00 WHERE ID='"+id+"'";

    	    // Use the connection to create the SQL statement.
    	    statement = connection.createStatement();

    	    // Execute the statement.
    	    resultSet = statement.executeQuery(sqlString);

    	    // Loop through the results
    	    if (resultSet.next())
    	    {
    	        // Print out the row data
    	        System.out.println(
    	            "Customer with ID " + 
    	            resultSet.getString("ID") + 
    	            " has name " +
    	            resultSet.getString("Name") + ", living in " +
    	            resultSet.getString("country"));
    	        System.out.println("Processing complete.");   
        	    return true;
    	    }


    	}
    	
    	 // Exception handling
        catch (ClassNotFoundException cnfe)  
        {

            System.out.println("ClassNotFoundException " +
                               cnfe.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close resources.
                if (null != connection) connection.close();
                if (null != statement) statement.close();
                if (null != resultSet) resultSet.close();
            }
            catch (SQLException sqlException)
            {
                // No additional action if close() statements fail.
            }
        } 
    	
    	
    	return false;
    }
    
    public void createOrder(String id, String number,String customer, Date date, int price )
    {
    	if(findCustomer(customer))
    	{
    	OrderEntity order=new OrderEntity(id,number,customer,date,price);
    	java.sql.Date sqlDate=new java.sql.Date(date.getTime());
    	System.out.println(order.getPartitionKey() +
	            " " + order.getRowKey() + 
	            "\t" + order.getNumber() +
	            "\t" + order.getDate() +
	            "\t" + order.getPrice());
    	String str=
    			"INSERT INTO Order00 " + 
                "(ID, Customer, Number,Date,price) " + 
                "VALUES('"+order.getRowKey()+"','"+order.getPartitionKey()+"','"+order.getNumber()+"','"+sqlDate+"',"+order.getPrice()+")";
    	System.out.println(str);
    	operation(str);
    	}
    	else
    	{
    		System.out.println("Customer does not exist");
    	}
    }
    
    public void showAllCustomers()
    {
    	Connection connection = null;  // For making the connection
    	Statement statement = null;    // For the SQL statement
    	ResultSet resultSet = null;    // For the result set, if applicable

    	try
    	{
    	    // Ensure the SQL Server driver class is available.
    	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    	    // Establish the connection.
    	    connection = DriverManager.getConnection(connectionString);

    	    // Define the SQL string.
    	    String sqlString = "SELECT * FROM Customer00";

    	    // Use the connection to create the SQL statement.
    	    statement = connection.createStatement();

    	    // Execute the statement.
    	    resultSet = statement.executeQuery(sqlString);

    	    // Loop through the results
    	    while (resultSet.next())
    	    {
    	        // Print out the row data
    	        System.out.println(
    	            "Customer with ID " + 
    	            resultSet.getString("ID") + 
    	            " has name " +
    	            resultSet.getString("Name") + ", living in " +
    	            resultSet.getString("country"));
    	        
        	   
    	    }
    	    System.out.println("Processing complete.");   


    	}
    	
    	 // Exception handling
        catch (ClassNotFoundException cnfe)  
        {

            System.out.println("ClassNotFoundException " +
                               cnfe.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close resources.
                if (null != connection) connection.close();
                if (null != statement) statement.close();
                if (null != resultSet) resultSet.close();
            }
            catch (SQLException sqlException)
            {
                // No additional action if close() statements fail.
            }
        } 
    	
    	
    	
    }
    
    public void showAllOrders()
    {
    	Connection connection = null;  // For making the connection
    	Statement statement = null;    // For the SQL statement
    	ResultSet resultSet = null;    // For the result set, if applicable

    	try
    	{
    	    // Ensure the SQL Server driver class is available.
    	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    	    // Establish the connection.
    	    connection = DriverManager.getConnection(connectionString);

    	    // Define the SQL string.
    	    String sqlString = "SELECT * FROM Order00 ";

    	    // Use the connection to create the SQL statement.
    	    statement = connection.createStatement();

    	    // Execute the statement.
    	    resultSet = statement.executeQuery(sqlString);

    	    // Loop through the results
    	    while (resultSet.next())
    	    {
    	        // Print out the row data
    	    	 System.out.println(
    	    	            "Order with ID " + 
    	    	            resultSet.getString("ID") + 
    	    	            " and SKU number " +
    	    	            resultSet.getString("Number") +
    	    	            " is bought by customer " +
    	    	            resultSet.getString("Customer") + 
    	    	            ", on " + resultSet.getString("Date") +
    	    	            " using " + resultSet.getString("Price") +" pounds");
    	    	        
    	        	   
    	    }
    	    System.out.println("Processing complete.");   


    	}
    	
    	 // Exception handling
        catch (ClassNotFoundException cnfe)  
        {

            System.out.println("ClassNotFoundException " +
                               cnfe.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close resources.
                if (null != connection) connection.close();
                if (null != statement) statement.close();
                if (null != resultSet) resultSet.close();
            }
            catch (SQLException sqlException)
            {
                // No additional action if close() statements fail.
            }
        } 
    	
    	
    	
    }
    
    
    public Boolean findOrder(String id)
    {
    	Connection connection = null;  // For making the connection
    	Statement statement = null;    // For the SQL statement
    	ResultSet resultSet = null;    // For the result set, if applicable

    	try
    	{
    	    // Ensure the SQL Server driver class is available.
    	    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

    	    // Establish the connection.
    	    connection = DriverManager.getConnection(connectionString);

    	    // Define the SQL string.
    	    String sqlString = "SELECT * FROM Order00 WHERE ID='"+id+"'";

    	    // Use the connection to create the SQL statement.
    	    statement = connection.createStatement();

    	    // Execute the statement.
    	    resultSet = statement.executeQuery(sqlString);

    	    // Loop through the results
    	    if (resultSet.next())
    	    {
    	        // Print out the row data
    	    	 System.out.println(
    	    	            "Order with ID " + 
    	    	            resultSet.getString("ID") + 
    	    	            " and SKU number " +
    	    	            resultSet.getString("Number") +
    	    	            " is bought by customer " +
    	    	            resultSet.getString("Customer") + 
    	    	            ", on " + resultSet.getString("Date") +
    	    	            " using " + resultSet.getString("Price") +" pounds");
    	    	        System.out.println("Processing complete.");   
    	        	    return true;
    	    }


    	}
    	
    	 // Exception handling
        catch (ClassNotFoundException cnfe)  
        {

            System.out.println("ClassNotFoundException " +
                               cnfe.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                // Close resources.
                if (null != connection) connection.close();
                if (null != statement) statement.close();
                if (null != resultSet) resultSet.close();
            }
            catch (SQLException sqlException)
            {
                // No additional action if close() statements fail.
            }
        } 
    	
    	
    	return false;
    }

}

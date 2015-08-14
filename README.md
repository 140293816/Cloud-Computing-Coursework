# Cloud-Computing-Coursework
message consuming queues based on Microsoft Azure
                                                       Student Number: 140293816
1.	System description: According to the requirements of this coursework, my system contains the 3 components: point of sale (POS) application, NoSQL Worker and Audit Worker. My POS could add a certain message into my Azure queue, while NoSQL Worker and Audit Worker check the top message of the queue and process the message according to the type of it.

1.1	Point of sale application: this application provides a command line interface to add messages to my Azure queue, using Apache Commons CLI library. All the messages sent from my POS are in JSON format. The command line interface contains 5 types of service:
a.	Provide help information;
b.	Send a query to the Azure queue, including finding customer by id, finding order by id, showing all customer details, showing all order details, calculating the total price of all the orders consumed by customers in a certain country, calculating the average price of per order consumed by customers in a certain country, showing all the orders created in the last 7 days in a certain country;
c.	Create customers in NoSQL and MSSQL database, namely add messages to the Azure queue to create these customers separately;
d.	Create orders in NoSQL and MSSQL database, namely add messages to the Azure queue to create these customers separately;
e.	Provide offline operation. If the machine is disconnected from the Internet, the generated messages will be passed to a local queue which is serialized. When the machine is reconnected, all the messages in the local queue will be popped and sent to my Azure queue.

1.2	NoSQL Worker: This program could monitor the Azure queue by checking the top message in the queue every 2 seconds. If that message is for Audit Worker, NoSQL Worker will do nothing. Otherwise, the message will be processed according the contents of it. If the message is in wrong format or contains illegal arguments, it will be deleted, while the legal message should be processed to create or search certain objects in the NoSQL database.

1.3	Audit Worker: This program could monitor the Azure queue by checking the top message in the queue every 2 seconds. If that message is for NoSQL, Audit Worker will do nothing. Otherwise, the message will be processed according the contents of it. If the message is in wrong format or contains illegal arguments, it will be deleted, while the legal message should be processed to create or search certain objects in the MSSQL database.

 
2.	According to the diagram given in the coursework specification, I made the following assumptions when designing the architecture of my system:
•	All the components in the system may be able to crash or fail at any time, so each of them should be built separately and work independently.
•	I assume the workload in this system is not considerable, so I only create 1 queue to store all the messages sent from points of sale. But if we need to handle some intensive tasks like what we need to do in part3, it may be a better choice to use 2 queues separately storing messages for the 2 Workers. Besides, if one of the Workers fails, the other one may stop working as well in my system. Therefore, building 2 queues may still be a better choice. 
•	As there are a lot of points of sale which work independently and any of them may fail or get disconnected from the Internet, offline operations and At Most One Processing must be ensured in the system.
•	As the SQL database is built for auditing purposes, the structure and contents of it should be nearly the same with NoSQL database.

              
3.	NoSQL Database: My NoSQL database contains two tables:
Customer table: Reserve customer entities. Each customer should have a customer name (String), a unique customer id (String) which is generated randomly in my POS application, a country which is the same as the POS address. I set country as partition key and id as row key, because customers could have the same country value and must have unique ids. According to the definitions of partition key and row key, records with the same partition key will be placed in the same section and row key must be unique to ensure the uniqueness of  a record. Therefore, designing the database in this way could improve the efficiency of queries in task4.
Order table: Store order entities. Each order should have a unique order id (String), a customer id (String) which should exist in the customer table, a unique SKU number (String), an order date (Date) and a total price (int). To improve the effiency of queries in task4, I set customer id as partition key and id as row key, because a customer could own many orders and each order id must be unique.
4.	SQL Database: My SQL database also contains two tables:
 
There are several differences between NoSQL database and relational database:
a.	It only needs a table name to create or delete a NoSQL database while we need to design the structure of a SQL database and use Joins to create it.
b.	We could directly insert, search, update, delete a certain object in the NoSQL database using the provided methods while all the operations for relational database are based on Joins or SQL codes.
c.	If we need to accomplish certain query which may focus on the relationships between different tables, we have to hard code to visit all the relevant tables in NoSQL database while we just need a Joins sentence to accomplish the same function.
d.	Therefore, if we only need to store certain records which hardly have connections between each other, it may be more appropriate to use NoSQL database. On the other side, if we are focusing more on the relationship between different data, using relational database would be a better choice.            

5.	Query Application:
5.1	Calculate order price of all orders placed by customers from a given country:  I create a total method to search all the customers placed in the given country and use their ids to search their orders in order table and add all the price  up to get the total price required above:
 

5.2	Calculate the mean average order price of all orders placed by customers from a given country: As I have done in 4.1, I create a average method to search all the customers placed in the given country and use their ids to search their orders in order table and add all the price up to get the total price and calculate the number of orders. At last, the average price is obviously total number/number.
 
5.3	List the details of all orders placed within the last seven days placed by customers from a given country: similarly, I create a ListRecentOrders method to search all the customers placed in the given country and use their ids to search their orders in order table and if orders are created in the last 7 days, they will be printed out.
 

6.	Generating test customers and orders: I use the following codes to send 1000 customers and 1000 orders to the Azure queue:
 
       As is shown above, I use a local file file.txt to store the existing customer details so that I could create 10000 orders randomly and correctly. I generate 3 java jar files to accomplish this task, so if you want to test my Test.jar, please set the file.txt in the path shown above or change the path:
 

7.	At Least Once Processing: the impact of this in our retail system processing customers’ orders is that the same message may be processed more than once. If NoSQL Worker or Audit Work is processing a certain message which may take a long time and it crashes, it may process the message for more than once, which may cause certain problems like cerating duplicate records in the database. 

To fix this problem, I create a unique id for each message stored in the Azure queue. After a Worker confirms a message should be processed, it will first store the message id and the process the message afterwards. Therefore, before a Worker processes a message, it needs to check whether the message id has already existed in the stored message. If so, it will directly delete the message in the queue to prevent processing the same message twice. Otherwise, it will continue processing the message.

In my project, all the ids of messages which have already been processed are stored in a NoSQL table in the Azure Storage Service. So if the Storage Service crashes and the table is lost, then a message in the Azure queue may be processed more than once. 

Besides, if there are too many messages stored in the table, it will take a long time for the Worker to confirm whether the message has already been processed before. Thus, it may not be very efficient to store all the messages, as more and more room will be needed to reserve them well. Actually, it may be a better solution for At Most Once Processing to store the ids of messages only within certain period, like 7 days. In other words, the table may need to be cleared regularly.


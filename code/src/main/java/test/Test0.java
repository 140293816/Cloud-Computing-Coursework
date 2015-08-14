package test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;






import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.text.DateFormatter;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;





import tableStorage.NoSQL;



public class Test0 {
	
	
   public static void main(String[] args) {
    	NoSQL a = new NoSQL();
//    	Date date =new Date();
//    	a.createTable("Message");
//    	a.createCustomer("Lu","LA");
//    	a.createCustomer("LOU","NCL");
//    	a.createCustomer("Rocky","PVG");
//    	a.findCustomerByid("194c7731-fe34-4e3a-a79d-edee31d711bb");
//    	System.out.println(a.getPriceOfCustomer("194c7731-fe34-4e3a-a79d-edee31d711bb"));
//    	System.out.println(a.getOrdersOfCustomer("194c7731-fe34-4e3a-a79d-edee31d711bb"));
//    	System.out.println(a.total("NCL"));
//    	System.out.println(a.average("NCL"));
//    	a.createTable("Order123");
//    	a.ListRecentOrders("LA");
//    	a.createOrder("1101","123434","194c7731-fe34-4e3a-a79d-edee31d711bb",date,2300);
//    	a.findOrderById("111");
//    	a.createOrder("2",date,10);
//    	a.createOrder("2",date,1000);
//    	a.findOrderById("57dfe8ef-1fd6-4078-8ac6-aa4464b65534");
//    	System.out.println(a.checkMessage("123"));
	   
   }
}
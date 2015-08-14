package tableStorage;

import java.util.Date;

import java.util.UUID;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class OrderEntity extends TableServiceEntity{



private String number;
private Date date;
private int price;

public OrderEntity(String id, String number,String customer, Date date, int price)
{
	this.partitionKey = customer;
    this.rowKey = id;
    this.date = date;
    this.number= number;
    this.price=price;
    
}

public OrderEntity(){}



public void setNumber(String number){this.number=number;}

public String getNumber(){return number;}

public void setDate(Date date){this.date=date;}

public Date getDate(){return date;}

public void setPrice(int price){this.price=price;}

public int getPrice(){return price;}


}

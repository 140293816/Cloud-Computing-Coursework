package tableStorage;

import com.microsoft.azure.storage.table.TableServiceEntity;
import java.util.UUID;

public class CustomerEntity extends TableServiceEntity{
	

private String name;

public CustomerEntity(String id,String name, String country)
{
	this.partitionKey = country;
    this.rowKey = id;
    this.name = name;
    
}

public CustomerEntity(){}

public void setName(String country)
{
	this.name=country;
}

public String getName()
{
	return name;
}


}

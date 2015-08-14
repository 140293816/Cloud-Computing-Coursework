package tableStorage;

import com.microsoft.azure.storage.table.TableServiceEntity;
import java.util.UUID;

public class Message extends TableServiceEntity{



public Message(String id,String message)
{
	this.partitionKey = message;
    this.rowKey =id;
    
    
}

public Message(){}


}

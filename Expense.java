package Application_Logic;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Expense implements Serializable {
    private double amount;
    private String description;
    private String type;
    private Date date;

    public Expense(double amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.date = new Date();
    }

    public double getAmount() {
    	return amount; 
    	}
    
    public void setAmount(double amount) {
    	this.amount = amount; 
    	}

    public String getDescription() { 
    	return description; 
    	}
    
    public void setDescription(String description) { 
    	this.description = description; 
    	}

    public String getType() { 
    	return type; 
    	}
    public void setType(String type) { 
    	this.type = type; 
    	}

    public Date getDate() { 
    	return date; 
    	}
    public void setDate(Date date) { 
    	this.date = date;
    	
    }
}

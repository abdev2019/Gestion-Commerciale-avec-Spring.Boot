package gc.rec.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Reglement implements Serializable
{
	@Id @GeneratedValue
	private Long id;
	
	@Size(min=2, max=100)
	private String type;  

	public Reglement() {
		super();
	}
 
	public Reglement(Long id, String type ) {
		super();
		this.id = id;
		this.type = type;  
	}
	 
	public Reglement(String type) {
		super(); 
		this.type = type;  
	}
  

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
 
	
	
}

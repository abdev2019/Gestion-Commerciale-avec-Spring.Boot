package gc.rec.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id; 
import javax.validation.constraints.Size;

@Entity
public class Famille implements Serializable
{
	@Id @GeneratedValue
	private Long code;
	
	@Size(min=2, max=1000)
	private String designation;
	 

	public Famille() {
		super();
	}
	
	public Famille(Long code,String designation) {
		super();
		this.code= code;
		this.designation = designation;
	}
	
	public Famille(String designation) {
		super();
		this.designation = designation;
	}

	@Override
	public String toString() {
		return designation;
	}
	
	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	
	
}

package gc.rec.entities;
 
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Role implements Serializable
{
	@Id @NotNull @GeneratedValue Long id;
	
	//@Size(min=3,max=100)  
	 private String role;
	
	//@Size(min=3,max=100) 
	 private String designation;

	public Role() {} 

	public Role(String r) {role=r;}
	public Role(Long id,String r) {role=r;this.id=id;}

	public String getRole() { return role; } 
	public void setRole(String role) { this.role = role; }
	@Override public String toString() {return role;}

	
	
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}

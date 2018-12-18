package gc.rec.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
 

@Entity
public class User implements Serializable
{
	@Id @NotNull @Size(min=4,max=100) private String username;
	
	@Size(min=4,max=100)
	@JsonIgnore
	private String password;
	
	@NotNull
	private boolean active = false;
	    
	@OneToMany(mappedBy="user",fetch=FetchType.LAZY) 
	private Collection<UsersRoles> roles;
	
	public User() {}
	 
	public User(String username, String password, Boolean active) {
		super();
		this.username = username;
		this.password = password;
		this.active = active; 
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<UsersRoles> getRoles() {
		return roles;
	}

	public void setRoles(Collection<UsersRoles> roles) {
		this.roles = roles;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}  

	@Override
	public String toString() { return username; }
}
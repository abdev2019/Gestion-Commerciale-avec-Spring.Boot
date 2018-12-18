package gc.rec.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable; 


@Entity 
public class Dossier  implements Serializable
{
	@Id  @GeneratedValue
	private Long numero;
	
	@Size(min=3, max=50)
	private String nom;
 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateCreation;
	 
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Nullable
	private Date dateFermeture;
	
	@ManyToOne
	@JoinColumn(name="USERNAME") 
	@NotFound(action = NotFoundAction.IGNORE)
	private User user;
	
	public Dossier() { }

	public Dossier(@Size(min = 2, max = 50) String nom, Date dateCreation) {
		super();
		this.nom = nom;
		this.dateCreation = dateCreation;
	}
	

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

  
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateFermeture() {
		return dateFermeture;
	}
 
	public void setDateFermeture(Date dateFermeture) {
		this.dateFermeture = dateFermeture;
	}
	
	@Override
	public String toString() { 
		return "{"+numero+", "+nom+", "+dateCreation+", "+dateFermeture+"}";
	}
	
}

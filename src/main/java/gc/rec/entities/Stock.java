package gc.rec.entities;
 
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne; 
import javax.validation.constraints.Min; 

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
 


@Entity
public class Stock implements Serializable 
{ 
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id; 
	private Date date;  
	@Min(0) private int quantite; 
	
	@ManyToOne
	@JoinColumn(name="REF_PRODUIT")  
	@NotFound(action = NotFoundAction.IGNORE)
	private Produit produit; 
	
	public Stock() {}
	public Stock(Date d, int qte,Produit prd) {quantite=qte; produit=prd;date=d;}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	} 
	
	
	
}

package gc.rec.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
public class LigneFacture  implements Serializable
{
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="NUM_FACTURE")
	private Facture facture; 
	
	@ManyToOne
	@JoinColumn(name="REF_PRODUIT")
	@NotFound(action = NotFoundAction.IGNORE)
	private Produit produit;
	
	private int qte;
	private double prix; 
	
	private double total;
	
	private double ttc;
	  
	
	public LigneFacture() {}

	public LigneFacture(Facture fact,  Produit produit, int qte, double prix) {
		super();  
		facture = fact; 
		this.produit = produit;
		this.qte = qte;
		this.prix = prix;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Facture getFacture() {
		return facture;
	}

	public void setFacture(Facture facture) {
		this.facture = facture;
	}
 

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public int getQte() {
		return qte;
	}

	public void setQte(int qte) {
		this.qte = qte;
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getTtc() {
		return ttc;
	}

	public void setTtc(double ttc) {
		this.ttc = ttc;
	} 
	
	
}

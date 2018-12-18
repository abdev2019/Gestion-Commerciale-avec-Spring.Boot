package gc.rec.entities;

import java.io.Serializable; 

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction; 



@Entity
public class LigneCommande  implements Serializable
{
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="NUM_COMMANDE")
	private Commande commande;
	
	@ManyToOne
	@JoinColumn(name="REF_PRODUIT")
	@NotFound(action = NotFoundAction.IGNORE)
	private Produit produit;
	
	private int qte;
	
	private double total;
	
	private double ttc;
	
	
	
	public LigneCommande() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LigneCommande(Commande commande, Produit produit, int qte) {
		super();
		this.commande = commande;
		this.produit = produit;
		this.qte = qte;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return produit+" : "+qte;
	}
}

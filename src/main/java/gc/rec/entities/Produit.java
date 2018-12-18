package gc.rec.entities;
 
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
 


@Entity
public class Produit implements Serializable 
{ 
	@Id @NotNull @Size(min=2, max=1000) private String ref; 
	@Size(min=2, max=1000) private String designation; 
	@DecimalMin("0")  private double prix; 
	@Min(0) private int quantite;
	@Min(0) private int quantiteAlert;
	
	@ManyToOne
	@JoinColumn(name="CODE_FAMILLE", nullable=true)  
	@NotFound(action = NotFoundAction.IGNORE)
	private Famille famille;
	
	@ManyToOne
	@JoinColumn(name="CODE_TVA", nullable=true)  
	@NotFound(action = NotFoundAction.IGNORE) 
	private Tva tva;
	
	public Produit() {} 
	
	public Produit(String ref, String designation, double prix, int quantite, Famille famille, Tva tva, int quantiteAlert) {
		super();
		this.ref = ref;
		this.designation = designation;
		this.prix = prix;
		this.quantite = quantite;
		this.quantiteAlert = quantiteAlert;
		this.famille = famille;
		this.tva = tva;
	}

	public Produit(String designation, double prix, int quantite, Famille famille, Tva tva, int quantiteAlert) {
		super(); 
		this.designation = designation;
		this.prix = prix;
		this.quantite = quantite;
		this.quantiteAlert = quantiteAlert;
		this.famille = famille;
		this.tva = tva;
	}

	public String getRef() {
		return ref;
	}



	public void setRef(String ref) {
		this.ref = ref;
	}



	public String getDesignation() {
		return designation;
	}



	public void setDesignation(String designation) {
		this.designation = designation;
	}



	public double getPrix() {
		return prix;
	}



	public void setPrix(double prix) {
		this.prix = prix;
	}



	public int getQuantite() {
		return quantite;
	}



	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}



	public int getQuantiteAlert() {
		return quantiteAlert;
	}



	public void setQuantiteAlert(int quantiteAlert) {
		this.quantiteAlert = quantiteAlert;
	}



	public Famille getFamille() {
		return famille;
	}



	public void setFamille(Famille famille) {
		this.famille = famille;
	}



	public Tva getTva() {
		return tva;
	}



	public void setTva(Tva tva) {
		this.tva = tva;
	}



	@Override
	public String toString() {
		String i = designation;
				/*"{ ref: "+ref+",\n"
				+ "Des: "+designation+",\n"
				+ "Prix: "+prix+" MAD,\n"
				+ "Quantite: "+quantite+",\n"
				+ "Famille : "+famille+",\n"
				+ "TVA : "+tva+"\n"
				+ "}";*/
		return i;
	}
	
}

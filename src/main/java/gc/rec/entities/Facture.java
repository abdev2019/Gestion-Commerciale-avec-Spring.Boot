package gc.rec.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
 
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id; 
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull; 
import javax.validation.constraints.Size;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
 


@Entity 
public class Facture  implements Serializable
{
	@Id @GeneratedValue
	private Long numero;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateFacture;
	 
	private double total;
	private double ttc;
	 
	@ManyToOne
	@JoinColumn(name="CODE_CLIENT")
	@Nullable
	@NotFound(action = NotFoundAction.IGNORE)
	private Client client;
	
	@ManyToOne 
	@JoinColumn(name="CODE_FOURNISSEUR")
	@Nullable
	@NotFound(action = NotFoundAction.IGNORE)
	private Fournisseur fournisseur;
	
	@ManyToOne
	@JoinColumn(name="NUM_COMMANDE")
	private Commande commande;
 
	@OneToMany(mappedBy="facture",fetch=FetchType.LAZY)
	private Collection<LigneFacture> lignesFacture;
	
	@OneToMany(mappedBy="facture",fetch=FetchType.LAZY)
	private Collection<ReductionFacture> reductions;

	@OneToMany(mappedBy="facture",fetch=FetchType.LAZY)
	private Collection<ReglementFacture> reglements; 
	 
	@ManyToOne
	@JoinColumn(name="NUM_LIVRAISON")
	@Nullable
	private Livraison livraison; 

	@ManyToOne
	@JoinColumn(name="NUM_DOSSIER")
	@NotFound(action = NotFoundAction.IGNORE)
	private Dossier dossier;
	
	public Facture() {
		super();
		// TODO Auto-generated constructor stub
	} 
	
	
	
	

	public Facture(Date dateFacture, double total, double ttc, Commande commande
	) {  
		this.dateFacture = dateFacture; 
		this.total = total;
		this.ttc = ttc;
		this.commande = commande;  
	}
 


	public Date getDateFacture() {
		return dateFacture;
	}

	

	public Collection<ReductionFacture> getReductions() {
		return reductions;
	}



	

	public void setReductions(Collection<ReductionFacture> reductions) {
		this.reductions = reductions;
	}





	public Collection<ReglementFacture> getReglements() {
		return reglements;
	}





	public void setReglements(Collection<ReglementFacture> reglements) {
		this.reglements = reglements;
	}
 





	public Livraison getLivraison() {
		return livraison;
	}





	public void setLivraison(Livraison livraison) {
		this.livraison = livraison;
	}





	public void setDateFacture(Date dateFacture) {
		this.dateFacture = dateFacture;
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



	public Commande getCommande() {
		return commande;
	}



	public void setCommande(Commande commande) {
		this.commande = commande;
	}



	public Collection<LigneFacture> getLignesFacture() {
		return lignesFacture;
	}



	public void setLignesFacture(Collection<LigneFacture> lignesFacture) {
		this.lignesFacture = lignesFacture;
	}

	

	public Client getClient() {
		return client;
	}





	public void setClient(Client client) {
		this.client = client;
	}





	public Fournisseur getFournisseur() {
		return fournisseur;
	}





	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}





	public Dossier getDossier() {
		return dossier;
	}





	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}





	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	} 
	
	

}

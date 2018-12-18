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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
 
 

@Entity  
public class Commande  implements Serializable
{
	@Id @GeneratedValue
	private long numero;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private Date dateCommande;
	
	@NotNull
	private boolean valide;
	
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
	
	@OneToMany(mappedBy="commande",fetch=FetchType.LAZY)
	private Collection<LigneCommande> lignesCommande;
	
	private double total;
	
	@ManyToOne
	@JoinColumn(name="NUM_DOSSIER")
	@NotFound(action = NotFoundAction.IGNORE)
	private Dossier dossier;
	
	
	public Commande() {}

	public Commande(Date dateCommande) {
		super();
		this.dateCommande = dateCommande; 
	}

	
	
	public boolean isValide() {
		return valide;
	}

	public void setValide(boolean valide) {
		this.valide = valide;
	}

	public Dossier getDossier() {
		return dossier;
	}

	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}

	public Fournisseur getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public Date getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}

	public Collection<LigneCommande> getLignesCommande() {
		return lignesCommande;
	}

	public void setLignesCommande(Collection<LigneCommande> lignesCommande) {
		this.lignesCommande = lignesCommande;
	}
	
	
	
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String c = client!=null?"Cliet:"+client.getNom():"";
		String f = fournisseur!=null?"Fournisseur:"+fournisseur.getNom():"";
		
		String res = "{num:"+numero+", date:"+dateCommande+", Valide:"+valide+", "+
		c+f+"}";
		return res;
	}
}

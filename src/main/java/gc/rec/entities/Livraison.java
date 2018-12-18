package gc.rec.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Livraison 
{
	@Id @GeneratedValue
	private Long id;
	/*
	@ManyToOne
	@JoinColumn(name="ID_TRANSPORT")
	private Transport transport;*/
	 

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateLivraison; 
	
	private String adresse;
	
	@ManyToOne
	@JoinColumn(name="NUM_FACTURE")
	private Facture facture;
	
	public Livraison() {} 
	
	 
	public Livraison(Date dateLivraison, Facture facture) {
		super();
		//this.transport = transport;
		this.dateLivraison = dateLivraison;
		this.facture = facture;
	}




	public Facture getFacture() {
		return facture;
	}

	public void setFacture(Facture facture) {
		this.facture = facture;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	} 
	
	
/*
	public Transport getTransport() {
		return transport;
	}


	public void setTransport(Transport transport) {
		this.transport = transport;
	}*/


	public Date getDateLivraison() {
		return dateLivraison;
	}

	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adress) {
		this.adresse = adress;
	}
	
	
}

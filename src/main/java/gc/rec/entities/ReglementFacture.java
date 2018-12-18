package gc.rec.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ReglementFacture implements Serializable
{
	@Id @GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name="ID_REGLEMENT")
	private Reglement reglement;
	 
	private Double percentage;
	
	@ManyToOne
	@JoinColumn(name="NUM_FACTURE")
	private Facture facture;

	public ReglementFacture() {
		super();
	}
	
	

	
	public ReglementFacture(Reglement reglement, Double percentage, Facture facture) {
		super();
		this.reglement = reglement;
		this.percentage = percentage;
		this.facture = facture;
	}




	public Double getPercentage() {
		return percentage;
	}

 
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
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


	public Reglement getReglement() {
		return reglement;
	}


	public void setReglement(Reglement reglement) {
		this.reglement = reglement;
	}   
}

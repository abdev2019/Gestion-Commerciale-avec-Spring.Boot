package gc.rec.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class ReductionFacture 
{
	@Id @GeneratedValue
	private Long id;


	@ManyToOne
	@JoinColumn(name="ID_REDUCTION")
	private Reduction reduction;
	 
	private Double taux;
	
	
	@ManyToOne
	@JoinColumn(name="NUM_FACTURE")
	private Facture facture;

	
	public ReductionFacture() {}

	
 
	public ReductionFacture(Reduction reduction, Double taux, Facture facture) {
		super();
		this.reduction = reduction;
		this.taux = taux;
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
	
	
	
	public Reduction getReduction() {
		return reduction;
	}



	public void setReduction(Reduction reduction) {
		this.reduction = reduction;
	}



	public Double getTaux() {
		return taux;
	}

	public void setTaux(Double taux) {
		this.taux = taux;
	} 
}

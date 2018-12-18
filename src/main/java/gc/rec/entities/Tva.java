package gc.rec.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Tva implements Serializable
{
	@Id @GeneratedValue 
	private Long code; 
	private String designation;
	private float taux;
	
	
	public Tva() {}
	
	public Tva(String designation, float taux) {
		super();
		this.designation = designation;
		this.taux = taux;
	}
	
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public float getTaux() {
		return taux;
	}
	public void setTaux(float taux) {
		this.taux = taux;
	}
	
	@Override
	public String toString() {
		return taux+"";
	}
}

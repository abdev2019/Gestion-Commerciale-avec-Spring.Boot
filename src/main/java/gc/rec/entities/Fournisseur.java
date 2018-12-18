package gc.rec.entities;

import java.io.Serializable;

import javax.persistence.Entity; 
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable; 


@Entity
public class Fournisseur  implements Serializable
{ 
	@Id @NotNull @Size(min=3, max=100)
	private String code;
	
	@Size(min=3, max=100)
	private String nom;
	
	@Email @NotBlank
	private String email;
	
	@Size(min=3, max=100)
	private String raisonSociale;
	
	@Size(min=3, max=100)
	private String adresse;
	
	@Size(min=8, max=100) @Nullable
	private String compteBancaire;
	
	@Min(1000) 
	@NotNull(message="Le capitale doit etre égal au moins 1000 MAD")
	private Double  capital;
	

	@Pattern(regexp="^(0[0-9]{9,})$",message="Numéro de télephone doit etre composé au minimum de 10 nombres !")
	private String tel;
	

	@Pattern(regexp="^(0[0-9]{9,})$",message="Fax doit etre composé au minimum  de 10 nombres !")
	private String fax;
	
	public Fournisseur() {}
			
	public Fournisseur(String nom, String email, String raisonSociale, String adresse, Double capital, String tel,
			String fax) {
		super();
		this.nom = nom;
		this.email = email;
		this.raisonSociale = raisonSociale;
		this.adresse = adresse;
		this.capital = capital;
		this.tel = tel;
		this.fax = fax;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRaisonSociale() {
		return raisonSociale;
	}

	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Double getCapital() {
		return capital;
	}

	public void setCapital(Double capital) {
		this.capital = capital;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	} 

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nom+" "+code;
	}

	public String getCompteBancaire() {
		return compteBancaire;
	}

	public void setCompteBancaire(String compteBancaire) {
		this.compteBancaire = compteBancaire;
	}
	
}

package gc.rec.entities;

import java.io.Serializable;

import javax.persistence.Entity; 
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


 
@Entity
public class Client  implements Serializable
{
	@Id @NotNull @Size(min=3, max=100)
	private String code;
	
	@Size(min=4, max=100)
	private String nom;
	
	@Size(min=4, max=100)
	private String prenom;
	
	@Min(18)
	private int age;
	 
	@Size(min=4, max=100)
	private String adresse; 
	
	@Email @NotBlank
	private String email;
	
	@Size(min=8, max=100,message="Le numéro de télephone doit etre composé de 10 nombres !")
	private String tel; 
	
	public Client() {}
	   
	public Client(String code, String nom, String prenom, int age, String adresse, String email, String tel) {
		super();
		this.code = code;
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.adresse = adresse;
		this.email = email;
		this.tel = tel;
	}
	
	
	public Client(String nom, String prenom, int age, String adresse, String email, String tel) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.adresse = adresse;
		this.email = email;
		this.tel = tel;
	}




	public String getTel() {
		return tel;
	}




	public void setTel(String tel) {
		this.tel = tel;
	}




	public int getAge() {
		return age;
	}

 
	public void setAge(int age) {
		this.age = age;
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
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return nom+" "+prenom;
	}
}

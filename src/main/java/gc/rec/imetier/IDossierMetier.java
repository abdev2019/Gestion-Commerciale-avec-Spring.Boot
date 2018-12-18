package gc.rec.imetier;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.Dossier;
 
public interface IDossierMetier 
{  
	public List<Dossier> getDossiers();
	
	public Page<Dossier> getDossiers(String nom, int page, int size);

	public Page<Dossier> getDossiersByDateFermeture(String nom, Date dateFermeture, int page, int size); 
	
	public Page<Dossier> getDossiersByDateCreation(String nom, Date dateCreation, int page, int size); 
 	
	public Page<Dossier> getDossiersByDateCreationFermeture(String nom,  Date dateCreation, Date dateFermeture, int page, int size);
		
	public Page<Dossier> getDossiersClosedByDateCreation(String nom, Date dateCreation, int page, int size);
		
	public Page<Dossier> getDossiersClosed(String nom, int page, int size); 
	
	
	
	public Page<Dossier> getDossiersNotClosed(String nom, int page, int size);

	public Page<Dossier> getDossiersNotClosedByDateCreation(String nom, Date dateCreation, int page, int size);
	
	
	public Dossier getDossier( Long num );   
	public Dossier saveDossier(Dossier frs);
	public boolean deleteDossier(Long code);
}

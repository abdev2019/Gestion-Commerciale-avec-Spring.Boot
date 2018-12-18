package gc.rec.imetier;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.Facture;
 

public interface IFactureMetier 
{  


	public List<Facture> getFactures();
	public Page<Facture> getFactures(Long numDossier,int page, int size );
	
	public Page<Facture> getFacturesClients(Long numDossier,int page, int size ); 
	public Page<Facture> getFacturesClients(Long numDossier,Date date, int page, int size );  
	
	public Page<Facture> getFacturesOfClient(Long numDossier, String code, int page, int size );   
	public Page<Facture> getFacturesOfClient(Long numDossier, String code, Date date, int page, int size );   



	public Page<Facture> getFacturesFournisseurs(Long numDossier,int page, int size );
	public Page<Facture> getFacturesFournisseurs(Long numDossier,Date date, int page, int size ); 
	
	public Page<Facture> getFacturesOfFournisseur(Long numDossier, String code, int page, int size );
	public Page<Facture> getFacturesOfFournisseur(Long numDossier, String code, Date date, int page,int size); 
	

	public int getNombreFactures(Long numDossier);
	public int getNombreAchats(Long numDossier);
	public int getNombreVentes(Long numDossier);
	public Double getPrixAchats(Long numDossier);
	public Double getPrixVentes(Long numDossier);
	

	public Integer getQteVentes(Date d1, Date d2);
	
	public Integer getSVentes(Long numDossier, Date d1, Date d2);
	public Integer getSVenteProduit(Long numDossier, Date d1, Date d2, String ref);

	public Integer getSAchats(Long numDossier, Date d1, Date d2);
	public Integer getSAchatsProduit(Long numDossier, Date d1, Date d2, String ref);
	
	public Double getSPrixVentes(Long numDossier, Date d1, Date d2);
	public Double getSPrixVenteProduit(Long numDossier, Date d1, Date d2, String p);
	public Double getSPrixAchats(Long numDossier, Date d1, Date d2);
	public Double getSPrixAchatsProduit(Long numDossier, Date d1, Date d2, String p);
	
	
	public Facture getFacture( Long num );   
	public Facture saveFacture(Facture facture);
	public boolean deleteFacture(Long num);
	public List<Object[]> getSProduitsAchete(Long numDossier, Date d1, Date d2);
	public List<Object[]> getSProduitsVendu(Long numDossier, Date d1, Date d2);
	
	
	public Double getRevenues(Date d1, Date d2);
	public Double getPrixVentes(Date d1, Date d2);
	public Double getPrixAchats(Date d1, Date d2);
}

package gc.rec.metier;

import java.util.Date;
import java.util.List; 

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest; 
import org.springframework.stereotype.Service;

import gc.rec.dao.FactureRepository;
import gc.rec.entities.Facture;
import gc.rec.imetier.IFactureMetier;
 

@Service
@Transactional
public class FactureMetierImpl implements IFactureMetier
{

	@Autowired private FactureRepository fctRep; 
	
	@Override
	public List<Facture> getFactures() { 
		return fctRep.findAll();
	}

	@Override
	public Page<Facture> getFactures(Long numDossier,int page, int size) { 
		return fctRep.findAll(new PageRequest(page, size));
	}
 
	@Override
	public Facture saveFacture(Facture commande) { 
		return fctRep.save(commande);
	}
	 

	@Override
	public Page<Facture> getFacturesClients(Long numDossier, int page, int size) { 
		return fctRep.findAllFacturesClients(numDossier,new PageRequest(page, size) );
	}

	@Override
	public Page<Facture> getFacturesFournisseurs(Long numDossier,int page, int size) { 
		return fctRep.findAllFacturesFournisseurs(numDossier,new PageRequest(page, size));
	}

	@Override
	public Page<Facture> getFacturesClients(Long numDossier,Date date, int page, int size) { 
		return fctRep.findAllFacturesClients(numDossier,date, new PageRequest(page, size));
	}

	@Override
	public Page<Facture> getFacturesFournisseurs(Long numDossier,Date date, int page, int size) { 
		return fctRep.findAllFacturesFournisseurs(numDossier,date, new PageRequest(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfClient(Long numDossier,String code, int page, int size) { 
		return fctRep.findAllFacturesOfClient(numDossier,code, new PageRequest(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfFournisseur(Long numDossier,String code, int page, int size) { 
		return fctRep.findAllFacturesOfFournisseur(numDossier,code, new PageRequest(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfClient(Long numDossier,String code, Date date, int page, int size) { 
		return fctRep.findAllFacturesOfClient(numDossier,code, date, new PageRequest(page, size));
	}

	@Override
	public Page<Facture> getFacturesOfFournisseur(Long numDossier,String code, Date date, int page, int size) { 
		return fctRep.findAllFacturesOfFournisseur(numDossier,code, date, new PageRequest(page, size));
	} 
  

	@Override
	public Facture getFacture(Long num) {
		return fctRep.find(num);
	}

	@Override
	public boolean deleteFacture(Long num) {
		Facture fct = fctRep.find(num); if(fct==null) return false; fctRep.delete(fct); return true;
	}

	
	
	@Override
	public int getNombreFactures(Long numDossier) { 
		return fctRep.count(numDossier);
	}

	@Override
	public int getNombreAchats(Long numDossier) { 
		return fctRep.countAchatsProduits(numDossier);
	}

	@Override
	public int getNombreVentes(Long numDossier) { 
		return fctRep.countVentesProduits(numDossier);
	}

	@Override
	public Double getPrixAchats(Long numDossier) { 
		return fctRep.prixAchatsProduits(numDossier);
	}

	@Override
	public Double getPrixVentes(Long numDossier) { 
		return fctRep.prixVentesProduits(numDossier);
	}

	@Override
	public Integer getSVentes(Long numDossier, Date d1, Date d2) {  
		return fctRep.countSVentesProduits(numDossier,d1,d2);
	}

	@Override
	public Integer getSVenteProduit(Long numDossier, Date d1, Date d2, String ref) { 
		return fctRep.countSVentesProduit(numDossier,d1,d2,ref);
	}

	@Override
	public Integer getSAchats(Long numDossier, Date d1, Date d2) {
		return fctRep.countSAchats(numDossier,d1,d2);
	}

	@Override
	public Integer getSAchatsProduit(Long numDossier, Date d1, Date d2, String ref) {
		return fctRep.countSAchatsProduit(numDossier,d1,d2,ref);
	}

	@Override
	public Double getSPrixVentes(Long numDossier, Date d1, Date d2) {
		return fctRep.countSPrixVentes(numDossier,d1,d2);
	}

	@Override
	public Double getSPrixVenteProduit(Long numDossier, Date d1, Date d2, String p) {
		return fctRep.countSPrixVentesProduit(numDossier,d1,d2,p);
	}

	@Override
	public Double getSPrixAchats(Long numDossier, Date d1, Date d2) {
		return fctRep.countSPrixAchats(numDossier,d1,d2);
	}

	@Override
	public Double getSPrixAchatsProduit(Long numDossier, Date d1, Date d2, String p) {
		return fctRep.countSPrixAchatsProduit(numDossier,d1,d2,p);
	}

	@Override
	public List<Object[]> getSProduitsAchete(Long numDossier, Date d1, Date d2) {
		return fctRep.countSProduitsAchete(numDossier, d1, d2);
	}

	@Override
	public List<Object[]> getSProduitsVendu(Long numDossier, Date d1, Date d2) {
		return fctRep.countSProduitsVendu(numDossier, d1, d2);
	}

	@Override
	public Integer getQteVentes(Date d1, Date d2) {
		return fctRep.countQteVentes(d1,d2);
	}

	@Override
	public Double getRevenues(Date d1, Date d2) {
		return 0d;//fctRep.countRevenues(d1,d2);
	}

	@Override
	public Double getPrixVentes(Date d1, Date d2) {
		return fctRep.countPrixVentes(d1, d2);
	}

	@Override
	public Double getPrixAchats(Date d1, Date d2) {
		return fctRep.countPrixAchats(d1, d2);
	}   
}




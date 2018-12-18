package gc.rec.imetier;

import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.Famille;
 

public interface IFamilleMetier 
{ 
	public List<Famille> getFamilles();
	public Page<Famille> getFamilles(int page, int size );
	
	public List<Famille> getFamillesByDesignation( String designation);
	public Page<Famille> getFamillesByDesignation( String designation, int page, int size );
	
	public Famille getFamille( Long code );   
	public Famille saveFamille(Famille famille);
	public boolean deleteFamille(Long ref);
}

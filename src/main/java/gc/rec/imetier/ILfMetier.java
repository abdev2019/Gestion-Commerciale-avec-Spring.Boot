package gc.rec.imetier; 

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.LigneFacture;
 

public interface ILfMetier 
{   
	public LigneFacture getLigneFacture( Long id );   
	public LigneFacture saveLigneFacture(LigneFacture ligneFacture);
	public boolean deleteLigneFacture(Long id);
	public Page<LigneFacture> getLignesFacture(Date d1, Date d2);
}

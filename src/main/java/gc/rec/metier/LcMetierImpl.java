package gc.rec.metier;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gc.rec.dao.LcRepository;
import gc.rec.entities.LigneCommande;
import gc.rec.imetier.ILcMetier;
 

@Service
@Transactional
public class LcMetierImpl implements ILcMetier
{ 
	@Autowired
	private LcRepository lncRep;

	@Override
	public LigneCommande getLigneCommande(Long id) {
		// TODO Auto-generated method stub
		return lncRep.getOne(id);
	}

	@Override
	public LigneCommande saveLigneCommande(LigneCommande lc) {
		// TODO Auto-generated method stub
		return lncRep.save(lc);
	}

	@Override
	public boolean deleteLigneCommande(Long id) {
		// TODO Auto-generated method stub
		LigneCommande lc = lncRep.getOne(id);
		if(lc==null) return false;
		lncRep.delete(lc);
		return true;
	}

	@Override
	public LigneCommande getLigneCommandeByProduit(Long numeroCommande, String ref) {
		// TODO Auto-generated method stub
		return lncRep.findLcByProduit(numeroCommande,ref);
	}
 
 
}

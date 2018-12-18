package gc.rec.metier;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import gc.rec.dao.LfRepository;
import gc.rec.entities.LigneFacture;
import gc.rec.imetier.ILfMetier;
 

@Service
@Transactional
public class LfMetierImpl implements ILfMetier
{ 
	@Autowired
	private LfRepository lnfRep;

	@Override
	public LigneFacture getLigneFacture(Long id) {
		// TODO Auto-generated method stub
		return lnfRep.getOne(id);
	}

	@Override
	public LigneFacture saveLigneFacture(LigneFacture lc) {
		// TODO Auto-generated method stub
		return lnfRep.save(lc);
	}

	@Override
	public boolean deleteLigneFacture(Long id) {
		// TODO Auto-generated method stub
		LigneFacture lc = lnfRep.getOne(id);
		if(lc==null) return false;
		lnfRep.delete(lc);
		return true;
	}

	@Override
	public Page<LigneFacture> getLignesFacture(Date d1, Date d2) {
		return lnfRep.findAllBetween(d1,d2,new PageRequest(0, 10));
	}
 
 
 
}

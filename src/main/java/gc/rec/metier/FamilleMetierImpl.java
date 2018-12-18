package gc.rec.metier;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import gc.rec.dao.FamilleRepository;
import gc.rec.entities.Famille;
import gc.rec.imetier.IFamilleMetier;
 

@Service
@Transactional
public class FamilleMetierImpl implements IFamilleMetier
{ 
	@Autowired
	private FamilleRepository fmlRep;

	@Override
	public Page<Famille> getFamilles(int page, int size) { 
		return fmlRep.findAll(new PageRequest(page,size));
	}

	@Override
	public Page<Famille> getFamillesByDesignation(String designation, int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Famille getFamille(Long code) {
		try {
		Famille f = fmlRep.findById(code).get();
		return f;
		}catch (Exception e) {return null;}
	}

	@Override
	public Famille saveFamille(Famille famille) {
		// TODO Auto-generated method stub
		return fmlRep.save(famille);
	}

	@Override
	public boolean deleteFamille(Long code) {
		// TODO Auto-generated method stub
		Famille f = getFamille(code);
		if(f==null) return false;
		fmlRep.delete(f);
		return true;
	}

	@Override
	public List<Famille> getFamilles() {
		// TODO Auto-generated method stub
		return fmlRep.findAll();
	}

	@Override
	public List<Famille> getFamillesByDesignation(String designation) {
		// TODO Auto-generated method stub
		return null;
	}
 
 
}

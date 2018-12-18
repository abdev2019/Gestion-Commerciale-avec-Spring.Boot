package gc.rec.metier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gc.rec.dao.ReglementFactureRepository;
import gc.rec.entities.ReglementFacture;
import gc.rec.imetier.IRGFMetier;
 

@Service
@Transactional
public class RGFMetierImpl implements IRGFMetier
{ 
	@Autowired
	private ReglementFactureRepository regRep;
 
	
	@Override
	public ReglementFacture getReglementFacture(Long id) {
		// TODO Auto-generated method stub
		return regRep.getOne(id);
	}

	@Override
	public ReglementFacture saveReglementFacture(ReglementFacture reglementFacture) {
		// TODO Auto-generated method stub
		return regRep.save(reglementFacture);
	}

	@Override
	public boolean deleteReglementFacture(Long id) {
		// TODO Auto-generated method stub
		ReglementFacture rgf = regRep.getOne(id);
		if(rgf==null) return false;
		regRep.delete(rgf);
		return true;
	}
 
 
}

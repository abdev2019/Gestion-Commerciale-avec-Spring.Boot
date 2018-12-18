package gc.rec.metier;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import gc.rec.dao.ProduitRepository;
import gc.rec.entities.Produit;
import gc.rec.imetier.IProduitMetier;
 

@Service
@Transactional
public class ProduitMetierImpl implements IProduitMetier
{ 
	@Autowired
	private ProduitRepository prdRep;

	@Override
	public Page<Produit> getProduitsByFamille(Long codeFamille, int page, int size) { 
		return prdRep.getAllByCodeFamille(codeFamille, new PageRequest(page, size));
	}

	@Override
	public Produit getProduit(String ref) { 
		return prdRep.getById(ref);
	}

	@Override
	public Page<Produit> getProduitsByMotCle(String mc,  int page, int size) {
		return prdRep.findAllByMotCle(mc,new PageRequest(page, size));
	}

	@Override
	public Produit saveProduit(Produit produit) {
		return prdRep.save(produit);
	}

	@Override
	public boolean deleteProduit(String ref) {
		Produit prd = prdRep.getById(ref);
		if(prd!=null) {
			prdRep.delete(prd);
			return true;
		}
		return false;
	}

	@Override
	public List<Produit> getProduitsByFamille(Long codeFamille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Produit> getProduits() {
		// TODO Auto-generated method stub
		return prdRep.findAll();
	}
 
}

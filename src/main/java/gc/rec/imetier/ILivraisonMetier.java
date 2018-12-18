package gc.rec.imetier;

import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.Livraison;
 

public interface ILivraisonMetier 
{  
	public List<Livraison> getAllLivraisons();
	public Page<Livraison> getAllLivraisons(int page, int size ); 
	public Livraison getLivraison( Long id );   
	public Livraison saveLivraison(Livraison livraison);
	public boolean deleteLivraison(Long id);
}

package gc.rec.imetier;

import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.Produit;
 

public interface IProduitMetier 
{ 
	public List<Produit> getProduits();
	public List<Produit> getProduitsByFamille( Long codeFamille );
	public Page<Produit> getProduitsByFamille( Long codeFamille, int page, int size );
	public Page<Produit> getProduitsByMotCle( String mc, int page, int size );
	public Produit getProduit( String ref );   
	public Produit saveProduit(Produit produit);
	public boolean deleteProduit(String ref);
}

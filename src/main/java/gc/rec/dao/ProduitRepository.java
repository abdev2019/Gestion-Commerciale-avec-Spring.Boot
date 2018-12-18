package gc.rec.dao;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.rec.entities.Produit;


@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long>
{
	@Query("select p from Produit p where p.ref=:x")
	public Produit getById( @Param("x")String ref );
	
	@Query("select p from Produit p where p.famille.code = :x")
	public Page<Produit> getAllByCodeFamille( @Param("x")Long codeFamille, Pageable pageable );
	
	@Query("select p from Produit p where p.designation like :x or p.ref like :x or p.famille.designation like :x or p.tva.taux like :x or p.prix like :x or p.quantite like :x or p.quantiteAlert like :x ")
	public Page<Produit> findAllByMotCle( @Param("x")String mc, Pageable pageable );
} 
 




package gc.rec.dao;
 
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.rec.entities.Facture; 


@Repository
public interface FactureRepository extends JpaRepository<Facture, Long>  
{
	@Query("select p from Facture p where p.numero=:num")
	public Facture find(@Param("num")Long numero);
 
	
	@Query("select p from Facture p where p.fournisseur=null and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesClients(@Param("num")Long numeroDossier, Pageable pageable);
 
	@Query("select p from Facture p where p.fournisseur=null and p.dateFacture=:x and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesClients(@Param("num")Long numeroDossier,@Param("x")Date date,Pageable pageable);
	  
	
	@Query("select p from Facture p where p.client.code=:x and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesOfClient(@Param("num")Long numeroDossier,@Param("x")String code, Pageable pageable);
 
	@Query("select p from Facture p where p.client.code=:c and p.dateFacture=:d and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesOfClient(@Param("num")Long numeroDossier,@Param("c")String code,@Param("d")Date date, Pageable pageable);
 
	

	@Query("select p from Facture p where p.client=null and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesFournisseurs(@Param("num")Long numeroDossier, Pageable pageable);

	@Query("select p from Facture p where p.client=null and p.dateFacture=:x and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesFournisseurs(@Param("num")Long numeroDossier,@Param("x")Date date, Pageable pageable);
 
	@Query("select p from Facture p where p.fournisseur.code=:x and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,  Pageable pageable);

	@Query("select p from Facture p where p.fournisseur.code=:x and p.dateFacture=:d and p.dossier.numero=:num")
	public Page<Facture> findAllFacturesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,@Param("d")Date date,  Pageable pageable);
  
 
	
	@Query("select count(p) from Facture p where p.dossier.numero=:num")
	public Integer count(@Param("num")Long numeroDossier);
	 
	@Query("select count(l) from LigneFacture l where l.facture.client=null and l.facture.dossier.numero=:num")
	public Integer countAchatsProduits(@Param("num")Long numeroDossier);
	
	@Query("select count(l) from LigneFacture l where  l.facture.fournisseur=null and l.facture.dossier.numero=:num")
	public Integer countVentesProduits(@Param("num")Long numeroDossier);
	 
	@Query("select coalesce(sum(p.ttc),0) from Facture p where p.client=null and p.dossier.numero=:num")
	public Double prixAchatsProduits(@Param("num")Long numeroDossier);

	@Query("select coalesce(sum(p.ttc),0) from Facture p where p.fournisseur=null and p.dossier.numero=:num")
	public Double prixVentesProduits(@Param("num")Long numeroDossier);
	
	 
	
	
	
	// statistiques


	// ventes
	@Query("select coalesce(sum(lf.qte),0) from LigneFacture lf where lf.facture.fournisseur=null and "
			+ "lf.facture.dossier.numero=:num and lf.facture.dateFacture between :d1 and :d2")
	public Integer countSVentesProduits(@Param("num")Long numeroDossier, 
			@Param("d1")Date d1, @Param("d2")Date d2);
	
	@Query("select coalesce(sum(p.qte),0) from LigneFacture p where p.facture.fournisseur=null "
			+ "and p.produit.ref=:r and p.facture.dossier.numero=:num and p.facture.dateFacture between :d1 and :d2 ")
	public Integer countSVentesProduit(@Param("num")Long numeroDossier, 
			@Param("d1")Date d1, @Param("d2")Date d2, @Param("r")String ref);
 
	@Query("select lf.produit,coalesce(sum(lf.qte),0) from LigneFacture lf where lf.facture.dossier.numero=:num "
			+ "and lf.facture.fournisseur=null and "
			+ "lf.facture.dateFacture between :d1 and :d2 GROUP BY lf.produit" )
	public List<Object[]> countSProduitsVendu(@Param("num")Long numeroDossier ,
			@Param("d1")Date d1, @Param("d2")Date d2);

	
	@Query("select coalesce(sum(f.ttc),0) from Facture f where f.fournisseur=null and "
			+ "f.dossier.numero=:num and f.dateFacture between :d1 and :d2")
	public Double countSPrixVentes(@Param("num")Long numeroDossier ,
			@Param("d1")Date d1, @Param("d2")Date d2);
	

	@Query("select coalesce(sum(lf.ttc),0) from LigneFacture lf where lf.facture.dossier.numero=:num "
			+ "and lf.facture.fournisseur=null and lf.facture.dateFacture between :d1 and :d2 "
			+ "and lf.produit.ref=:r GROUP BY lf.produit")
	public Double countSPrixVentesProduit(@Param("num")Long numeroDossier, 
			@Param("d1")Date d1, @Param("d2")Date d2, @Param("r")String ref);

	
	
	
	// achats

	@Query("select coalesce(sum(lf.qte),0) from LigneFacture lf where lf.facture.client=null and "
			+ "lf.facture.dossier.numero=:num and "
			+ "lf.facture.dateFacture between :d1 and :d2")
	public Integer countSAchats(@Param("num")Long numeroDossier, 
			@Param("d1")Date d1, @Param("d2")Date d2);
	 
	@Query("select coalesce(sum(f.ttc),0) from Facture f where f.client=null and "
			+ "f.dossier.numero=:num and f.dateFacture between :d1 and :d2")
	public Double countSPrixAchats(@Param("num")Long numeroDossier ,
			@Param("d1")Date d1, @Param("d2")Date d2);
 
	@Query("select coalesce(sum(p.qte),0) from LigneFacture p where p.facture.client=null "
			+ "and p.produit.ref=:r and p.facture.dossier.numero=:num "
			+ "and p.facture.dateFacture between :d1 and :d2")
	public Integer countSAchatsProduit(@Param("num")Long numeroDossier, 
			@Param("d1")Date d1, @Param("d2")Date d2, @Param("r")String ref);
	 

	@Query("select coalesce(sum(lf.ttc),0) from LigneFacture lf where lf.facture.dossier.numero=:num "
			+ "and lf.facture.client=null and lf.facture.dateFacture between :d1 and :d2 "
			+ "and lf.produit.ref=:r GROUP BY lf.produit")
	public Double countSPrixAchatsProduit(@Param("num")Long numeroDossier, 
			@Param("d1")Date d1, @Param("d2")Date d2, @Param("r")String ref);
 
	
	@Query("select lf.produit,coalesce(sum(lf.qte),0) from LigneFacture lf where lf.facture.dossier.numero=:num "
			+ "and lf.facture.client=null and lf.facture.dateFacture between :d1 and :d2 GROUP BY lf.produit" )
	public List<Object[]> countSProduitsAchete(@Param("num")Long numeroDossier ,
			@Param("d1")Date d1, @Param("d2")Date d2);

	
	
	
	@Query("select coalesce(sum(lf.qte),0) from LigneFacture lf where lf.facture.fournisseur=null and "
			+ "lf.facture.dateFacture between :d1 and :d2")
	public Integer countQteVentes(@Param("d1")Date d1, @Param("d2")Date d2);

	

	@Query("select coalesce(sum(fv.ttc)-sum(fa.ttc),0) from Facture fv,Facture fa where "
			+ "fv.fournisseur=null and fa.client=null or "
			+ "fv.dateFacture between :d1 and :d2 or "
			+ "fa.dateFacture between :d1 and :d2")
	public Double countRevenues(@Param("d1")Date d1, @Param("d2")Date d2);


	@Query("select coalesce(sum(f.ttc),0) from Facture f where f.fournisseur=null and "
			+ "f.dateFacture between :d1 and :d2")
	public Double countPrixVentes(@Param("d1")Date d1, @Param("d2")Date d2);


	@Query("select coalesce(sum(f.ttc),0) from Facture f where f.client=null and "
			+ "f.dateFacture between :d1 and :d2")
	public Double countPrixAchats(@Param("d1")Date d1, @Param("d2")Date d2); 

	
}  



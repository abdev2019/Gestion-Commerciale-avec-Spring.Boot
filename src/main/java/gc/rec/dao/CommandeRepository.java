package gc.rec.dao;
 
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.rec.entities.Commande; 


@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long>
{
	@Query("select p from Commande p where p.fournisseur=null and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesClients(@Param("num")Long numeroDossier, Pageable pageable);
 
	@Query("select p from Commande p where p.fournisseur=null and p.dateCommande=:x and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesClients(@Param("num")Long numeroDossier,@Param("x")Date date,Pageable pageable);

	@Query("select p from Commande p where p.fournisseur=null and p.valide=:v and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesClients(@Param("num")Long numeroDossier,@Param("v")Boolean valide,Pageable pageable);

	@Query("select p from Commande p where p.fournisseur=null and p.valide=:v and p.dateCommande=:d and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesClients(@Param("num")Long numeroDossier,@Param("v")Boolean valide, @Param("d")Date date ,Pageable pageable);
 
	@Query("select p from Commande p where p.client.code=:x and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfClient(@Param("num")Long numeroDossier,@Param("x")String code, Pageable pageable);
 
	@Query("select p from Commande p where p.client.code=:c and p.dateCommande=:d and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfClient(@Param("num")Long numeroDossier,@Param("c")String code,@Param("d")Date date, Pageable pageable);

	@Query("select p from Commande p where p.client.code = :c and p.valide = :v  and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfClient(@Param("num")Long numeroDossier,@Param("c")String code,@Param("v")Boolean valide,Pageable pageable);
 
	@Query("select p from Commande p where p.client.code = :c and p.valide = :v and p.dateCommande=:d  and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfClient(@Param("num")Long numeroDossier,@Param("c")String code,@Param("v")Boolean valide,@Param("d")Date date,Pageable pageable);
 
	

	@Query("select p from Commande p where p.client=null and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesFournisseurs(@Param("num")Long numeroDossier, Pageable pageable);

	@Query("select p from Commande p where p.client=null and p.dateCommande=:x and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesFournisseurs(@Param("num")Long numeroDossier,@Param("x")Date date, Pageable pageable);

	@Query("select p from Commande p where p.client=null and p.valide=:v and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesFournisseurs(@Param("num")Long numeroDossier,@Param("v")Boolean valide, Pageable pageable);
 
	@Query("select p from Commande p where p.client=null and p.valide=:v and p.dateCommande=:d and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesFournisseurs(@Param("num")Long numeroDossier,@Param("v")Boolean valide, @Param("d")Date date, Pageable pageable);

	
	@Query("select p from Commande p where p.fournisseur.code=:x and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,  Pageable pageable);

	@Query("select p from Commande p where p.fournisseur.code=:x and p.dateCommande=:d and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,@Param("d")Date date,  Pageable pageable);

	@Query("select p from Commande p where p.fournisseur.code=:x and p.valide=:v and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,@Param("v")Boolean valide,  Pageable pageable);

	@Query("select p from Commande p where p.fournisseur.code=:x and p.valide=:v and p.dateCommande=:d and p.dossier.numero=:num")
	public Page<Commande> findAllCommandesOfFournisseur(@Param("num")Long numeroDossier,@Param("x")String code,@Param("v")Boolean valide,@Param("d")Date date ,  Pageable pageable);
	
	

	@Query("select count(p) from Commande p where p.dossier.numero=:num")
	public Integer count(@Param("num")Long numeroDossier);

	@Query("select count(p) from Commande p where p.dateCommande between :d1 and :d2")
	public Integer countBetween(@Param("d1")Date d1, @Param("d2")Date d2);
	
} 
 


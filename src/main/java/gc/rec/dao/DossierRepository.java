package gc.rec.dao;
  

import java.util.Date;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.rec.entities.Dossier; 


@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long>  
{  
	@Query("select p from Dossier p where p.nom like :n order by p.dateCreation desc")
	public Page<Dossier> findAll(
			@Param("n")String nom, Pageable pageable); 

	
	@Query("select p from Dossier p where p.nom like :n and (p.dateFermeture=:df) order by p.dateCreation desc")
	public Page<Dossier> findAllByDateFermeture(
			@Param("n")String nom, @Param("df")Date dateFermeture, Pageable pageable); 

	
	@Query("select p from Dossier p where p.nom like :n and p.dateCreation=:do order by p.dateCreation desc")
	public Page<Dossier> findAllByDateCreation(
			@Param("n")String nom, @Param("do")Date dateCreation, Pageable pageable); 
 
	
	@Query("select p from Dossier p where p.nom like :n and p.dateCreation=:do and p.dateFermeture=:df order by p.dateCreation desc")
	public Page<Dossier> findAllByDateCreationFermeture(
			@Param("n")String nom, @Param("do")Date dateCreation,@Param("df")Date dateFermeture, Pageable pageable);
	
	
	@Query("select p from Dossier p where p.nom like :n and p.dateCreation=:do and p.dateFermeture!=null order by p.dateCreation desc")
	public Page<Dossier> findAllClosedByDateCreation(
			@Param("n")String nom, @Param("do")Date dateCreation, Pageable pageable);
	
	
	@Query("select p from Dossier p where p.nom like :n and p.dateFermeture!=null order by p.dateCreation desc")
	public Page<Dossier> findAllClosed(
			@Param("n")String nom, Pageable pageable);

	
	
	
	
	@Query("select p from Dossier p where p.nom like :n and p.dateFermeture=null order by p.dateCreation desc")
	public Page<Dossier> findAllNotClosed(
			@Param("n")String nom, Pageable pageable);

	@Query("select p from Dossier p where p.nom like :n and p.dateFermeture=null and p.dateCreation=:do  order by p.dateCreation desc")
	public Page<Dossier> findAllNotClosedByDateCreation(
			@Param("n")String nom,@Param("do")Date dateCreation, Pageable pageable);
	
}  


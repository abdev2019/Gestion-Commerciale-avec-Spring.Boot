package gc.rec.dao;
 
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.rec.entities.Client; 


@Repository
public interface ClientRepository extends JpaRepository<Client, Long>  
{
	@Query("select c from Client c where c.code = :x")
	public Client findByCode( @Param("x")String code ); 
	
	@Query("select p from Client p where p.code like :x or p.nom like :x or p.prenom like :x or p.age like :x or p.adresse like :x or p.email like :x or p.tel like :x ")
	public Page<Client> findAllByMotCle( @Param("x")String mc, Pageable pageable );
 
}  










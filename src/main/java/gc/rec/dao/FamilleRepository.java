package gc.rec.dao;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gc.rec.entities.Famille; 


@Repository
public interface FamilleRepository extends JpaRepository<Famille, Long>  
{  
	@Query("select p from Famille p where p.designation like :x")
	public Page<Famille> findAllByDesignation( @Param("x")String mc, Pageable pageable );
}  


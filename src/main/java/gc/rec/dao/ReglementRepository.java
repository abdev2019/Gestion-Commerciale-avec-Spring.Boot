package gc.rec.dao;
  
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import gc.rec.entities.Reglement; 


@Repository
public interface ReglementRepository extends JpaRepository<Reglement, Long>  
{
}  


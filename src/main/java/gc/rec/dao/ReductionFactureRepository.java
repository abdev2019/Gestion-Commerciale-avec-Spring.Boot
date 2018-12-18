package gc.rec.dao;
  
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import gc.rec.entities.ReductionFacture; 


@Repository
public interface ReductionFactureRepository extends JpaRepository<ReductionFacture, Long>  
{
}  


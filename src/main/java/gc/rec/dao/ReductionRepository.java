package gc.rec.dao;
  
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import gc.rec.entities.Reduction; 


@Repository
public interface ReductionRepository extends JpaRepository<Reduction, Long>  
{
}  


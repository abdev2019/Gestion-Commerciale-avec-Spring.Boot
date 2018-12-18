package gc.rec.dao;
  
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import gc.rec.entities.ReglementFacture; 


@Repository
public interface ReglementFactureRepository extends JpaRepository<ReglementFacture, Long>  
{
}  


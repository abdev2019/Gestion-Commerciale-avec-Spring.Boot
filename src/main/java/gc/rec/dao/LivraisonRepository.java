package gc.rec.dao;
  
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import gc.rec.entities.Livraison; 


@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long>  
{
}  


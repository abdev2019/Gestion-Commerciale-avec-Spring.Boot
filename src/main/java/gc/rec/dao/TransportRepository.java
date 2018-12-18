package gc.rec.dao;
  
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import gc.rec.entities.Transport; 


@Repository
public interface TransportRepository extends JpaRepository<Transport, Long>  
{
}  


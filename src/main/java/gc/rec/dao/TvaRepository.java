package gc.rec.dao;
 
import java.util.List;

import org.springframework.data.domain.Page; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import gc.rec.entities.Tva; 


@Repository
public interface TvaRepository extends JpaRepository<Tva, Long>  
{
}  


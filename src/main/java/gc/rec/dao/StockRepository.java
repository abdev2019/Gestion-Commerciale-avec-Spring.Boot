package gc.rec.dao;
 
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;  
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
import gc.rec.entities.Stock; 


@Repository
public interface StockRepository extends JpaRepository<Stock, Long>  
{  
	//SELECT AVG(total) FROM `facture` GROUP BY MONTH(date_facture)
	@Query("select HOUR(s.date) , coalesce(avg(s.quantite),0) from Stock s where s.produit.ref=:p and YEAR(s.date)=:a and MONTH(s.date)=:m and DAY(s.date)=:j group by HOUR(s.date)")
	public List<Object[]> getMoyenStocksJour(@Param("p") String refp,@Param("a")Integer a, @Param("m")Integer m, @Param("j")Integer j);
	
	@Query("select DAY(s.date) , coalesce(avg(s.quantite),0) from Stock s where s.produit.ref=:p and YEAR(s.date)=:a and MONTH(s.date)=:m group by DAY(s.date)")
	public List<Object[]> getMoyenStocksMois(@Param("p") String refp, @Param("a")Integer a, @Param("m")Integer m); 
	 
	@Query("select MONTH(s.date) , coalesce(avg(s.quantite),0) from Stock s where s.produit.ref=:p and YEAR(s.date)=:a group by MONTH(s.date)")
	public List<Object[]> getMoyenStocksAnne(@Param("p") String refp, @Param("a")Integer a);

	
	// all produits
	@Query("select s.produit.designation, HOUR(s.date) , coalesce(avg(s.quantite),0) from Stock s where YEAR(s.date)=:a and MONTH(s.date)=:m and DAY(s.date)=:j group by HOUR(s.date) order by s.produit.ref,HOUR(s.date) asc")
	public List<Object[]> getMoyenStocksJour(@Param("a")Integer a, @Param("m")Integer m, @Param("j")Integer j);
	
	@Query("select s.produit.designation, DAY(s.date) , coalesce(avg(s.quantite),0) from Stock s where YEAR(s.date)=:a and MONTH(s.date)=:m group by DAY(s.date) order by s.produit.ref,DAY(s.date) asc")
	public List<Object[]> getMoyenStocksMois(@Param("a")Integer a, @Param("m")Integer m);

	@Query("select s.produit.designation, MONTH(s.date) , coalesce(avg(s.quantite),0) from Stock s where YEAR(s.date)=:a group by MONTH(s.date),s.produit.ref order by s.produit.ref,MONTH(s.date) asc")
	public List<Object[]> getMoyenStocksAnne(@Param("a")Integer a); 
}  



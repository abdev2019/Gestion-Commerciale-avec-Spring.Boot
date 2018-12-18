package gc.rec.metier;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;
 
import gc.rec.dao.StockRepository;
import gc.rec.entities.Stock;
import gc.rec.imetier.IStockMetier;
 

@Service
@Transactional
public class StockMetierImpl implements IStockMetier
{ 
	@Autowired private StockRepository sRep;

	@Override
	public List<Object[]> getMoyenStocksAnne(String refp, Integer y) { 
		return sRep.getMoyenStocksAnne(refp, y);
	}

	@Override
	public List<Object[]> getMoyenStocksMois(String refp, Integer y,Integer m) {
		return sRep.getMoyenStocksMois(refp,  y,m);
	}

	@Override
	public List<Object[]> getMoyenStocksJour(String refp, Integer y,Integer m,Integer j) {
		return sRep.getMoyenStocksJour(refp, y,m,j);
	}

	@Override
	public void saveStock(Stock stock) {
		sRep.save(stock);
	}

	@Override
	public List<Object[]> getMoyenStocksJour(Integer a, Integer m, Integer j) {
		return sRep.getMoyenStocksJour(a,m,j);
	}

	@Override
	public List<Object[]> getMoyenStocksMois(Integer a, Integer m) {
		return sRep.getMoyenStocksMois(a,m);
	}

	@Override
	public List<Object[]> getMoyenStocksAnne(Integer a) {
		return sRep.getMoyenStocksAnne(a);
	} 
 
}

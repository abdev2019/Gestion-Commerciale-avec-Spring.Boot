package gc.rec.imetier;
 

import gc.rec.entities.ReductionFacture;
 

public interface IRDFMetier 
{   
	public ReductionFacture getReductionFacture( Long id );   
	public ReductionFacture saveReductionFacture(ReductionFacture reglementFacture);
	public boolean deleteReductionFacture(Long id);
}

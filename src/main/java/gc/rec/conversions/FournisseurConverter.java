package gc.rec.conversions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import gc.rec.entities.Fournisseur;
import gc.rec.imetier.IFournisseurMetier;

public final class FournisseurConverter implements Converter<String, Fournisseur> {

	@Autowired private IFournisseurMetier mf;
    public FournisseurConverter(IFournisseurMetier mf) {this.mf = mf;} 
    @Override public Fournisseur convert(String id) { return mf.getFournisseur(id); }
}
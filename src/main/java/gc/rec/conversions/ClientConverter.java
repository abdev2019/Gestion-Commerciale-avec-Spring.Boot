package gc.rec.conversions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import gc.rec.entities.Client;
import gc.rec.imetier.IClientMetier;

public final class ClientConverter implements Converter<String, Client> {

	@Autowired private IClientMetier mc;
    public ClientConverter(IClientMetier mc) {this.mc = mc;} 
    @Override public Client convert(String id) { return mc.getClient(id); }
}
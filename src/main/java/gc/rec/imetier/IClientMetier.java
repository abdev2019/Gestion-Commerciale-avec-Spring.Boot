package gc.rec.imetier;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import gc.rec.entities.Client;
 
public interface IClientMetier 
{ 
	public List<Client> getClients(); 
	public Page<Client> getClients(int page, int size); 
	public Page<Client> getClientsByMotCle(String mc,int page, int size); 
	public Client getClient( String code );   
	public Client saveClient(Client frs);
	public boolean deleteClient(String code); 
}

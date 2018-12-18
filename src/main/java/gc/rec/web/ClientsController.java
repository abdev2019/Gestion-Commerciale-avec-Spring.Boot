package gc.rec.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gc.rec.entities.Client;
import gc.rec.imetier.IClientMetier;


@Controller 
public class ClientsController 
{
	@Autowired
	private IClientMetier metierClient;
	
	@RequestMapping(value= {"/clients"})
	public String index
		( 
			Model model,
			@RequestParam(name="page",defaultValue="0")int p,
			@RequestParam(name="size",defaultValue="8")int s,
			@RequestParam(name="mc",defaultValue="")String mc
		) 
	{
		Page<Client> clients = metierClient.getClientsByMotCle("%"+mc+"%", p, s);
		model.addAttribute("clients", clients.getContent());
		model.addAttribute("pages", new int[clients.getTotalPages()]);
		model.addAttribute("size", s);
		model.addAttribute("pageCourant", p);
		model.addAttribute("mc", mc); 
		
		if(!model.containsAttribute("client"))
		model.addAttribute("client", new Client()); 
		
		return "clients"; 
	}
	
	

	@RequestMapping(value= {"/clients/add"}, method=RequestMethod.POST)
	public String addClient(@Valid Client client, BindingResult result, Model model) 
	{    	
		metierClient.getClient(client.getCode());
		if( metierClient.getClient(client.getCode())!=null )
			model.addAttribute("dejaExist", true);
		
		if(saveClient(client,result,model))  
			model.addAttribute("addOk","Client ajouté !");
		else model.addAttribute("addFailed",true); 
		
		return index(model,0,8,"");
	}
	
	
	@RequestMapping(value="/clients/update",method=RequestMethod.POST)
	public String updateClient(@Valid Client client, BindingResult result, Model model) 
	{    
		if(saveClient(client,result,model)) 
			model.addAttribute("updateOk","Client "+client.getCode()+" est Mis à jour!");
		else model.addAttribute("updateFailed",true);
		
		return index(model,0,8,"");
	} 
	
	private boolean saveClient(Client client, BindingResult result, Model model)
	{
		if (result.hasErrors()) 
		{
			model.addAttribute("client", client);
			return false;
		}

		metierClient.saveClient(client);
		return true;
	}
	
 
	@RequestMapping(value="/clients/delete")
	public String deleteClient(Model model,@RequestParam(name="code",defaultValue="0")String code) 
	{  
		metierClient.deleteClient(code);
		model.addAttribute("deleteOk","Client "+code+" est supprimé");
		return index(model,0,8,"");
	}
	
	
	
	
	
	@RequestMapping(value="/clients/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Client getroduit(@RequestParam(name="code")String code) 
	{  
		Client frs = metierClient.getClient(code);  
		return frs;
	}
	
}

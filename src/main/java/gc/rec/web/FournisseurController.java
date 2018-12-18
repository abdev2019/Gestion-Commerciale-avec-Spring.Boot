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

import gc.rec.entities.Fournisseur;
import gc.rec.imetier.IFournisseurMetier;
 

@Controller 
public class FournisseurController 
{
	@Autowired
	private IFournisseurMetier metierFournisseur;
	
	
	@RequestMapping(value= {"/fournisseurs"})
	public String index
		( 
			Model model,
			@RequestParam(name="page",defaultValue="0")int p,
			@RequestParam(name="size",defaultValue="8")int s,
			@RequestParam(name="mc",defaultValue="")String mc
		) 
	{
		Page<Fournisseur> fournisseurs = metierFournisseur.getFournisseursByMotCle("%"+mc+"%",p,s );
		model.addAttribute("fournisseurs", fournisseurs.getContent());
		model.addAttribute("pages", new int[fournisseurs.getTotalPages()]);
		model.addAttribute("size", s);
		model.addAttribute("pageCourant", p);
		model.addAttribute("mc", mc); 
		
		if(!model.containsAttribute("fournisseur"))
		model.addAttribute("fournisseur", new Fournisseur()); 
		
		return "fournisseurs"; 
	}
	
	

	@RequestMapping(value= {"/fournisseurs/add"}, method=RequestMethod.POST)
	public String addFournisseur(@Valid Fournisseur fournisseur, BindingResult result, Model model) 
	{    	
		metierFournisseur.getFournisseur(fournisseur.getCode());
		if( metierFournisseur.getFournisseur(fournisseur.getCode())!=null )
			model.addAttribute("dejaExist", true);
		
		else if(saveFournisseur(fournisseur,result,model))  
			model.addAttribute("addOk","Fournisseur ajouté !");
		else model.addAttribute("addFailed",true); 
		
		return index(model,0,8,"");
	}
	 
	@RequestMapping(value="/fournisseurs/update",method=RequestMethod.POST)
	public String updateFournisseur(@Valid Fournisseur fournisseur, BindingResult result, Model model) 
	{    
		if(saveFournisseur(fournisseur,result,model)) 
			model.addAttribute("updateOk","Fournisseur "+fournisseur.getCode()+" est Mis à jour!");
		else model.addAttribute("updateFailed",true); 
		
		return index(model,0,8,"");
	} 
	
	private boolean saveFournisseur(Fournisseur fournisseur, BindingResult result, Model model)
	{
		if (result.hasErrors()) 
		{
			System.err.println(result.getAllErrors());
			model.addAttribute("fournisseur", fournisseur); 
			return false;
		}

		metierFournisseur.saveFournisseur(fournisseur);
		return true;
	}
	
 
	@RequestMapping(value="/fournisseurs/delete")
	public String deleteFournisseur(Model model,@RequestParam(name="code",defaultValue="0")String code) 
	{  
		metierFournisseur.deleteFournisseur(code);
		model.addAttribute("deleteOk","Fournisseur "+code+" est supprimé");
		return index(model,0,8,"");
	}
	
	
	
	
	
	@RequestMapping(value="/fournisseurs/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Fournisseur getroduit(@RequestParam(name="code")String code) 
	{  
		Fournisseur frs = metierFournisseur.getFournisseur(code);  
		return frs;
	}
	
}

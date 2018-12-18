package gc.rec.web;
 

import java.util.List;

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

import gc.rec.entities.Produit;
import gc.rec.imetier.IFamilleMetier;
import gc.rec.imetier.IProduitMetier;
import gc.rec.imetier.ITvaMetier;


@Controller
public class ProduitController  
{
	@Autowired private IProduitMetier metierProduit;
	@Autowired private IFamilleMetier metierFamille;
	@Autowired private ITvaMetier metierTva;
	
	// GET -------------------------------------------------------------
	@RequestMapping(value= {"/produits"})
	public String index( 
			Model model,
			@RequestParam(name="page",defaultValue="0")Integer p,
			@RequestParam(name="size",defaultValue="8")Integer s,
			@RequestParam(name="mc",defaultValue="")String mc
	){
		Page<Produit> prds = metierProduit.getProduitsByMotCle("%"+mc+"%",p,s );
		model.addAttribute("produits", prds.getContent());
		model.addAttribute("pages", new int[prds.getTotalPages()]);
		model.addAttribute("size", s);
		model.addAttribute("pageCourant", p);
		model.addAttribute("mc", mc);  
		
		model.addAttribute("familles",metierFamille.getFamilles());
		model.addAttribute("tvas",metierTva.getTvas());
		
		if(!model.containsAttribute("produit"))
		model.addAttribute("produit", new Produit()); 
		
		return "produits"; 
	}
	
	
	  
	@RequestMapping(value= {"/produits/add"}, method=RequestMethod.POST)
	public String addProduit(@Valid Produit produit, BindingResult result, Model model) 
	{    	
		metierProduit.getProduit(produit.getRef());
		if( metierProduit.getProduit(produit.getRef())==null )
		{
			if(saveProduit(produit,result,model))  
				model.addAttribute("addOk","Produit ajouté !");
			else model.addAttribute("addField","");
		}
		else {
			model.addAttribute("dejaExist", true);
			model.addAttribute("addField","");
		}
		
		return index(model,0,8,"");
	}
	 
	@RequestMapping(value="/produits/update",method=RequestMethod.POST)
	public String updateProduit(@Valid Produit produit, BindingResult result, Model model) 
	{    
		if(saveProduit(produit,result,model)) 
			model.addAttribute("updateOk","Produit "+produit.getRef()+" est Mis à jour!");
		else model.addAttribute("updateField","");
		model.addAttribute("produit",produit); 
		return index(model,0,8,"");
	} 
	
	private boolean saveProduit(Produit produit, BindingResult result, Model model)
	{
		if (result.hasErrors()) 
		{
			model.addAttribute("produit", produit); 
			return false;
		}

		metierProduit.saveProduit(produit);
		return true;
	}
	
 
	@RequestMapping(value="/produits/delete")
	public String deleteProduit(Model model,@RequestParam(name="ref",defaultValue="0")String ref) 
	{  
		metierProduit.deleteProduit(ref);
		model.addAttribute("deleteOk","Produit "+ref+" est supprimé");
		return index(model,0,8,"");
	}
	
	@RequestMapping(value="/produits/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Produit getproduit(@RequestParam(name="ref")String ref) 
	{  
		Produit p = metierProduit.getProduit(ref); 
		return p;
	}
	
}

















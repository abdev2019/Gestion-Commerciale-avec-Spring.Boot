package gc.rec.web;
 
 

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
 
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; 
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gc.rec.entities.Client;
import gc.rec.entities.Commande;
import gc.rec.entities.Dossier;
import gc.rec.entities.Fournisseur;
import gc.rec.entities.LigneCommande;
import gc.rec.entities.Produit;
import gc.rec.imetier.IClientMetier;
import gc.rec.imetier.ICommandeMetier;
import gc.rec.imetier.IFournisseurMetier;
import gc.rec.imetier.ILcMetier;
import gc.rec.imetier.IProduitMetier; 


@Controller
public class CommandeController  
{
	@Autowired private ICommandeMetier metierCommande; 
	@Autowired private ILcMetier metierLigneCommande;  
	@Autowired private IProduitMetier metierProduit;
	@Autowired private IFournisseurMetier metierFournisseur;
	@Autowired private IClientMetier metierClient;
	
	@Autowired private HttpSession session;
	
	
	
 	@RequestMapping(value= {"/commandes"})
	public String index( 
			Model model,
			@RequestParam(name="pagevente",defaultValue="0")	Integer pv,
			@RequestParam(name="pageachat",defaultValue="0")	Integer pa,
			@RequestParam(name="size",defaultValue="8")			Integer s, 
			@RequestParam(name="type",defaultValue="")			String type,
			@RequestParam(name="datevente",defaultValue="")		String datevente,
			@RequestParam(name="dateachat",defaultValue="")		String dateachat,
			@RequestParam(name="num",defaultValue="")			String num,
			@RequestParam(name="fournisseur",defaultValue="")	String frs,
			@RequestParam(name="client",defaultValue="")		String clt,
			@RequestParam(name="valideachat",defaultValue="-1")	String valideachat,
			@RequestParam(name="validevente",defaultValue="-1")	String validevente
	){            
		if(!num.isEmpty())
		{ 
			Commande cmd = metierCommande.getCommande(Long.valueOf(num));
			if(cmd != null) {
				if( ((Dossier)session.getAttribute("dossier")).getNumero() != cmd.getNumero() );
				else
				if(cmd.getClient()!=null) {
					model.addAttribute("commandesVente", new Commande[] {cmd}); 
					model.addAttribute("vente", true);
				}
				else {
					if(cmd.getFournisseur()!=null)  model.addAttribute("commandesAchat", new Commande[] {cmd}); 
					model.addAttribute("achat", true); 
				}  
				model.addAttribute("numero", num);
			}
		}
		else  
		switch(type)
		{
			case "vente"  : indexVente(model,pv,s,datevente,clt,validevente); break;
			case "achat"  : indexAchat(model,pa,s,dateachat,frs,valideachat); break;
			default  	  : indexVente(model,pv,s,datevente,clt,validevente); 
							indexAchat(model,pa,s,dateachat,frs,valideachat);
		}  

		model.addAttribute("size", s);
		model.addAttribute("type", type);  
		model.addAttribute("pageCourantAchat", pa);
		model.addAttribute("dateachat", dateachat);
		model.addAttribute("valideachat", valideachat);
		model.addAttribute("frs", frs); 
		
		model.addAttribute("pageCourantVente", pv); 
		model.addAttribute("datevente", datevente);
		model.addAttribute("validevente", validevente);
		model.addAttribute("clt", clt);  
		return "commandes"; 
	}

 	
 	private void indexAchat(Model model, Integer pa, Integer s, String dateachat, String frs, String valideachat) 
 	{ 
 		Long numDossier = ((Dossier)session.getAttribute("dossier")).getNumero();
 		Page<Commande> cmds;
 		Date date = null;
 		Boolean valide = valideachat.equals("-1")?null:valideachat.equals("1")?true:false;
 		
 		if(!dateachat.isEmpty())
 		{
			SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			try{ date = mdyFormat.parse(dateachat);  }
			catch(Exception e){ date=null; }
 		} 
 		 
		if(!frs.isEmpty())
		{
			if(valide!=null)
			{
				if(date!=null) 
					cmds  = metierCommande.getCommandesOfFournisseur(numDossier,frs,valide,date,pa,s);
				else cmds = metierCommande.getCommandesOfFournisseur(numDossier,frs,valide,pa,s);
			}
			else if(date!=null) cmds  = metierCommande.getCommandesOfFournisseur(numDossier,frs,date,pa,s);
			else cmds = metierCommande.getCommandesOfFournisseur(numDossier,frs,pa,s);
		}
		else if(valide!=null)
		{
			if(date!=null) cmds = metierCommande.getCommandesFournisseurs(numDossier,valide,date,pa,s);
			else cmds = metierCommande.getCommandesFournisseurs(numDossier,valide,pa,s);
		}
		else if(date!=null) cmds = metierCommande.getCommandesFournisseurs(numDossier,date, pa, s);
		else cmds = metierCommande.getCommandesFournisseurs(numDossier,pa,s);
		
		double totalAchats = 0;
		for(Commande cmd : cmds)  totalAchats += cmd.getTotal();
		
		model.addAttribute("commandesAchat", cmds.getContent());
 		model.addAttribute("fournisseurs",metierFournisseur.getFournisseurs());
 		model.addAttribute("totalAchats", totalAchats); 
 		model.addAttribute("achat", true);
 		model.addAttribute("pagesAchat", new int[cmds.getTotalPages()]); 
 	}
 	
 	private void indexVente(Model model, Integer pv, Integer s, String datevente, String clt, String validevente) 
 	{ 
 		Long numDossier = ((Dossier)session.getAttribute("dossier")).getNumero();
 		Page<Commande> cmds;
 		Date date = null;
 		Boolean valide = validevente.equals("-1")?null:validevente.equals("1")?true:false;
 		
 		if(!datevente.isEmpty())
 		{
			SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			try{ date = mdyFormat.parse(datevente);  }
			catch(Exception e){ date=null; }
 		}  
 		
		if(!clt.isEmpty())
		{
			if(valide!=null)
			{
				if(date!=null) 
					cmds  = metierCommande.getCommandesOfClient(numDossier,clt,valide,date,pv,s);
				else cmds = metierCommande.getCommandesOfClient(numDossier,clt,valide,pv,s);
			}
			else if(date!=null) cmds  = metierCommande.getCommandesOfClient(numDossier,clt,date,pv,s);
			else cmds = metierCommande.getCommandesOfClient(numDossier,clt,pv,s);
		}
		else if(valide!=null)
		{
			if(date!=null) cmds = metierCommande.getCommandesClients(numDossier,valide,date,pv,s);
			else cmds = metierCommande.getCommandesClients(numDossier,valide,pv,s);
		}
		else if(date!=null) cmds = metierCommande.getCommandesClients(numDossier,date, pv, s);
		else cmds = metierCommande.getCommandesClients(numDossier,pv,s);
		
		double totalVentes = 0;
		for(Commande cmd : cmds)  totalVentes += cmd.getTotal();
		
		model.addAttribute("commandesVente", cmds.getContent());
		model.addAttribute("totalVentes", totalVentes); 
		model.addAttribute("vente", true); 
 		model.addAttribute("clients",metierClient.getClients());
 		model.addAttribute("pagesVente", new int[cmds.getTotalPages()]); 
 	}

 	
 	
 	@RequestMapping(value="/commandes/get")
	public String afficherCommande(Model model, @RequestParam(name="numero",defaultValue="0")Long numero) 
	{     
 		Commande cmd = metierCommande.getCommande(numero);
 		if(cmd==null) return "redirect:/commandes";
 		model.addAttribute("commande", cmd);
 		model.addAttribute("nbrProduits", cmd.getLignesCommande().size());
		return "commande";
	}
	
	@RequestMapping(value="/commandes/nouveau")
	public String nouveaucommande(Model model) 
	{     
		if(!model.containsAttribute("commande")) model.addAttribute("commande", new Commande()); 
		
		model.addAttribute("produits",     metierProduit.getProduits());
		model.addAttribute("fournisseurs", metierFournisseur.getFournisseurs());
		model.addAttribute("clients",      metierClient.getClients());
		
		HashMap<String,LigneCommande> lcs = (HashMap<String,LigneCommande>)session.getAttribute("lcs");
		if(lcs==null) lcs = new HashMap<String,LigneCommande>(); 
		model.addAttribute("lcs", lcs.values());
		
		return "nouveaucommande";
	}
	
	@RequestMapping(value="/commandes/commander")
	public String commanderproduit(Model model,@RequestParam(name="ref",defaultValue="0")String ref) 
	{     
		model.addAttribute("commande", new Commande()); 
		model.addAttribute("produits",     metierProduit.getProduits());
		model.addAttribute("fournisseurs", metierFournisseur.getFournisseurs()); 
		
		HashMap<String,LigneCommande> lcs = (HashMap<String,LigneCommande>)session.getAttribute("lcs");
		if(lcs==null) lcs = new HashMap<String,LigneCommande>(); 
		
		Produit prd = metierProduit.getProduit(ref);
		if(prd!=null) {
			lcs.put(ref, new LigneCommande(null,prd,1));
			session.setAttribute("lcs", lcs); 
		}
		model.addAttribute("lcs", lcs.values());
		
		return "nouveaucommande";
	} 
	   
	@RequestMapping(value= {"/commandes/add"}, method=RequestMethod.POST)
	public String addCommande(@Valid Commande commande, BindingResult result, Model model) 
	{    	 
		HashMap<String,LigneCommande> lcs = (HashMap<String,LigneCommande>)session.getAttribute("lcs");
		if(lcs==null || lcs.size()==0)
			model.addAttribute("errLcs","Vous devez au moins commander un produit !");

		if((commande.getClient()==null&&commande.getFournisseur()==null) || (commande.getClient()!=null&&commande.getFournisseur()!=null))
			model.addAttribute("errDest","Vous devez selectionner soit un client ou bien un fournisseur");
		
		if(result.hasErrors() || model.containsAttribute("errLcs")|| model.containsAttribute("errDest")) { 
			model.addAttribute("commande",commande);
			return  nouveaucommande(model);
		}		 
		
		
		for(LigneCommande lc : lcs.values())
			commande.setTotal( lc.getTtc() + commande.getTotal() );
		
		commande.setDossier( ((Dossier)session.getAttribute("dossier")) );
		commande = metierCommande.saveCommande(commande);
				
		for(LigneCommande lc : lcs.values())
		{
			lc.setCommande(commande); 
			lcs.put(lc.getProduit().getRef(), metierLigneCommande.saveLigneCommande(lc));
		} 
		commande.setLignesCommande(lcs.values());
		 
		session.removeAttribute("lcs");

		model.addAttribute("addOk","Commande NUM"+commande.getNumero()+" est ajouté !");
		model.addAttribute("commande",commande);
		
		return "redirect:/commandes/get?numero="+commande.getNumero();
	}
	
	@RequestMapping(value="/commandes/edit",method=RequestMethod.GET)
	public String editCommande(Model model, @RequestParam(name="numero",defaultValue="0")Long numero) 
	{    
		if(!model.containsAttribute("commande"))
		{
			Commande commande = metierCommande.getCommande(numero);   
			if(commande==null) return "redirect:/commandes";
			model.addAttribute("commande",commande);
		}
		 
		model.addAttribute("produits", metierProduit.getProduits());
		model.addAttribute("fournisseurs", metierFournisseur.getFournisseurs());
		model.addAttribute("clients", metierClient.getClients());
		return "editcommande";
	} 
	 
	@RequestMapping(value="/commandes/update",method=RequestMethod.POST)
	public String updateCommande(Model model, 
			@RequestParam(name="numero",defaultValue="0")Long numero,
			@RequestParam(name="dateCommande",defaultValue="0")String dateCommandeString,
			@RequestParam(name="valide",defaultValue="0")String valideString,
			@RequestParam(name="client",defaultValue="0")String codeClient,
			@RequestParam(name="fournisseur",defaultValue="0")String codeFournisseur) 
	{
		try 
		{ 
			Date dateCommande = null; 
			SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			try{ dateCommande = mdyFormat.parse(dateCommandeString);  }
			catch(Exception e){ throw new Exception("Entrer une date valide !"); }
			
			Boolean valide = null;
			try { valide = Boolean.valueOf(valideString); }
			catch(Exception e){throw new Exception("Selectioner la validité du commande !");} 
			
			Fournisseur fournisseur = metierFournisseur.getFournisseur(codeFournisseur);
			Client client = metierClient.getClient(codeClient);  
			if(client==null && fournisseur==null || client!=null && fournisseur!=null)
				throw new Exception("Vous devez selectionner soit un client ou bien un fournisseur"); 
			
			Commande commande = metierCommande.getCommande(numero); 
			commande.setClient(client);
			commande.setFournisseur(fournisseur);
			commande.setDateCommande(dateCommande);
			commande.setValide(valide);
			    
			for(LigneCommande lc : commande.getLignesCommande())
				commande.setTotal( lc.getTtc() + commande.getTotal() );
			
			commande = metierCommande.saveCommande(commande);
			model.addAttribute("updateOk","Commande NUM"+commande.getNumero()+" est Mis à jour!");
		}
		catch (Exception e) {
			model.addAttribute("updateField",e.getMessage());
		}
		return editCommande(model,numero);
	}
	  
	@RequestMapping(value="/commandes/delete")
	public String deleteCommande(Model model,@RequestParam(name="numero",defaultValue="0")Long num) 
	{  
		metierCommande.deleteCommande(num);
		model.addAttribute("deleteOk","Commande "+num+" est supprimé");
		return index(model,0,0,8,"","","","","","","","");
	}
	
	@RequestMapping(value="/commandes/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Commande getcommande(@RequestParam(name="numero")Long numero) 
	{  
		Commande p = metierCommande.getCommande(numero);  
		return p;
	}
	
	  
	
	
	
	
	//**************************** Lignes commandes ***************************
	
	@RequestMapping(value="/commandes/ligne/add",method=RequestMethod.POST)
	public String addlignecommande(Model model, 
			@RequestParam(name="produit",defaultValue="0")String ref,
			@RequestParam(name="commande",defaultValue="0")Long numero,
			@RequestParam(name="qte",defaultValue="0")Integer qte) 
	{     
		try 
		{
			if(qte<=0) throw new Exception("Quantité minimale est 1");
			
			LigneCommande lc = metierLigneCommande.getLigneCommandeByProduit(numero, ref);
			if(lc==null) {
				lc = new LigneCommande();
				lc.setCommande(metierCommande.getCommande(numero));
				if(lc.getCommande()==null) throw new Exception("Commande non valide !");
				
				lc.setProduit(metierProduit.getProduit(ref));
				if(lc.getProduit()==null) throw new Exception("Selectionner un produit !");
				
				model.addAttribute("addLcOk","Produit "+lc.getProduit().getDesignation()+" est ajouté");
			}
			else 
				model.addAttribute("updateLcOk","La quantité du produit "+lc.getProduit().getDesignation()+" est mise à jour à "+qte);
			 
			lc.setQte(qte);
			  
			lc.setTotal( lc.getQte() * lc.getProduit().getPrix() );
			lc.setTtc( lc.getTotal() + (lc.getTotal()*lc.getProduit().getTva().getTaux())/100.0);
			  
			lc = metierLigneCommande.saveLigneCommande(lc); 
		}
		catch (Exception e) 
		{ model.addAttribute("exception", e); }
	  
		return editCommande(model,numero);
	}
	
	@RequestMapping(value="/commandes/ligne/update",method=RequestMethod.POST)
	public String updatelignecommande( Model model, 
			@RequestParam(name="produit",defaultValue="0")String ref,
			@RequestParam(name="commande",defaultValue="0")Long numero,
			@RequestParam(name="qte",defaultValue="0")Integer qte,BindingResult result)  
	{  	
		LigneCommande lc = new LigneCommande();
		lc.setQte(qte);
		lc.setCommande(metierCommande.getCommande(numero));
		lc.setProduit(metierProduit.getProduit(ref));
		
		if (result.hasErrors()) 
		{
			model.addAttribute("ligneCommande",lc);  
			model.addAttribute("updateLcField","");
			return editCommande(model,lc.getCommande().getNumero());
		}
		  
		lc.setTotal( lc.getQte() * lc.getProduit().getPrix() );
		lc.setTtc( lc.getTotal() + (lc.getTotal()*lc.getProduit().getTva().getTaux())/100.0);
		
		metierLigneCommande.saveLigneCommande(lc); 
		model.addAttribute("updateLcOk","Ligne "+lc.getId()+" est mise à jour");  
		
		return editCommande(model,lc.getCommande().getNumero());
	}
	  
	@RequestMapping(value="/commandes/ligne/delete")
	public String deletelignecommande(Model model,@RequestParam(name="id",defaultValue="0")Long id,@RequestParam(name="numCommande",defaultValue="0")Long numCommande) 
	{  
		LigneCommande lc  = metierLigneCommande.getLigneCommande(id);
		metierLigneCommande.deleteLigneCommande(id);
		model.addAttribute("deleteLcOk","Produit "+lc.getProduit().getDesignation()+" est retiré de la commande");
		return editCommande(model,numCommande);
	}
	
	@RequestMapping(value="/commandes/ligne/store", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Result storelignecommande(  
			@RequestParam(name="refProduit",defaultValue="0")String refProduit, 
			@RequestParam(name="qte",defaultValue="0")Integer qte
	) 
	{   
		if(qte<=0) return new Result("La quantité doit etre au minimum 1 !",null,"");
		if(refProduit.equals("0")) return new Result("Selectionner d'abord le produit !",qte+"",null);
		
		HashMap<String,LigneCommande> lcs = (HashMap<String,LigneCommande>)session.getAttribute("lcs");
		 
		if(lcs==null) lcs = new HashMap<String,LigneCommande>();
		 
		LigneCommande lc = new LigneCommande();
		lc.setProduit( metierProduit.getProduit(refProduit) );
		lc.setQte(qte);
		lcs.put(refProduit, lc);

		lc.setTotal( lc.getQte() * lc.getProduit().getPrix() );
		lc.setTtc( lc.getTotal() + (lc.getTotal()*lc.getProduit().getTva().getTaux())/100.0);
		
		session.setAttribute("lcs", lcs);
		
		return new Result("Le produit "+lc.getProduit()+" est ajouté !",qte+"",lc.getProduit().toString()); 
	}
	class Result implements java.io.Serializable{
		public String message; public String qte; public String produit;
		Result(String m,String q, String p){qte=q; message=m;produit=p;}
	}
	
	@RequestMapping(value="/commandes/ligne/remove", method=RequestMethod.POST,produces = "text/html")
	public @ResponseBody String removelignecommande(@RequestParam(name="ref",defaultValue="0")String ref) 
	{   
		HashMap<String,LigneCommande> lcs = (HashMap<String,LigneCommande>)session.getAttribute("lcs");
		if(lcs==null) return new String("Ligne retiré !"); 
		lcs.remove(ref); 
		session.setAttribute("lcs", lcs); 
		return new String("Ligne retiré !"); 
	}
	
	
	
	
	Commande validerCommande(Model model, String dateCommandeString, String valideString, String codeClient, String codeFournisseur )
	{
		Commande commande;
		try 
		{
			Date dateCommande = null; 
			SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			try{ dateCommande = mdyFormat.parse(dateCommandeString);  }
			catch(Exception e){ throw new Exception("Entrer une date valide !"); }
			
			Boolean valide = null;
			try { valide = Boolean.valueOf(valideString); }
			catch(Exception e){throw new Exception("Selectioner la validité du commande !");} 
			
			Fournisseur fournisseur = metierFournisseur.getFournisseur(codeFournisseur);
			Client client = metierClient.getClient(codeClient);  
			if(client==null && fournisseur==null || client!=null && fournisseur!=null)
				throw new Exception("Vous devez selectionner soit un client ou bien un fournisseur"); 
			
			HashMap<String,LigneCommande> lcs = (HashMap<String,LigneCommande>)session.getAttribute("lcs");
			if(lcs==null || lcs.size()==0)
				throw new Exception("Vous devez au moins commander un produit !");
			
			commande = new Commande(); 
			commande.setClient(client);
			commande.setFournisseur(fournisseur);
			commande.setDateCommande(dateCommande);
			commande.setValide(valide); 
			
			commande.setDossier((Dossier) session.getAttribute("dossier"));
		}	
		catch (Exception e) { 
			model.addAttribute("addField", e.getMessage());
			return null; 
		}
		
		return commande;
	}
}

















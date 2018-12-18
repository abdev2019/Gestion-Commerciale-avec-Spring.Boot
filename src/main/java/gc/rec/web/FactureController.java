package gc.rec.web;
 
 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model;  
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.ServletContextPropertyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.itextpdf.html2pdf.HtmlConverter;

import gc.rec.entities.Client;
import gc.rec.entities.Commande;
import gc.rec.entities.Dossier;
import gc.rec.entities.Facture;
import gc.rec.entities.Fournisseur;
import gc.rec.entities.LigneCommande;
import gc.rec.entities.LigneFacture;
import gc.rec.entities.Livraison;
import gc.rec.entities.ReductionFacture;
import gc.rec.entities.ReglementFacture;
import gc.rec.entities.Stock;
import gc.rec.imetier.IClientMetier;
import gc.rec.imetier.ICommandeMetier;
import gc.rec.imetier.IFactureMetier;
import gc.rec.imetier.IFournisseurMetier;
import gc.rec.imetier.ILfMetier;
import gc.rec.imetier.ILivraisonMetier;
import gc.rec.imetier.IProduitMetier;
import gc.rec.imetier.IRDFMetier;
import gc.rec.imetier.IRGFMetier;
import gc.rec.imetier.IReductionMetier;
import gc.rec.imetier.IReglementMetier;
import gc.rec.imetier.IStockMetier; 


@Controller
public class FactureController  
{
	@Autowired private IFactureMetier metierFacture;  
	@Autowired private ICommandeMetier metierCommande;  
	
	@Autowired private IProduitMetier metierProduit;
	@Autowired private IFournisseurMetier metierFournisseur;
	@Autowired private IClientMetier metierClient; 
	@Autowired private IReglementMetier metierReglement;
	@Autowired private IReductionMetier metierRed;
	@Autowired private ILfMetier metierLigneFacture;
	@Autowired private IRGFMetier metierRgf;
	@Autowired private IRDFMetier metierRdf;
	@Autowired private ILivraisonMetier metierLiv;
	
	@Autowired private IStockMetier metierStk;
	
	
	@Autowired private HttpSession session;
	
	 @Autowired
	    private ServletContext servletContext;
	 
	@RequestMapping(value= {"/factures/preview"})
	public String previewFacture(Model model,@RequestParam(name="numero",defaultValue="0")Long num)
	{
		try {
			Facture fct = metierFacture.getFacture(num);
	 		if(fct==null) return "redirect:/factures";
	 		 System.err.println("path : "+servletContext.getContextPath());
	 		model.addAttribute("url", servletContext.getContextPath() );
	 		model.addAttribute("facture",fct);
	 		model.addAttribute("nbrProduits",0);
	 		model.addAttribute("maSociete", metierFournisseur.getFournisseur("CODE_0"));
		}catch (Exception e) {}
		
		return "factureimpr";
	}
	 
	
	@RequestMapping(value= {"/factures/print"})
	public ResponseEntity<InputStreamResource> index(Model model,@RequestParam(name="num",defaultValue="0")Long num,
		@RequestParam(name="dest",defaultValue="")String dest)
	{
		try {
			Facture fct = metierFacture.getFacture(num);
	 		if(fct==null) return null;
	 		
	    	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
	    	templateResolver.setSuffix(".html");
	    	templateResolver.setTemplateMode("HTML");
	    	 
	    	TemplateEngine templateEngine = new TemplateEngine();
	    	templateEngine.setTemplateResolver(templateResolver);
	    	 
	    	Context context = new Context();
	    	ClassPathResource rcss = new ClassPathResource("/static/resources");
	    	context.setVariable("url", rcss.getFile().getAbsolutePath());
	    	context.setVariable("facture", fct);
	    	context.setVariable("nbrProduits", 0);
	    	context.setVariable("maSociete", metierFournisseur.getFournisseur("CODE_0"));
  
	    	ClassPathResource r = new ClassPathResource("/templates/factureimpr");
	    	String html = templateEngine.process(r.getPath(), context);
	 		 
	 		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	    	try { HtmlConverter.convertToPdf(html, out); } 
	    	catch (Exception e) { e.printStackTrace(); }
	    	  
	    	ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
	 				
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "inline; filename=Facture_"+fct.getNumero()+".pdf");

	        return ResponseEntity
	                .ok()
	                .headers(headers)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(new InputStreamResource(bis));
		} 
		catch (Exception e) { e.printStackTrace(); } 
		return null;
	}
	
 	@RequestMapping(value= {"/factures"})
	public String index( 
			Model model,
			@RequestParam(name="pagevente",defaultValue="0")	Integer pv,
			@RequestParam(name="pageachat",defaultValue="0")	Integer pa,
			@RequestParam(name="size",defaultValue="8")			Integer s, 
			@RequestParam(name="type",defaultValue="")			String type,
			@RequestParam(name="datevente",defaultValue="")		String datevente,
			@RequestParam(name="dateachat",defaultValue="")		String dateachat,
			@RequestParam(name="num",defaultValue="0")			Long num,
			@RequestParam(name="fournisseur",defaultValue="")	String frs,
			@RequestParam(name="client",defaultValue="")		String clt
	){            
 		
		if(num!=0) 
		{ 
			Facture fct = metierFacture.getFacture(num);
 
			if(fct != null) 
			{
				//if( ((Dossier)session.getAttribute("dossier")).getNumero() != fct.getDossier().getNumero() );
				//else 
				{
					if(fct.getClient()!=null) {
						model.addAttribute("facturesVente", new Facture[] {fct}); 
						model.addAttribute("vente", true);
					}
					else {
						if(fct.getFournisseur()!=null)  
						{
							model.addAttribute("facturesAchat", new Facture[] {fct}); 
							model.addAttribute("achat", true); 
						}
					}  
				}
				model.addAttribute("numero", num);
			}
		}
		else  
		switch(type)
		{
			case "vente"  : indexVente(model,pv,s,datevente,clt); break;
			case "achat"  : indexAchat(model,pa,s,dateachat,frs); break;
			default  	  : indexVente(model,pv,s,datevente,clt); 
							indexAchat(model,pa,s,dateachat,frs);
		}   
		
		model.addAttribute("size", s);
		model.addAttribute("type", type);  
		model.addAttribute("pageCourantAchat", pa);
		model.addAttribute("dateachat", dateachat); 
		model.addAttribute("frs", frs); 
		
		model.addAttribute("pageCourantVente", pv); 
		model.addAttribute("datevente", datevente); 
		model.addAttribute("clt", clt);  
		
		return "factures"; 
	}
 
 	private void indexAchat(Model model, Integer pa, Integer s, String dateachat, String frs) 
 	{ 
 		Long numDossier = ((Dossier)session.getAttribute("dossier")).getNumero();
 		Page<Facture> fcts;
 		Date date = null; 
 		
 		if(!dateachat.isEmpty())
 		{
			SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			try{ date = mdyFormat.parse(dateachat);  }
			catch(Exception e){ date=null; }
 		} 
 		 
		if(!frs.isEmpty())
		{
			if(date!=null) fcts  = metierFacture.getFacturesOfFournisseur(numDossier,frs,date,pa,s);
			else fcts = metierFacture.getFacturesOfFournisseur(numDossier,frs,pa,s);
		}
		else if(date!=null) fcts = metierFacture.getFacturesFournisseurs(numDossier,date, pa, s);
		else fcts = metierFacture.getFacturesFournisseurs(numDossier,pa,s);
		
		double totalAchats = 0;
		for(Facture fct : fcts)  totalAchats += fct.getTotal();
		
		model.addAttribute("facturesAchat", fcts.getContent());
 		model.addAttribute("fournisseurs",metierFournisseur.getFournisseurs());
 		model.addAttribute("totalAchats", totalAchats); 
 		model.addAttribute("achat", true);
 		model.addAttribute("pagesAchat", new int[fcts.getTotalPages()]); 
 	}
 	
 	private void indexVente(Model model, Integer pv, Integer s, String datevente, String clt) 
 	{ 
 		Long numDossier = ((Dossier)session.getAttribute("dossier")).getNumero();
 		Page<Facture> fcts;
 		Date date = null; 
 		
 		if(!datevente.isEmpty())
 		{
			SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
			try{ date = mdyFormat.parse(datevente);  }
			catch(Exception e){ date=null; }
 		}  
 		
		if(!clt.isEmpty())
		{
			if(date!=null) fcts  = metierFacture.getFacturesOfClient(numDossier,clt,date,pv,s);
			else fcts = metierFacture.getFacturesOfClient(numDossier,clt,pv,s);
		} 
		else if(date!=null) fcts = metierFacture.getFacturesClients(numDossier,date, pv, s);
		else fcts = metierFacture.getFacturesClients(numDossier,pv,s);
		
		double totalVentes = 0;
		for(Facture fct : fcts)  totalVentes += fct.getTotal();
		
		model.addAttribute("facturesVente", fcts.getContent());
		model.addAttribute("totalVentes", totalVentes); 
		model.addAttribute("vente", true); 
 		model.addAttribute("clients",metierClient.getClients());
 		model.addAttribute("pagesVente", new int[fcts.getTotalPages()]); 
 	}
 
 	@RequestMapping(value="/factures/get")
	public String afficherFacture(Model model, @RequestParam(name="numero",defaultValue="0")Long numero,
			@RequestParam(name="uok",defaultValue="0")String uok) 
	{     
 		Facture fct = metierFacture.getFacture(numero);
 		if(fct==null) return "redirect:/factures";
 		model.addAttribute("facture", fct);
 		model.addAttribute("nbrProduits", fct.getLignesFacture().size());
 		if(uok.equals("1")) model.addAttribute("uok",true);
		return "facture";
	}
 
	@RequestMapping(value="/factures/nouveau")
	public String nouveaufacture(Model model, @RequestParam(name="numCmd",defaultValue="0")Long numCmd) 
	{      
		if(!model.containsAttribute("facture")) { 
			session.removeAttribute("lfs");
			session.removeAttribute("lfsr");
			session.removeAttribute("reds");
			session.removeAttribute("redsr");
			session.removeAttribute("regs");
			session.removeAttribute("regsr");
			model.addAttribute("facture", new Facture()); 
		}
		else 
		{ 		
			HashMap<String,LigneFacture> lcs = (HashMap<String,LigneFacture>)session.getAttribute("lfs");
			if(lcs==null) lcs = new HashMap<String,LigneFacture>(); 
			model.addAttribute("lfs", lcs.values());
			
			HashMap<String,ReductionFacture> redsf = 
					(HashMap<String,ReductionFacture>)session.getAttribute("reds");
			if(redsf!=null) model.addAttribute("redsf", redsf.values());
			
			HashMap<String,ReglementFacture> regsf = 
					(HashMap<String,ReglementFacture>)session.getAttribute("regs");
			if(regsf!=null) model.addAttribute("regsf", regsf.values());
		}
		
		model.addAttribute("produits",     metierProduit.getProduits());
		model.addAttribute("fournisseurs", metierFournisseur.getFournisseurs());
		model.addAttribute("clients",      metierClient.getClients());
		model.addAttribute("regs",   metierReglement.getReglements());
		model.addAttribute("reds",   metierRed.getReductions());
		
		if(numCmd!=0) {
			Commande cmd = metierCommande.getCommande(numCmd);
			if(cmd!=null)
			{ 
				HashMap<String, LigneFacture> lfs = new HashMap<>();
				for(LigneCommande lc : cmd.getLignesCommande()) {
					LigneFacture lf = new LigneFacture(null, lc.getProduit() , lc.getQte(), lc.getProduit().getPrix());  
					lf.setTotal(lc.getTotal());
					lf.setTtc(lc.getTtc());
					lfs.put(lc.getProduit().getRef(), lf);   
				}
				session.setAttribute("lfs", lfs);
				model.addAttribute("lfs", lfs.values());
				model.addAttribute("cmd",cmd);
				return "nouveaufacture";
			}
		}
		 
		
		return "nouveaufacture";
	}
	  
	@RequestMapping(value= {"/factures/save"}, method=RequestMethod.POST)
	public String saveFacture(Model model,  
			@RequestParam(name="operation",defaultValue="0")	String operation, 
			@RequestParam(name="numero",defaultValue="0")		Long num,
			@RequestParam(name="dateFacture",defaultValue="")	String dateFactureString, 
			@RequestParam(name="client",defaultValue="0")		String codeClient,
			@RequestParam(name="fournisseur",defaultValue="0")	String codeFournisseur,
			@RequestParam(name="numCmd",defaultValue="0")		Long numCmd,
			@RequestParam(name="dateLivraison",defaultValue="")		String dateLivraison,
			@RequestParam(name="adresseLivraison",defaultValue="-")		String adresseLivraison
			) 
	{    	 
		HashMap<String, Object> res = validerFacture(model,operation,num,dateFactureString, codeClient, codeFournisseur);
		
		Date dL = null; 
		SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		try{ dL = mdyFormat.parse(dateLivraison);  }
		catch(Exception e){ res.put("10","Date livraison invalide !"); }
		
		
		if(res.size()>1) {
			model.addAttribute("facture",(Facture)res.remove("facture"));
			model.addAttribute("errs", res.values());
			if(operation.equals("1")) model.addAttribute("edit",true);
			return nouveaufacture(model,numCmd); 
		}
		
		Facture facture = (Facture)res.get("facture");
		
		if(numCmd!=0) {
			facture.setCommande(metierCommande.getCommande(numCmd));
			if(facture.getCommande()!=null) {
				facture.getCommande().setValide(true);
				metierCommande.saveCommande(facture.getCommande());
			}
		}
		 
		// lfs ------------------------
		HashMap<String,LigneFacture> lfs = (HashMap<String,LigneFacture>)session.getAttribute("lfs");
		ArrayList<Long> lfsr = (ArrayList<Long>) session.getAttribute("lfsr");

		facture.setTtc(0); facture.setTotal(0);
		for(LigneFacture lf : lfs.values()) {
			facture.setTtc( lf.getTtc() + facture.getTtc() );
			facture.setTotal( lf.getTotal() + facture.getTotal() );
		}
		facture = metierFacture.saveFacture(facture); 
		
		Livraison livraison = facture.getLivraison()==null? new Livraison() : facture.getLivraison();
		livraison.setAdresse(adresseLivraison);
		livraison.setDateLivraison(dL);
		livraison.setFacture(facture);
		facture.setLivraison(livraison);
		metierLiv.saveLivraison(livraison);
		
		
		for(LigneFacture lf : lfs.values())
		{
			lf.setFacture(facture); 
			lfs.put(lf.getProduit().getRef(), metierLigneFacture.saveLigneFacture(lf));
			if(facture.getClient()==null) {
				lf.getProduit().setQuantite(lf.getProduit().getQuantite() + lf.getQte()); 
			}else {
				lf.getProduit().setQuantite(lf.getProduit().getQuantite() - lf.getQte()); 
			}
			metierProduit.saveProduit(lf.getProduit());
			metierStk.saveStock( new Stock(new Date(),lf.getProduit().getQuantite(),lf.getProduit()) );
		} 
		if(lfsr!=null) for(Long lf : lfsr) 
			metierLigneFacture.deleteLigneFacture(lf); 
		facture.setLignesFacture(lfs.values());
		session.removeAttribute("lfs");
		session.removeAttribute("lfsr");
		
		
		// reglements ------------------------
		HashMap<String,ReglementFacture> regs = (HashMap<String,ReglementFacture>)session.getAttribute("regs"); 
		ArrayList<Long> regsr = (ArrayList<Long>) session.getAttribute("regsr"); 

		if(regs!=null) for(ReglementFacture reg : regs.values()) {
			reg.setFacture(facture);
			metierRgf.saveReglementFacture(reg); 
		}
		if(regsr!=null) for(Long reg : regsr) 
			metierRgf.deleteReglementFacture(reg); 
		session.removeAttribute("regs");
		session.removeAttribute("regsr");
		
		
		// reductions ------------------------
		HashMap<String,ReductionFacture> reds = (HashMap<String,ReductionFacture>)session.getAttribute("reds");
		ArrayList<Long> redsr = (ArrayList<Long>) session.getAttribute("redsr");

		if(reds!=null) for(ReductionFacture red : reds.values()) {
			red.setFacture(facture);
			metierRdf.saveReductionFacture(red);
		}
		if(redsr!=null) for(Long red : redsr) 
			metierRdf.deleteReductionFacture(red); 
		
		session.removeAttribute("reds");
		session.removeAttribute("redsr");

		  
		model.addAttribute("addOk","Facture NUM"+facture.getNumero()+" est ajouté !");
		model.addAttribute("facture",facture);
		
		return "redirect:/factures/get?numero="+facture.getNumero()+""+(operation.equals("1")?"&uok=1":"");
	}
	 
	@RequestMapping(value="/factures/edit",method=RequestMethod.GET)
	public String editFacture(Model model, @RequestParam(name="numero",defaultValue="0")Long numero) 
	{    
		Facture facture;
		if(!model.containsAttribute("facture"))
		{
			facture = metierFacture.getFacture(numero);   
			if(facture==null) return "redirect:/factures";
			model.addAttribute("facture",facture);
		}
		else facture = (Facture)model.asMap().get("facture");

		HashMap<String, ReductionFacture> redsf = new HashMap<>();
		HashMap<String, ReglementFacture> regsf = new HashMap<>();
		HashMap<String, LigneFacture> lfs = new HashMap<>();
		
		for(ReglementFacture rf : facture.getReglements()) regsf.put(""+rf.getReglement().getId(), rf);
		for(ReductionFacture rd : facture.getReductions()) redsf.put(""+rd.getReduction().getId(), rd);
		for(LigneFacture lf : facture.getLignesFacture()) lfs.put(lf.getProduit().getRef(), lf);
		
		session.setAttribute("lfs", lfs);
		session.setAttribute("regs", regsf);
		session.setAttribute("reds", redsf);
		model.addAttribute("lfs", facture.getLignesFacture());
		model.addAttribute("regsf", facture.getReglements());
		model.addAttribute("redsf", facture.getReductions());
		
		model.addAttribute("produits", metierProduit.getProduits());
		model.addAttribute("fournisseurs", metierFournisseur.getFournisseurs());
		model.addAttribute("clients", metierClient.getClients());
		model.addAttribute("regs",   metierReglement.getReglements());
		model.addAttribute("reds",   metierRed.getReductions());
		model.addAttribute("edit",true);
		return "nouveaufacture";
	} 
	  
	@RequestMapping(value="/factures/delete")
	public String deleteFacture(Model model,@RequestParam(name="numero",defaultValue="0")Long num) 
	{  
		metierFacture.deleteFacture(num);
		model.addAttribute("deleteOk","Facture "+num+" est supprimé");
		return "redirect:/factures";
	}
	
	@RequestMapping(value="/factures/get", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Facture getfacture(@RequestParam(name="numero")Long numero) 
	{  
		Facture p = metierFacture.getFacture(numero);  
		return p;
	}
	
	  

	HashMap<String, Object> validerFacture(Model model,String operation, Long numero, String date, String codeClient, String codeFournisseur )
	{
		HashMap<String, Object> res = new HashMap<>();
		 
		Facture facture = metierFacture.getFacture(numero); 
		
		Date dateFact = null; 
		SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		try{ dateFact = mdyFormat.parse(date);  }
		catch(Exception e){ res.put("3","Date facture invalide !"); }
		
		Fournisseur fournisseur = metierFournisseur.getFournisseur(codeFournisseur);
		Client client = metierClient.getClient(codeClient);  
		
		if(client==null && fournisseur==null || client!=null && fournisseur!=null)
			res.put("4","Vous devez selectionner soit un client ou bien un fournisseur"); 
		
		HashMap<String,LigneFacture> lfs = (HashMap<String,LigneFacture>)session.getAttribute("lfs");
		if(lfs==null || lfs.size()==0)
			res.put("5","Vous devez déterminer les produits !");
		  
		if(facture==null) facture = new Facture(); 
		facture.setNumero(numero);
		facture.setClient(client);
		facture.setFournisseur(fournisseur);
		facture.setDateFacture(dateFact);
		facture.setDossier((Dossier) session.getAttribute("dossier"));
		res.put("facture",facture);
		return res;
	}
	
	
	
	//**************************** Lignes/rd/rg factures ***************************
	 
	@RequestMapping(value="/factures/ligne/store", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody HashMap storelf(  
			@RequestParam(name="refProduit",defaultValue="0")String refProduit, 
			@RequestParam(name="qte",defaultValue="0")Integer qte,
			@RequestParam(name="prix",defaultValue="0")Double prix
	) 
	{     
		HashMap<String, Object> res = new HashMap<>();
		if(refProduit.equals("0")) res.put("produit","Selectionner d'abord le produit !");
		if(prix<=0) res.put("prix","Le prix doit etre un réel !");
		if( qte<=0) res.put("qte", "La quantité doit etre au minimum 1 !");
		if(res.size()>0) return res;
		
		HashMap<String,LigneFacture> lfs = (HashMap<String,LigneFacture>)session.getAttribute("lfs");
		 
		if(lfs==null) lfs = new HashMap<String,LigneFacture>();
		 
		LigneFacture lf = lfs.get(refProduit);
		if(lf==null){ 
			lf = new LigneFacture();
			lf.setProduit( metierProduit.getProduit(refProduit) ); 
		}

		lf.setQte(qte);
		lf.setPrix(prix); 
		lf.setTotal( lf.getQte() * prix );
		lf.setTtc( lf.getTotal() + (lf.getTotal()*lf.getProduit().getTva().getTaux())/100.0);
		lfs.put(refProduit, lf);
		
		session.setAttribute("lfs", lfs);

		res.put("ok","Le produit "+lf.getProduit()+" est ajouté !"); 
		res.put("total", lf.getTotal());
		res.put("tva", lf.getProduit().getTva().getTaux());
		res.put("ttc", lf.getTtc());
		return res; 
	}
	 
	@RequestMapping(value="/factures/ligne/remove", method=RequestMethod.POST,produces = "text/html")
	public @ResponseBody String removelf(@RequestParam(name="ref",defaultValue="0")String ref) 
	{   
		HashMap<String,LigneFacture> lfs = (HashMap<String,LigneFacture>)session.getAttribute("lfs");

		if(lfs==null) return new String("Ligne retiré !"); 
		LigneFacture lf = lfs.remove(ref);  
		if(lf.getId()!=null)
		{
			ArrayList<Long> lfsr = (ArrayList<Long>) session.getAttribute("lfsr");
			if(lfsr==null) lfsr = new ArrayList<>();
			lfsr.add(lf.getId());
			session.setAttribute("lfsr", lfsr);
		}
		session.setAttribute("lcs", lfs); 
		return new String("Ligne retiré !"); 
	}
	 
	
	
	
	@RequestMapping(value="/factures/reg/store", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody HashMap storergf(  
			@RequestParam(name="reg",defaultValue="0")Long reg, 
			@RequestParam(name="taux",defaultValue="0")Double taux
	) 
	{   
		HashMap<String, Object> res = new HashMap<>();
		if(taux<=0) res.put("taux", "Le taux doit etre un nombre positif !");
		if(reg==0) res.put("reg","Selectionner d'abord un reglement !");
		if(res.size()>0) return res;
		
		HashMap<String,ReglementFacture> regs = (HashMap<String,ReglementFacture>)session.getAttribute("regs");
		 
		if(regs==null) regs = new HashMap<String,ReglementFacture>();
		 
		ReglementFacture rgf = regs.get(reg+"");
		if(rgf == null) {
			rgf = new ReglementFacture();
			rgf.setReglement( metierReglement.getReglement(reg) );
		}
		rgf.setPercentage(taux); 
		
		regs.put(reg+"", rgf);
 
		session.setAttribute("regs", regs);

		res.put("ok","Reglement par "+rgf.getReglement().getType()+" : "+taux+"%"); 
		res.put("taux",taux);
		res.put("nom",rgf.getReglement().getType()); 
		return res;
	}
	
	@RequestMapping(value="/factures/reg/remove", method=RequestMethod.POST,produces = "text/html")
	public @ResponseBody String removergf(@RequestParam(name="reg",defaultValue="0")String id) 
	{   
		HashMap<String,ReglementFacture> regs = (HashMap<String,ReglementFacture>)session.getAttribute("regs");
		if(regs==null) return new String("Reglement retiré !"); 
		ReglementFacture reg = regs.remove(id);  
		if(reg.getId()!=null)
		{
			ArrayList<Long> regsr = (ArrayList<Long>) session.getAttribute("regsr");
			if(regsr==null) regsr = new ArrayList<>();
			regsr.add(reg.getId());
			session.setAttribute("regsr", regsr);
		}
		session.setAttribute("regs", regs); 
		return new String("Reglement retiré !"); 
	}
	 
	
	
	
	@RequestMapping(value="/factures/red/store", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody HashMap storerdf(  
			@RequestParam(name="red",defaultValue="0")Long id, 
			@RequestParam(name="taux",defaultValue="0")Double taux
	) 
	{   
		HashMap<String, Object> res = new HashMap<>();
		if(taux<=0) res.put("taux", "Le taux doit etre un nombre positif !");
		if(id==0) res.put("red","Selectionner d'abord une reduction !");
		if(res.size()>0) return res;
		
		HashMap<String,ReductionFacture> reds = (HashMap<String,ReductionFacture>)session.getAttribute("reds");
		 
		if(reds==null) reds = new HashMap<String,ReductionFacture>();
		 
		ReductionFacture red = reds.get(id+"");
		if(red==null) {
			red = new ReductionFacture();
			red.setReduction( metierRed.getReduction(id) );
		}
		red.setTaux(taux); 
		
		reds.put(id+"", red);
 
		session.setAttribute("reds", reds);
		res.put("ok","La reduction "+red.getReduction().getType()+" est ajouté !");
		res.put("taux",taux);
		res.put("nom",red.getReduction().getType()); 
		return res;
	} 
	
	@RequestMapping(value="/factures/red/remove", method=RequestMethod.POST,produces = "text/html")
	public @ResponseBody String removerdf(@RequestParam(name="red",defaultValue="0")String id) 
	{   
		HashMap<String,ReductionFacture> reds = (HashMap<String,ReductionFacture>)session.getAttribute("reds");
		if(reds==null) return new String("Reduction retiré !"); 
		ReductionFacture red = reds.remove(id); 
		if(red.getId()!=null)
		{
			ArrayList<Long> redsr = (ArrayList<Long>) session.getAttribute("redsr");
			if(redsr==null) redsr = new ArrayList<>();
			redsr.add(red.getId());
			session.setAttribute("redsr", redsr);
		}
		session.setAttribute("reds", reds); 
		return new String("Reduction retiré !"); 
	}  
}

















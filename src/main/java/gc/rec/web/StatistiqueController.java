package gc.rec.web;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import gc.rec.dao.FactureRepository;
import gc.rec.entities.Dossier;
import gc.rec.entities.LigneFacture;
import gc.rec.entities.Produit;
import gc.rec.entities.Stock;
import gc.rec.imetier.IClientMetier;
import gc.rec.imetier.ICommandeMetier;
import gc.rec.imetier.IDossierMetier;
import gc.rec.imetier.IFactureMetier;
import gc.rec.imetier.IFamilleMetier;
import gc.rec.imetier.IFournisseurMetier;
import gc.rec.imetier.ILfMetier;
import gc.rec.imetier.IProduitMetier;
import gc.rec.imetier.IRDFMetier;
import gc.rec.imetier.IRGFMetier;
import gc.rec.imetier.IReductionMetier;
import gc.rec.imetier.IReglementMetier;
import gc.rec.imetier.IStockMetier; 


@Controller
public class StatistiqueController 
{   
	@Autowired private IFactureMetier metierFacture; 
	@Autowired private ILfMetier metierLf;   
		
	@Autowired private IProduitMetier metierProduit; 
	@Autowired private IDossierMetier metierDos; 
	@Autowired private IFamilleMetier metierFamille; 

	@Autowired private ICommandeMetier metierCmd; 

	@Autowired private IStockMetier metierStk; 
	
	
	@RequestMapping("/") public String index(Model model)
	{
		model.addAttribute("dossiers", metierDos.getDossiers());
		model.addAttribute("produits", metierProduit.getProduits());
		return "index";
	}
	
		 
	@RequestMapping("/statistiques") public String index(
			Model model,
			@RequestParam(name="p",defaultValue="0")int p,
			@RequestParam(name="s",defaultValue="8")int s,
			@RequestParam(name="mc",defaultValue="")String mc
	){ 
		model.addAttribute("dossiers", metierDos.getDossiers());
		model.addAttribute("categories", metierFamille.getFamilles() );
		model.addAttribute("produits", metierProduit.getProduits());
		
		return "rapports";
	} 
	
	@RequestMapping(value="/statistiques/getstatistiques", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody HashMap<String,List<Statistique>> getStatistiques( 
			@RequestParam(name="d",defaultValue="0")Long numDossier,
			@RequestParam(name="j",defaultValue="0")Integer j,
			@RequestParam(name="m",defaultValue="0")Integer m,
			@RequestParam(name="a",defaultValue="0")Integer a,
			@RequestParam(name="p",defaultValue="")String p
	)
	{
		HashMap<String,List<Statistique>> sts = new HashMap<>(); 
		sts.put("sachats", new ArrayList<>());
		sts.put("sventes", new ArrayList<>());
		sts.put("sprixachats", new ArrayList<>());
		sts.put("sprixventes", new ArrayList<>()); 
		sts.put("produitsAchete", new ArrayList<>());
		sts.put("produitsVendu", new ArrayList<>());
		
		String[] ms = {"Janvier", "Fevrier", "Mars", "Avril", "May", "Juin", "Juillet", "Aout", "September", "Octob", "November", "December"};
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
  
		if(a!=0 && m==0) {
			for(int i=1;i<=12;i++)  
			try{ 
				Date d1 = sf.parse(a+"-"+(i)+"-1"), d2 = sf.parse(a+"-"+(i+1)+"-1");
				regler2(numDossier, d1, d2, p, sts, ms[i-1]);
			}
			catch(Exception e){System.err.println("nooo");}
  
			try{  
				Date d1 = sf.parse(a+"-1-1"), d2 = sf.parse((a+1)+"-1-1");
				regler(sts.get("produitsAchete"),metierFacture.getSProduitsAchete(numDossier ,d1,d2));
				regler(sts.get("produitsVendu"),metierFacture.getSProduitsVendu(numDossier ,d1,d2));
			} catch(ParseException e){} 
		}
		else if(a!=0){
			if(j!=0) {
				try{
					Date d1 = sf.parse(a+"-"+m+"-"+j);  
					regler2(numDossier, d1, d1, p, sts, j+"");
				}catch (Exception e) {};
			}
			else for(int i=1;i<=31;i++)  
			try{  
				Date d1 = sf.parse(a+"-"+m+"-"+i) ,d2 = sf.parse(a+"-"+(m)+"-"+i);  
				regler2(numDossier, d1, d2, p, sts, i+"");
			}
			catch(Exception e){ }  

			try{ 
				Date d1 = sf.parse(a+"-"+m+"-1") ,d2 = sf.parse(a+"-"+(m+1)+"-1");  
				regler(sts.get("produitsAchete"),metierFacture.getSProduitsAchete(numDossier ,d1,d2));
				regler(sts.get("produitsVendu"),metierFacture.getSProduitsVendu(numDossier ,d1,d2));
			} catch(ParseException e){}
		} 
		
		return sts;
	}
	
	
	 
	
	private void regler2(Long numDossier, Date d1, Date d2, String p,
			HashMap<String, List<Statistique>>listes, String label)
	{  
		Integer totalv=0, totala=0; Double totalpa=0d,totalpv=0d;
		if(p.isEmpty()){
			totala  = metierFacture.getSAchats(numDossier,d1,d2);
			totalpa = metierFacture.getSPrixAchats(numDossier,d1,d2);
			totalv  = metierFacture.getSVentes(numDossier,d1,d2);
			totalpv = metierFacture.getSPrixVentes(numDossier,d1,d2);
		}
		else{ 
			totala  = metierFacture.getSAchatsProduit(numDossier,d1,d2,p);  
			totalpa = metierFacture.getSPrixAchatsProduit(numDossier,d1,d2,p);  
			totalv  = metierFacture.getSVenteProduit(numDossier,d1,d2,p); 
			totalpv = metierFacture.getSPrixVenteProduit(numDossier,d1,d2,p); 
		}
		listes.get("sventes") 		.add(new Statistique(label, totalv+"")); 
		listes.get("sprixventes")	.add(new Statistique(label, totalpv+"")); 
		listes.get("sachats")		.add(new Statistique(label, totala+"")); 
		listes.get("sprixachats")	.add(new Statistique(label, totalpa+""));
	}
	
	private void regler(List<Statistique>list,List<Object[]> total)
	{ 
		for(int x=0;x<total.size();x++) {  
			Object[] objs = total.get(x);
			Produit prd = (Produit)objs[0];
			Object nbr = objs[1]; 
			list.add(new Statistique(prd+"" , nbr+""));
		}
	} 
	
	
	@RequestMapping(value="/statistiques/getglobalstatistiques", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody HashMap<String,Object> getGlobalStatistiques(  
			@RequestParam(name="m",defaultValue="0")Integer m,
			@RequestParam(name="a",defaultValue="0")Integer a 
	)
	{ 
		HashMap<String,Object> statistiques = new HashMap<>(); 

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
		Date d1=null,d2=null,d3=null,d4=null;
		try{ 
			d1 = sf.parse(a+"-"+(m-1)+"-1"); d2 = sf.parse(a+"-"+(m)+"-1");  
			d3 = sf.parse(a+"-"+(m)+"-1"); d4 = sf.parse(a+"-"+(m+1)+"-1"); 
		}
		catch(Exception e){}
		
		//commandes
		Integer nCmdLast = metierCmd.getNombreCommandes(d1,d2);
		Integer nCmd     = metierCmd.getNombreCommandes(d3,d4);  
		Double res = nCmdLast!=0?((nCmdLast>nCmd)?
				(-( (nCmdLast-nCmd)/(double)nCmdLast)*100):
				(( (nCmd-nCmdLast)/(double)nCmdLast)*100)):0 ;
		statistiques.put("nbrCmd", nCmd);  
		statistiques.put("resCmd", res);

		//Qte vendu 
		nCmdLast = metierFacture.getQteVentes(d1,d2);
		nCmd     = metierFacture.getQteVentes(d3,d4);   
		res = nCmdLast!=0?((nCmdLast>nCmd)?
				(-( (nCmdLast-nCmd)/(double)nCmdLast)*100):
				(( (nCmd-nCmdLast)/(double)nCmdLast)*100) ):0;
		statistiques.put("nbrQte", nCmd);  
		statistiques.put("resQte", res);

		//revenue 
		Double revLast = metierFacture.getRevenues(d1,d2);
		Double rev     = metierFacture.getRevenues(d3,d4);  
		res = revLast!=0?((revLast>rev)?
				(-( (revLast-rev)/(double)revLast)*100):
				(( (rev-revLast)/(double)revLast)*100) ):0;
		statistiques.put("nbrRev", rev);  
		statistiques.put("resRev", res);
		
		//croissance 
		Date d_1=null, d_2=null;
		try{ 
			d_1 = sf.parse(a+"-"+(m-2)+"-1"); d_2 = sf.parse(a+"-"+(m-1)+"-1"); 
			Double lastCro = revLast - metierFacture.getRevenues(d_1,d_2);
			Double actuCro = rev - revLast;
			res = lastCro!=0?((lastCro>actuCro)?
					(-( (lastCro-actuCro)/(double)lastCro)*100):
					(( (actuCro-lastCro)/(double)lastCro)*100) ):0;
			statistiques.put("nbrCro", actuCro);  
			statistiques.put("resCro", res);
		} 
		catch(Exception e){} 
		return statistiques;
	}
	
	
	@RequestMapping(value="/statistiques/getStatistiquesAV", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody HashMap<String,List> getStatistiquesAV( 
			@RequestParam(name="m",defaultValue="0")Integer m,
			@RequestParam(name="a",defaultValue="0")Integer a 
	) 
	{
		HashMap<String,List> sts = new HashMap<>();  
		sts.put("labels", new ArrayList<Label>()); 
		sts.put("achats", new ArrayList<Value>());
		sts.put("ventes", new ArrayList<Value>()); 
		sts.put("revenus", new ArrayList<Value>()); 
		
		String[] ms = {"Janvier", "Fevrier", "Mars", "Avril", "May", "Juin", "Juillet", "Aout", "September", "Octob", "November", "December"};
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
  
		Date d1,d2;
			for(int i=1;i<=12;i++)  
			try{ 
				d1= sf.parse(a+"-"+(i)+"-1"); d2 = sf.parse(a+"-"+(i+1)+"-1");

				//  = metierFacture.getRevenues(d1,d2);
				Double totalv   = metierFacture.getPrixVentes(d1,d2);
				Double totala   = metierFacture.getPrixAchats(d1,d2); 
				
				sts.get("labels").add(  new Label(ms[i-1]));
				sts.get("revenus").add( new Value((totalv-totala)+"") ); 
				sts.get("achats").add(  new Value(totala+"") ); 
				sts.get("ventes").add(  new Value(totalv+"") ); 
			}
			catch(Exception e){System.err.println("nooo"); e.printStackTrace();}
    
		return sts;
	}
	
	@RequestMapping(value="/statistiques/getmostselled", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody HashMap<String,List<String>> getMostSelled( 
			@RequestParam(name="m",defaultValue="0")Integer m,
			@RequestParam(name="a",defaultValue="0")Integer a 
	) 
	{
		HashMap<String,List<String>> lfs= new HashMap<String,List<String>>(); 
		lfs.put("produits", new ArrayList<>()); 
		lfs.put("prixs", new ArrayList<>()); 
		lfs.put("qtes", new ArrayList<>()); 
		lfs.put("ttcs", new ArrayList<>()); 
		lfs.put("ids", new ArrayList<>()); 
		
		try{ 
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d2 = sf.parse(a+"-12-31"), d1 = sf.parse(a+"-1-1");  
			List<LigneFacture> list = metierLf.getLignesFacture(d1,d2).getContent();
			//System.err.println(d1+" "+d2);
			for(LigneFacture lf : list) {
				lfs.get("produits").add(lf.getProduit().getDesignation());
				lfs.get("prixs").add(lf.getPrix()+"");
				lfs.get("qtes").add(lf.getQte()+"");
				lfs.get("ttcs").add(lf.getTtc()+""); 
				lfs.get("ids").add(lf.getProduit().getRef()); 
			}
		}
		catch(Exception e){ System.err.println("nooo"); } 

		return lfs;
	}
	
	@RequestMapping(value="/statistiques/getstocks", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody Collection getStocks( 
			@RequestParam(name="m",defaultValue="0")Integer m,
			@RequestParam(name="a",defaultValue="2018")Integer a,
			@RequestParam(name="j",defaultValue="0")Integer j,
			@RequestParam(name="ref",defaultValue="0")String refProduit
	) 
	{
		HashMap<String,String> list= new HashMap<String,String>(); 
		List<Statistique> res = new ArrayList<Statistique>(); 
		
		String[] ms = {"Janvier", "Fevrier", "Mars", "Avril", "May", "Juin", "Juillet", "Aout", "September", "Octob", "November", "December"};
  
		try{    
			List<Object[]> stocks;
			if(m>0)
			{
				if(j>0) stocks = metierStk.getMoyenStocksJour(refProduit,a,m,j);  
				else    stocks = metierStk.getMoyenStocksMois(refProduit,a,m); 
			}
			else stocks = metierStk.getMoyenStocksAnne(refProduit,a); 
			
			for(Object[] stock : stocks) { 
				list.put(""+stock[0],""+stock[1]);  
			}
			//res.add(new Statistique("","0"));
			  
 
			if(m>0&&j>0) for(int i=0;i<=24;i++) {
				if(list.containsKey(""+i)) 
					res.add(new Statistique(i+":00",list.get(""+i))); 
				else res.add(new Statistique(i+":00","0"));
			}
			else if(m>0) for(int i=1;i<=31;i++) {
				if(list.containsKey(""+i)) 
					res.add(new Statistique(i+"",list.get(""+i))); 
				else res.add(new Statistique(i+"","0"));
			}
			else for(int i=1;i<=12;i++) {
				if(list.containsKey(""+i)) 
					res.add(new Statistique(""+ms[i-1],list.get(""+i))); 
				else res.add(new Statistique(""+ms[i-1],"0"));
			}
			
		}
		catch(Exception e){ System.err.println("stock "+e.getMessage()); } 

		return res;
	}

	@RequestMapping(value="/statistiques/getallstocks", method=RequestMethod.POST,produces = "application/json")
	public @ResponseBody FusionChart getAllStocks( 
			@RequestParam(name="m",defaultValue="0")Integer m,
			@RequestParam(name="a",defaultValue="2018")Integer a,
			@RequestParam(name="j",defaultValue="0")Integer j
	) 
	{
		HashMap<String,HashMap<Integer,Value>> list = new HashMap<String,HashMap<Integer,Value>>();  
		
		List<DataSet> ret = new ArrayList<>();
		Collection<Label> categories = new ArrayList<>();
		Collection<Categorie> cats = new ArrayList<>();
		 
		try{    
			List<Object[]> stocks;
			if(m>0)
			{
				if(j>0) stocks = metierStk.getMoyenStocksJour(a,m,j);
				else    stocks = metierStk.getMoyenStocksMois(a,m);
			}
			else stocks = metierStk.getMoyenStocksAnne(a);
			  
			int n=12;
			if(m>0) {
				if(j>0) n=24; else n=31;  
				for(int k=1;k<=n;k++) categories.add(new Label(""+k));
			}
			else { 		
				String[] ms = {"Janvier", "Fevrier", "Mars", "Avril", "May", "Juin", "Juillet", "Aout", "September", "Octob", "November", "December"};
				for(int k=0;k<n;k++) categories.add(new Label(ms[k])); 
			}
			
			Integer v=0,  k=1; 
			int i; 
			
			for(i=0;i<stocks.size();i++) 
			{  
				Object[] stock = stocks.get(i);
				
				try { v=Integer.valueOf(stock[1].toString()); }catch (Exception e) {v=-1;}

				if( !list.containsKey(stock[0]+"") ) {   
					list.put(stock[0]+"",new HashMap<Integer,Value>()); 
					
					if(i>0) {
						ret.add( new DataSet(stocks.get(i-1)[0]+"",list.get(stocks.get(i-1)[0]).values()) );
					}
				}
				else {
					list.get(""+stock[0]).put(v,new Value(stock[2]+"")); 
					continue;
				}
				 
				for(k=1;k<=n;k++)
				{  
					if(k == v) {
						list.get(""+stock[0]).put(v,new Value(stock[2]+"")); 
					}  
					else list.get(""+stock[0]).put(k,new Value("0")); 
				}  
			}  
			if(i>0) ret.add( new DataSet(stocks.get(i-1)[0]+"",list.get(stocks.get(i-1)[0]).values()) );
			 
			cats.add(new Categorie(categories));
		}
		catch(Exception e){ System.err.println("stock 2 "+e.getMessage()); } 
 
		FusionChart fc = new FusionChart(cats, ret);		
		return fc;
	}
	
	
	public class Label { public Label(String v){label=v;} public String label; }  
	public class Value { public Value(String v){value=v;} public String value; }  
	public class Statistique  { public String label,value; public Statistique(String l, String v){label=l;value=v;} }

	public class DataSet{
		public String seriesname;
		public Collection<Value> data;
		public DataSet(String s,Collection c) { seriesname=s; data = c;}
	}
	
	public class Categorie{ 
		public Collection<Label> category;
		public Categorie(Collection<Label> c) { category=c;}
	}
	
	public class FusionChart{
		public Collection<Categorie> categories;
		public Collection<DataSet> dataset;
		public FusionChart(Collection<Categorie> c, Collection<DataSet> d) {categories=c; dataset=d;}
	}
	
	
}












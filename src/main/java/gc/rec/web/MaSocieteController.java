package gc.rec.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.multipart.MultipartFile;

import gc.rec.entities.Fournisseur;
import gc.rec.imetier.IFournisseurMetier;
 

@Controller 
public class MaSocieteController 
{
	@Autowired private IFournisseurMetier metierFournisseur;
	@Autowired private HttpSession session;
	 
	@RequestMapping(value= {"/masociete"})
	public String masociete( Model model ) 
	{
		Fournisseur maSociete = metierFournisseur.getFournisseur("CODE_0"); 
		model.addAttribute("masociete",maSociete);  
		if(!model.containsAttribute("fournisseur")) { 
			model.addAttribute("fournisseur", maSociete);
		}
		session.setAttribute("maSociete", maSociete);
		return "masociete";
	}
	
	@RequestMapping(value= {"/masociete/update"}, method=RequestMethod.POST)
	public String updatemasociete( @Valid Fournisseur fournisseur, BindingResult result, Model model ) 
	{
		if(saveFournisseur(fournisseur,result,model)) 
			model.addAttribute("updateOk","Informations sont mises à jour!");
		else model.addAttribute("updateFailed",true); 
		return masociete(model);
	}
	
	@RequestMapping(value= {"/masociete/logo/update"}, method=RequestMethod.POST)
	public String updatemasociete(@RequestParam("logo") MultipartFile file, Model model) 
	{
		fileUpload(file);
		return masociete(model);
	}
	
	public String fileUpload(MultipartFile file) {
	    InputStream inputStream = null;
	    OutputStream outputStream = null;   
    	ClassPathResource rc = new ClassPathResource("/static/resources/img/1.png");
	    File newFile;
		try {
			newFile = rc.getFile();
		} 
		catch (IOException e1) { e1.printStackTrace(); return ""; }

	    try {
	        inputStream = file.getInputStream();

	        if (!newFile.exists()) {
	            newFile.createNewFile();
	        }
	        outputStream = new FileOutputStream(newFile);
	        int read = 0;
	        byte[] bytes = new byte[1024];

	        while ((read = inputStream.read(bytes)) != -1) {
	            outputStream.write(bytes, 0, read);
	        }
	    } 
	    catch (Exception e) {  e.printStackTrace(); }

	    return newFile.getAbsolutePath();
	}
	 
	private boolean saveFournisseur(Fournisseur fournisseur, BindingResult result, Model model)
	{
		if (result.hasErrors()) 
		{  
			model.addAttribute("masociete", fournisseur); 
			model.addAttribute("fournisseur", fournisseur); 
			return false;
		}

		metierFournisseur.saveFournisseur(fournisseur);
		return true;
	} 
}

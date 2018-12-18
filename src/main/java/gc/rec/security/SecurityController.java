package gc.rec.security;
 

import java.security.Principal;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; 
 
import gc.rec.imetier.IFournisseurMetier; 


@Controller
public class SecurityController implements ErrorController  
{

	@Autowired private HttpSession session;
	@Autowired private IFournisseurMetier mf; 
	

	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) 
	{
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) 
	    {    
	        new SecurityContextLogoutHandler().logout(request, response, auth); 
	    }
	    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	

	@RequestMapping("/login") public String login(Principal p)
	{ 
		session.setAttribute("maSociete",mf.getFournisseur("CODE_0")); 
		return p==null?"login":"redirect:/" ; 
	} 
	
	@RequestMapping("/403") public String forbidden()
	{
		return "403";
	} 
	
	@RequestMapping("/404") public String notfound()
	{
		return "404";
	} 
	
	
	@RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return "404";
	        } 
	    }
	    return "error";
	}
	
	 @Override
	    public String getErrorPath() {
	        return "/error";
	    }
}  
 

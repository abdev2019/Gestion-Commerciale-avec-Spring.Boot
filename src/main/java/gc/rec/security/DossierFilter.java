package gc.rec.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gc.rec.entities.Dossier;




@WebFilter(urlPatterns = {	"/commandes","/factures",
							"/nouveaucommande","/nouveaufacture",
							"/editcommande","/editfacture",
							"/updatecommande","/updatefacture",
							"/deletecommande","/deletefacture",
							"/commande","/facture"})  
public class DossierFilter implements Filter
{ 
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
 
    	
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse ) res;
             
            Dossier dos = (Dossier) request.getSession().getAttribute("dossier"); 
            if(dos==null) 
            	response.sendRedirect(request.getContextPath()+"/dossiers?err=1"); 
            else chain.doFilter(req, res);

    }

    @Override public void init(FilterConfig arg0) throws ServletException {}
    @Override public void destroy() {}
}

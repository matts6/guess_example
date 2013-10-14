package beans;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.Authenticator;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.User;
import org.owasp.esapi.reference.DefaultUser;
import org.owasp.esapi.reference.FileBasedAuthenticator;

/**
 *  * OWASP Enterprise Security API (ESAPI)
 * 
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 *
 * Copyright (c) 2007 - The OWASP Foundation
 * 
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 * 
 * @author      Rakesh
 * @author      Dr.Prof.Emmanuel
 * @author 		Matthey Samuel
 * @version     2.0
 * @date		07/05/2013
 * @since       1.0
 */

@ManagedBean(name="user2")
public class UserBean implements Serializable
{
	private static final long serialVersionUID = -6761470489331898873L;
	private String email = "";
	private String entry = "http://localhost:8080/easpi_project/default.xhtml?entryName=entry1";
	private String textAreaValue = "";
	private String userName;
	private String password;
	private String role;
	private Boolean logedin;
	private Integer intValue = 0;
	private String input;
	private String input1;
	private String input2;
	
	
	public UserBean()
	{
		  FacesContext ctx = FacesContext.getCurrentInstance();
		  String path = ctx.getExternalContext().getRequestContextPath();
			
	      ResponseWriter writer = ctx.getResponseWriter();
	}
	
 	public String getEmail() 
	{
 		
		return email;
 		//return ESAPI.encoder().encodeForHTML("<script>alert(1);<script>");
 		//return  "<script> alert(1);<script>";
	}
 
	public void setEmail(String email) 
	{
		this.email = email;
	}
	
	public String getEntry() 
	{
		return entry;
	}

	public void setEntry(String entry) 
	{
		this.entry = entry;
	}
	
	public String getTextAreaValue() 
	{
		return textAreaValue;
	}

	public void setTextAreaValue(String textAreaValue) 
	{
		this.textAreaValue = textAreaValue;
	}
	
	public String action()
	{
		 FacesContext facesContext = FacesContext.getCurrentInstance();
	     ExternalContext externalContext = facesContext.getExternalContext();
	     Map<String,String> params = externalContext.getRequestParameterMap();
	     
	     String propertyValue = params.get("action");
	     System.out.println("****Action***:"+propertyValue);
	     
	     HttpServletRequest httpServletRequest = (HttpServletRequest)externalContext.getRequest();

         //getSession(false), which returns null if no session already exists for the current client.
         HttpSession session =(HttpSession)externalContext.getSession(false);
         
	     if (session == null)
	     {
	    	 	session = httpServletRequest.getSession(true);
	    	 	session.setAttribute("userName", userName);
	    	 	session.setAttribute("mySessionBean", new Integer(1));
         }
	     else if(session != null && session.getAttribute("mySessionBean") != null && propertyValue != null && propertyValue.equals("AddValue"))
	     {
	    	 intValue = (Integer)session.getAttribute("value");
	    	 if(intValue != null)
	    	 {
	    		 intValue = intValue + 1;
	    	 }
	    	 else
	    	 {
	    		 intValue = 0;
	    	 }
	    	 
		     session.setAttribute("userName", userName);
		     session.setAttribute("value", intValue);
		     session.setAttribute("mySessionBean", new Integer(1));
		     session.setMaxInactiveInterval(60*5);  
	     }
	     else
	     {
		     session.setAttribute("userName", userName);
		     session.setAttribute("value", intValue);
		     session.setAttribute("mySessionBean", new Integer(1));
		     session.setMaxInactiveInterval(60*5);  
	     }
	     /**else
	     {
		    	session.setAttribute("logedin", "1");
	            session.setMaxInactiveInterval(-1);  
	
	            Enumeration e = session.getAttributeNames();
	            while (e.hasMoreElements()) 
	            {
	                String attr = (String)e.nextElement();
	                System.err.println("attr  = "+ attr);
	                Object value = session.getAttribute(attr);
	                System.err.println("value = "+ value);

	            } //end of while
		   	     logedin=true;
			     session.setAttribute("logedin", 1);
			     session.setAttribute("userName", userName);
			     session.setAttribute("value", new Integer(0));
			     userName = session.getAttribute("userName").toString();
			     session.setMaxInactiveInterval(60);  //1 min
	     }
	     **/
		
	     return "viewPage";
	}
	
	public String authentication()
	{
		FileBasedAuthenticator auth = (FileBasedAuthenticator)FileBasedAuthenticator.getInstance();
        User user = (DefaultUser) auth.getUser(userName);
        if (user == null) 
        {
        	try
        	{
               user = auth.createUser(userName, password, password);
     	       user.addRole(role);
     	       user.enable();
     	       user.unlock();
     	       auth.saveUsers();
     	       auth.setCurrentUser(user);
     	       System.out.println("User account " + user.getAccountName() + " updated");
        	}
        	catch(Exception exception)
        	{
        		exception.printStackTrace();
        	}
	    }
        else 
        {
        	auth.setCurrentUser(user);
        	System.out.println(user.getRoles());
	        System.err.println("User account " + user.getAccountName() + " already exists!");
	    }
		return "greeting";
	}
	
	public String nextPage()
	{
		return "greeting";
	}
	
	public String result()
	{
		System.out.println("****************Result***************"+email);
		return "result";
	}
	
	public String logout() 
	{
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        //return "faces/login.xhtml?faces-redirect=true";
        //return "login?faces-redirect=true";
        return "login";
    }
	
	public String getUserName() 
	{
		return userName;
	}

	public void setUserName(String userName) 
	{
		this.userName = userName;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public Integer getIntValue() 
	{
		return intValue;
	}

	public void setIntValue(Integer intValue) 
	{
		this.intValue = intValue;
	}

	public String getRole() 
	{
		return role;
	}

	public void setRole(String role) 
	{
		this.role = role;
	}
	
	/**
	 * @return the input
	 */
	public String getInput() 
	{
		return input;
		//return ESAPI.encoder().encodeForHTML(input);
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(String input)
	{
		this.input = input;
	}

	/**
	 * @return the input1
	 */
	public String getInput1() {
		return input1;
	}

	/**
	 * @return the input2
	 */
	public String getInput2() {
		return input2;
	}

	/**
	 * @param input1 the input1 to set
	 */
	public void setInput1(String input1) {
		this.input1 = input1;
	}

	/**
	 * @param input2 the input2 to set
	 */
	public void setInput2(String input2) {
		this.input2 = input2;
	}
	
}

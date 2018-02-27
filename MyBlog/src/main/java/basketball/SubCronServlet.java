package basketball;

import java.io.*;
import java.util.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.*;


import basketball.Landing;


@SuppressWarnings("serial")
public class SubCronServlet extends HttpServlet {
	
	User user = null;

	private static final Logger _logger = Logger.getLogger(SubCronServlet.class.getName());
	
	public String latest24(ArrayList<Landing> p) {
		String textMsg = "";
		for(Landing t : p) {
			textMsg = t.getUser().toString() + " wrote: \n" + t.getTitle() + "\n" +
					t.getContent() + "\n\n" + textMsg;
		}
		return textMsg;
	}
	@Override
   public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
	   
	 ArrayList<Landing> x = new ArrayList<Landing>();
	 
	
       
       Date now = new Date();
       //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -1);
       Date yesterday = cal.getTime();
       
       ObjectifyService.register(Landing.class);
		List<Landing> greetings = ObjectifyService.ofy().load().type(Landing.class).list();   
		Collections.sort(greetings); 
		
		String digest = "";
		
		if(!greetings.isEmpty()) {
       	 
       	 for (Landing greeting: greetings) {
       		 _logger.info("Last 24 hrs? " + (greeting.getDate().before(now) && greeting.getDate().after(yesterday)));
       		 if(greeting.getDate().before(now) && greeting.getDate().after(yesterday))
           		x.add(greeting);
           }
       	 if(!x.isEmpty()) {
       		 digest = latest24(x);
       	 }
		}
		

	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("lopez.manuel214@gmail.com"));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress("lopez.manuel214@gmail.com"));
	      msg.setSubject("Digest of the last 24 hrs");
	      msg.setText(digest);
	      Transport.send(msg);
	      
	    } catch (Exception e) {
	    		_logger.info("error");
      }
      
      resp.sendRedirect("/basketLand.jsp?guestbookName=" + "default");
   }
   
   @Override
   public void doPost(HttpServletRequest req, HttpServletResponse resp)
   throws IOException {
	   
	   UserService userService = UserServiceFactory.getUserService();
       user = userService.getCurrentUser();
	   doGet(req, resp);
   }
} 
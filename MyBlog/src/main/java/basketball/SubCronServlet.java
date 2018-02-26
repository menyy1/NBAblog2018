package basketball;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.logging.Logger;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import basketball.Landing;


@SuppressWarnings("serial")
public class SubCronServlet extends HttpServlet {

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
   public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
	   
	 ArrayList<Landing> x = new ArrayList<Landing>();
	   
	   UserService userService = UserServiceFactory.getUserService();
       User user = userService.getCurrentUser();
       
       Date now = new Date();
       //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, -1);
       Date yesterday = cal.getTime();
       
       ObjectifyService.register(Landing.class);
		List<Landing> greetings = ObjectifyService.ofy().load().type(Landing.class).list();   
		Collections.sort(greetings); 
       
       
       
      // Recipient's email ID needs to be mentioned.
      String to = "lopez.manuel214@gmail.com";
 
      // Sender's email ID needs to be mentioned
      String from = "lopez.manuel214@gmail.com";
 
      // Assuming you are sending email from localhost
      String host = "10.145.255.255";
 
      // Get system properties
      Properties properties = System.getProperties();
 
      // Setup mail server
      properties.setProperty("mail.smtp.host", host);
 
      // Get the default Session object.
      Session session = Session.getInstance(properties, null);
      
      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);
         
         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));
         
         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         
         // Set Subject: header field
         message.setSubject("This is the Subject Line!");
         
         out.println(greetings.toString());
         // Now set the actual message
         _logger.info("does greeting contain data? " + !greetings.isEmpty());
         if(!greetings.isEmpty()) {
        	 
        	 for (Landing greeting: greetings) {
        		 _logger.info("Last 24 hrs? " + (greeting.getDate().before(now) && greeting.getDate().after(yesterday)));
        		 if(greeting.getDate().before(now) && greeting.getDate().after(yesterday))
            		x.add(greeting);
            }
        	 if(!x.isEmpty()) {
        		 String digest = latest24(x);
        		 _logger.info(digest);
        	 
        	 	message.setText(digest);
         // Send message
        	 	Transport.send(message);
        	 }
         }
         
      } catch (MessagingException mex) {
         mex.printStackTrace();
      }
      
      response.sendRedirect("/basketLand.jsp?guestbookName=" + "default");
   }
   
   @Override
   public void doPost(HttpServletRequest req, HttpServletResponse resp)
   throws IOException {
	   doGet(req, resp);
   }
} 
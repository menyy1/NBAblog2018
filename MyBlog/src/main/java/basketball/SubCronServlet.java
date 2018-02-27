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

//import javax.mail.*;
//import javax.mail.internet.*;
//import javax.activation.*;

//import com.sendgrid.ASM;
//import com.sendgrid.Attachments;
//import com.sendgrid.BccSettings;
//import com.sendgrid.ClickTrackingSetting;
////import com.sendgrid.Client;
//import com.sendgrid.Content;
////import com.sendgrid.Email;
//import com.sendgrid.FooterSetting;
//import com.sendgrid.GoogleAnalyticsSetting;
//import com.sendgrid.Mail;
//import com.sendgrid.MailSettings;
////import com.sendgrid.Method;
//import com.sendgrid.OpenTrackingSetting;
//import com.sendgrid.Personalization;
////import com.sendgrid.Request;
////import com.sendgrid.Response;
//import com.sendgrid.SendGrid;
//import com.sendgrid.Setting;
//import com.sendgrid.SpamCheckSetting;
//import com.sendgrid.SubscriptionTrackingSetting;
//import com.sendgrid.TrackingSettings;
import com.sendgrid.*;

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
   public void doGet(HttpServletRequest req, HttpServletResponse resp)
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
		
		Email from = new Email("lopez.manuel214@gmail.com	");
	    String subject = "Testing Blog";
	    Email to = new Email("lopez.manuel214@gmail.com");
	    Content content = new Content("text/plain", digest);
	    
	    Mail mail = new Mail(from, subject, to, content);
	    

	    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
	    _logger.info(sg.toString());
	    Request request = new Request();

      try {
    	  
    	  	  request.setMethod(Method.POST);
          request.setEndpoint("mail/send");
          request.setBody(mail.build());
          Response response = sg.api(request);
          System.out.println(response.getStatusCode());
          System.out.println(response.getBody());
          System.out.println(response.getHeaders());
         
         
      } catch (Exception mex) {
         mex.printStackTrace();
      }
      
      resp.sendRedirect("/basketLand.jsp?guestbookName=" + "default");
   }
   
   @Override
   public void doPost(HttpServletRequest req, HttpServletResponse resp)
   throws IOException {
	   doGet(req, resp);
   }
} 
package basketball;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import basketball.Landing;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasketServlet extends HttpServlet{
	
	private static final Logger _logger = Logger.getLogger(SubCronServlet.class.getName());
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        _logger.info(user.toString());
        
        String guestbookName = req.getParameter("guestbookName");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        Date date = new Date();
        
        if(title.equals("") || content.equals("")) {
        		resp.sendRedirect("/repostPost.jsp?guestbookName=" + guestbookName);
        }
        else {
        		Landing greeting = new Landing(user, content, guestbookName, title);
        		ofy().save().entity(greeting).now();
        
        		resp.sendRedirect("/basketLand.jsp?guestbookName=" + guestbookName);
        }
	}

}

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %> 

<%@ page import="com.google.appengine.api.users.User" %>

<%@ page import="com.google.appengine.api.users.UserService" %>

<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="java.util.Collections" %>

<%@ page import="com.googlecode.objectify.*" %>

<%@ page import="basketball.Landing" %>

<html>
	<head> 
		<style>
			div.boxpad{
				border: 2px solid powderblue;
    				padding: 30px;
			}
		</style>
		<link type="text/css" rel="stylesheet" href="https://bootswatch.com/4/solar/bootstrap.min.css" />
	</head>
	
	<body>
	
		<% String guestbookName = request.getParameter("guestbookName");
		    if (guestbookName == null) {
		        guestbookName = "default";
		    }
		    
		    pageContext.setAttribute("guestbookName", guestbookName);
		    UserService userService = UserServiceFactory.getUserService();
		    User user = userService.getCurrentUser();
		    
		    if (user != null) {
		      pageContext.setAttribute("user", user);
%>

		<h1>2018 NBA playoff predictions</h1>
		<img src="http://i2.cdn.turner.com/nba/nba/dam/assets/160413134216-playoffs-2016-official-t1-creative.home-t1.jpg" alt="http://www.nba.com/news/2016-nba-playoffs-first-round-schedule/" align="center" width="600" height="250">
		<hr>
		<p> This webpage is a blog to discuss NBA playoff predictions, write who are your top 8 teams from the western conference and the eastern conference, who are going to be out in each playoff round and what the overall score would be, and finally share who you think will be the new 2018 NBA champ and its most valuable player (MVP) </p>
		<p> Hello, ${fn:escapeXml(user.nickname)}! (You can <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.) If you wish to participate in this blog please click in the "Create a new post" button</p>
		<div> <input type="button" value="Create new post" onclick="window.location.href='createPost.jsp'"> 
		</div>

	<%
		    } else {
	%>
	
		<h1>2018 NBA playoff predictions</h1>
		<img src="http://i2.cdn.turner.com/nba/nba/dam/assets/160413134216-playoffs-2016-official-t1-creative.home-t1.jpg" alt="http://www.nba.com/news/2016-nba-playoffs-first-round-schedule/" align="center" width="600" height="250">
		<hr>
		<p> This webpage is a blog to discuss NBA playoff predictions, write who are your top 8 teams from the western conference and the eastern conference, who are going to be out in each playoff round and what the overall score would be, and finally share who you think will be the new 2018 NBA champ and its most valuable player (MVP) </p>
		<p> In order to participate in this blog you will have to <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a> </p>
	<% 
			} 
	%>
	
	
	
	
	<%

    // Run an ancestor query to ensure we see the most up-to-date

    // view of the Greetings belonging to the selected Guestbook.

ObjectifyService.register(Landing.class);
List<Landing> greetings = ObjectifyService.ofy().load().type(Landing.class).list();   
Collections.sort(greetings); 

    if (greetings.isEmpty()) {
        %>
        <p> Be the first one to post!</p>
        <%
    } else {
        %>
        <p><h3>These are the latest posts</h3></p>
        <%
        for (int i = greetings.size()-1; i>greetings.size()-6 && i >= 0; i--) {
        		Landing greeting = greetings.get(i);
        		pageContext.setAttribute("greeting_title", greeting.getTitle());
            pageContext.setAttribute("greeting_content", greeting.getContent());
                pageContext.setAttribute("greeting_user", greeting.getUser());
                pageContext.setAttribute("greeting_date", greeting.getDate());
                %>
                <div class="boxpad"><p><b>${fn:escapeXml(greeting_user.nickname)}</b> wrote on ${fn:escapeXml(greeting_date)}:</p>
                <p><b>${fn:escapeXml(greeting_title)}</b></p>
            		<blockquote><q>${fn:escapeXml(greeting_content)}</q></blockquote>
            </div>
            <%
        }
    }

%>
<div>
	<input type="button" value="Load more posts" onclick="window.location.href='allPosts.jsp'" />
</div>

<form action="/cron/subscribe" method="post"> 

	<a href="subscribed.jsp">Subscribe Here!</a>

    </form>
    <form>
    <a href="unsubscribed.jsp">Unsubscribe here</a>
    </form>
	
	</body>
</html>
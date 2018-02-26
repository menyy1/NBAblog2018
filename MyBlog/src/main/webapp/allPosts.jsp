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
		<h1> These are all the posts:</h1>
<% 
		ObjectifyService.register(Landing.class);
		List<Landing> greetings = ObjectifyService.ofy().load().type(Landing.class).list();   
		Collections.sort(greetings); 

    if (greetings.isEmpty()) {
        %>
        <p> Be the first one to post!</p>
        <%
    } else {
        for (int i = greetings.size()-1; i>=0; i--) {
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
<div><a href="basketLand.jsp">Return to main page</a></div>
	</body>

</html>
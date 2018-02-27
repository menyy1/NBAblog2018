<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head> 
		<link type="text/css" rel="stylesheet" href="https://bootswatch.com/4/solar/bootstrap.min.css" />
	</head>
	<body>
	<h2> Enter your comment:</h2>
	<hr>
     <form action='/basket' method="post">
	<table>
	  <tr>
	  	<td> Title:</td> 
	  	<td><textarea name="title" rows="1" cols="60"></textarea></td> 
	  </tr>
	  <tr>
	  	<td> Comment: </td>
	  	<td> <textarea name="content" rows="20" cols="60"></textarea></td>
	</tr>
	<tr>
	<td></td>
		<td><h4 style="color:red">*Please fill out all fields before submitting</h4></td>
	</tr>
      <tr>
      <td></td>
      	<td><input type="submit" value="Post Comment" onclick="window.location.href='basketLand.jsp'" />
      	<input type="button" value="Cancel" onclick="window.location.href='basketLand.jsp'"></td>
      
    <input type="hidden" name="guestbookName" value="default"/>
   </tr>
	</table>
	</form>
</body>
</html>
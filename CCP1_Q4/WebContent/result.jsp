<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html >
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>AoAoGo Search Result</title>

    <!-- Bootstrap -->
    
    <link href="css/bootstrap.min.css" rel="stylesheet">
    

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
    
    <link rel="SHORTCUT ICON" href="img/iconb.ico"> 
  </head>
  
  
  <body>
 <%@ page import = "Search.SearchReadResult" %>  
<%@ page import = "java.util.Arrays" %>
<%@ page import = "java.util.ArrayList" %>

<% 
String query = (String)request.getAttribute("query"); 
String timeUsed = (String)request.getAttribute("timeUsed"); 

String hdfs = "hdfs://master:9000/text_"+ query.toLowerCase() +"/part-00000"; 
ArrayList <String> result = new ArrayList<String>();
result = SearchReadResult.ReadFile(hdfs);
%>
  	<nav class="navbar navbar-default">
  		<div class="container-fluid">
    		<div class="navbar-header">
      			<a class="navbar-brand" ><img src="img/icon.png"></a>
      			<a class="navbar-brand" href="#">AoAoGo</a>
    		</div>
    		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1 ">
    		<ul class="nav navbar-nav navbar-right">
        		<li><a href="index.html">Search <span class="sr-only">(current)</span></a></li>
      		</ul>
    		</div>
  		</div>
	</nav><!-- navbar navbar-default -->
  
  	<div class="row">
  		<div class="col-md-1"> </div>
  		<div class="col-md-6"> 
  			
  			<div class="row">

				<div class="container">
    			<form class="input-group input-group-lg" action="/CCP1_Q4/Search/Search"  method="POST">
      				<input type="text" class="form-control"  name="query" placeholder="">
      				<span class="input-group-btn">
        			<button class="btn btn-default" type="submit">  
        			<span class="glyphicon glyphicon-search" aria-hidden="true"></span> 
        			</button>
      				</span>
    			</form><!-- /form -->
    			</div>
			</div><!-- /.row -->

        		
        	</div>
  		<div class="col-md-5"></div>
  	</div><!-- /.row -->

	<div class="row">
	  	<div class="col-md-1"> </div>
	  	<div class="col-md-8"> 
			<div class="page-header">
  					<h1><small>Seaerch result for</small>   <%out.println(query); %></h1>
  					<p>Time used: <%out.println(timeUsed); %> seconds</p>
			</div>
				<%
				if(result.size()==0){
					out.print("No such word and please try another word");
				}
				else{
					for(int i=0;i<result.size()&&!result.get(i).equals("");i++){
						out.print("</br><p id='p'>" + result.get(i)+"</p>");
						out.print("<HR style='margin-top:40px'/>");
					}
				}
				%>

		</div>
		<div class="col-md-3"></div>
	</div>

  </body>
</html>
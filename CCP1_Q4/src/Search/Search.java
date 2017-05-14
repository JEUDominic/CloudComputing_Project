package Search;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String, String[]> parameters = request.getParameterMap();	
		if (parameters.containsKey("query")){
			long startTime=System.currentTimeMillis(); 
			String query = request.getParameter("query").toLowerCase();
			boolean check = SearchReadResult.findFileIsExit(query);
			if(!SearchReadResult.findFileIsExit("1G_index")){
				String a[]={"hdfs://master:9000/input/1G.txt","hdfs://master:9000/1G_index"};
				Index.main(a);
			}
			
			if(!check){
				String[] args=new String[5];  
				args[0]="hdfs://master:9000/1G_index/part-00000";
				args[1]="hdfs://master:9000/index_"+query;
				args[2]=query;
				args[3]="hdfs://master:9000/input/1G.txt";
				args[4]="hdfs://master:9000/text_"+query;

				try {
					//SearchMapReduce.startSearch(args);
					
					SearchMapReduce.startSearch(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
				long endTime=System.currentTimeMillis(); 
				long timeused = endTime-startTime;
				String timeUsed = Long.toString(timeused/1000)+"."+Long.toString(timeused%1000);
				request.setAttribute("query",request.getParameter("query"));
				request.setAttribute("timeUsed",timeUsed );
				RequestDispatcher rd = request.getRequestDispatcher("result.jsp");
				rd.forward(request, response);
			}
			else{
				long endTime=System.currentTimeMillis(); 
				long timeused = endTime-startTime;
				String timeUsed = Long.toString(timeused/1000)+"."+Long.toString(timeused%1000);
				request.setAttribute("query",request.getParameter("query"));
				request.setAttribute("timeUsed",timeUsed );
				request.setAttribute("timeUsed", Long.toString((endTime-startTime)/1000)+"."+Long.toString((endTime-startTime)%1000));
				RequestDispatcher rd = request.getRequestDispatcher("result.jsp");
				rd.forward(request, response);
			}
		}
		//request.getRequestDispatcher("result.jsp").forward(request, response);
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}


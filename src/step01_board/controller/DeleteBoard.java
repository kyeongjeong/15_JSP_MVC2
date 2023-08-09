package step01_board.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import step01_board.dao.BoardDAO;

@WebServlet("/bDelete")
public class DeleteBoard extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute("boardId", Long.parseLong(request.getParameter("boardId")));
		
		RequestDispatcher dis = request.getRequestDispatcher("step01_boardEx/bDelete.jsp");
		dis.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		BoardDAO.getInstance().deleteBoard(Long.parseLong(request.getParameter("boardId")));

		String	jsScript = "<script>";
				jsScript += "alert('삭제 되었습니다.');";
				jsScript += "location.href='bList';";
				jsScript += "</script>";

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsScript);
	
	}

}

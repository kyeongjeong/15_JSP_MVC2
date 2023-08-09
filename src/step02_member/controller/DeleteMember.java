package step02_member.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import step02_member.dao.MemberDAO;

@WebServlet("/deleteMember")
public class DeleteMember extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		request.setAttribute("memberId", (String)session.getAttribute("memberId"));
		
		RequestDispatcher dis = request.getRequestDispatcher("step02_memberEx/mDelete.jsp");
		dis.forward(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		String file_repo_path = "D:\\abu\\view_workspace\\15_jsp_mvc2\\15_jsp_mvc2\\WebContent\\step02_memberEx\\memberImg\\";
		
		String imgNm = MemberDAO.getInstance().getMemberDetail((String)session.getAttribute("memberId")).getImgNm(); // 삭제하고 싶은 이미지파일 명을 가져옴
		new File(file_repo_path + imgNm).delete();	// delete메서드를 이용하여 이미지 파일을 삭제
		
		MemberDAO.getInstance().deleteMember((String)session.getAttribute("memberId"));

		session.invalidate();
		
		String jsScript = "<script>";
			   jsScript += "alert('탈퇴 되었습니다.');";
			   jsScript += "location.href='mainMember';";
			   jsScript += "</script>";
	
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();	
		out.print(jsScript);

	}
	
}

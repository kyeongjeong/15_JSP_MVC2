package step02_member.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import step02_member.dao.MemberDAO;
import step02_member.dto.MemberDTO;

@WebServlet("/updateMember")
public class UpdateMember extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		request.setAttribute("memberDTO", MemberDAO.getInstance().getMemberDetail((String)session.getAttribute("memberId")));
		
		RequestDispatcher dis = request.getRequestDispatcher("step02_memberEx/mUpdate.jsp");
		dis.forward(request, response);
		
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String file_repo_path = "D:\\abu\\view_workspace\\15_jsp_mvc2\\15_jsp_mvc2\\WebContent\\step02_memberEx\\memberImg\\";
		MultipartRequest multi = new MultipartRequest(request, file_repo_path , 1024 * 1024 * 30 , "utf-8" , new DefaultFileRenamePolicy());  // 파일 업로드
		
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemberId(multi.getParameter("memberId"));
		memberDTO.setMemberNm(multi.getParameter("memberNm"));
		memberDTO.setSex(multi.getParameter("sex"));
		memberDTO.setBirthDt(multi.getParameter("birthDt"));
		memberDTO.setHp(multi.getParameter("hp"));
		if (multi.getParameter("smsRecvAgreeYn") == null) memberDTO.setSmsRecvAgreeYn("N");				
		else										  	  memberDTO.setSmsRecvAgreeYn(multi.getParameter("smsRecvAgreeYn"));
		memberDTO.setEmail(multi.getParameter("email"));
		if (multi.getParameter("emailRecvAgreeYn") == null) memberDTO.setEmailRecvAgreeYn("N");
		else 												memberDTO.setEmailRecvAgreeYn(multi.getParameter("emailRecvAgreeYn"));
		memberDTO.setZipcode(multi.getParameter("zipcode"));
		memberDTO.setRoadAddress(multi.getParameter("roadAddress"));
		memberDTO.setJibunAddress(multi.getParameter("jibunAddress"));
		memberDTO.setNamujiAddress(multi.getParameter("namujiAddress"));
        
		
		Enumeration files =  multi.getFileNames();					// <input type="file"> 엘리먼트들을 반환
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	// 파일명에 업로드 날짜를 추가하기 위한 형식 생성
		
		String imgNm = "";
		String originalFileName = "";
		if (files.hasMoreElements()) {								// <input type="file">이 존재하면
			
			String element = (String)files.nextElement();			// 엘리먼트를 읽어옴
			if (multi.getOriginalFileName(element) != null) {		// 원본파일명이 있으면 > 파일을 업로드했으면
				
				originalFileName = multi.getOriginalFileName(element);								// 업로드한 파일 이름을 읽어옴
				imgNm = sdf.format(new Date()) + "_" + UUID.randomUUID() + "_" + originalFileName;	// 날짜_해쉬함수_업로드파일 형식으로 파일명 생성 (UUID.randomUUID() : 해쉬함수 생성)
				
				String deleteImgNm = MemberDAO.getInstance().getMemberDetail(multi.getParameter("memberId")).getImgNm(); // 기존의 프로필 이미지명을 읽어옴
				new File(file_repo_path + deleteImgNm).delete();														 // 기존에 업로드한 파일을 삭제
				
				File file = new File(file_repo_path + originalFileName);	// 새로 업로드한 파일을 읽어옴
				File renameFile = new File(file_repo_path + imgNm);			// 변환된 파일명으로 새로운 파일을 생성
				file.renameTo(renameFile);									// 기존에 업로드한 파일을 변환된 파일명으로 이름 변경
				
			}
			
		}
		
		memberDTO.setImgNm(imgNm);	// 이미지를 업로드한 경우 > '날짜_해쉬함수_업로드파일명' 형식으로 DTO에 set
									// 이미지를 업로드하지 않은 경우 > "" set
		MemberDAO.getInstance().updateMember(memberDTO);
		
		String jsScript = "<script>";
			   jsScript += "alert('수정 되었습니다.');";
			   jsScript += "location.href='detailMember';";
			   jsScript += "</script>";
	
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();	
		out.print(jsScript);
		
	}

}

package step01_board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import step01_board.dto.BoardDTO;

public class BoardDAO {
	
	private BoardDAO() {}
	private static BoardDAO instance = new BoardDAO();
	public static BoardDAO getInstance() {
		return instance;
	}
	
	private Connection conn 		= null;
	private PreparedStatement pstmt = null;
	private ResultSet rs 			= null;
	
	
	private void getConnection() {
		
		try {
			
			/*
			
				이클립스에서 Servers폴더에 있는 Context.xml파일에 아래의 설정 추가 
			
				<Resource 
					auth="Container" 
					driverClassName="com.mysql.cj.jdbc.Driver"
					type="javax.sql.DataSource"
					url="jdbc:mysql://localhost:3306/MVC2_PRACTICE?serverTimezone=Asia/Seoul&amp;useSSL=false"
					name="jdbc/board" 
					username="root"
					password="1234" 
					loginTimeout="10" 
					maxWait="5000" 
				/> 
			
			 */
			
			Context initctx = new InitialContext();
			Context envctx = (Context) initctx.lookup("java:comp/env");       // lookup 메서드를 통해 context.xml 파일에 접근하여 자바환경 코드를 검색
			DataSource ds = (DataSource) envctx.lookup("jdbc/board"); 		  // <Context>태그안의 <Resource> 환경설정의 name이 jdbc/board인 것을 검색
			conn = ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void getClose() {
		
    	if (rs != null)    {try {rs.close();}   catch (SQLException e) {}}
    	if (pstmt != null) {try {pstmt.close();} catch (SQLException e) {}}
        if (conn != null)  {try {conn.close();}  catch (SQLException e) {}}
        
    }
	
	
	public void insertBoard(BoardDTO boardDTO) {
		
		try {
			
			getConnection();
			String sql = "INSERT INTO BOARD(WRITER , EMAIL , SUBJECT , PASSWORD , CONTENT , READ_CNT , ENROLL_DT)";
				   sql += " VALUES(? , ? , ? , ? , ? , 0 , NOW())";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardDTO.getWriter());
			pstmt.setString(2, boardDTO.getEmail());
			pstmt.setString(3, boardDTO.getSubject());
			pstmt.setString(4, boardDTO.getPassword());
			pstmt.setString(5, boardDTO.getContent());
			pstmt.executeUpdate();
				   
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
		
	}
	
	
	public ArrayList<BoardDTO> getBoardList() {
		
		ArrayList<BoardDTO> boardList = new ArrayList<BoardDTO>();
		
		try {
			
			getConnection();
			
			pstmt = conn.prepareStatement("SELECT * FROM BOARD");
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				BoardDTO temp = new BoardDTO();
				temp.setBoardId(rs.getLong("BOARD_ID"));
				temp.setWriter(rs.getString("WRITER"));
				temp.setSubject(rs.getString("SUBJECT"));
				temp.setEnrollDt(rs.getDate("ENROLL_DT"));
				temp.setReadCnt(rs.getLong("READ_CNT"));
				
				boardList.add(temp);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
		
		//System.out.println(boardList);
		return boardList;
		
	}
	
	
	public BoardDTO getBoardDetail(long boardId) {
		
		BoardDTO boardDTO = new BoardDTO();
		
		try {
			
			getConnection();
			
			pstmt = conn.prepareStatement("UPDATE BOARD SET READ_CNT = READ_CNT + 1 WHERE BOARD_ID = ?");
			pstmt.setLong(1 , boardId);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("SELECT * FROM BOARD WHERE BOARD_ID = ?");
			pstmt.setLong(1, boardId);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				boardDTO.setBoardId(rs.getLong("BOARD_ID"));
				boardDTO.setReadCnt(rs.getLong("READ_CNT"));
				boardDTO.setWriter(rs.getString("WRITER"));
				boardDTO.setEnrollDt(rs.getDate("ENROLL_DT"));
				boardDTO.setEmail(rs.getString("EMAIL"));
				boardDTO.setSubject(rs.getString("SUBJECT"));
				boardDTO.setContent(rs.getString("CONTENT"));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
		
		//System.out.println(boardDTO);
		return boardDTO;
		
	}
	
	
	public boolean checkAuthorizedUser(BoardDTO boardDTO) {
		
		boolean isAuthorizedUser = false;

		try {
			
			getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM BOARD WHERE BOARD_ID = ? AND PASSWORD = ?");
			pstmt.setLong(1, boardDTO.getBoardId());
			pstmt.setString(2, boardDTO.getPassword());
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				isAuthorizedUser = true;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
		
		return isAuthorizedUser;
		
	}
	
	
	public void updateBoard(BoardDTO boardDTO) {
		
		try {
			
			getConnection();
			String sql = "UPDATE BOARD SET ";
			       sql += "SUBJECT = ? , ";
			       sql += "CONTENT = ? ";
			       sql += "WHERE BOARD_ID = ?";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardDTO.getSubject());
			pstmt.setString(2, boardDTO.getContent());
			pstmt.setLong(3, boardDTO.getBoardId());
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
		
	}
	
	
	public void deleteBoard(long boardId) {
		
		try {
			
			getConnection();
			pstmt = conn.prepareStatement("DELETE FROM BOARD WHERE BOARD_ID = ?");
			pstmt.setLong(1, boardId);
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			getClose();
		}
		
	}
	
	
}

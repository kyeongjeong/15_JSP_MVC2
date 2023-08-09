<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>bAuthentication</title>
</head>
<body>

	<div align="center"> 
		<h3>사용자 인증</h3>
		<form action="boardAuthentication" method="post">
			<table border="1">
				<tr>
					<td>작성자</td>
					<td>${mainBoardDTO.writer }</td>
				</tr>
				<tr>
					<td>작성일</td>
					<td>${mainBoardDTO.enrollDt }</td>
				</tr>
				<tr>
					<td>제목</td>
					<td>${mainBoardDTO.subject }</td>
				</tr>
				<tr>
					<td>패스워드</td>
					<td><input type="password" name="passwd"></td>
				</tr>
			</table>
			<p>
				<input type="hidden" name="boardId" value="${mainBoardDTO.boardId }" /> 
				<input type="hidden" name="menu" value="${menu }" /> 
				<input type="submit" value="인증" />
				<input type="button" onclick="location.href='boardList'" value="목록보기" />
			</p>
		</form>
	</div>

</body>
</html>
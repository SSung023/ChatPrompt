<%@ page language="java"  import="java.text.DecimalFormat" contentType= "text/html;charset=utf8" pageEncoding="utf8"%>
<%@ include file="./file_write.jsp"%>
<%
	// 태스크 번호, 구축자, 지시문 2개 정보 전달 
	request.setCharacterEncoding( "utf8" ); //한글 깨짐 방지
	String task_id = request.getParameter( "task_id" );	
	String annotator = request.getParameter( "annotator" );	
	String definition1 = request.getParameter( "definition1" );	
	String definition2 = request.getParameter( "definition2" );	

	// 다음 태스크
	DecimalFormat df = new DecimalFormat("000");
	String next_task_id = df.format( Integer.parseInt( task_id ) + 1 );

	// 파일 출력 
	String filePath = "/usr/local/tomcat/webapps/ROOT/prompt/";
	FileManager filemanager = new FileManager();
	filemanager.write( definition1 + "\n ( split this page up and down ) \n" + definition2, 
			   filePath + "definition/" + task_id + "_definition_" + annotator +".txt" );
%>
<form name = "form_move" method = "post" action = '<%="./editDefinition.jsp"%>' >
	<input type = "hidden" name = "task_id"   	value = "<%=next_task_id%>" >
	<input type = "hidden" name = "annotator"	value = "<%=annotator%>" >
</form>
<script language="javascript">
	document.form_move.submit();
</script>



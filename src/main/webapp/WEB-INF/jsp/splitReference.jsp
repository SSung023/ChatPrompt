<%@ page language="java"  import="java.text.DecimalFormat" contentType= "text/html;charset=utf8" pageEncoding="utf8"%>
<%@ include file="./file_write.jsp"%>
<%
	// 다음 태스크
	DecimalFormat df = new DecimalFormat("000");
	String next_task_id = df.format( Integer.parseInt( task_id ) + 1 );

	// 파일 출력 
	String filePath = "/usr/local/tomcat/webapps/ROOT/prompt/";
	FileManager filemanager = new FileManager();
	String reference = filemanager.read( filePath + "preparation/sample.xml");
	String template = filemanager.read( filePath + "preparation/template_reference.txt");
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



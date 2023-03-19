<%@ page language="java"  import="java.text.DecimalFormat" contentType= "text/html;charset=utf8" pageEncoding="utf8"%>
<%@ include file="./file_write.jsp"%>
<%
	// �½�ũ ��ȣ, ������, ���ù� 2�� ���� ���� 
	request.setCharacterEncoding( "utf8" ); //�ѱ� ���� ����
	String task_id = request.getParameter( "task_id" );	
	String annotator = request.getParameter( "annotator" );	
	String definition1 = request.getParameter( "definition1" );	
	String definition2 = request.getParameter( "definition2" );	

	// ���� �½�ũ
	DecimalFormat df = new DecimalFormat("000");
	String next_task_id = df.format( Integer.parseInt( task_id ) + 1 );

	// ���� ��� 
//	String filePath = "/usr/local/tomcat/webapps/ROOT/prompt/";
	String filePath = "prompt/";
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



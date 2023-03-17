<%@ page language="java" contentType= "text/html;charset=utf8" pageEncoding="utf8"%>
<html>
<style> #container { float:left; position:absolute; top:0px; left:20%; width:1050px; border:0px solid #bcbcbc; } </style>
<head><script language="javascript">
	function move( action )
	{
		var task_id = Number( document.form_move.task_id.value );
		if ( ( 1 <= task_id ) && ( task_id <= 120 ) )
		{
 			document.form_move.action = action;
			document.form_move.submit();
		}
		else 
		{
			alert( "현재 태스크번호는 " + task_id +"입니다. \n 태스크번호는 001부터 120까지 입니다");
		}
	}
</script></head>
<body>
<div id = "container">
<br>
<%@ include file="./file_write.jsp"%>
<%
	// 태스크 번호 및 구축자 정보 전달 
	String task_id = ( ( request.getParameter( "task_id" ) == null ) ? "001" : request.getParameter( "task_id" ) );	
	String annotator = ( ( request.getParameter( "annotator" ) == null ) ? "G" : request.getParameter( "annotator" ) );	

	// 파일 출력 
	String filePath = "/usr/local/tomcat/webapps/ROOT/prompt/";
	FileManager filemanager = new FileManager();
	String[] referenceText = filemanager.read( filePath + "reference/" + task_id + "_reference.txt" ).split( "\n \\( split this page up and down \\) \n" );
	String[] definitionText = filemanager.read( filePath + "definition/" + task_id + "_definition_" + annotator +".txt" ).split( "\n \\( split this page up and down \\) \n" );
%>
<form name = "form_move" method = "post">
	<table width='1024px'>
		<tr>
			<td>
				구축자명 :
      					<select name = "annotator">
          					<option value = "A" <%=annotator.equals("A")? "selected":""%>>구축자1</option>
          					<option value = "B" <%=annotator.equals("B")? "selected":""%>>구축자2</option>
         					<option value = "C" <%=annotator.equals("C")? "selected":""%>>구축자3</option>
         					<option value = "D" <%=annotator.equals("D")? "selected":""%>>김서래</option>
         					<option value = "E" <%=annotator.equals("E")? "selected":""%>>김다은</option>
         					<option value = "F" <%=annotator.equals("F")? "selected":""%>>성희연</option>
         					<option value = "G" <%=annotator.equals("G")? "selected":""%>>박소영</option>
       					</select> &nbsp; &nbsp; 
        				태스크번호 : <input type="text" name = "task_id" value = "<%=task_id%>">
			</td>
			<td align='right' width='450'>
				<input type="button" onClick="javascript:move( './editDefinition.jsp' );" value = "저장 없이 왼쪽 지정 페이지로 바로 이동"> 
				<input type="button" onClick="javascript:move( './saveDefinition.jsp' );" value = "저장하고 다음 페이지로 이동" >
<!--				<input type="button" onClick="javascript:move( './??.jsp' );" value = "유사도검사결과" >
				<input type="button" onClick="javascript:move( './??.jsp' );" value = "작업진행현황" >
-->			</td>
		</tr>
	</table><br>
	<%= ( (referenceText.length>0)&&(referenceText[0]!=null) ? referenceText[0] : "" )%>
	<table width='1024px'>
		<tr>
			<td align='left'> * 다음 유사 지시문 2개를 작성하시오. </td>
			<td align='right'>  </td>
		</tr>
	</table><br>
	<table border='1' width='1024px' style='table-layout:fixed; word-break:break-all;'>
		<tr>
			<td width='100px' align='center'> 유사지시문1 </td>
			<td><textarea name='definition1' rows="20" cols="114" style="border: none" ><%= ( (definitionText.length>0)&&(definitionText[0]!=null) ? definitionText[0] : "" )%></textarea></td> 
		</tr>
		<tr>
			<td align='center'> 유사지시문2 </td>
			<td><textarea name='definition2' rows="20" cols="114" style="border: none" ><%= ( (definitionText.length>1)&&(definitionText[1]!=null) ? definitionText[1] : "" )%></textarea></td> 
		</tr>
	</table>
</form>
<br>* 참고자료<br><br>
	<%= ( (referenceText.length>1)&&(referenceText[1]!=null) ? referenceText[1] : "" )%>
</div>
</body>
</html>


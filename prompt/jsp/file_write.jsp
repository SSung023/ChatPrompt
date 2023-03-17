<%@ page language="java" import="java.io.*"%>
<%
class FileManager
{
	public String read( String filename ) 
	{
		return read( filename, "utf8" );
	}

	public String read( String filename, String encoding ) 
	{
		String str = ""; 

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try
		{
			// 파일 열기
			fis = new FileInputStream( filename );
			isr = new InputStreamReader( fis, encoding ); 
			br = new BufferedReader( isr );

			// 파일 읽기
			for ( String line=null; ( line = br.readLine() ) != null; str += ( line + "\n" ) );

			// 파일 닫기
			if( br != null ) br.close();
			if( isr != null ) isr.close();
			if( fis != null ) fis.close(); 
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return str;
	}

	public void write( String str, String filename ) 
	{
		write( str, filename, false, "utf8" ); 
	}

	public void write( String str, String filename, boolean append, String encoding ) 
	{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		
		try
		{
			// 파일 열기
			fos = new FileOutputStream( filename, append );
			osw = new OutputStreamWriter( fos, encoding ); 
			bw = new BufferedWriter( osw );
			pw = new PrintWriter ( bw );

			// 파일 출력
			pw.println( str );

			// 파일 닫기
			if( pw != null ) pw.close(); 
			if( bw != null ) bw.close();
			if( osw != null ) osw.close();
			if( fos != null ) fos.close();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean find( String filename ) 
	{
		File file = new File( filename );
		return file.exists();
	}
}
%>
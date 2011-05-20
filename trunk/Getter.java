import java.io.*;
import java.net.*;

public class Getter {
	public static String getData(String path) 
		throws Exception {
			URL url = new URL(path);
			URLConnection uconn = url.openConnection();

			BufferedReader br = 
				new BufferedReader(
						new InputStreamReader(
							uconn.getInputStream()));
			String buffer = "";
			String str = null;

			while ((str = br.readLine()) != null) {
				buffer += str + "\n";
			}
			
			br.close();

			return buffer;
		}
		
	public static BufferedReader getBufferedData(String path) throws Exception
	{
		URL url = new URL(path);
		URLConnection uconn = url.openConnection();

		BufferedReader br = new BufferedReader(new InputStreamReader(uconn.getInputStream()));
		return br;
	}
		
	public static String getLocalData(String path) throws Exception
	{
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(
		        new FileReader(path));
		char[] buf = new char[1024];
		int numRead=0;
		while((numRead=reader.read(buf)) != -1){
		    String readData = String.valueOf(buf, 0, numRead);
		    fileData.append(readData);
		    buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
}

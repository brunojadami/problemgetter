import java.util.*;

public class Status
{
	public final static String STATUS_URL = "http://uva.onlinejudge.org/index.php?option=onlinejudge&page=show_authorstats&userid=";
	
	public static HashSet<Integer> getSolved() throws Exception
	{
		HashSet<Integer> solved = new HashSet<Integer>();
		
		String conts = Getter.getLocalData("contestants");
		Scanner scanner = new Scanner(conts);
		
		while (scanner.hasNext())
		{
			String id = scanner.next();
			System.out.println("Ignoring problems from user " + id);
			String status = Getter.getData(STATUS_URL + id);
			int pos = 0;
			while (true)
			{
				pos = status.indexOf("</a></td>", pos+1);
				if (pos == -1)
					break;
				String number = "";
				for (int i = pos-1; i >= 0; --i)
				{
					if (status.charAt(i) == '>')
						break;
					else
						number = new StringBuffer(number).insert(0, status.charAt(i)).toString();
				}
				int get = 0;
				try
				{
					get = Integer.parseInt(number);
					solved.add(get);
				}
				catch (Exception ex)
				{
			
				}
			}
		}
		
		return solved;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<Integer> solved = getSolved();
		Iterator<Integer> it = solved.iterator();
		while (it.hasNext())
			System.out.println(it.next());
	}
}


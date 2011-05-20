import java.util.*;
import java.io.*;

public class ToolKit {

	public static final String EXTERNAL_URL = "http://uva.onlinejudge.org/external/";

	public static HashMap<Integer, Problem> problems;
	
	public synchronized static void addProblem(Problem p){
		problems.put(p.getId(), p);
	}

	public static HashMap<Integer, Problem> getProblems(boolean force) throws Exception{
	
		System.out.print("0%");
		for (int i = 0; i < 50; ++i)
			System.out.print("-");
		System.out.println("100%");
		System.out.print("  ");
		
		if (!force)
		{
			checkCache();
			if (problems != null)
				return problems;
		}
	
		String buff = Getter.getData("http://uvatoolkit.com/problemssolve.php");
		problems = new HashMap<Integer, Problem>();
		Scanner sc = new Scanner(buff); int id;
		while(sc.hasNext())
			if(sc.next().equals("none\">")) break;
		int n = Thread.activeCount();
		
		LinkedList<Integer> list = new LinkedList<Integer>();
		while(sc.hasNext())
		{
			id = sc.nextInt();
			list.add(id);
			sc.nextLine();
			if (id == 11827)
				break;
		}
		
		Iterator<Integer> it = list.iterator();
		int total = list.size(), counter = 0, per = 0;
		while (it.hasNext())
		{
			Hunter hunter = new Hunter(it.next());
			while(Thread.activeCount() > 100) Thread.sleep(10);
			hunter.start();
			++counter;
			if (counter*100/total/2 > per)
			{
				System.out.print("@");
				++per;
			}
		}
		while(Thread.activeCount() != n) Thread.sleep(10);
		
		createCache();
		
		System.out.println();
		
		return problems;
	}
	
	public static void checkCache() throws Exception
	{
		File file = new File("cache");
		if (!file.exists())
			return;
			
		String content = Getter.getLocalData("cache");
		Scanner scanner = new Scanner(content);
		problems = new HashMap<Integer, Problem>();
		
		while (scanner.hasNext())
		{	
			int id = scanner.nextInt();
			int acc = scanner.nextInt();
			String url = scanner.next();
			float per = scanner.nextFloat();
			Problem problem = new Problem(id, acc, url, per);
			problems.put(id, problem);
		}
	}
	
	public static void createCache() throws Exception
	{
		FileWriter fstream = new FileWriter("cache");
        	BufferedWriter out = new BufferedWriter(fstream);
        	
        	Iterator<Problem> it = problems.values().iterator();
        	while (it.hasNext())
        	{
        		Problem problem = it.next();
        		out.write("" + problem.getId() + " " + problem.getAcc() + " " + problem.getUrl() + " " + problem.getPer() + "\n");
        	}
        	
        	out.close();
	}
	
	public static void main(String[] args){
		try{
			getProblems(false);
		}
		catch(Exception e){}
	}
}

class Hunter extends Thread{
	private int id;

	public Hunter(int id){
		this.id = id;	
	}

	public void run(){		
		try{
			BufferedReader reader = Getter.getBufferedData("http://uvatoolkit.com/gotouvastats.php?id=" + id);
			String in = null;
			while ((in = reader.readLine()) != null)
			{
				if (in.indexOf("sectiontableentry1") != -1)
					break;
			}
			String mount = "";
			mount += reader.readLine() + "\n";
			mount += reader.readLine() + "\n";
			mount += reader.readLine() + "\n";
			reader.close();
			
			Scanner sc2 = new Scanner(mount);
			String a = sc2.nextLine();
			int i = a.indexOf('>'); int j = a.indexOf('<', i);
			int total = Integer.parseInt(a.substring(i + 1, j));
			a = sc2.nextLine();
			i = a.indexOf('>'); j = a.indexOf('<', i);
			int tried = Integer.parseInt(a.substring(i + 1, j));
			a = sc2.nextLine();
			i = a.indexOf('>'); j = a.indexOf('<', i);
			int solved = Integer.parseInt(a.substring(i + 1, j));
			//System.out.println(id + " " + total + " " + tried + " " + solved);
			String url = ToolKit.EXTERNAL_URL + (id / 100) + "/" + id + ".html";
			Problem p = new Problem(id, solved, url, (float) solved/tried);
			ToolKit.addProblem(p);
		}
		catch(Exception e)
		{}
	}
}

import java.util.*;
import java.lang.*;
import java.io.*;

public class Maker
{
	
	public static LinkedList<Problem> makeProblems(HashMap<Integer, Problem> problems, HashSet<Integer> solved, boolean save) throws Exception
	{
		System.out.println("Choosing problems..");
		Iterator<Integer> it = solved.iterator();
		while (it.hasNext())
		{
			int ignore = it.next();
			problems.remove(ignore);
		}
		
		LinkedList<Problem> sorted = new LinkedList<Problem>();
		Iterator<Problem> pb = problems.values().iterator();
		while (pb.hasNext())
		{
			Problem problem = pb.next();
			sorted.add(problem);
			//System.out.println(problem.getDif() + "");
		}
		Collections.sort(sorted, new DifficultyComparator());
	
		LinkedList<Problem> contest = new LinkedList<Problem>();
		
		String config = Getter.getLocalData("parameters");
		Scanner scanner = new Scanner(config);
		
		while (scanner.hasNext())
		{
			int count = scanner.nextInt();
			int low = scanner.nextInt();
			int high = scanner.nextInt();
			Problem tmp1 = new Problem(low);
			Problem tmp2 = new Problem(high);
			int l = Collections.binarySearch(sorted, tmp1, new DifficultyComparator());
			int h = Collections.binarySearch(sorted, tmp2, new DifficultyComparator());
			if (l < 0)
			{
				l += 2;
				l *= -1;
				if (l < 0)
					l = 0;
			}
			if (h < 0)
			{
				h += 2;
				h *= -1;
				if (h < 0)
					h = 0;
			}
			System.out.println("Range for " + low + " - " + high + " has " + (h-l) + " problems");
			if (h - l < count*3)
				h = l + count*3;
			if (h >= sorted.size() || l < 0)
			{
				System.out.println("Bad choice for the difficulty range..");
				continue;
			}
			HashSet<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < count; ++i)
			{
				//System.out.println(low + " " + high + " " + l + " " + h);
				int random = (int) (Math.random() * (h-l));
				if (set.contains(random))
					continue;
				set.add(random);
				Problem got = sorted.get(l + random);
				contest.add(got);
			}
		}
		
		Collections.shuffle(contest);
		
		if (save)
			saveProblems(contest);
		
		return contest;
	}
	
	public static void saveProblems(LinkedList<Problem> problems) throws Exception
	{
		FileWriter fstream = new FileWriter("problems");
        	BufferedWriter out = new BufferedWriter(fstream);
        	
        	Iterator<Problem> it = problems.iterator();
        	while (it.hasNext())
        	{
        		Problem problem = it.next();
        		out.write("" + problem.getUrl() + "\n");
        	}
        	
        	out.close();
	}
}

class DifficultyComparator implements Comparator<Problem>
{
	public int compare(Problem a, Problem b)
	{
		if (a.getDif() > b.getDif())
			return 1;
		if (a.getDif() < b.getDif())
			return -1;
		return 0;
	}
}


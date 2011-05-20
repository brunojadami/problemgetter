import java.util.*;
import java.lang.*;

public class Core
{
	public static void intro()
	{
		System.out.println("\n\n******** ICMC UVa Problem Getter 1.0 ********\n\n");
		System.out.println("Memorize these options, I wont show them again:\n");
		System.out.println("0 - Recreate problems cache (do this first if its your first time)");
		System.out.println("1 - Automaticaly choose problems and save to file");
		System.out.println("Other - Exit");
	}
	
	public static void execute() throws Exception
	{
		intro();
		Scanner scanner = new Scanner(System.in);
		
		while (true)
		{
			System.out.print("\nMake a choice: ");
			String opt = scanner.next();
			if (opt.equals("0"))
			{
				ToolKit.getProblems(true);
				System.out.println("Problems cache created!");
			}
			else if (opt.equals("1"))
			{
				HashSet<Integer> solved = Status.getSolved();
				HashMap<Integer, Problem> problems = ToolKit.getProblems(false);
				Maker.makeProblems(problems, solved, true);
				System.out.println("Problems chosen, they were saved to 'problems' file!");
			}
			else
				break;
		}
		
		System.out.println("\nBye!");
	}
	
	public static void main(String[] args) throws Exception
	{
		execute();
	}
}


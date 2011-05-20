import java.lang.*;

public class Problem 
{
	private int id, acc, dif;
       	private String url;
	private float per;
	
	public Problem(int id, int acc, String url, float per)
	{
		this.id = id;
		this.acc = acc;
		this.url = url;
		this.per = per;
		
		dif = (int) ((per * 100) * (acc / 200.0 + 1));
		dif = Math.min(dif, 1000);
		//System.out.println("" + per + " " + acc);
	}
	
	public Problem(int dif)
	{
		this.dif = dif;
	}
	
	public int getId() { return id; }
	public int getAcc() { return acc; }
	public String getUrl() { return url; }
	public float getPer() { return per; }
	public int getDif() { return dif; }
	
	public String toString()
	{
		String str = "ID: " + id + " URL: " + url;
		return str;
	}
}

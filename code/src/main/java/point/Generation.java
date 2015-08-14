package point;


import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Generation{
	private static Random ram=new Random();
	
	
	public static void main(String[] args)
	{
		POS a=new POS("NCL");
		
		for(int i=0;i<1000;i++)
		{
		String[] str=new String[]{"--customer","{'name':'"+createName()+"'}"};
		a.parse(str);
		}
		
		ArrayList<String> list=customer();
		int size=list.size();
		
		for(int i=0;i<10000;i++)
		{
			String[] str=new String[]{"--order","{'date':'10-12-2014','price':"+ram.nextInt(1000)+",'customer':'"+list.get(ram.nextInt(size))+"'}"};
			a.parse(str);
			
		}
		
	}
	
	public static String createName()
	{
		String name="";
		for(int i=0;i<5;i++)
		{
			int j=ram.nextInt(25)+65;
			name+=(char)j;
		}
		return name;
	} 
	
	
	
	public static ArrayList<String> customer()
	{
		ArrayList<String> list=new ArrayList<String>();
		try
		{
		File file = new File("D:\\workspace\\pos\\file.txt");
		Scanner in=new Scanner(new FileReader(file));
		while(in.hasNext())
		{
			list.add(in.next());
			in.nextLine();
		}
		return list;
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
			return null;
		}
	}
	
	

}

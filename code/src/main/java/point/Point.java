package point;


import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Point{
	
	public static void main(String[] args)
	{
		Scanner reader = new Scanner(System.in);
		System.out.println("Please enter the name of your country");		
		POS a=new POS(reader.nextLine());			
		a.parse(args);		

		
	}
	
	

}

import java.io.*;
import java.util.*;

public class macro_pass_2 {

	public static void main(String[] args) {
		
		try {
		
			BufferedReader br = new BufferedReader(new FileReader("inputmac2.txt"));
			BufferedWriter bw=new BufferedWriter(new FileWriter("pass2_output.txt"));
			BufferedReader br2=new BufferedReader(new FileReader("ALA.txt")); 
			LinkedHashMap<String,String>output=new LinkedHashMap<String,String>();
			String m;
	        String alal;
		    while((alal=br2.readLine())!=null) {
		 	   if(alal.contains("Expanded"))
	    	   {
	   		   String part[]=alal.split("\\s+");
	   		  // System.out.println(part[2]);
	    		String str="";
	   		   while(!((alal=br2.readLine()).contains("ALA")))
	 			   {
	  			    str=str+alal+"\n";  			    
			 		}
			   output.put(part[2],str);
			   }	    	   	    	 
			   }
		    String currline;
		    int flag=0;
		    while((currline=br.readLine())!=null)
		    {
		    	String part[]=currline.split("\\s+");
		    	if(currline.contains("START"))
		    	{
		    		flag=1;
		    	}
		    	if(flag==1)
		    	{
		    		if(output.containsKey(part[1]))
		    		{
		    			bw.write(output.get(part[1]));
		    			System.out.print(output.get(part[1]));
		    		}
		    		else
		    			{
		    			System.out.println(currline);
		    		     bw.write(currline+"\n");
		    			}
		    	}
		    }
		    
			bw.close();
			br.close();
			//br1.close();
			br2.close();
		
		}
		catch(Exception e) {
                  System.out.println("ERROR:"+e);			
		}
	}
}

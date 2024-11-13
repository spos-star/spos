import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter; 
//import java.io.IOException;

public class Pass2asm {
    ArrayList<Tables> SYMTAB,LITTAB;

	public Pass2asm()
	{
		SYMTAB=new ArrayList<>();
		LITTAB=new ArrayList<>();
	}
	public static void main(String[] args) throws Exception {
		Pass2asm pass2=new Pass2asm();
		
		try {
			pass2.generateCode("IC.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
            System.out.println("Error: "+e);
		}
	}
	public void readtables()
	{
		BufferedReader br;
		String line;
		try
		{
			br=new BufferedReader(new FileReader("SYMTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+");

				SYMTAB.add(new Tables(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0]) ));
			}
			br.close();
			br=new BufferedReader(new FileReader("LITTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+");
				LITTAB.add(new Tables(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0])));
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println("Error: "+e);
		}
	}

	public void generateCode(String filename) throws Exception
	{
		readtables();
		BufferedReader br=new BufferedReader(new FileReader(filename));

		BufferedWriter bw=new BufferedWriter(new FileWriter("PASS2.txt"));
		String line,code;
		while((line=br.readLine())!=null)
		{
			if(line.contains("(RG,01)"))
				line = line.replace("(RG,01)", "1");
			if(line.contains("(RG,02)"))
				line = line.replace("(RG,02)", "2");
			if(line.contains("(RG,03)"))
				line = line.replace("(RG,03)", "3");
			//else if(line.contains("(RG,02)"))
			//line = line.replace("(RG,01)", "2");

			String parts[]=line.split("\\s+");
			//System.out.println("l" parts.length);
//			for(String s : parts) {
//
//				System.out.print(s + "-");
//			}
			if(parts[0].contains("IS,00"))
			{
				code="00\t00\t00\n";
				bw.write(code);
				break;
			}
			
			if(parts[0].contains("AD"))
			{
				bw.write("\n");
				continue;
			}
			else if(parts.length==2)
			{
				if(parts[0].contains("DL")) //DC INSTR
				{
					
					parts[0]=parts[0].replaceAll("[^0-9]", "");
//					if(Integer.parseInt(parts[0])==1)
//					{
						int constant=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
						code="00\t00\t"+String.format("%03d", constant)+"\n";
						bw.write(code);	
					//}
//					else if(Integer.parseInt(parts[0])==2)
//					{
//						int constant=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
//						code="00\t00\t"+String.format("%03d", constant)+"\n";
//						bw.write(code);						
//					}
				}
				else if(parts[0].contains("IS"))
				{
					int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
					if(opcode==10)
					{
						if(parts[1].contains("S"))
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddess())+"\n";
							bw.write(code);
						}
						else if(parts[1].contains("L"))
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", LITTAB.get(symIndex-1).getAddess())+"\n";
							bw.write(code);
						}
						
					}
				}
			}
//			
			else if(parts.length==1 && parts[0].contains("IS"))
			{
				int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
				code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", 0)+"\n";
				bw.write(code);
			}
			else if(parts[0].contains("IS") && parts.length==3) //All OTHER IS INSTR
			{
				int opcode=	Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
			
				int regcode= Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
			
				if(parts[2].contains("S"))
				{
					int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
					code=String.format("%02d", opcode)+"\t0"+regcode+"\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddess())+"\n";
					bw.write(code);
				}
				else if(parts[2].contains("L"))
				{
					int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
					code=String.format("%02d", opcode)+"\t0"+regcode+"\t"+String.format("%03d", LITTAB.get(symIndex).getAddess())+"\n";
					bw.write(code);
				}		
			}	
		}
		bw.close();
		printMC();
		br.close();
	}
	void printMC() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("PASS2.txt"));
		String line = br.readLine();
		System.out.println("Machine Code");
		while(line != null)
		{
		  System.out.println(line);
		  line = br.readLine();
		}
		br.close();
	}
}

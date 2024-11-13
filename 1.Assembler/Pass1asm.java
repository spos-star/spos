import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter; 
import java.io.IOException;

public class Pass1asm {
	int lc=0;
	int newptr=0,pre=0;
	int libtab_ptr=0,pooltab_ptr=0;
	int symIndex=0,litIndex=0;
	LinkedHashMap<String, Tables> SYMTAB;
	ArrayList<Tables> LITTAB;
	ArrayList<Integer> POOLTAB;
	private BufferedReader br;

	public Pass1asm()
	{
		SYMTAB =new LinkedHashMap<>();
		LITTAB=new ArrayList<>();
		POOLTAB=new ArrayList<>();
		lc=0;
		POOLTAB.add(0);
	}
	public static void main(String[] args) {
		Pass1asm one=new Pass1asm();
		try
		{
			one.parseFile();
		}
		catch (Exception e) {
			System.out.println("Error: "+e); 
		}
	}
	public void parseFile() throws Exception
	{
		String prev="";
		String line,code;
		br = new BufferedReader(new FileReader("input.txt"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("IC.txt"));
		OPtable lookup=new OPtable();
		//bw.write("Intermediate Code\n\n");
		System.out.println("\nIntermediate Code");
		while((line=br.readLine())!=null)
		{
			
			String parts[]=line.split("\\s+");
			if(!parts[0].isEmpty()) //processing of label
			{
				if(SYMTAB.containsKey(parts[0]))
					SYMTAB.put(parts[0], new Tables(parts[0], lc, SYMTAB.get(parts[0]).getIndex()));
				else
					SYMTAB.put(parts[0],new Tables(parts[0], lc, ++symIndex));
			}

			if(parts[1].equals("LTORG") || parts[1].equals("END"))
			{
				int x=libtab_ptr-newptr;
				if(pre!=0 && pre<libtab_ptr ){
				pooltab_ptr++;
				POOLTAB.add(x);}
				pre=libtab_ptr;
				int ptr=POOLTAB.get(pooltab_ptr);
				for(int j=ptr;j<libtab_ptr;j++)
				{
					LITTAB.set(j, new Tables(LITTAB.get(j).getSymbol(),lc));
					code="(DL,02)\t(C,"+LITTAB.get(j).symbol+")";
					bw.write(code+"\n");
					lc++;
				}
				newptr=0;

				if(parts[1].equals("END"))
				{
					code="(AD,02)";
					bw.write(code+"\n");
					break;
				}

			}
			if(parts[1].equals("START"))
			{
				lc=expr(parts[2]);
				code="(AD,01)\t(C,"+lc+")";
				bw.write(code+"\n");
				prev="START";
			}
			
			if(parts[1].equals("ORIGIN"))
			{
				lc=expr(parts[2]);
				if(parts[2].contains("+"))
				{
					//lc=expr(parts[2]);
					String splits[]=parts[2].split("\\+"); //Same for - SYMBOL //Add code
					int x=SYMTAB.get(splits[0]).getAddess()+Integer.parseInt(splits[1]);
					code="(AD,03)\t(C,"+x+")";
					bw.write(code+"\n");
				}
				else if(parts[2].contains("-"))
				{
					
					String splits[]=parts[2].split("\\-"); //Same for - SYMBOL //Add code
					int x=SYMTAB.get(splits[0]).getAddess()-Integer.parseInt(splits[1]);
					code="(AD,03)\t(C,"+x+")";
					bw.write(code+"\n");
				}
				
			}

			//Now for EQU
			if(parts[1].equals("EQU"))
			{
				int loc=expr(parts[2]);
				if(parts[2].contains("+"))
				{
					String splits[]=parts[2].split("\\+");
					int x=SYMTAB.get(splits[0]).getAddess()+Integer.parseInt(splits[1]);
					code="(AD,04)\t(C,"+x+")";

				}
				else if(parts[2].contains("-"))
				{
					String splits[]=parts[2].split("\\-");
					int x=SYMTAB.get(splits[0]).getAddess()-Integer.parseInt(splits[1]);
					code="(AD,04)\t(C,"+x+")";
				}
				else
				{
					code="(AD,04)\t(C,"+Integer.parseInt(parts[2]+")");
				}
				bw.write(code+"\n");
				if(SYMTAB.containsKey(parts[0]))
					SYMTAB.put(parts[0], new Tables(parts[0],loc,SYMTAB.get(parts[0]).getIndex())) ;
				else
					SYMTAB.put(parts[0], new Tables(parts[0],loc,++symIndex));	 
			}

			if(parts[1].equals("DC"))
			{
				lc++;
				int constant=Integer.parseInt(parts[2].replace("'",""));
				code="(DL,02)\t(C,"+constant+")";
				bw.write(code+"\n");
			}
			else if(parts[1].equals("DS"))
			{
				
				int size=Integer.parseInt(parts[2].replace("'", ""));

				code="(DL,01)\t(C,"+size+")";
				bw.write(code+"\n");
				/*if(prev.equals("START"))
				{
					lc=lc+size-1;//System.out.println("here");
					
				}
				else
*/					lc=lc+size;
				prev="";
			}
			if(lookup.getMnemonic(parts[1]).equals("IS"))
			{
				code="(IS,0"+lookup.getOpcode(parts[1])+")\t";
				int j=2;
				String code2="";
				while(j<parts.length)
				{
					parts[j]=parts[j].replace(",", "");
					if(lookup.getMnemonic(parts[j]).equals("RG"))
					{
						code2+="(RG,0"+lookup.getOpcode(parts[j])+")\t";
					}
					else if(lookup.getMnemonic(parts[j]).equals("CC"))
					{
						code2+="(CC,0"+lookup.getOpcode(parts[j])+")\t";
					}
					else
					{
						if(parts[j].contains("="))
						{
							parts[j]=parts[j].replace("=", "").replace("'", "");
							code2+="(L,"+(litIndex)+")";
							LITTAB.add(new Tables(parts[j], -1,++litIndex));
							libtab_ptr++;
							newptr++;
//							code2+="(L,"+(litIndex)+")";
						}
						else if(SYMTAB.containsKey(parts[j]))
						{
							int ind=SYMTAB.get(parts[j]).getIndex();
							code2+= "(S,0"+ind+")"; 
						}
						else
						{
							SYMTAB.put(parts[j], new Tables(parts[j],-1,++symIndex));
							int ind=SYMTAB.get(parts[j]).getIndex();
							code2+= "(S,0"+ind+")";
						}
					}
					j++;
				}
				lc++;
				code=code+code2;
				bw.write(code+"\n");
			}

		}
		bw.close();
		printIC();
		printSYMTAB();
		//Printing Literal table
		PrintLITTAB();
		printPOOLTAB();
	}
	private String x(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	void printIC() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("IC.txt"));
		String line = br.readLine();
		while(line != null)
		{
		  System.out.println(line);
		  line = br.readLine();
		}
		br.close();
	}
	void PrintLITTAB() throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("LITTAB.txt"));
		System.out.println("\nLiteral Table");
		System.out.println("Index\tLiteral\tAddress");
		//bw.write("\nLiteral Table\n");
		//Processing LITTAB
		for(int i=0;i<LITTAB.size();i++)
		{
			Tables row=LITTAB.get(i);
			System.out.println(i+"\t"+row.getSymbol()+"\t"+row.getAddess());
			bw.write((i+1)+"\t"+row.getSymbol()+"\t"+row.getAddess()+"\n");
		}
		bw.close();
	}
	void printPOOLTAB() throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("POOLTAB.txt"));
		System.out.println("\nPOOLTAB");
		System.out.println("Index\tLitIndex");
		//bw.write("\nPOOLTAB\n");
		//bw.write("Index\t#first\n");
		if(libtab_ptr==0)
		POOLTAB.clear();
		
		for (int i = 0; i < POOLTAB.size(); i++) {
			System.out.println(i+"\t"+POOLTAB.get(i));
			bw.write(i+"\t"+POOLTAB.get(i)+"\n");
		}
		bw.close();
	}
	void printSYMTAB() throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter("SYMTAB.txt"));
		//Printing Symbol Table
		java.util.Iterator<String> iterator = SYMTAB.keySet().iterator();
		System.out.println("\nSYMBOL TABLE");
		System.out.println("Index\tSymbol\tAddress");
		//bw.write("\nSYMBOL TABLE\n");
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			Tables value = SYMTAB.get(key);

			System.out.println(value.getIndex()+"\t" + value.getSymbol()+"\t"+value.getAddess());
			bw.write(value.getIndex()+"\t" + value.getSymbol()+"\t"+value.getAddess()+"\n");
		}
		bw.close();
	}
	public int expr(String str)
	{
		int temp=0;
		if(str.contains("+"))
		{
			String splits[]=str.split("\\+");
			temp=SYMTAB.get(splits[0]).getAddess()+Integer.parseInt(splits[1]);
		}
		else if(str.contains("-"))
		{
			String splits[]=str.split("\\-");
			temp=SYMTAB.get(splits[0]).getAddess()-(Integer.parseInt(splits[1]));
		}
		else
		{
			temp=Integer.parseInt(str);
		}
		return temp;
	}
}

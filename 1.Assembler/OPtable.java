
import java.util.*;

public class OPtable {

	HashMap<String, Integer> AD,RG,IS,CC,DL;
	
	public OPtable() {
		AD=new HashMap<>();
		CC = new HashMap<>();
		IS = new HashMap<>();
		RG = new HashMap<>();
		DL=new HashMap<String, Integer>();
		DL.put("DS",01);
		DL.put("DC", 02);
		IS.put("STOP",00);
		IS.put("ADD",01);
		IS.put("SUB",02);
		IS.put("MULT",03);
		IS.put("MOVER",04);
		IS.put("MOVEM",05);
		IS.put("COMP",06);
		IS.put("BC",07);
		IS.put("DIV",8);
		IS.put("READ",9);
		IS.put("PRINT",10);
		AD.put("START",01);
		AD.put("END",02);
		AD.put("ORIGIN",03);
		AD.put("EQU",04);
		AD.put("LTORG",05);
		RG.put("AREG",01);
		RG.put("BREG",02);
		RG.put("CREG",03);
		CC.put("EQ", 01);
		CC.put("LT", 02);
		CC.put("GT", 03);
		CC.put("LE", 04);
		CC.put("GE", 05);
		CC.put("NE", 06);
		CC.put("ANY",07);
	}
	
	public String getMnemonic(String s)
	{
		s=s.toUpperCase();
		
		if(AD.containsKey(s))
			return "AD";
		else if(IS.containsKey(s))
			return "IS";
		else if(DL.containsKey(s))
			return "DL";
		else if(RG.containsKey(s))
			return "RG";
		else if(CC.containsKey(s))
			return "CC";
		return "";
	}
	
	public int getOpcode(String s)
	{
		s = s.toUpperCase();
		if(AD.containsKey(s))
			return AD.get(s);
		else if(IS.containsKey(s))
			return IS.get(s);
		else if(CC.containsKey(s))
			return CC.get(s);
		else if(DL.containsKey(s))
			return DL.get(s);
		else if(RG.containsKey(s))
			return RG.get(s);
		return -1;
	}

}

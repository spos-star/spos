import java.io.*;
import java.util.*;

public class pass1_macro {

	public static void main(String[] args) {
		
		FileReader fr = null;
		BufferedReader br =  null;		
		try {
			String inputFile ="input_macro.txt";
			fr = new FileReader(inputFile);
			br = new BufferedReader(fr);
			
			LinkedHashMap<String,Integer> MNT = new LinkedHashMap<String,Integer>();
			ArrayList<LinkedHashMap<String,String>> ALA = new ArrayList<LinkedHashMap<String,String>>();
			ArrayList<String> MDT = new ArrayList<String>();
			
			String ALAoutput = "ALA.txt";
			
			FileWriter f2 = null; 
			BufferedWriter b2 = null; 
			
			f2 = new FileWriter(ALAoutput);
			b2 = new BufferedWriter(f2);
			String sCurrentLine;
			int MNTloc = 0;
			int MDTloc = 0; 
			int isMacro = 0;
			int macroLine = 0 ;
			int start = 0;
			while((sCurrentLine = br.readLine()) != null ) {
			
			String[] s = sCurrentLine.split(" ");
    		
			if(s[1].equals("MACRO") && isMacro == 0 ) {
				isMacro +=1 ;
				macroLine = 1;
				continue;
			}	
            if( isMacro != 0 ) {
            	
            	if(s[1].equals("MACRO")) {
            		
            		MDT.add(sCurrentLine);
            		MDTloc++;
    				isMacro +=1 ;
    				continue;
    			}          		
            	if( s[1].equals("MEND") ) {
            		MDT.add(sCurrentLine);
            		MDTloc++;
            		isMacro -= 1; 
            		if( isMacro == 0) {
            		macroLine = 0;
            		MNTloc++;
            		}
            		continue;
            	}           	
            	if( macroLine == 1 ) {
            	//System.out.println(s[1]+" "+sCurrentLine);
            	MNT.put( s[1] , MDTloc );
            	MDT.add(sCurrentLine);
            	LinkedHashMap<String,String> temp = new LinkedHashMap<String,String>();
            	
            	if( s.length >= 3 ) { 
            	
            	String[] ss = s[2].split("\\,");
            	int n = ss.length; 
            	int alapointer = 0;
            	for( int i = 0 ; i<n; i++ ) {
            	           			
            			String alaput = "#" + String.valueOf(alapointer);
            			
            			if( ss[i].indexOf("=") != -1 ) {
            			ss[i] = ss[i].split("=")[0];
            			}
            			
            			temp.put(alaput,ss[i]);
            			alapointer++;
            	           		
            	}
            	if( temp.isEmpty() ) {
            		ALA.add(null);
            	}
            	else {
            		
            		ALA.add(temp);
            		
                 }
            	
            	}
            	
            	MDTloc++;
            	macroLine++;
            	continue;
            	}
            	else {
            		
            		String mdtput = "";
            		String[] ss = sCurrentLine.split(" |\\,");
            		
            		int n = ss.length; 
            		int op = 0; 
            				
            		for( int i = 0; i<n; i++ ) {		
            			if( i >= 1 && i<=2 ) mdtput += " ";
            			else if( i>2 ) mdtput += ",";
            			
            			op = 0;
            			
            			for (Map.Entry<String,String> m : (ALA.get(MNTloc)).entrySet()) {
            				
            				
    						if (m.getValue().equals(ss[i]) ) {
    							
    							
    							mdtput += m.getKey(); 
    							op = 1; 
    						}
    					}
            			
            			if( op == 0 ) { 
            				mdtput += ss[i];
            			}
            			
            			
            			
            		}
            		
            		MDT.add(mdtput);
            		
            		
            		macroLine++; 
            		MDTloc++;
            		continue;
            		
            	}	
			}

			if( start == 1 ) {
				
				
				if( s[1] == "END" ) {
					start = 0 ; 
					continue;
				}
				int isMacroinprog = 0 ;
				int ifmacrofounditsindinMNT = 0 ;
				int ifmacrofounditsaddinMDT = 0;
				
				for (Map.Entry<String,Integer> m : MNT.entrySet() ) {
					if (m.getKey().equals(s[1]) ) {				
						
						ifmacrofounditsaddinMDT = m.getValue();
						isMacroinprog = 1;
					//	System.out.println("AAAAYYYYEEEAAAHHHH!!!!");	
						break;
						
						
					}
					
					ifmacrofounditsindinMNT++;
				}		
			if( isMacroinprog == 1) {
					
				    String[] s2 = (MDT.get(ifmacrofounditsaddinMDT)).split(" ");
				    ifmacrofounditsaddinMDT++;
				    LinkedHashMap<String,String> ma = new LinkedHashMap<String,String>(); 
				    
				    if( s2.length >= 3 ) {
				    	
					String[] ss = s[2].split("\\,");
				    String[] sss = s2[2].split("\\,");
				    
				    int n = sss.length; 
				    int ml = ss.length;
				    
				    for( int i = 0 ; i<n; i++ ) {
				    	
				    	String tem = "" ;
				    	if( ml==0 ) {
				    		
				    		tem = sss[i].split("\\=")[1];
				    		sss[i] =  sss[i].split("\\=")[0];
				    		
				    	}
				    	else {
				    		
				    		tem = ss[i];
				    		--ml;
				    	
				    	}
				    	for (Map.Entry<String,String> m : (ALA.get(ifmacrofounditsindinMNT)).entrySet() ) {
							if ( m.getValue().equals(sss[i]) ) {				
									ma.put(m.getKey(),tem);								
									sss[i] = tem;
							}
						}
				    }
				    
				    b2.write("ALA for Macro: " + s2[1] + "\n");
				    
				    b2.write( s2[1] + " " );
				    for( int i = 0 ; i<sss.length;  i++  ) {
				    	b2.write(sss[i]);
				    	if( i!= sss.length - 1 ) {
				    		b2.write(",");
				    	}
				    	else {
				    		b2.write("\n");
				    	}
				    }
				    
				    
				    for (Map.Entry<String,String> m : ma.entrySet() ) {
						b2.write( m.getValue() + "\n");
					}
				    
				    b2.write("Expanded Code: "+s2[1]+"\n");
				    }
				    
				    
				    String csCurrentLine;
				    int cisMacro = 0; 
				    int cmacroLine = 0 ;
				    
				    
				    while( isMacroinprog == 1 ) {
				    	
				    	csCurrentLine = MDT.get(ifmacrofounditsaddinMDT);
				    	String[] cs = csCurrentLine.split(" ");
				    	
				    	if(  cs[1].equals("MACRO") && cisMacro == 0) {
				    		
				    	
				    		cisMacro = 1; 
				    		cmacroLine = 1;
				    		ifmacrofounditsaddinMDT++;				    		
				    		continue;
				    	
				    	}
				    	if( cs[1].equals("MEND") && cisMacro==0 ) {
		            		break;
		            	}
				    	
				    	String tput = "";
	            		String[] sts = csCurrentLine.split(" |\\,");
	            		
	            		int n1 = sts.length; 
	            		int op1 = 0; 
	            				
	            		for( int i = 0; i<n1; i++ ) {
	            			
	            			if( i >= 1 && i<=2 ) tput += " ";
	            			else if( i>2 ) tput += ",";
	            			
	            			op1 = 0;
	            			
	            			for (Map.Entry<String,String> m : ma.entrySet()) {
	            				
	    									
	            				if( m.getKey().equals(sts[i])) {
	    	    							tput += m.getValue();
	    	    							
	    	    							op1 = 1; 
	            				}
	    	    						
	    	    					
	    						
	    					}
	            			
	            			if( op1 == 0 ) { 
	            				tput += sts[i];
	            			}
	            			
	            			
	            			
	            		}
	            		b2.write(tput + "\n");
				    	
				    	
				    	if( cisMacro == 1) {	
				    		if( cs[1].equals("MEND") ) {
			            		MDT.add(csCurrentLine);
			            		MDTloc++;
			            		cisMacro = 0; 
			            		cmacroLine = 0;
			            		MNTloc++;
			            		break;
			            	}
				    		
				    	if( cmacroLine == 1) {
				    		
				    		LinkedHashMap<String,String> temp = new LinkedHashMap<String,String>();
				    		
				    		for (Map.Entry<String,String> m : ma.entrySet() ) {
								if ( m.getKey().equals(cs[1]) ) {				
										cs[1] = m.getValue();								
								}
							}
				    		
				    		MNT.put( cs[1] , MDTloc );
				    		int n = cs.length;
				    		String putsinmdt = "";
				    		for( int i = 0; i<n; i++ ) {
				    			
				    			if( i!=0 ) putsinmdt += " ";
				    			putsinmdt += cs[i];
				    		}
			            	MDT.add(putsinmdt);
			            	
			            	if( cs.length >= 3 ) { 
			            	
			            	String[] ss = cs[2].split("\\,");
			            	n = ss.length; 
			            	int alapointer = 0;
			            	for( int i = 0 ; i<n; i++ ) {
			            	           			
			            			String alaput = "#" + String.valueOf(alapointer);
			            			if( ss[i].indexOf("\\=") != -1 ) {
			            			ss[i] = ss[i].split("\\=")[0];
			            			}
			            			temp.put(alaput,ss[i]);
			            			alapointer++;
			            	           		
			            	}
			            	
			            	if( temp.isEmpty() ) {
		                		ALA.add(null);
		                	}
		                	else {
		                		ALA.add(temp);
		                	}
			            	
			            	}
			            	
			            	MDTloc++;
			            	cmacroLine++;
			            	ifmacrofounditsaddinMDT++;
			            	
			            	continue;
				    		
				    		
				    	}
				    	else {
				    		
				    		String mdtput = "";
		            		String[] ss = csCurrentLine.split(" |\\,");
		            		
		            		int n = ss.length; 
		            		int op = 0; 
		            				
		            		for( int i = 0; i<n; i++ ) {
		            			
		            			if( i >= 1 && i<=2 ) mdtput += " ";
		            			else if( i>2 ) mdtput += ",";
		            			
		            			op = 0;
		            			
		            			for (Map.Entry<String,String> m : (ALA.get(MNTloc)).entrySet()) {
		    						if (m.getValue() == ss[i] ) {				
		    							mdtput += m.getKey();
		    							op = 1; 
		    						}
		    					}
		            			
		            			if( op == 0 ) { 
		            				mdtput += ss[i];
		            			}
		            		}
		            		
		            		MDT.add(mdtput);
		            		
		            		
		            		cmacroLine++; 
		            		MDTloc++;
		            		ifmacrofounditsaddinMDT++;
		            		continue;
				    	}
				    	
				    	}// if cismacro==1;
				    	
				   
				    	ifmacrofounditsaddinMDT++;		
				    } // while loop
				}
				else {
					continue;
				}	
			}
			if( s[1].equals("START") ) {
				start = 1;
				continue;
			}
			}
			
			FileWriter f = null; 
			BufferedWriter b = null; 
			
			String MNToutput = "MNT.txt";
			f = new FileWriter(MNToutput);
			b = new BufferedWriter(f);
			
			ArrayList<String> arr = new ArrayList<String>();
			ArrayList<Integer> arr2 = new ArrayList<Integer>();
			int k=0;
			for (Map.Entry<String,Integer> m : MNT.entrySet() ) {
				b.write(k+"\t\t"+m.getKey() + "\t" + m.getValue()+"\n");
				arr.add(m.getKey());
				arr2.add(m.getValue());
				k++;
				
			}
			
			FileWriter f1 = null; 
			BufferedWriter b1 = null; 
			
			String MDToutput ="MDT.txt" ;
			 f1 = new FileWriter(MDToutput);
			 b1 = new BufferedWriter(f1);
			
			
			int n = MDT.size();
			
			for( int i = 0 ; i<n; i++ ) {
				
				b1.write(i+"\t\t"+MDT.get(i) + "\n");
				
			}
			b2.write("ALA");
			br.close();
			b.close();
			b1.close();
			b2.close();
		}
		 catch ( IOException e) {
			e.printStackTrace();
		}
	}
}


import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Edittree {
	public static String A = "fjbifabdcjadjbsldfkjdfdfidbjkhadsbdahbduaudhskahd";
	public static String B = "dbkasjkdshfjlsx dnkjdnkakhdsicdijshifsdchdsbcysdyhcis";
	public static StringBuilder PatchedA;
	public static ArrayList<String> R = new ArrayList<>();
	public static ArrayList<String> M = new ArrayList<>();
	public static ArrayList<String> S = new ArrayList<>();
	public static ArrayList<String> V = new ArrayList<>();
	public static ArrayList<String> N = new ArrayList<>();
	public static ArrayList<String> Char = new ArrayList<>();
	public static StringBuilder xml = new StringBuilder() ;
	public static boolean bool;
	public static double one  =1;
	public static double top,diag,side = 10000;
	
	public static void main(String[] args) throws Exception {

		R.add("G"); R.add("A");
		
		M.add("A"); M.add("C");
		
		S.add("G"); S.add("C");
		
		V.add("G"); V.add("A");V.add("C");
		
		N.add("G"); N.add("U");N.add("A");N.add("C");
		
		Char.add("R"); Char.add("M");Char.add("S");Char.add("V");Char.add("N");
		
		String[][] AB = new String[B.length()+2][A.length()+2];
		PatchedA = new StringBuilder(A);
		xml.append("<?xml version = \"1.0\"?>" + "\n");
		xml.append("<Instructions>" + "\n");
		
		addtomatrix(AB, A,B);
		fillmatrixzero(AB);
		computeedittree(AB);
		printmatrix(AB);
		EditScript(AB,B.length()+1,A.length()+1);
		//System.out.println(editscript.toString());
		xml.append("</Instructions>");
		SaveScriptToxml(xml.toString());
		ScriptPatching();	System.out.println(xml.toString());

		System.out.println(PatchedA.toString());
		
	}
	static int counter=0;

	static StringBuilder editscript = new StringBuilder();
	public static void EditScript(String[][] matrix, int j, int i) {
		
		// find the minimum between surrounding
		//if min is at top:del ; diag: update; side:insert
		// recursion on the newly found cell
		
		side = top = diag = 10000;
		if(i>=1 && j>=1) {
			
		if(i==1 && j!=1) {
			editscript.append("Insert:"+(i-2)+":"+B.charAt(j-2) +"\n");
			xml.append("\t");
			xml.append("<Instruction>");
			xml.append("Insert;"+(i-2)+":"+B.charAt(j-2));
			xml.append("</Instruction>" + "\n");
			j=j-1;
			EditScript(matrix,j,i);
			
		}
		if(j==1 && i!=1) {
			editscript.append("Delete:"+(i-2) +"\n");
			xml.append("\t");
			xml.append("<Instruction>");
			xml.append("Delete;"+(i-2));
			xml.append("</Instruction>" + "\n");
			i=i-1;
			EditScript(matrix,j,i);

		}
		if(i==1 && j==1) {
			
		}
		if(i>=2 && j>=2) {
		try {		
		 side = Double.parseDouble(matrix[j-1][i]) ;
		 top = Double.parseDouble(matrix[j][i-1]);
		 diag = Double.parseDouble(matrix[j-1][i-1]);
		}
		catch(Exception e) {
		}
		double min = Math.min(side, top);
			   min = Math.min(min, diag);
		   
	    if(min == side && min != diag) {
	    	editscript.append("Insert:"+(i-2)+":" +B.charAt(j-2) +"\n" );
	    	xml.append("\t");
	    	xml.append("<Instruction>");
			xml.append("Insert;"+(i-2)+":" +B.charAt(j-2));
			xml.append("</Instruction>" + "\n");
	    	j=j-1;
		    EditScript(matrix,j,i);

	    }
	    if(min == top && min !=diag) {
	    	editscript.append("Delete:" + (i-2) +"\n");
	    	xml.append("\t");
	    	xml.append("<Instruction>");
			xml.append("Delete;" + (i-2));
			xml.append("</Instruction>" + "\n");
	    	i=i-1;
		    EditScript(matrix,j,i);

	    }
	   
	    
	    if(min == diag && diag == Double.parseDouble(matrix[j][i])) {
	    	i=i-1;j=j-1;
		    EditScript(matrix,j,i);

	    }
	    if(min == diag && diag!= Double.parseDouble(matrix[j][i])) {
	    	editscript.append("Update:" + (i-2) +":"+B.charAt(j-2) + "\n");
	    	xml.append("\t");
	    	xml.append("<Instruction>");
			xml.append("Update;" + (i-2) +":"+B.charAt(j-2));
			xml.append("</Instruction>" + "\n");
	    	i=i-1;j=j-1;
		    EditScript(matrix,j,i);

	    }
		}
		
		}
		}
			
		
	
	
	
	  public static void computeedittree(String[][] matrix) { matrix[1][1] = "0";
	  
	  for(int i=2;i<A.length()+2;i++) {
		  matrix[1][i] = (Double.sum((Double.parseDouble(matrix[1][i-1].toString())),one)) +"";
	  }
	  for(int j=2;j<B.length()+2;j++) {
		  matrix[j][1] = (Double.sum(Double.parseDouble(matrix[j-1][1].toString()),1)) +"";
	  }  
	  
	  for(int i=2;i<A.length()+2;i++) {
		  for(int j=2;j<B.length()+2;j++) {
			  double min = Math.min(Double.parseDouble(matrix[j-1][i-1])+updatecost(j,i,matrix), Double.parseDouble(matrix[j][i-1])+1);
		  matrix[j][i] = Math.min(min, Double.parseDouble(matrix[j-1][i])+1) +"";
		  }
	  }
	//  System.out.println(matrix[4][9]);
	  }
	 
	
	  
	private static double updatecost(int j, int i, String[][] matrix) {
		double costtoreturn = 1;
		if(matrix[0][i].equals(matrix[j][0]))  {
			costtoreturn = 0;
		}
		//boolean ans = Char.contains("R");
		if(Char.contains(matrix[0][i]) && Char.contains(matrix[j][0])) {
			 int index1 = Char.indexOf(matrix[0][i])+1;
			int index2 = Char.indexOf(matrix[j][0])+1;
			
			if((index1 == 1 && index2 ==2) ||(index1==2 && index2==1) ) {
				costtoreturn = 0.75;
			}
			if((index1 == 1 && index2 ==3)|| index1 ==3 && index2==1) {
				costtoreturn = 0.75;
			}
			if((index1 == 1 && index2 ==4)||(index1 ==4 && index2==1)) {
				costtoreturn = 0.67;
			}
			if((index1 == 1 && index2 ==5)||(index1==5) && index2==1) {
				costtoreturn = 0.75;
			}
			if((index1 == 2 && index2 ==3) ||(index1==3 && index2==2) ) {
				costtoreturn =0.75 ;
			}
			if((index1 == 2 && index2 ==4) ||(index1==4 && index2==2) ) {
				costtoreturn =0.67 ;
			}
			if((index1 == 2 && index2 ==5) ||(index1==5 && index2==2) ) {
				costtoreturn =0.75 ;
			}
			if((index1 == 3 && index2 ==4) ||(index1==4 && index2==3) ) {
				costtoreturn =0.67 ;
			}
			if((index1 == 3 && index2 ==5) ||(index1==5 && index2==3) ) {
				costtoreturn =0.75 ;
			}
			if((index1 == 4 && index2 ==5) ||(index1==5 && index2==4) ) {
				costtoreturn =0.75 ;
			}
		}
		 if(Char.contains(matrix[0][i]) && Char.contains(matrix[j][0])==false) {

			int index = Char.indexOf(matrix[0][i]) +1;
			if(index == 1 && R.contains(matrix[j][0])) {
				costtoreturn = 0.5;
			}
			if(index == 2 && M.contains(matrix[j][0])) {
				costtoreturn = 0.5;
			}
			if(index == 3 && S.contains(matrix[j][0])) {
				costtoreturn = 0.5;
			}
			if(index == 4 && V.contains(matrix[j][0])) {
				costtoreturn = 0.67;
			}
			if(index == 5 && N.contains(matrix[j][0])) {
				costtoreturn = 0.75;
			}
		}
		 if(Char.contains(matrix[0][i])==false && Char.contains(matrix[j][0])) {
			int index3 = Char.indexOf(matrix[j][0]) +1;
			if(index3 == 1 && R.contains(matrix[0][i])) {
				costtoreturn = 0.5;
			}
			if(index3 == 2 && M.contains(matrix[0][i])) {
				costtoreturn = 0.5;
			}
			if(index3 == 3 && S.contains(matrix[0][i])) {
				costtoreturn = 0.5;
			}
			if(index3 == 4 && V.contains(matrix[0][i])) {
				costtoreturn = 0.67;
			}
			if(index3 == 5 && N.contains(matrix[0][i])) {
				costtoreturn = 0.75;
			}
		}
		
		return costtoreturn;
	}



	public static void printmatrix(String[][] matrix) {
		matrix[1][1] ="0";
		matrix[0][0] = "";
		matrix[1][0] = "";
		matrix[0][1] = "";
		for(int i=0; i<A.length()+2;i++) {
			for(int j=0; j<B.length()+2;j++) {
				System.out.print("\t");
				System.out.print(matrix[j][i]);
			}
			System.out.println("\n");
		}
	}
	public static void fillmatrixzero(String[][] matrix) {
		for(int i=1;i<A.length()+2;i++) {
			for(int j=1;j<B.length()+2;j++) {
				matrix[j][i] = 0 +"";
			}
		}
	}
	
	public static void addtomatrix(String[][] matrix, String A1, String B1) {
	for(int i=0;i<A1.length();i++) {
		matrix[0][i+2] = A1.charAt(i)+"";
	//	System.out.println("done");
	}
	for(int j=0;j<B1.length();j++) {
		
		matrix[j+2][0] = B1.charAt(j)+"";
		//System.out.println("done");
	}
}
	public static void SaveScriptToxml(String xmlSource) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

	    // Write the parsed document to an xml file
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    DOMSource source = new DOMSource(doc);

	    StreamResult result =  new StreamResult(new File("Script.xml"));
	    transformer.transform(source, result);	
	    System.out.println("File Saved Successfuly");
	}
	public static void ScriptPatching() {
		  try {
		         File inputFile = new File("Script.xml");
		         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		         Document doc = dBuilder.parse(inputFile);
		         doc.getDocumentElement().normalize();
		         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		         NodeList nList = doc.getElementsByTagName("Instruction");
		         System.out.println("----------------------------");
		         
		         for (int temp = 0; temp < nList.getLength(); temp++) {
		            Node nNode = nList.item(temp);
		           
		            // System.out.println("RNA sequence" + nNode.getTextContent());
		            
		            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		               Element eElement = (Element) nNode;
		              
		              String script = eElement.getTextContent();
		              ExecuteScript(script);
		            }
		         }
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
	}
	public static void ExecuteScript (String script) {
		int instructionindex = script.indexOf(";");
		String instruction = script.substring(0,instructionindex);
		System.out.println(instruction);
		if(instruction.equals("Update")) {
			int index = Integer.parseInt(script.substring(instructionindex+1, script.lastIndexOf(":")));
			char replacechar = script.charAt(script.lastIndexOf(":")+1);
			PatchedA.setCharAt(index, replacechar);
		}
		if(instruction.equals("Delete")) {
			int index = Integer.parseInt(script.substring((instructionindex)+1));
			PatchedA.deleteCharAt(index);
		}
		if(instruction.equals("Insert")) {
			int index = Integer.parseInt(script.substring(instructionindex+1, script.lastIndexOf(":")));
			char insertchar = script.charAt(script.lastIndexOf(":")+1);
			System.out.println("index"+index);
			
				PatchedA.insert(index+1, insertchar);

			
			
		}
	}
}

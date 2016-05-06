package auiTest.GA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

class JavaWriteFile {
	public void WriteFile(ArrayList<Integer> a,ArrayList<Double> b,ArrayList<Double> c){
		try{
			// Create file 
			FileWriter fstream = new FileWriter("out.csv");
			BufferedWriter out = new BufferedWriter(fstream);
			for(int i=0;i<a.size();i++){
				out.write(a.get(i)+","+b.get(i)+","+c.get(i)+"\n");
			}
			
			
			//Close the output stream
			out.close();
			}catch (Exception e){//Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
	}
}
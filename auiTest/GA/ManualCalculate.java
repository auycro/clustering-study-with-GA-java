package auiTest.GA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ManualCalculate {
	public static int chargeStationNum = 8;
	public static double maxEletric = 7000;
	public static String dataFile = "StationData22.csv";
	public static String orderFile = "StationOrder22.txt";
	
	
	
	
	public static void main(String[] args) throws Exception {
		StationSwarm stationFactory = new StationSwarm();
		ManualCalculate manCalculate = new ManualCalculate();
		
		//Read File
		Station[] constantStation = manCalculate.initializeReplaceStation(dataFile, "./FileStore/"); //GENERATE STATIC STATION
		//int ReplaceStationNumber = constantStation.length;
		
		//Gen chageStation
		Station[] chargeStationList = new Station[chargeStationNum];
		
		for (int k=0;k < chargeStationList.length;k++){
			chargeStationList[k] = new Station(k);
		}
		
		manCalculate.setChargeStation(constantStation);
		
		stationFactory.SetChargeStationLocation(chargeStationNum, constantStation, chargeStationList, maxEletric);
		
		//manCalculate.calculateObj(constantStation, chargeStationList);
		
		String[] assignStation =  manCalculate.assignStation(chargeStationNum, constantStation);
		
		for(int i=0;i<chargeStationNum;i++){
			System.out.print(chargeStationList[i].getX()+"\t");
			System.out.print(chargeStationList[i].getY()+"\t");
			System.out.print(chargeStationList[i].getElectric()+"\t");
			System.out.print(assignStation[i]);
			System.out.println();
		}
	}
	
	public void setChargeStation(Station[] replaceStationList) throws Exception{
		BufferedReader stationOrderFile = new BufferedReader(new FileReader("./FileStore/"+orderFile));
		String dataRow = stationOrderFile.readLine(); // Read header line.
		
		if(dataRow != null){
			String[] dataArray = dataRow.split(" ");
		
			for(int i=0;i<dataArray.length;i++){
				replaceStationList[i].setParent(Integer.parseInt(dataArray[i]));
			}
		}else{
			System.out.println("file = Null");
		}
		
		// Close the file once all data has been read.  
		stationOrderFile.close();
	}

	public double calculateObj(Station[] replaceStationList,Station[] chargeStationList){
		double result = 0;
		
		for(int k=0;k<replaceStationList.length;k++){
			
			//SetChargeStationLocation(chargeStationList.length, replaceStationList, chargeStationList, 7000);
			
			Station parent = chargeStationList[replaceStationList[k].getParent()];
			Station child = replaceStationList[k];
			
			double difX = parent.getX() - child.getX();
			double difY = parent.getY() - child.getY();
			
			//result =  result + (Math.sqrt((difX*difX)+(difY*difY)))*penaltyFactor;
			result =  result + (Math.sqrt((difX*difX)+(difY*difY)));
		}
		return result;
	}

	public String[] assignStation(int chargeStationNum,Station[] replaceStationList){
		String[] result = new String[chargeStationNum];
		
		for(int i = 0;i<chargeStationNum;i++){
			result[i] = "";
		}
		
		for(int i = 0;i<replaceStationList.length;i++){
			result[replaceStationList[i].getParent()] = result[replaceStationList[i].getParent()]+replaceStationList[i].getNumber()+",";
		}
		
		return result;
	}
	
	public Station[] initializeReplaceStation(String FileName,String FolderName) throws Exception{
		//generate data
		ArrayList<Station> tempList = new ArrayList<Station>();
		
		BufferedReader CSVFile =         new BufferedReader(new FileReader(FolderName+FileName));
		String dataRow = CSVFile.readLine(); // Read header line.
		
		if(dataRow != null){
			
			dataRow = CSVFile.readLine(); // Read next line of data.
			while (dataRow != null){	
				String[] dataArray = dataRow.split(",");
				
				Station tempStation = new Station();
				
				tempStation.setNumber(Integer.parseInt(dataArray[0]));
				tempStation.setX(Double.parseDouble(dataArray[1]));
				tempStation.setY(Double.parseDouble(dataArray[2]));
				tempStation.setElectric(Double.parseDouble(dataArray[3]));				    
				
				tempList.add(tempStation);
				
				dataRow = CSVFile.readLine(); // Read next line of data.  
			
			} 
		}
		
		// Close the file once all data has been read.  
		CSVFile.close();
		
		//convert ArrayList to Array
		Station[] replaceStationList = new Station[tempList.size()];
		tempList.toArray(replaceStationList);
		
		return replaceStationList;
	}
}

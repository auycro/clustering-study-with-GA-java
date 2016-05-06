package auiTest.GA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class StationSwarm {
	Random random = new Random();
	String filename = "StationData80.csv";
	
	public Station[] initializeReplaceStation() throws Exception{
		//generate data
		ArrayList<Station> tempList = new ArrayList<Station>();
		
		BufferedReader CSVFile =         new BufferedReader(new FileReader(filename));
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

	public boolean initialChargeStation(Station[][] chargeStationList,int ParticleSize){
		//chargeStationList = new Station[ParticleSize][ChargeStationNum];
		
		for(int j=0;j<ParticleSize;j++){
			for (int k=0;k < chargeStationList[j].length;k++){
				chargeStationList[j][k] = new Station(k);
			}
		}
		
		return true;
	}
	
	public int calculateChargeStationNumber(Station[] replaceStationList,double EletricPerChargeStation){
		double SumElectric = 0;
		int result;
		
		//calculate Electric Quantity
		for (int i=0;i<replaceStationList.length;i++){
			SumElectric = SumElectric + replaceStationList[i].getElectric();
		}
		result = (int) Math.round((SumElectric/EletricPerChargeStation)+0.5)+3;
		
		return result;
	}

	public void initialState(int ParticleSize,int ReplaceStationNum,double MaxElectric,Station[] constantReplaceStationList,Station[][] chargeStationList,Station[][] replaceStationList) {
		initialChargeStation(chargeStationList, ParticleSize);
		
		for (int j=0;j < ParticleSize;j++){
			
			for(int k=0;k < constantReplaceStationList.length;k++){
				replaceStationList[j][k] = new Station(constantReplaceStationList[k]);
				
				//Random Charge Station
	    		//RandomChargeStation(chargeStationList[j].length, replaceStationList[j][k], chargeStationList[j]);
			}
			
			//For initialize
			
			int P1 = random.nextInt(ReplaceStationNum);
			int P2 = random.nextInt(chargeStationList[j].length);
			int P2_constant = P2;
			
			int[] assigned = new int[ReplaceStationNum];
			
			for(int k=0;k<ReplaceStationNum;k++){
				assigned[k] = -1;
			}
			
			//Loop
			while(P2<chargeStationList[j].length){
				for(int k=P1;k<ReplaceStationNum;k++){
					
					double tempElec = chargeStationList[j][P2].getElectric() + replaceStationList[j][k].getElectric();
					
					if((tempElec<MaxElectric)&&(assigned[k]==-1)){
						
						chargeStationList[j][P2].setElectric(tempElec);
						replaceStationList[j][k].setParent(P2);
						assigned[k] = P2;
						
					}
				}
				
	
				for(int k=0;k<P1;k++){
					
					double tempElec = chargeStationList[j][P2].getElectric() + replaceStationList[j][k].getElectric();
					
					if((tempElec<MaxElectric)&&(assigned[k]==-1)){
						
						chargeStationList[j][P2].setElectric(tempElec);
						replaceStationList[j][k].setParent(P2);
						assigned[k] = P2;
						
					}
					
				}
				P2++;
			}
			
			//Loop
			int temp_count = 0;
			while(temp_count<P2_constant){
				for(int k=P1;k<ReplaceStationNum;k++){
					
					double tempElec = chargeStationList[j][temp_count].getElectric() + replaceStationList[j][k].getElectric();
					
					if((tempElec<MaxElectric)&&(assigned[k]==-1)){
						
						chargeStationList[j][temp_count].setElectric(tempElec);
						replaceStationList[j][k].setParent(temp_count);
						assigned[k] = temp_count;
						
					}
				}
				
	
				for(int k=0;k<P1;k++){
					
					double tempElec = chargeStationList[j][temp_count].getElectric() + replaceStationList[j][k].getElectric();
					
					if((tempElec<MaxElectric)&&(assigned[k]==-1)){
						
						chargeStationList[j][temp_count].setElectric(tempElec);
						replaceStationList[j][k].setParent(temp_count);
						assigned[k] = temp_count;
						
					}
					
				}
				temp_count++;
			}
			
    		SetChargeStationLocation(chargeStationList[j].length, replaceStationList[j], chargeStationList[j],MaxElectric);
	    }
	}

	public boolean RandomChargeStation(int ChargeStationNum,Station replaceStation,Station[] chargeStationList){
		int randomNumber = random.nextInt(ChargeStationNum);
		
		replaceStation.setParent(randomNumber);
		
		return true;
	}
	
	public boolean SetChargeStationLocation(int ChargeStationNum,Station[] replaceStation,Station[] chargeStationList,double maxEletric){
		
		double[] sumLocationX = new double[ChargeStationNum];
		double[] sumLocationY = new double[ChargeStationNum];
		double[] sumElectric = new double[ChargeStationNum];
		
		for (int k=0;k < replaceStation.length;k++){
			int parent = replaceStation[k].getParent();
			
			sumElectric[parent] += replaceStation[k].getElectric();
			sumLocationX[parent] += (replaceStation[k].getX() * replaceStation[k].getElectric());
			sumLocationY[parent] += (replaceStation[k].getY() * replaceStation[k].getElectric());
		}

		for	(int k=0;k < chargeStationList.length;k++){
			chargeStationList[k].setElectric(sumElectric[k]);
			
			double locationX = sumLocationX[k]/sumElectric[k];
			double locationY = sumLocationY[k]/sumElectric[k];
			
			chargeStationList[k].setElectric(sumElectric[k]);
			chargeStationList[k].setX(locationX);
			chargeStationList[k].setY(locationY);
			
			if(sumElectric[k] > maxEletric){
				chargeStationList[k].setFeasible(false);
			}
			
		}
		
		//System.out.print(chargeStationList[0].getElectric());
		//System.out.print(","+chargeStationList[1].getElectric());
		//System.out.print(","+chargeStationList[2].getElectric());
		//System.out.println(","+chargeStationList[3].getElectric());
		
		return true;
	}
	
	public int calculateObjValue(int ParticleSize,double eletricPerChargeStation, Station[][] chargeStationList,Station[][] replaceStationList,double[] ObjValue){
		int infeasibilityCount = 0;
		
		for(int j=0;j<ParticleSize;j++){
			double result = 0.0;
			double penaltyFactor = 1.0;
			for(int k=0;k<replaceStationList[j].length;k++){
				
				SetChargeStationLocation(chargeStationList[j].length, replaceStationList[j], chargeStationList[j], 7000);
				
				Station parent = chargeStationList[j][replaceStationList[j][k].getParent()];
				Station child = replaceStationList[j][k];
				
				double difX = parent.getX() - child.getX();
				double difY = parent.getY() - child.getY();
				
				if(parent.getElectric() > eletricPerChargeStation){
					penaltyFactor = -1.0;
					//System.out.println("Parent Electric"+parent.getElectric());
				}
				
				result =  result + (child.getElectric()*(Math.sqrt((difX*difX)+(difY*difY))));
				//result =  result + ((Math.sqrt((difX*difX)+(difY*difY))));
			}
			
			if(penaltyFactor==-1){
				ObjValue[j] = 9999999;
				infeasibilityCount++;
			}else{
				ObjValue[j] = result;
			}
			
		}
		
		return infeasibilityCount;
	}

	public boolean SortByFitnessValue(int ParticleSize,Station[][] replaceStationList,Station[][] chargeStationList,double[] objVal){
		
		for (int j=0;j < ParticleSize-1;j++){
			for (int k=j+1; k < ParticleSize; k++){
				
				if(objVal[j]>objVal[k]){
					double tempObj = objVal[j];
					objVal[j] = objVal[k];
					objVal[k] = tempObj;
				
					Station[] tempReplaceStation = replaceStationList[j];
					replaceStationList[j] = replaceStationList[k];
					replaceStationList[k] = tempReplaceStation;
					
					Station[] tempChargeStation = chargeStationList[j];
					chargeStationList[j] = chargeStationList[k];
					chargeStationList[k] = tempChargeStation;
				}
				
			}
		}
		
		return true;
	}

	public void initialParticle(int ParticleSize,int ReplaceStationNum,double MaxElectric,Station[] constantReplaceStationList,Station[][] chargeStationList,Station[][] replaceStationList,Station[][] tempStation){
		
		initialChargeStation(chargeStationList, ParticleSize);
		
		//Particle Selection
		for (int j=0;j < tempStation.length;j++){
	    	for (int k=0;k < ReplaceStationNum;k++){
	    		replaceStationList[j][k] = new Station(tempStation[j][k]);
	    	}
	    	//Set Charge Station Location
    		SetChargeStationLocation(chargeStationList[j].length, replaceStationList[j], chargeStationList[j],MaxElectric);
	    }
		
	}

	public void ReplaceByTemp(int ParticleSize,int ReplaceStationNum,double MaxElectric,Station[][] chargeStationList,Station[][] replaceStationList,Station[][] tempStation) {
		if(tempStation!=null){
			for (int j=0;j < tempStation.length;j++){
		    	for (int k=0;k < ReplaceStationNum;k++){
		    		replaceStationList[ParticleSize-j-1][k] = new Station(tempStation[j][k]);
		    	}
		    	//Set Charge Station Location
	    		SetChargeStationLocation(chargeStationList[j].length, replaceStationList[j], chargeStationList[j],MaxElectric);
		    }
		}
	}

}

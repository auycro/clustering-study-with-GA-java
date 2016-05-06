//Copyright (c) 2016 Gumpanat Keardkeawfa
//Licensed under the MIT license

package auiTest.GA;

import java.util.Random;

public class GeneticAlgorithm {
	Random random = new Random();
	int cross_no = 2;
	double crossOverRate = 0.5;
	double mutationRate = 0.05;
	double selectionRate = 0.1;
	StationSwarm stationFactory = new StationSwarm();
	
	/* Standard
	 * CrossOver = 2
	 * CrossRate = 0.5
	 * MutationRate = 0.05
	 * Selection = 0.1
	 */
	
	public void Pairing(int particle,int replaceStationNum,int chargeStationNum,Station[][] stationRandomList,Station[][] chargeStationList){
	    
		Station[] temp_gene;
		int temp_in;
		
		for (int j=0;j<particle;j++){
			temp_in = random.nextInt(particle);
				
			temp_gene = stationRandomList[temp_in];
			stationRandomList[temp_in] = stationRandomList[j];
			stationRandomList[j] = temp_gene;
		}
		
	}
	
	public void CrossOver(int particle,int replaceStationNum,int chargeStationNum,Station[][] stationRandomList,Station[][] chargeStationList){
	    
		Station temp_gene_station;
		int temp_in;
		int cross_point;
		
		for (int i=0;i<particle-1;i+=2){
			temp_in = i;
			
			if (random.nextDouble() < crossOverRate) {
				
				for(int k=0;k<cross_no;k++){
					
					cross_point = random.nextInt(replaceStationNum);
					
					for (int j=0;j<cross_point;j++){
						temp_gene_station = stationRandomList[i][j];
						stationRandomList[i][j] = stationRandomList[temp_in+1][j];
						stationRandomList[temp_in+1][j] = temp_gene_station;
					}
					
				}
				
			}
			
		}
	}
	
	public void Mutation(int particle,int replaceStationNum,int chargeStationNum,Station[][] stationRandomList,Station[][] chargeStationList){
		for (int i=0;i<particle;i++){
			for (int j=0;j<replaceStationNum;j++){
				if (random.nextDouble() < mutationRate) {
					int mutationChargeStation = random.nextInt(chargeStationNum);
					
					stationRandomList[i][j].setParent(mutationChargeStation);	
				}
			}
		}

	}
	
	public Station[][] TournamentSelection(int particle,int replaceStationNum,int chargeStationNum,Station[][] stationRandomList){
		int topStationNumber = (int) Math.round(particle*selectionRate);
		Station[][] tempParticleStore = new Station[topStationNumber][replaceStationNum];
		
		for(int top=0;top<topStationNumber;top++){
			for(int k=0;k<replaceStationNum;k++){
				tempParticleStore[top][k] = new Station(stationRandomList[top][k]);
			}
		}
		
		return tempParticleStore;
	}

	
	
}

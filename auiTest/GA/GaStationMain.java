package auiTest.GA;

import java.util.ArrayList;


public class GaStationMain {
	public static int ParticleSize = 100;
	public static double EletricPerChargeStation =7;
	public static int IterationNum = 10000;
	public static int NoChange = 10;
	public static int ShowRate = 100;
	
	/* Standard
	 * Particle size = 100 
	 * ElectricLimit=7
	 * IterNo = 10000
	*/
	
	public static void main(String[] args) throws Exception {
		//Factory Class Create
		StationSwarm stationFactory = new StationSwarm();
		GeneticAlgorithm gaFactory = new GeneticAlgorithm();
		GaStationMain main = new GaStationMain();
		
		//readFile
		Station[] constantStation = stationFactory.initializeReplaceStation(); //GENERATE STATIC STATION
		int ReplaceStationNumber = constantStation.length;
		
		int ChargeStationNumber = stationFactory.calculateChargeStationNumber(constantStation, EletricPerChargeStation); //CALCULATE CHARGE STATION NUMBER
		
		ArrayList ObjList1 = new ArrayList<Double>();
		ArrayList ObjList2 = new ArrayList<Double>();
		ArrayList ObjList3 = new ArrayList<Double>();
		
		//LastValue
		double BestOBJ = 999999;
		int BestIter = 0;
		int BestParticle = 0;
		Station[] BestParticleStationSet = new Station[ReplaceStationNumber];
		long runtime = 0;
		long startTime = System.currentTimeMillis();

		//Iteration Variable
		Station[][][] particleReplaceStation = new Station[IterationNum][ParticleSize][ReplaceStationNumber];
		Station[][][] chargeStationList = new Station[IterationNum][ParticleSize][ChargeStationNumber];
		int[] infeasibilityNumber = new int[IterationNum];
		double[][] objValue = new double[IterationNum][ParticleSize];
		Station[][] tempParticleStore = null; //FOR TOURNAMENT SELECTION
		
		stationFactory.initialState(ParticleSize, ReplaceStationNumber,EletricPerChargeStation, constantStation, chargeStationList[0], particleReplaceStation[0]);
		
		main.displayStation2(particleReplaceStation[0][0], 0);
		System.out.println();
		
		infeasibilityNumber[0] = stationFactory.calculateObjValue(ParticleSize,EletricPerChargeStation, chargeStationList[0], particleReplaceStation[0], objValue[0]);	
		
		System.out.println(infeasibilityNumber[0]);
		
		ObjList1.add(0);
		ObjList2.add(objValue[0]);
		ObjList3.add(infeasibilityNumber[0]);
		
		//add initial state
		
		
		stationFactory.SortByFitnessValue(ParticleSize, particleReplaceStation[0], chargeStationList[0], objValue[0]);
		
		tempParticleStore = gaFactory.TournamentSelection(ParticleSize, ReplaceStationNumber, ChargeStationNumber, particleReplaceStation[0]);
		
		/*
		for(int i=0;i<ParticleSize;i++){
			System.out.print(objValue[0][i]+" ");
		}
		System.out.println();
		*/
		
		//start Run
		for(int i=1;i<IterationNum;i++){
			//clear old value
			if(i>2){
				particleReplaceStation[i-2] = null;
				chargeStationList[i-2] = null;
				objValue[i-2] = null;
			}
			
			stationFactory.initialParticle(ParticleSize, ReplaceStationNumber, EletricPerChargeStation, constantStation, chargeStationList[i], particleReplaceStation[i], particleReplaceStation[i-1]);
			
			gaFactory.Pairing(ParticleSize, ReplaceStationNumber, ChargeStationNumber, particleReplaceStation[i], chargeStationList[i]);
			
			gaFactory.CrossOver(ParticleSize, ReplaceStationNumber, ChargeStationNumber, particleReplaceStation[i], chargeStationList[i]);
			
			gaFactory.Mutation(ParticleSize, ReplaceStationNumber, ChargeStationNumber, particleReplaceStation[i], chargeStationList[i]);
			
			infeasibilityNumber[i] = stationFactory.calculateObjValue(ParticleSize,EletricPerChargeStation, chargeStationList[i], particleReplaceStation[i], objValue[i]);
			
			stationFactory.SortByFitnessValue(ParticleSize, particleReplaceStation[i], chargeStationList[i], objValue[i]);
			/*
			for(int j=0;j<ParticleSize;j++){
				System.out.print(objValue[i][j]+" ");
			}
			System.out.println();
			*/
			stationFactory.ReplaceByTemp(ParticleSize, ReplaceStationNumber,EletricPerChargeStation, chargeStationList[i], particleReplaceStation[i], tempParticleStore);
			
			infeasibilityNumber[i] = stationFactory.calculateObjValue(ParticleSize,EletricPerChargeStation, chargeStationList[i], particleReplaceStation[i], objValue[i]);
			/*
			for(int j=0;j<ParticleSize;j++){
				System.out.print(objValue[i][j]+" ");
			}
			System.out.println();
			*/
			stationFactory.SortByFitnessValue(ParticleSize, particleReplaceStation[i], chargeStationList[i], objValue[i]);
			/*
			for(int j=0;j<ParticleSize;j++){
				System.out.print(objValue[i][j]+" ");
			}
			System.out.println();
			*/
			tempParticleStore = gaFactory.TournamentSelection(ParticleSize, ReplaceStationNumber, ChargeStationNumber, particleReplaceStation[i]);
			/*
			for(int j=0;j<ParticleSize;j++){
				System.out.print(objValue[i][j]+" ");
			}
			System.out.println("-------------");
			*/
			//infeasibilityNumber[i] = stationFactory.calculateObjValue(ParticleSize,EletricPerChargeStation, chargeStationList[i], particleReplaceStation[i], objValue[i]);
			
			for(int j=0;j<ParticleSize;j++){
				if((objValue[i][j]>0)&&(BestOBJ>objValue[i][j])){
					BestOBJ = objValue[i][j];
					BestIter = i;
					BestParticle = j;
					BestParticleStationSet = particleReplaceStation[i][j];			
				}
			}
			
			
			
			runtime = System.currentTimeMillis() - startTime;
			
			int divide = i%ShowRate;
			
			if(divide==0){
				if(BestOBJ!=999999){
					//System.out.println(System.currentTimeMillis() - startIterationTime);
					System.out.print(i+"\t");
					System.out.print(ParticleSize+"\t");
					System.out.print(IterationNum+"\t");
					System.out.print(gaFactory.cross_no+"\t");
					System.out.print(gaFactory.crossOverRate+"\t");
					System.out.print(gaFactory.mutationRate+"\t");
					System.out.print(gaFactory.selectionRate+"\t");
					System.out.print(EletricPerChargeStation+"\t");
					System.out.print(BestOBJ+"\t");
					System.out.print(BestIter+"\t");
					System.out.print(BestParticle+"\t");
					try{
						main.displayStation2(BestParticleStationSet, BestIter);
					}catch(NullPointerException e){
						System.out.println(e);
					}
					System.out.print("\t");
					System.out.print(infeasibilityNumber[i]+"\t");				
					System.out.println(runtime);
				}else{
					System.out.println("cant find good solution");
				}
			}
			
			
			//write file
			ObjList1.add(i);
			ObjList2.add(BestOBJ);
			ObjList3.add(infeasibilityNumber[i]);
			
			
		}// end ITERATION
		
		JavaWriteFile jwf = new JavaWriteFile();
		jwf.WriteFile(ObjList1,ObjList2,ObjList3);
		
		runtime = System.currentTimeMillis() - startTime;
		
		System.out.println("*****Best******");
		if(BestOBJ!=999999){
			System.out.print(ParticleSize+"\t");
			System.out.print(IterationNum+"\t");
			System.out.print(gaFactory.cross_no+"\t");
			System.out.print(gaFactory.crossOverRate+"\t");
			System.out.print(gaFactory.mutationRate+"\t");
			System.out.print(gaFactory.selectionRate+"\t");
			System.out.print(EletricPerChargeStation+"\t");
			System.out.print(BestOBJ+"\t");
			System.out.print(BestIter+"\t");
			System.out.print(BestParticle+"\t");
			try{
				main.displayStation2(BestParticleStationSet, BestIter);
			}catch(NullPointerException e){
				System.out.println(e);
			}
			System.out.print("\t");
			System.out.println(runtime);
		}else{
			System.out.println("cant find good solution");
		}
	
	}
	
	private void displayStation2(Station[] bestParticleStationSet, int bestIter) {
		for(int k=0;k<bestParticleStationSet.length;k++){
			System.out.print(bestParticleStationSet[k].getParent()+" ");
		}
	}
	
}

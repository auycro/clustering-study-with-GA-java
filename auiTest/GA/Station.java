//Copyright (c) 2016 Gumpanat Keardkeawfa
//Licensed under the MIT license

package auiTest.GA;

public class Station {
	private int number;
	private double x;
	private double y;
	private double electric;
	private int parent;
	//private ArrayList<Integer> childs;
	private boolean feasible=true;
	
	public Station(){
		//this.childs = new ArrayList<Integer>();
	}
	
	public Station(int number){
		this.number = number;
		//this.childs = new ArrayList<Integer>();
	}
	
	public Station(Station a){
		this.number = a.getNumber();
		this.x = a.getX();
		this.y = a.getY();
		this.electric = a.getElectric();
		this.parent = a.getParent();
		//this.childs = a.getChilds();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getElectric() {
		return electric;
	}

	public void setElectric(double electric) {
		this.electric = electric;
	}
	
	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public boolean isFeasible() {
		return feasible;
	}

	public void setFeasible(boolean feasible) {
		this.feasible = feasible;
	}

	public String toString(){
		String a = number+","+x+","+y+","+electric; 
		return a;
	}
}

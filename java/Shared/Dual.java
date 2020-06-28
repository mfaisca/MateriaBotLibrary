package Shared;
public final class Dual<O1, O2>{  
	private O1 value1;
	private O2 value2;

	public Dual(O1 v1, O2 v2){
		value1 = v1; value2 = v2;
	}

	public O1 getValue1() { return value1; }
	public O2 getValue2() { return value2; }
	public void setValue1(O1 v) { value1 = v; }
	public void setValue2(O2 v) { value2 = v; }
}
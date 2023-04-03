package Distance;

import java.util.ArrayList;

public class CompareSet {
	public static ArrayList<int[]> compareSet(ArrayList<int[]> aArray,ArrayList<int[]> bArray){
		ArrayList<int[]> common=new ArrayList<int[]>();
		for(int x=0;x<aArray.size();x++){
			for(int y=0;y<bArray.size();y++){
				if(compareArray(aArray.get(x),bArray.get(y))){
					common.add(aArray.get(x));
				}
			}
		}
		return common;
	}
	public static boolean compareArray(int[] c,int[] d){
		if(c.length==d.length){
			for(int i=0;i<c.length;i++){
				if(c[i]!=d[i]){
					return false;
				}
			}
			return true;
		}
		else return false;
	}
}

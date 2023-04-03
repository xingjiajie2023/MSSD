package Distance;

import java.util.ArrayList;

public class CompareSetOne {
	public static ArrayList<ArrayList<Node>> compareSetOne(ArrayList<ArrayList<Node>> aArray,ArrayList<ArrayList<Node>> bArray){
		ArrayList<ArrayList<Node>> common=new ArrayList<ArrayList<Node>>();
		for(int j=0;j<aArray.size();j++){
			for(int k=0;k<bArray.size();k++){
				if(compareArrayOne(aArray.get(j),bArray.get(k))){
					common.add(aArray.get(j));
				}
			}
		}
		return common;
	}
	public static boolean compareArrayOne(ArrayList<Node> cLeaf,ArrayList<Node> dLeaf){
		if(cLeaf.size()!=dLeaf.size()){
			return false;
		}
		else{
			ArrayList<Node> common=new ArrayList<Node>();
			for(int i=0;i<cLeaf.size();i++){
				for(int j=0;j<dLeaf.size();j++){
					if(cLeaf.get(i).value.equals(dLeaf.get(j).value)){
						common.add(cLeaf.get(i));
					}
				}
			}
			if(cLeaf.size()!=common.size()){
				return false;
			}
		}
		return true;
	}
}

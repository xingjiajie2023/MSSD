package Distance;

import java.util.ArrayList;

import net.Network;

public class Tree_GetDistance {  //Calculate the distance between two networks
	
	public static float Tree_TwoDistance(Network aNet,Network bNet){
		float distance=0;
	    ArrayList<ArrayList<Node>> aSet0=Tree_clusterSet3.getClusterSet(aNet.net);
	    ArrayList<ArrayList<Node>> bSet0=Tree_clusterSet3.getClusterSet(bNet.net);
	    ArrayList<ArrayList<Node>> aSet=Tree_clusterSet2.getClusterSet(aNet.net);
	    ArrayList<ArrayList<Node>> bSet=Tree_clusterSet2.getClusterSet(bNet.net);
	    if(!CompareSetOne.compareArrayOne(getLeafArr(aNet.net), getLeafArr(bNet.net))){
	    	 distance=(float)(aSet0.size()+bSet0.size())/2;
	    	 return distance;
	    }
	    else{
	    	ArrayList<ArrayList<Node>> common=CompareSetOne.compareSetOne(aSet, bSet);
		    distance=(float)(aSet0.size()+bSet0.size()-common.size())/2;
		    return distance;
	    }
	}
	public static ArrayList<Node> getLeafArr(ArrayList<Node> a){
		ArrayList<Node> leaf=new ArrayList<Node>();
		for(Node k:a){
			if(k instanceof LeafNode){
				leaf.add(k);
			}
		}
		return leaf;
	}
	public static ArrayList<Node> getLeafArray(ArrayList<Node> net){
		ArrayList<Node> leaf=new ArrayList<Node>();
		for(int i=0;i<net.size();i++){
			if(net.get(i) instanceof LeafNode){
				leaf.add(net.get(i));
			}
		}
		return leaf;
	}
}

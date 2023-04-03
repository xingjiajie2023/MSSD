package Distance;

import java.util.ArrayList;

import net.Network;

public class CountDis {
	
	public static float countDis(Network net1, Network net2){
		float distance = 0;
		ArrayList<Node> leafArray = LeafArray(net1.net, net2.net);
		ArrayList<int[]> aSet=MkNetColl.mkNetColl(net1.net,leafArray,leafArray.size());
	    ArrayList<int[]> bSet=MkNetColl.mkNetColl(net2.net,leafArray,leafArray.size());
	    ArrayList<int[]> temp=CompareSet.compareSet(aSet, bSet);
	    distance=(float)(aSet.size()+bSet.size()-2*temp.size())/2;
	    return distance;
	}
	
	public static float countDisForCNodes(Network net1, Network net2){
		float distance = 0;
		ArrayList<Node> cNodes = Network.findAllCommonNodes(net1, net2);
		distance = net2.net.size()-cNodes.size();
		return distance;
	}
	
	public static float countDisOne(String a,String b){
		float distance=0;
		ArrayList<Node> aNet=ToNet.toNet(a);
	    ArrayList<Node> bNet=ToNet.toNet(b);
	    ArrayList<Node> leafArray=LeafArray(aNet,bNet);
	    /*以0、1的形式表示簇，对于叶子节点有或者没有*/
	    ArrayList<int[]> aSet=MkNetColl.mkNetColl(aNet,leafArray,leafArray.size());
	    ArrayList<int[]> bSet=MkNetColl.mkNetColl(bNet,leafArray,leafArray.size());
	    ArrayList<int[]> temp=CompareSet.compareSet(aSet, bSet);
	    distance=(float)(aSet.size()+bSet.size()-2*temp.size())/2;
	    return distance;
	}
	
	public static ArrayList<Node> LeafArray(ArrayList<Node> net,ArrayList<Node> nets){
		ArrayList<Node> leaf=new ArrayList<Node>();
		for(int i=0;i<net.size();i++){
			if(net.get(i) instanceof LeafNode){
				leaf.add(net.get(i));
			}
		}
		for(int j=0;j<nets.size();j++){
			if(nets.get(j) instanceof LeafNode&&MkNetColl.add(leaf,nets.get(j))){
				leaf.add(nets.get(j));
			}
		}
		return leaf;
		
	}
	
	
	public static float countDisTwo(String a,String b){
		float distance=0;
	    ArrayList<Node> aNet=ToNet.toNet(a);
	    ArrayList<Node> bNet=ToNet.toNet(b);
	    ArrayList<ArrayList<Node>> aSet=MkNetCollOne.mkNetCollOne(aNet);
	    ArrayList<ArrayList<Node>> bSet=MkNetCollOne.mkNetCollOne(bNet);
	    ArrayList<ArrayList<Node>> temp=CompareSetOne.compareSetOne(aSet, bSet);
	    distance=(float)(aSet.size()+bSet.size()-2*temp.size())/2;
	    return distance;
	}

	//Calculate the set of leaves of the final generated network, although there are multiple networks, but the leaves do not overlap.
	public static ArrayList<Node> LeafArrayOfNet(ArrayList<Network> networks) {
		// TODO Auto-generated method stub
		ArrayList<Node> leaf=new ArrayList<Node>();
		for (int i = 0; i < networks.size(); i++) {
			Network net = networks.get(i);
			for (int j = 0; j < net.net.size(); j++) {
				if (net.net.get(j).children.size()==0) {
					leaf.add(net.net.get(j));
				}
			}
		}
		return leaf;
	}

}

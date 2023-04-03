package Distance;

import java.util.ArrayList;

public class Tree_clusterSet2 {

	public static ArrayList<ArrayList<Node>> getClusterSet(ArrayList<Node> net){
		ArrayList<ArrayList<Node>> clusterSet=new ArrayList<ArrayList<Node>>();
		for(int i=0;i<net.size();i++){
			if(net.get(i).parents.size()!=0){
				ArrayList<Node> cluster=new ArrayList<Node>();
				ArrayList<Node> clusterBuji=new ArrayList<Node>();
				cluster=getCluster(cluster,net.get(i));
				clusterBuji=getClusterBuji(net,cluster);
				if(MkNetCollOne.IsAbleAdd(clusterSet, cluster)){
					clusterSet.add(cluster);
					clusterSet.add(clusterBuji);
				}
			}
		}
		return clusterSet;
	}
	
	public static ArrayList<Node> getCluster(ArrayList<Node> cluster,Node node){
		if(node instanceof LeafNode){
			  cluster.add(node);
			  return cluster;
		}
		for(int i=0;i<node.children.size();i++){
			cluster=getCluster(cluster,node.children.get(i));
		}
		return cluster;
	}
	public static ArrayList<Node> getClusterBuji(ArrayList<Node> net,ArrayList<Node> cluster){
		ArrayList<Node> leaf=new ArrayList<Node>();
		leaf=Tree_GetDistance.getLeafArray(net);
		ArrayList<Node> clusterBuji=new ArrayList<Node>();
		for(Node k:leaf){
			if(MkNetCollOne.simpleIsAbleAdd(cluster, k)){
				clusterBuji.add(k);
			}
		}
		return clusterBuji;
	}
}

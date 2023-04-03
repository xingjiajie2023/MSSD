package net;

import java.util.ArrayList;
import java.util.Map;

import Distance.CompareSet;
import Distance.CountDis;
import Distance.Node;
import Distance.ToNet;

import java.io.Serializable;

import main.CloneUtils;

@SuppressWarnings("serial")
public class Network implements Cloneable,Serializable{
	
	public ArrayList<Node> net=null; //用于存储即将生成的网络节点
	public Map<String,Node> record=null;  //用于记录网络节点与其标识符的对应关系
	public int RNum;
	public ArrayList<Node> reticulates = new ArrayList<Node>();
	public Node root;
	public int insertType; //0 表示包含，不插入，1表示普通插入， 2表示复杂插入，3为最复杂插入（可能不存在）
	public ArrayList<Network> tcpTrees = new ArrayList<Network>();
	public String tcpFlag = "";
	
	
	//计算网络表示树列表中是否包含一棵树
	public static int[] contains1(ArrayList<Network> tcpTrees, Network tree, int temp){
		int[] flag = new int[2];
		flag[0] = -1;
		flag[1] = -1;
		if(tcpTrees.size()==0){
			flag[0] = 1;
			flag[1] = -1;
			return flag;
		}
		int[] t =new int[2];
		t[0] = -1;
		t[1] = -1;
		for (int j = 0; j < tcpTrees.size(); j++) {
			ArrayList<Node> leafArray= Distance.CountDis.LeafArray(tcpTrees.get(j).net, tree.net);
			ArrayList<int[]> aSet = Distance.MkNetColl.mkNetColl(tcpTrees.get(j).net, leafArray, leafArray.size());
			ArrayList<int[]> bSet = Distance.MkNetColl.mkNetColl(tree.net, leafArray, leafArray.size());
			ArrayList<int[]> commonSet = Distance.CompareSet.compareSet(aSet,bSet);
			int a = aSet.size();
			int b = bSet.size();
			int c = commonSet.size();
			float distance = (float)((a+b)-2*c)/2;
			//如果距离为0，即网络包含这棵树
			if(distance==0.0||c==b){
				flag[0] = 0;
				break;
			}
			if(c==0){//如果距离为二者的簇数量和除以2，即没有重叠
				flag[0] = 1;
			}
			if(distance>0&&distance<(a+b)/2) {
				flag[0] = 2;
				flag[1] = j;
				t[0] = 2;
				t[1] = j;
				//System.out.println("flag[0]=0");
			}
					
			
		}
		if(t[0]==2&&flag[0]!=0){
			return t;
		}
		return flag;
	}
	
	public static int contains(Network net1, Network net2) {
		// TODO Auto-generated method stub
		ArrayList<Node> leafArray= Distance.CountDis.LeafArray(net1.net, net2.net);
		ArrayList<int[]> aSet = Distance.MkNetColl.mkNetColl(net1.net, leafArray, leafArray.size());
		ArrayList<int[]> bSet = Distance.MkNetColl.mkNetColl(net2.net, leafArray, leafArray.size());
		ArrayList<int[]> commonSet = Distance.CompareSet.compareSet(aSet,bSet);
		int a = aSet.size();
		int b = bSet.size();
		int c = commonSet.size();
		
		/*for (int i = 0; i < leafArray.size(); i++) {
			System.out.print(leafArray.get(i).value+"  ");
		}
		System.out.println();
		for (int i = 0; i < aSet.size(); i++) {
			for (int j = 0; j < aSet.get(i).length; j++) {
				System.out.print(aSet.get(i)[j]+"  ");
			}
			System.out.println();
		}
		System.out.println("________________________________");
		for (int i = 0; i < bSet.size(); i++) {
			for (int j = 0; j < bSet.get(i).length; j++) {
				System.out.print(bSet.get(i)[j]+"  ");
			}
			System.out.println();
		}*/
		
		float distance = (float)((a+b)-2*c)/2;
		//如果距离为0，即网络包含这棵树
		if(CountDis.countDisForCNodes(net1, net2)==0){
			return 0;
		}
		if(distance==0.0||c==b){
			return 0;
		}else{
			if(c==0){//如果距离为二者的簇数量和除以2，即没有重叠
				return 1;
			}
			if(distance>0&&distance<(a+b)/2) {
				return 2;
			}
				
		}
	
	return -1;
	}
	
	//冗余簇数
	public static int[] reduntantClusterNum(ArrayList<Network> networks,ArrayList<Network> subTrees,
										ArrayList<Network> trees){
		ArrayList<Node> leafArray= Distance.CountDis.LeafArrayOfNet(networks);
		int[] result = new int[2];
		result[0] = 0;
		result[1] = 0;
		int[] num = new int[subTrees.size()];
		for (int i = 0; i < subTrees.size(); i++) {
			ArrayList<int[]> netSet = Distance.MkNetColl.mkNetColl(subTrees.get(i).net, leafArray, leafArray.size());
			result[0]+=netSet.size();
			for (int j = 0; j < trees.size(); j++) {
				ArrayList<int[]> tSet = Distance.MkNetColl.mkNetColl(trees.get(j).net, leafArray, leafArray.size());
				netSet = reduntantCluster(netSet, tSet);
			}
			num[i] = netSet.size();
		}
		for (int i = 0; i < num.length; i++) {
			result[1]+=num[i];
		}
		if(subTrees.size()==0){
			result[0] = Distance.MkNetColl.mkNetColl(networks.get(0).net,leafArray,leafArray.size()).size();
		}
		return result;
	}
	//
	
	private static ArrayList<int[]> reduntantCluster(ArrayList<int[]> netSet,
			ArrayList<int[]> tSet) {
		// TODO Auto-generated method stub
		for (int i = 0; i < netSet.size(); i++) {
			for (int j = 0; j < tSet.size(); j++) {
				if(CompareSet.compareArray(netSet.get(i), tSet.get(j))){
					netSet.remove(i);
					i--;
					break;
				}
			}
		}
		return netSet;
	}

	//首先找n和t相同的节点，全部存储好
	//然后找到n和t中最大的节点，再找到他的所有子节点，然后从n和t中删除
	public static ArrayList<ArrayList<Node>> selectCommonNodes(ArrayList<Node> n,
			ArrayList<Node> t) {
		// TODO Auto-generated method stub
		ArrayList<Node> n1 = new ArrayList<Node>();
		ArrayList<Node> t1 = new ArrayList<Node>();
		for (int i = 0; i < n.size(); i++) {
			for (int j = 0; j < t.size(); j++) {
				String sn = n.get(i).value;
				String st = t.get(j).value;
				if(Distance.CountDis.countDisOne(sn, st)==0){
					n1.add(n.get(i));
					t1.add(t.get(j));
				}
				if(n.get(i).isNewRoot){
					sn = n.get(i).newRootFlag;
					if(Distance.CountDis.countDisOne(sn, st)==0){
						n1.add(n.get(i));
						t1.add(t.get(j));
					}
				}
				if(t.get(j).isNewRoot){
					st = t.get(j).newRootFlag;
					if(Distance.CountDis.countDisOne(sn, st)==0){
						n1.add(n.get(i));
						t1.add(t.get(j));
					}
				}
			}
		}

		n = n1;
		t = t1;
		for (int i = 0; i < n.size(); i++) {
			for (int j = i+1; j < n.size(); j++) {
				String si = n.get(i).value;
				String sj = n.get(j).value;
				if(si.indexOf(sj)!=-1){
					n.remove(j);
					j--;
					continue;
				}
				if(sj.indexOf(si)!=-1){
					n.remove(i);
					i=0;
					j=0;
					continue;
				}
				if(n.get(i).isNewRoot){
					if(n.get(i).newRootFlag.indexOf(sj)!=-1){
						n.remove(j);
						j--;
						continue;
					}
					if(sj.indexOf(n.get(i).newRootFlag)!=-1){
						n.remove(i);
						i=0;
						j=0;
						continue;
					}
				}
				if(n.get(j).isNewRoot){
					if(n.get(j).newRootFlag.indexOf(sj)!=-1){
						n.remove(j);
						j--;
						continue;
					}
					if(sj.indexOf(n.get(j).newRootFlag)!=-1){
						n.remove(i);
						i=0;
						j=0;
						continue;
					}
				}
			}
		}
		for (int i = 0; i < t.size(); i++) {
			
			for (int j = i+1; j < t.size(); j++) {
				String si = t.get(i).value;
				String sj = t.get(j).value;
				if(si.indexOf(sj)!=-1){
					t.remove(j);
					j--;
					continue;
				}
				if(sj.indexOf(si)!=-1){
					t.remove(i);
					i=0;
					j=0;
					continue;
				}
				if(t.get(i).isNewRoot){
					if(t.get(i).newRootFlag.indexOf(sj)!=-1){
						t.remove(j);
						j--;
						continue;
					}
					if(sj.indexOf(t.get(i).newRootFlag)!=-1){
						t.remove(i);
						i=0;
						j=0;
						continue;
					}
				}
				if(t.get(j).isNewRoot){
					if(t.get(j).newRootFlag.indexOf(sj)!=-1){
						t.remove(j);
						j--;
						continue;
					}
					if(sj.indexOf(t.get(j).newRootFlag)!=-1){
						t.remove(i);
						i=0;
						j=0;
						continue;
					}
				}
			}
		}
		ArrayList<ArrayList<Node>> cNodes = new ArrayList<ArrayList<Node>>();
		cNodes.add(n);
		cNodes.add(t);
		return cNodes;
	}
	
	
	//找到公共的簇（NJ）
	public static ArrayList<ArrayList<Node>> findCommonCluster(Network net1, Network net2){
		ArrayList<Node> leafArray= Distance.CountDis.LeafArray(net1.net, net2.net);
		ArrayList<int[]> aSet = Distance.MkNetColl.mkNetColl(net1.net, leafArray, leafArray.size());
		ArrayList<int[]> bSet = Distance.MkNetColl.mkNetColl(net2.net, leafArray, leafArray.size());
		ArrayList<int[]> commonSet = Distance.CompareSet.compareSet(aSet,bSet);
		commonSet = reduction(commonSet);
		
		//去重之后在两棵树中找到对应的节点
		ArrayList<ArrayList<Node>> commonNode = new ArrayList<ArrayList<Node>>();
		commonNode.add(findNodeByCluster(commonSet, net1.net, leafArray));
		commonNode.add(findNodeByCluster(commonSet, net2.net, leafArray));
		return commonNode;
	}
	
	//对得到的公共簇集合进行去重
	private static ArrayList<int[]> reduction(ArrayList<int[]> commonSet){
		ArrayList<int[]> commonSet1 = new ArrayList<int[]>();
		boolean[] repFlag = new boolean[commonSet.size()];
		for (int i = 0; i < commonSet.size(); i++) {
			int num1 = countNumOfOne(commonSet.get(i));
			for(int k = i+1; k < commonSet.size(); k++){
				int flag = 0;
				int num2 = countNumOfOne(commonSet.get(k));
				for (int j = 0; j < commonSet.get(i).length; j++) {
					if(commonSet.get(i)[j]==commonSet.get(k)[j]&&commonSet.get(i)[j]!=0){
						flag++;
					}
				}
				if(flag == num1){
					repFlag[i] = true;
				}
				if(flag == num2){
					repFlag[k] = true;
				}
			}
		}
		for (int i = 0; i < repFlag.length; i++) {
			if(!repFlag[i]){
				commonSet1.add(commonSet.get(i));
			}
		}
		return commonSet1;
	}
	
	public static int countNumOfOne(int[] comSet){
		int num = 0;
		for (int i = 0; i < comSet.length; i++) {
			if(comSet[i] == 1){
				num++;
			}
		}
		return num;
	}
	
	//根据公共簇，找到在树或网络中对应的 
	private static ArrayList<Node> findNodeByCluster(ArrayList<int[]> commonSet, 
													ArrayList<Node> net,
													ArrayList<Node> leafArray){
		ArrayList<Node> commonNode = new ArrayList<Node>();
		for (int i = 0; i < commonSet.size(); i++) {
			for (int j = 0; j < net.size(); j++) {
				int[] record = new int[leafArray.size()];
				for (int k = 0; k < leafArray.size(); k++) {
					//求net的第j个节点的簇
					record[k] = Distance.MkNetColl.pathNum(net, leafArray, j, k);
				}
				if(isIndentical(record, commonSet.get(i))){
					//如果簇相同，就把当前节点当做公共节点
					if(!net.get(j).isRet){
						commonNode.add(net.get(j));
					}
				}
			}
		}
		return commonNode;
	}

	private static boolean isIndentical(int[] record, int[] is) {
		// TODO Auto-generated method stub
		for (int i = 0; i < record.length; i++) {
			if(record[i]!=is[i]){
				return false;
			}
		}
		return true;
	}
	
	///
	public static ArrayList<ArrayList<Node>> findCommonNodes(Network n, Network t){
		//先把全部的公共簇找到
		ArrayList<Node> leafArray= Distance.CountDis.LeafArray(n.net, t.net);
		ArrayList<int[]> aSet = Distance.MkNetColl.mkNetColl(n.net, leafArray, leafArray.size());
		ArrayList<int[]> bSet = Distance.MkNetColl.mkNetColl(t.net, leafArray, leafArray.size());
		ArrayList<int[]> commonSet = Distance.CompareSet.compareSet(aSet,bSet);
		
		//转换成对应的公共节点
		ArrayList<ArrayList<Node>> commonNode = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> nNode = findNodeByCluster(commonSet, n.net, leafArray);
		ArrayList<Node> tNode = findNodeByCluster(commonSet, t.net, leafArray);
		commonNode.add(nNode);
		commonNode.add(tNode);
		//截止到这里得到的公共节点有错误，因为只是简单的根据簇找到了在网络和树中的对应节点
		
		commonNode = selectCommonNodes(nNode, tNode);
		
		return commonNode;
	}
	
	//通过网络的包含树寻找重叠节点
	public static ArrayList<ArrayList<ArrayList<Node>>> findCNBySubt(Network network, Network tree){
		ArrayList<Network> subTree = network.tcpTrees;
		ArrayList<ArrayList<ArrayList<Node>>> allCommonNodes = new ArrayList<ArrayList<ArrayList<Node>>>();
		for (int i = 0; i < subTree.size(); i++) {
			Network ti = subTree.get(i);
			ArrayList<ArrayList<Node>> commoni = findCommonNodes(ti, tree);
			allCommonNodes.add(commoni);
		}
		//allCommonNodes的size就是共有几颗子树有重叠节点，然后去掉被包含的节点 
		//有多余的重叠节点未被删除
		for (int i = 0; i < allCommonNodes.size(); i++) {
			//第i棵树的公共节点
			ArrayList<ArrayList<Node>> commoni = allCommonNodes.get(i);
			for (int j = i+1; j < allCommonNodes.size(); j++) {
				//第j棵树的公共节点
				ArrayList<ArrayList<Node>> commonj = allCommonNodes.get(j);
				for (int k = 0; k < commoni.get(0).size(); k++) {
					//第i棵树的第k个节点
					Network tik = new Network();
					tik.net = ToNet.toNet(commoni.get(0).get(k).value);
					for (int k2 = 0; k2 < commonj.get(0).size(); k2++) {
						//第j棵树的第k2个节点
						Network tjk2 = new Network();
						tjk2.net = ToNet.toNet(commonj.get(0).get(k2).value);
						if(contains(tik, tjk2)==0){
							commonj.get(0).remove(k2);
							commonj.get(1).remove(k2);
							k2--;
							continue;
						}
						if(contains(tjk2, tik)==0){
							commoni.get(0).remove(k);
							commoni.get(1).remove(k);
							k--;
							break;
						}
					}
				}
			}
		}
		//去重结束后要反向找到网络
		for (int i = 0; i < allCommonNodes.size(); i++) {
			ArrayList<ArrayList<Node>> commoni = allCommonNodes.get(i);
			for (int j = 0; j < commoni.get(0).size(); j++) {
				String value = commoni.get(0).get(j).value;
				if(commoni.get(0).get(j).isNewRoot){
					
					for (int k = 0; k < network.net.size(); k++) {
						//有问题
						if(network.net.get(k).value.equals(commoni.get(0).get(j).newRootFlag)){
							commoni.get(0).set(j, network.net.get(k));
						}
					}
				}else{
					for (int k = 0; k < network.net.size(); k++) {
						if(network.net.get(k).value.equals(value)){
							commoni.get(0).set(j, network.net.get(k));
						}
					}
				}
			}
		}
		//去除在网络中找到的相同的节点
		for (int i = 0; i < allCommonNodes.size(); i++) {
			ArrayList<ArrayList<Node>> commoni = allCommonNodes.get(i);
			for (int j = 0; j < commoni.get(0).size(); j++) {
				Node nodei = allCommonNodes.get(i).get(0).get(j);
				/*System.out.print(allcommon.get(i).get(0).get(j).value+"    "+t1.net.indexOf(allcommon.get(i).get(0).get(j)));
				System.out.print(allcommon.get(i).get(1).get(j).value+"    "+t4.net.indexOf(allcommon.get(i).get(1).get(j)));*/
				for (int k = i+1; k < allCommonNodes.size(); k++) {
					ArrayList<ArrayList<Node>> commonk = allCommonNodes.get(k);
					for (int k1 = 0; k1 < commonk.get(0).size(); k1++) {
						Node nodek = allCommonNodes.get(k).get(0).get(k1);
						if(network.net.indexOf(nodei)==network.net.indexOf(nodek)){
							allCommonNodes.remove(k);
						}
					}
				}
			}
		}
		
		return allCommonNodes;
	}
	
	//网络和网络之间寻找重叠节点
	public static ArrayList<ArrayList<ArrayList<Node>>> findCNBySubts(Network net1, Network net2){
		ArrayList<Network> subT1 = net1.tcpTrees;	
		ArrayList<ArrayList<ArrayList<Node>>> allcommon = new ArrayList<ArrayList<ArrayList<Node>>>();
		
		for (int i = 0; i < subT1.size(); i++) {
			Network sub1 = subT1.get(i);	//sub1当做Ttree， net2当做network
			//net1 的第i个子树与net2 的子树的重叠节点
			ArrayList<ArrayList<ArrayList<Node>>> commoni = findCNBySubt(net2, sub1); //commoni 中第一个是net2的节点，第二个是sub1的节点，需要将sub1的转换成net1的
			//要反向找到net1中的节点
			for (int j = 0; j < commoni.size(); j++) {
				ArrayList<ArrayList<Node>> commonj = commoni.get(j);
				for (int k = 0; k < commonj.get(1).size(); k++) {
					String value = commonj.get(1).get(k).value;
					for (int l = 0; l < net1.net.size(); l++) {
						if(net1.net.get(l).value.equals(value)){
							commonj.get(1).set(k, net1.net.get(l));
						}
					}
				}
			}
			ArrayList<Node> nodeOfNet = new ArrayList<Node>();
			ArrayList<Node> nodeOfTree = new ArrayList<Node>();
			ArrayList<ArrayList<Node>> commonNode = new ArrayList<ArrayList<Node>>();
			for (int i1 = 0; i1 < commoni.size(); i1++) {
				for (int j = 0; j < commoni.get(i1).get(0).size(); j++) {
					nodeOfNet.add(commoni.get(i1).get(0).get(j));
					nodeOfTree.add(commoni.get(i1).get(1).get(j));
				}
			}
			commonNode.add(nodeOfNet);
			commonNode.add(nodeOfTree);
			allcommon.add(commonNode);
		}
		for (int i = 0; i < allcommon.size(); i++) {
			//第i棵树的公共节点
			ArrayList<ArrayList<Node>> commoni1 = allcommon.get(i);
			for (int j = i+1; j < allcommon.size(); j++) {
				//第j棵树的公共节点
				ArrayList<ArrayList<Node>> commonj = allcommon.get(j);
				for (int k = 0; k < commoni1.get(0).size(); k++) {
					//第i棵树的第k个节点
					Network tik = new Network();
					tik.net = ToNet.toNet(commoni1.get(0).get(k).value);
					for (int k2 = 0; k2 < commonj.get(0).size(); k2++) {
						//第j棵树的第k2个节点
						Network tjk2 = new Network();
						tjk2.net = ToNet.toNet(commonj.get(0).get(k2).value);
						if(contains(tik, tjk2)==0){
							commonj.get(0).remove(k2);
							commonj.get(1).remove(k2);
							k2--;
							continue;
						}
						if(contains(tjk2, tik)==0){
							commoni1.get(0).remove(k);
							commoni1.get(1).remove(k);
							k--;
							break;
						}
					}
				}
			}
		}
		return allcommon;
	}
	
	
	public static ArrayList<Node> selectAllCaommonNodes(ArrayList<Node> n,
			ArrayList<Node> t){
		ArrayList<Node> n1 = new ArrayList<Node>();
		ArrayList<Node> t1 = new ArrayList<Node>();
		for (int i = 0; i < n.size(); i++) {
			for (int j = 0; j < t.size(); j++) {
				String sn = n.get(i).value;
				String st = t.get(j).value;
				if(Distance.CountDis.countDisOne(sn, st)==0){
					n1.add(n.get(i));
					t1.add(t.get(j));
				}
			}
			
		}
		
		return t1;
	}
	
	public static ArrayList<Node> findAllCommonNodes(Network n, Network t){
		//先把全部的公共簇找到
		ArrayList<Node> leafArray= Distance.CountDis.LeafArray(n.net, t.net);
		ArrayList<int[]> aSet = Distance.MkNetColl.mkNetColl(n.net, leafArray, leafArray.size());
		ArrayList<int[]> bSet = Distance.MkNetColl.mkNetColl(t.net, leafArray, leafArray.size());
		ArrayList<int[]> commonSet = Distance.CompareSet.compareSet(aSet,bSet);
		//转换成对应的公共节点
		ArrayList<Node> nNode = findNodeByCluster(commonSet, n.net, leafArray);
		ArrayList<Node> tNode = findNodeByCluster(commonSet, t.net, leafArray);
		tNode = selectAllCaommonNodes(nNode, tNode);
		//返回树中的公共节点以作比较
		return tNode;
	}
	
/*	private static ArrayList<int[]> delReduntant(ArrayList<int[]> treeCSet) {
		// TODO Auto-generated method stub
		for (int i = 0; i < treeCSet.size(); i++) {
			for (int j = i+1; j < treeCSet.size(); j++) {
				if(CompareSet.compareArray(treeCSet.get(i), treeCSet.get(j))){
					treeCSet.remove(j);
				}
			}
		}
		return treeCSet;
	}*/
	@Override
	public Object clone(){
		Network net = null;
		try {
			net = (Network)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return net;
	}

	public static int[] containsOfNet(ArrayList<Network> networks,
			Network network, int a) {
		// TODO Auto-generated method stub
		int[] flag = new int[2];
		flag[0] = -1;
		flag[1] = -1;
		for (int i = 0; i < networks.size(); i++) {
			if(i == a){
				continue;
			}
			int type = contains(networks.get(i), network);
			if(type==0){
				flag[0] = 0;
				flag[1] = networks.indexOf(network);
				break;
			}
			
			if(type == 2){
				//insert
				flag[0] = 2;
				flag[1] = i;
			}
			
		}
		return flag;
	}

	public static ArrayList<Network> createRoot(ArrayList<Network> networks){
		for (int i = 0; i < networks.size(); i++) {
			Node root = new Node();
			root.value = "root";
			for (int j = 0; j < networks.get(i).net.size(); j++) {
				if (networks.get(i).net.get(j).parents.size()==0) {
					root.children.add(networks.get(i).net.get(j));
					networks.get(i).net.get(j).parents.add(root);
				}
			}
			networks.get(i).net.add(root);
			networks.get(i).root = root;
			for (int k2 = 0; k2 < networks.get(i).reticulates.size(); k2++) {
				networks.get(i).reticulates.get(k2).value = "H"+Integer.toString(k2);
			}
		}
		
		return networks;
	}
	
	public static Network createRootForClone(Network network){
		Node root = new Node();
		root.value = "root";
		for (int j = 0; j < network.net.size(); j++) {
			if (network.net.get(j).parents.size()==0) {
				root.children.add(network.net.get(j));
				network.net.get(j).parents.add(root);
			}
		}
		network.net.add(root);
		network.root = root;
		for (int k2 = 0; k2 < network.reticulates.size(); k2++) {
			network.reticulates.get(k2).value = "H"+Integer.toString(k2);
		}
		return network;
	}

	public static Network createRootOfTree(Network net, Network treeNode) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<Node>> cNodes = findCommonCluster(net, treeNode);
		Network copy = CloneUtils.clone(treeNode);
		net = InsertTree.insertTree(cNodes.get(0), cNodes.get(1), net, treeNode, copy );
		return net;
	}

	public static int overlap(ArrayList<Network> networks, Network node) {
		// TODO Auto-generated method stub
		for (int i = 0; i < networks.size(); i++) {
			ArrayList<Node> leafArray = CountDis.LeafArray(networks.get(i).net, node.net);
			ArrayList<int[]> aSet = Distance.MkNetColl.mkNetColl(networks.get(i).net, leafArray, leafArray.size());
			ArrayList<int[]> bSet = Distance.MkNetColl.mkNetColl(node.net, leafArray, leafArray.size());
			ArrayList<int[]> commonSet = Distance.CompareSet.compareSet(aSet,bSet);
			if(commonSet.size()>0){
				return i;
			}
		}
		return -1;
	}

	public static int reduntantClusterNum(Network subTree,
			Network tree) {
		// TODO Auto-generated method stub
		ArrayList<Node> leafArray = CountDis.LeafArray(subTree.net, tree.net);
		ArrayList<int[]> aSet = Distance.MkNetColl.mkNetColl(subTree.net, leafArray, leafArray.size());
		ArrayList<int[]> bSet = Distance.MkNetColl.mkNetColl(tree.net, leafArray, leafArray.size());
		return reduntantCluster(aSet,bSet).size();
	}

	
}

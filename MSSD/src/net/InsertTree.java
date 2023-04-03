package net;

import java.util.ArrayList;

import main.CloneUtils;
import Distance.CountDis;
import Distance.Node;
/**
 * @author song
 *
 */
public class InsertTree {

	//插入树到网络中
	public static Network insertTree(ArrayList<Node> nodeOfNet,ArrayList<Node> nodeOfTree, 
								  Network net, Network tree, Network copyTree){
		/*System.out.println("size"+nodeOfNet.size());
		for (int i = 0; i < nodeOfNet.size(); i++) {
			System.out.println(nodeOfNet.get(i).value);
		}*/
		boolean isAdd = true;
		boolean addtcp = false;
		int a =0;
		if(nodeOfNet.size()>nodeOfTree.size()){
			a = nodeOfTree.size();
		}else{
			a = nodeOfNet.size();
		}
		/*int flag = -1;
		Node rootNode = new Node();*/
		Network copyNet  = main.CloneUtils.clone(net);
		for (int i = 0; i < a; i++) {
			Node node1 = nodeOfNet.get(i);
			Node node2 = nodeOfTree.get(i);
			
			if(node2.parents.size()==0&&node1.parents.size()!=0){
				//如果没有父节点，就表示这个节点是根节点，它与网络重合，即网络包含它(可能是网络和网络比较)
				if (tree.RNum==0) {
					if(a==1){
						isAdd = false;
					}
					continue;
				}
				node1.parents.get(0).children.add(node2);
				node2.parents.add(node1.parents.get(0));
				ArrayList<Node> node1List = new ArrayList<Node>();
				findAllNode(node1, node1List);
				node1.parents.get(0).children.remove(node1);
				node1.parents.remove(0);
				net.net.removeAll(node1List);
				//net.net.addAll(tree.net);
				tree.insertType = 0;
				net.tcpTrees.addAll(tree.tcpTrees);
				Network copy2 = CloneUtils.clone(copyNet);
				if(net.RNum>0&&tree.RNum>0&&!addtcp){
					net.tcpTrees.addAll(tree.tcpTrees);
					addtcp = true;
				}else{
					net.tcpTrees = updateTcpTree(net.tcpTrees, copy2 , node2.value, node2.value);
				}
				continue;
			}
			if(node1.parents.size()==0&&node2.parents.size()!=0){
				//网络中的某个根节点与树的内部节点重合
				//就把树的重合节点的父节点指向网络中对应的节点
				node2.parents.get(0).children.add(node1);
				node1.parents.add(node2.parents.get(0));
				node2.parents.get(0).children.remove(node2);
				ArrayList<Node> node2List = new ArrayList<Node>();
				findAllNode(node2, node2List);
				tree.net.removeAll(node2List);
				tree.insertType = 1;
				//存储
				Network copy2 = main.CloneUtils.clone(copyTree);
				copy2.tcpFlag = node2.parents.get(0).value;
				if(net.RNum>0&&tree.RNum>0&&!addtcp){
					net.tcpTrees.addAll(tree.tcpTrees);
					addtcp = true;
				}else{
					if(node1.isNewRoot){
						//找到node1对应的两棵树，进行变换
						//node1.value 一定和tcpFlag对应
						for (int j = 0; j < net.tcpTrees.size(); j++) {
							if(net.tcpTrees.get(j).tcpFlag.equals(node1.value)){
								net.tcpTrees.set(j, updateTcpTree(net.tcpTrees.get(j), copy2, node2.value));
							}
						}
					}else{
						net.tcpTrees = updateTcpTree(net.tcpTrees, copy2, node1.value, node2.value);
					}
				}
				continue;
			}
			if(node1.parents.size()==0&&node2.parents.size()==0){
				continue;
			}
			//
			if(node2.parents.get(0).parents.size()==0){		//node2的父节点是根节点
				int indexT = 1- node2.parents.get(0).children.indexOf(node2);
				if(node1.parents.size()==1){				//node1不是网络节点
					if(node1.parents.get(0).parents.size()==0){//node1的父节点是根节点
						int index = 1- node1.parents.get(0).children.indexOf(node1);
						if(node1.parents.get(0).children.get(index).isRet){//node1的兄弟节点是网络节点
							
							Node h = node1.parents.get(0).children.get(index);
							int rootIndex = 1- h.parents.indexOf(node1.parents.get(0));
							Node root = h.parents.get(rootIndex);
							String subTree = node2.parents.get(0).children.get(indexT).value;
							if(CountDis.countDisOne(root.value, subTree)==0){//node1的兄弟节点的父节点与node2的兄弟节点完全重叠
								
								node2.parents.get(0).children.add(node1.parents.get(0));
								node2.parents.get(0).isNewRoot = true;
								node2.parents.get(0).newRootFlag = node2.parents.get(0).value;
								node1.parents.get(0).parents.add(node2.parents.get(0));
								node2.parents.get(0).children.remove(node2);
								ArrayList<Node> node2List = new ArrayList<Node>();
								findAllNode(node2, node2List);
								tree.net.removeAll(node2List);
								Network copy2 = main.CloneUtils.clone(copyTree);
								
								if(root.isNewRoot){
									net.tcpTrees = updateTcps(net.tcpTrees, node1.value,copy2, node2.value);
									continue;
								}
								//存储1
								String rvalue = node1.parents.get(0).value;
								String hvalue = h.children.get(0).value;
								//找到要删除的点（要更新子树列表）
								Node b = null;
								Node c = null;
								for (int j = 0; j < copy2.net.size(); j++) {
									if(Distance.CountDis.countDisOne(copy2.net.get(j).value,hvalue)==0) {
										b = copy2.net.get(j);
									}
									if(Distance.CountDis.countDisOne(node1.value,copy2.net.get(j).value)==0){
										c = copy2.net.get(j);
									}
								}
								Node newroot = c.parents.get(0);
								Node oldroot = null;
								int indexOfTree = -1;
								//找到子树列表中对应的子树(根)
								for (int j = 0; j < net.tcpTrees.size(); j++) {
									for (int j2 = 0; j2 < net.tcpTrees.get(j).net.size(); j2++) {
										if(Distance.CountDis.countDisOne(net.tcpTrees.get(j).net.get(j2).value, rvalue)==0){
											oldroot = net.tcpTrees.get(j).net.get(j2);
											indexOfTree = j;
										}
									}
								}
								b.parents.get(0).children.remove(b);
								b.parents.get(0).children.get(0).parents.add(b.parents.get(0).parents.get(0));
								b.parents.get(0).children.get(0).parents.remove(0);
								b.parents.get(0).parents.get(0).children.add(b.parents.get(0).children.get(0));
								b.parents.get(0).parents.get(0).children.remove(b.parents.get(0));
								copy2.net.remove(b.parents.get(0));
								//连接（b，c）
								newroot.children.add(oldroot);
								oldroot.parents.add(newroot);
								//将（b，c）全部添加到copy2中
								copy2.net.addAll(net.tcpTrees.get(indexOfTree).net);
								//将（b，c）从原来的tcptree中删除
								net.tcpTrees.remove(indexOfTree);
								//copy2.net.remove(b);
								ArrayList<Node> bplist = new ArrayList<Node>();
								findAllNode(b, bplist);
								copy2.net.removeAll(bplist);
								copy2.net.remove(b.parents.get(0));
								
								c.parents.get(0).children.remove(c);
								c.parents.remove(0);
								//copy2.net.remove(c);
								ArrayList<Node> clist = new ArrayList<Node>();
								findAllNode(c, clist);
								copy2.net.removeAll(clist);
								
								
								newroot.value = "("+newroot.children.get(0).value+","+newroot.children.get(1).value+")";
								newroot.isNewRoot = true;
								newroot.newRootFlag = node2.parents.get(0).value;
								copy2.tcpFlag = node2.parents.get(0).value;
								net.tcpTrees.add(copy2);
								//去重
								//直接是对子树列表操作的，所以不需要去重
								
								continue;
							}
						}
					}
				}
			}
				//创建一个网络节点 ret 记为 H0、H1···Hn
				Node ret = new Node();
				ret.value = "H"+Integer.toString(net.reticulates.size());
				ret.isRet = true;
				//设置ret的父节点与子节点
				ret.parents.addAll(node1.parents);
				ret.parents.addAll(node2.parents);
				ret.children.add(node1);
				//设置node1、2的父节点的子节点为ret
				node1.parents.get(0).children.add(ret);
				node2.parents.get(0).children.add(ret);
				//吧重叠的节点——node1的父节点设为net
				node1.parents.add(ret);
				//删除重复节点与其父节点的关系
				node1.parents.get(0).children.remove(node1);
				node1.parents.remove(0);
				node2.parents.get(0).children.remove(node2);
				//把重复的节点从tree中删除
				ArrayList<Node> node2List = new ArrayList<Node>();
				findAllNode(node2, node2List);
				tree.net.removeAll(node2List);
				//把net节点添加到网络的节点列表
				net.net.add(ret);
				//把ret节点放入reticulate列表中
				net.reticulates.add(ret);
				//Rnum++
				net.RNum++;
				if(net.RNum>0&&tree.RNum>0&&!addtcp){
					net.tcpTrees.addAll(tree.tcpTrees);
					addtcp = true;
				}else{
					Network copy2 = CloneUtils.clone(copyTree);
					copy2.tcpFlag = node2.parents.get(0).value;
					net.tcpTrees.add(copy2);
				}
		}
		//把树中的所有节点都添加到网络的节点列表里
		if(isAdd)
			net.net.addAll(tree.net);
		net.tcpTrees = clean(net.tcpTrees);
		return net;
	}
	
	private static ArrayList<Network> updateTcps(ArrayList<Network> tcpTrees, String value, Network copy, String value2) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < tcpTrees.size(); i++) {
			Network copytcp1 = CloneUtils.clone(tcpTrees.get(i));
			for (int j = 0; j < copytcp1.net.size(); j++) {
				if(tcpTrees.get(i).net.get(j).value.equals(value)){
					Node node1 = copytcp1.net.get(j);
					Network copy2 = CloneUtils.clone(copy);
					for (int k = 0; k < copy2.net.size(); k++) {
						if(copy2.net.get(k).value.equals(value2)){
							Node node2 = copy2.net.get(k);
							//先删除node1的兄弟节点及其后代节点
							int index1 = 1-node1.parents.get(0).children.indexOf(node1);
							Node node1b = node1.parents.get(0).children.get(index1);
							node1b.parents.get(0).children.remove(node1b);
							ArrayList<Node> node1bList = new ArrayList<Node>();
							findAllNode(node1b, node1bList);
							copytcp1.net.removeAll(node1bList);
							//再删除node1与其父节点的链接
							copytcp1.net.remove(node1.parents.get(0));
							node1.parents.remove(0);
							
							//然后删除node2及其后代节点   和  其父节点与其的链接
							Node node2f = node2.parents.get(0);
							node2f.children.remove(node2);
							ArrayList<Node> node2List = new ArrayList<Node>();
							findAllNode(node2, node2List);
							copy2.net.removeAll(node2List);
							
							//链接node1与node2f
							node1.parents.add(node2f);
							node2f.children.add(node1);
							copy2.net.addAll(copy2.net);
							tcpTrees.add(copy2);
							break;
						}
					}
				}
			}
		}
		return tcpTrees;
	}

	public static ArrayList<Network> clean(ArrayList<Network> tcpTrees) {
		// TODO Auto-generated method stub
		for (int i = 0; i < tcpTrees.size(); i++) {
			for (int j = i+1; j < tcpTrees.size(); j++) {
				if(Network.contains(tcpTrees.get(i), tcpTrees.get(j))==0){
					tcpTrees.remove(j);
					j--;
					continue;
				}
				if(Network.contains(tcpTrees.get(j), tcpTrees.get(i))==0){
					tcpTrees.remove(i);
					i--;
					break;
				}
			}
		}
		return tcpTrees;
	}

	private static ArrayList<Network> updateTcpTree(
			ArrayList<Network> tcpTrees, Network copy2, String value, String value2) {
		// TODO Auto-generated method stub
		//找到对应的tcptrees和其对应的节点
		for (int i = 0; i < tcpTrees.size(); i++) {
			if(tcpTrees.get(i).tcpFlag.equals(value)){
				Network tcps = new Network();
				tcps = tcpTrees.get(i);
				for (int j = 0; j < tcps.net.size(); j++) {
					if(tcps.net.get(j).value.equals(value)||tcps.net.get(j).newRootFlag.equals(value)){
						Node root = new Node();
						root = tcps.net.get(j);
						Node fnode = new Node();
						Node node2 = new Node();
						//每次都要使用新的copyT
						Network copyT = CloneUtils.clone(copy2);
						for (int k = 0; k < copyT.net.size(); k++) {
							if(copyT.net.get(k).value.equals(value2)){
								node2 = copyT.net.get(k);
								fnode = node2.parents.get(0);
							}
						}
						fnode.children.remove(node2);
						ArrayList<Node> node2List = new ArrayList<Node>();
						findAllNode(node2, node2List);
						copyT.net.removeAll(node2List);
						fnode.children.add(root);
						root.parents.add(fnode);
						copyT.net.addAll(tcps.net);
						if(copyT.net.get(0).parents.size()==0){
							copyT.tcpFlag = copyT.net.get(0).value;
						}else{
							for (int k = 0; k < copyT.net.size(); k++) {
								if(copyT.net.get(k).parents.size()==0){
									copyT.tcpFlag = copyT.net.get(k).value;
								}
							}
						}
						tcpTrees.set(i, copyT);
						copyT = tcpTrees.get(i);
						if(tcps.net.get(j).isNewRoot){
							fnode.isNewRoot = true;
							fnode.newRootFlag = copyT.tcpFlag;
							fnode.value = "("+fnode.children.get(0).value+","+fnode.children.get(1).value+")";
						}
						
						break;
					}
				}
			}
		}
		//tcpTrees = clean(tcpTrees, copyT);
		return tcpTrees;
	}
	public static Network updateTcpTree(Network network, Network copy2, String value) {
		// TODO Auto-generated method stub
		/*System.out.println("copy2 is ");
		for (int i = 0; i < copy2.net.size(); i++) {
			System.out.print(copy2.net.get(i).value+"  ");
		}
		System.out.println();*/
		Network copyTree = CloneUtils.clone(copy2);
		Node node2 = new Node();
		for (int i = 0; i < copyTree.net.size(); i++) {
			if(copyTree.net.get(i).value.equals(value)){
				node2 = copyTree.net.get(i);
				break;
			}
		}
		Node fNode = node2.parents.get(0);
		fNode.children.remove(node2);
		ArrayList<Node> node2List = new ArrayList<Node>();
		findAllNode(node2, node2List);
		copyTree.net.removeAll(node2List);
		
		Node rootOfNet = new Node();
		for (int i = 0; i < network.net.size(); i++) {
			if (network.net.get(i).parents.size()==0) {
				rootOfNet = network.net.get(i);
			}
		}
		fNode.children.add(rootOfNet);
		rootOfNet.parents.add(fNode);
		if(rootOfNet.isNewRoot){
			fNode.isNewRoot = true;
			fNode.newRootFlag = copyTree.tcpFlag;
			fNode.value = "("+fNode.children.get(0).value+","+fNode.children.get(1).value+")";
		}
		
		copyTree.net.addAll(network.net);
		
		return copyTree;
	}
	
	public static void findAllNode(Node node2, ArrayList<Node> node2List) {
		// TODO Auto-generated method stub
		if(node2 == null){
			return;
		}
		node2List.add(node2);
		if(node2.children.size()!=0){
			findAllNode(node2.children.get(0), node2List);
		}
		if(node2.children.size()>1){
			findAllNode(node2.children.get(1), node2List);
		}
		
	}

	public static Network insertNetwork(ArrayList<Node> nodeOfNet2,
			ArrayList<Node> nodeOfTree2, Network network1, Network network2) {
		// TODO Auto-generated method stub
		return null;
	}

	

}

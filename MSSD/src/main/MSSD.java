package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import Distance.Node;
import Distance.ToNet;
import net.InsertTree;
import net.Network;
import net.PrintOut;
/**
 * @author song
 *Input m gene trees, and separat them by semicolons
 *The output is constructed phylogenetic network
 *
 */
public class MSSD {

	public static int ans=0;
	private static String filename;
	public static PrintWriter outt;
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		Scanner s = new Scanner(System.in);
		System.out.println("Please type the full path of the file:");
		filename = s.nextLine();
				long startTime=System.currentTimeMillis();
				ArrayList<String> files = new ArrayList<String>();
				ArrayList<Network> trees = new ArrayList<Network>();
				File file_In = new File(filename);
				if (!file_In.exists()) {
					System.err.println("can't find" + file_In);
					return;
				}
				
				try {
					@SuppressWarnings("resource")
					BufferedReader br = new BufferedReader(new FileReader(file_In));
					String line;
					while((line = br.readLine()) != null){
						files.add(net.NewickToEdge.DelBranchLength(line));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < files.size(); i++) {
					Network net = new Network();
					net.net = ToNet.toNet(files.get(i));
					trees.add(net);
				}
				int[] d = new int[files.size()];
				for (int i = 0; i < trees.size(); i++) {
					d[i] = deepth(trees.get(i).net.get(0), 1);
				}
				ans = 0;
				
				ArrayList<Network> networks = new ArrayList<Network>();
				int ds = d.length;
				while(ds>0){
					
					ArrayList<ArrayList<Network>> nodesList = new ArrayList<ArrayList<Network>>();
					
					for (int i = 0; i < trees.size(); i++) {
						if (d[i]>1) {
							ArrayList<Network> nodes = NodeToNet(d[i]-1, trees.get(i));
							nodesList.add(nodes);
							d[i]--;
						}
						if(d[i]<=1){
							ds--;
						}
					}
					for (int i = 0; i < nodesList.size(); i++) {
						
						for (int j = 0; j < nodesList.get(i).size(); j++) {
							int[] flag = new int[2];
							flag[0] = -1;
							flag[1] = -1;
							int[] tempf = new int[2];
							tempf[0] = -1;
							tempf[1] = -1;
							
							
							ArrayList<Network> nodes = nodesList.get(i);
							if(nodes.get(j).net.size()==1){
								
								continue;
							}
							
							if(networks.size()==0){
									flag[0] = 1;
							}
							for (int k = 0; k < networks.size(); k++) {
								
								Network net = networks.get(k);
								flag = Network.contains1(net.tcpTrees, nodes.get(j), 0);
								if(flag[0] == 0){
									break;
								}
								if(flag[0] == 2){
									tempf[0] = 2;
									tempf[1] = k;
								}
								
							}

							if(flag[0]==0){
								continue;
							}
							
							if(tempf[0] == 2){
								flag[0] = 2;
								flag[1] = tempf[1];
							}
							switch(flag[0]){
							case 0:			
								
								break;
							case 1:			
								networks.add(nodes.get(j));
								Network copytree = CloneUtils.clone(nodes.get(j));
								copytree.tcpFlag = nodes.get(j).net.get(0).value;
								networks.get(networks.size()-1).tcpTrees.add(copytree);
								break;
							case 2:			
								
								Network network = new Network();
								network = networks.get(flag[1]);
								
								ArrayList<ArrayList<ArrayList<Node>>> allcommon = Network.findCNBySubt(network, nodes.get(j));
								Network copyTree = CloneUtils.clone(nodes.get(j));
								ArrayList<Node> nodeOfNet = new ArrayList<Node>();
								ArrayList<Node> nodeOfTree = new ArrayList<Node>();
								for (int i1 = 0; i1 < allcommon.size(); i1++) {
									for (int j1 = 0; j1 < allcommon.get(i1).get(0).size(); j1++) {
										nodeOfNet.add(allcommon.get(i1).get(0).get(j1));
										nodeOfTree.add(allcommon.get(i1).get(1).get(j1));
									}
								}
								network = InsertTree.insertTree(nodeOfNet, nodeOfTree, network, nodes.get(j), copyTree);
								int index = networks.indexOf(network);
								flag = Network.containsOfNet(networks, network, index);
								if (flag[0] == 2) {
									Network network1 = networks.get(flag[1]);
									
									allcommon = Network.findCNBySubts(network1, network);
									copyTree = CloneUtils.clone(network1);
									ArrayList<Node> nodeOfNet2 = new ArrayList<Node>();
									ArrayList<Node> nodeOfTree2 = new ArrayList<Node>();
									for (int i1 = 0; i1 < allcommon.size(); i1++) {
										for (int j1 = 0; j1 < allcommon.get(i1).get(0).size(); j1++) {
											nodeOfNet2.add(allcommon.get(i1).get(0).get(j1));
											nodeOfTree2.add(allcommon.get(i1).get(1).get(j1));
										}
									}
									network = InsertTree.insertTree(nodeOfNet2, nodeOfTree2, network, network1, copyTree);
									network.reticulates.addAll(network1.reticulates);
									network.RNum = network.reticulates.size();
									networks.remove(network1);
								}
								if(flag[0]==0){
									networks.remove(networks.get(flag[1]));
								}
								break;
							}
						}
					}
					
				}
				
				networks = Network.createRoot(networks);
				long endTime=System.currentTimeMillis();
				System.out.println("Running time is "+((endTime-startTime)/1000.0)+"s.");
				int[] num = new int[1];
				num[0] = 0;
				String netName = PrintOut.print(networks.get(0).root,  num);
				System.out.println(netName);
				
				
			}
		
	
	public static int deepth(Node node , int d){
		if(node == null){
			return d;
		}else{
			node.d = d;
			if(node.children.size()>0){
				deepth(node.children.get(0), d+1);
			}
			if(node.children.size()==2){
				deepth(node.children.get(1), d+1);
			}
			if(node.children.size()==0){
				if(ans<d)
					ans = d;
			}
		}
		return ans;
	}
	
	
	public static ArrayList<Network> NodeToNet(int d, Network net){
		ArrayList<Network> nets = new ArrayList<Network>();
		for (int i = 0; i < net.net.size(); i++) {
			if(net.net.get(i).d==d){
				String name = net.net.get(i).value;
				Network n1 = new Network();
				n1.net = ToNet.toNet(name);
				nets.add(n1);
			}
		}
		return nets;
	}
	
}

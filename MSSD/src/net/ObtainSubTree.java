package net;

import java.util.ArrayList;

import Distance.Node;

/**
 * @author song
 *
 */
public class ObtainSubTree {
	//为父节点编号
	public static ArrayList<String[]> serialOfFathers(Network network){
		ArrayList<String[]> indexsOfFathers = new ArrayList<String[]>();
		for (int i = 0; i < network.net.size(); i++) {
			if(network.net.get(i).parents.size()>1){
				network.net.get(i).isRet = true;
				//为该网络节点的父节点编号------
				String[] arr = new String[network.net.get(i).parents.size()];
				for(int j = 0; j < network.net.get(i).parents.size(); j++){
					arr[j] = String.valueOf(j);
				}
				indexsOfFathers.add(arr);
			}
		}
		return indexsOfFathers;
	}
	
	//组合父节点,即要删除的“边”进行组合
	public static ArrayList<String> combination(ArrayList<String[]> list, String[] arr, String str, ArrayList<String> list1){
		for(int i = 0; i<list.size(); i++){
			if(i==list.indexOf(arr)){
				for(String st : arr){
					st = str + st;
					if(i<list.size()-1){
						combination(list, list.get(i+1), st, list1);
					}else if (i == list.size()-1) {
						list1.add(st);
					}
				}
			}
		}
		return list1;
	}
	
	//按照上面的组合删除父节点
	/*
	 * 1、深拷贝当前网络以便后续操作不会改变它 
	 * 2、根据comList（要删除的父节点指针的组合）进行删除 
	 * 		2A、删除当前网络节点的父节点指针只保留一个
	 * 		2B、删除上步父节点的孩子指针 
	 * 		2C、删除这个节点（把这个节点的保留下来的父节点和子节点连在一起）
	 * 3、删除操作结束，清空这个（拷贝）网络的网络节点列表，然后存入网络（树）列表用作后续距离计算
	 */
	public static ArrayList<Network> obtainSubTree(Network network, ArrayList<String> comList){
		ArrayList<Network> netList = new ArrayList<Network>();
		for (int i = 0; i < comList.size(); i++) {
			Network net = new Network();
			net = main.CloneUtils.clone(network);
			for (int j = 0; j < comList.get(i).length(); j++){
				Node father = net.reticulates.get(j).parents.get(Integer.parseInt(String.valueOf(comList.get(i).charAt(j))));
				
				//删除这个节点的父节点
				Node ret = net.reticulates.get(j);
				ret.parents.remove(father);
				//删除对应父节点的子节点
				father.children.remove(ret);
				//删除该节点
				if(ret.parents.size()==1){
					ret.parents.get(0).children.addAll(ret.children);
					ret.parents.get(0).children.remove(ret);
					for (int k = 0; k < ret.children.size(); k++) {
						ret.children.get(k).parents.add(ret.parents.get(0));
						ret.children.get(k).parents.remove(ret);
					}
					net.net.remove(ret);
				}else if(ret.parents.size()>1){
					//int num = ret.parents.size();
					//System.out.println("网络节点  " + ret.value + " 的初始父节点大于2：" + num);
				}else{
					System.out.println("该节点：" + ret.value + "不是网络节点");
				}
				/*
				 * if(comList.get(i).charAt(j)=='0'){ //把该节点的父节点的子节点设为该节点的子节点
				 * ret.parents.get(1).children.addAll(ret.children); //删除指向该节点的指针（父->子）
				 * ret.parents.get(1).children.remove(ret); //把该节点的子节点的父节点设为该节点的父节点
				 * //删除指向该节点的指针（子->父） for (int k = 0; k < ret.children.size(); k++) {
				 * ret.children.get(k).parents.add(ret.parents.get(1));
				 * ret.children.get(k).parents.remove(ret); } }
				 * if(comList.get(i).charAt(j)=='1'){ //把该节点的父节点的子节点设为该节点的子节点
				 * ret.parents.get(0).children.addAll(ret.children); //删除指向该节点的指针（父->子）
				 * ret.parents.get(0).children.remove(ret); //把该节点的子节点的父节点设为该节点的父节点
				 * //删除指向该节点的指针（子->父） for (int k = 0; k < ret.children.size(); k++) {
				 * ret.children.get(k).parents.add(ret.parents.get(0));
				 * ret.children.get(k).parents.remove(ret); } }
				 */
				net.RNum--;
			}
			net.reticulates.clear();
			netList.add(net);
		}
		//删除出度为1的点
		for (int i = 0; i < netList.size(); i++) {
			Network net = new Network();
			net = netList.get(i);
			for (int j = 0; j < net.net.size(); j++) {
				if(net.net.get(j).children.size()==1){
					net.net.get(j).children.get(0).parents.remove(net.net.get(j));
					net.net.remove(net.net.get(j));
				}
				//if(net.net.get(j).parents.size()==1)
			}
		}
		return netList;
	}
	
	
}

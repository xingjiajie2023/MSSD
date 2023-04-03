package net;


import Distance.Node;

public class PrintOut {

	public static String nodeName = "";
	
	public static String print(Node node,int num){
		System.out.println("print in!");
		printNode(node, num);
		//System.out.println(nodeName);
		//nodeName+=';';
		return nodeName;
	}
	
	private static void printNode(Node node, int num) {
		// TODO Auto-generated method stub
		if(node.parents.size()<2){
			if(node.children.size()==0){
				nodeName+=node.value;
				return;
			}
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				printNode(node.children.get(i),num);
				nodeName+=',';
			}
			printNode(node.children.get(node.children.size()-1), num);
			nodeName+=')';
		}else if(node.parents.size()>1&&node.visited == false){
			node.strflag=getFlagK(num);
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				printNode(node.children.get(i), num);
				nodeName+=',';
			}
			printNode(node.children.get(node.children.size()-1), num);
			nodeName+=')';
			nodeName+='#';
			nodeName+=node.strflag;
			node.visited=true;
		}else if(node.visited == true){
			nodeName+='#';
			nodeName+=node.strflag;
		}
	}

	public static String print(Node node, int[] num){
		PrintTree(node, num);
		nodeName+=';';
		return nodeName;
	}

	private static void PrintTree(Node node, int[] num) {
		// TODO Auto-generated method stub
		if(node.parents.size()<2){
			if(node.children.size()==0){
				nodeName+=node.value;
				return;
			}
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				PrintTree(node.children.get(i), num);
				nodeName+=',';
			}
			PrintTree(node.children.get(node.children.size()-1), num);
			nodeName+=')';
		}else if(node.parents.size()>1&&node.visited == false){
			node.strflag=getFlag(num);
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				PrintTree(node.children.get(i), num);
				nodeName+=',';
			}
			PrintTree(node.children.get(node.children.size()-1), num);
			nodeName+=')';
			nodeName+='#';
			nodeName+=node.strflag;
			node.visited=true;
		}else if(node.visited == true){
			nodeName+='#';
			nodeName+=node.strflag;
		}
	}

	private static String getFlagK(int num) {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append('K');
		buf.append(num);
		num++;
		String string = buf.toString();
		return string;
	}
	
	private static String getFlag(int[] num) {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append('H');
		buf.append(num[0]);
		num[0]++;
		String string = buf.toString();
		return string;
	}
	
}

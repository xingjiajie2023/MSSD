package net;

import java.io.PrintWriter;

import Distance.Node;

public class PrintNet {

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
				System.out.print(node.value);
				nodeName+=node.value;
				return;
			}
			System.out.print('(');
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				printNode(node.children.get(i),num);
				System.out.print(',');
				nodeName+=',';
			}
			printNode(node.children.get(node.children.size()-1), num);
			System.out.print(')');
			nodeName+=')';
		}else if(node.parents.size()>1&&node.visited == false){
			node.strflag=getFlagK(num);
			System.out.print('(');
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				printNode(node.children.get(i), num);
				System.out.print(',');
				nodeName+=',';
			}
			printNode(node.children.get(node.children.size()-1), num);
			System.out.print(')');
			nodeName+=')';
			System.out.print('#');
			nodeName+='#';
			System.out.print(node.strflag);
			nodeName+=node.strflag;
			node.visited=true;
		}else if(node.visited == true){
			System.out.print('#');
			nodeName+='#';
			System.out.print(node.strflag);
			nodeName+=node.strflag;
		}
	}

	public static String print(Node node, PrintWriter out,int[] num){
		PrintTree(node, out,num);
		System.out.println(';');
		out.write(';');
		nodeName+=';';
		out.println();
		out.flush();
		return nodeName;
	}

	private static void PrintTree(Node node, PrintWriter out, int[] num) {
		// TODO Auto-generated method stub
		if(node.parents.size()<2){
			if(node.children.size()==0){
				System.out.print(node.value);
				nodeName+=node.value;
				out.print(node.value);
				return;
			}
			System.out.print('(');
			out.write('(');
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				PrintTree(node.children.get(i), out,num);
				System.out.print(',');
				out.write(',');
				nodeName+=',';
			}
			PrintTree(node.children.get(node.children.size()-1), out,num);
			System.out.print(')');
			out.write(')');
			nodeName+=')';
		}else if(node.parents.size()>1&&node.visited == false){
			node.strflag=getFlag(num);
			System.out.print('(');
			out.write('(');
			nodeName+='(';
			for (int i = 0; i < node.children.size() - 1; i++) {
				PrintTree(node.children.get(i), out,num);
				System.out.print(',');
				out.write(',');
				nodeName+=',';
			}
			PrintTree(node.children.get(node.children.size()-1), out,num);
			System.out.print(')');
			out.write(')');	
			nodeName+=')';
			System.out.print('#');
			out.write('#');
			nodeName+='#';
			System.out.print(node.strflag);
			out.write(node.strflag);
			nodeName+=node.strflag;
			node.visited=true;
		}else if(node.visited == true){
			System.out.print('#');
			out.write('#');
			nodeName+='#';
			System.out.print(node.strflag);
			out.write(node.strflag);
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

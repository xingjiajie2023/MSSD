package Distance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ToNet {  //Convert strings to networks, nodes and relationships between them.
	public static ArrayList<Node> net=null; //Used to store the network nodes to be generated.
	public static Map<String,Node> record=null;  //Used to record the correspondence between a network node and its identifier.
	public static ArrayList<Node> toNet(String info){  
		String pattern = ":[0-9.]*";
		info = info.replaceAll(pattern, "");
		net=new ArrayList<Node>();
		record=new HashMap<String,Node>(); 
		net.add(new Node());
		mkNod(net.get(0),info.substring(0, info.length()));
		return net;
	}
	public static void mkNod(Node node,String value){ //This method is used to create nodes and determine the parent-child node relationship and determine the value.
		node.value=value;
		if(record.containsKey(value)){  //Check if the value is the identifier of another network node.
			Node newNode=record.get(value); //Fetch the corresponding node.
			for(int i=0;i<newNode.parents.size();i++){
				newNode.parents.get(i).children.remove(record.get(value));
			}
			newNode.parents.addAll(node.parents);
			for(int i=0;i<newNode.parents.size();i++){
				newNode.parents.get(i).children.add(newNode);
				newNode.parents.get(i).children.remove(node);
			}//让父亲知道自己的孩子
			net.remove(record.get(value));
			net.remove(node);  //Remove the original node from the network.
			net.add(newNode);   //Add the updated node.
			record.put(value, newNode);//Update the key-value pair, the key remains the same, the value node has a new parent node.
		}
		if((!(record.containsKey(value)))&&(!(value.contains("(")))&&(!(value.contains(",")))&&(!(value.contains(")")))){
			//If there is no (,), then the node should be a leaf node.
			LeafNode newLeafNode=new LeafNode(); //If the condition is satisfied, it means it is a leaf node.
			newLeafNode.parents.addAll(node.parents);  //Add parent node.
			newLeafNode.value = value ;  //Assignment.
			for(int i=0;i<newLeafNode.parents.size();i++){
				newLeafNode.parents.get(i).children.add(newLeafNode);
				newLeafNode.parents.get(i).children.remove(node);
			}
			net.remove(node);  //Remove node nodes from the net array.
			net.add(newLeafNode); //Add the leaf nodes to it.
		}
		if(value.startsWith("(") && value.endsWith(")")){
			Stack<Character> stk = new Stack<Character>();
			ArrayList<Integer> comma=new ArrayList<Integer>();
			for(int i=0;i<value.length();i++){  
				if(value.charAt(i)=='('){
					stk.push(value.charAt(i));  //If the character is '(' then it goes on the stack.
				}
				if(value.charAt(i)==')'){
					stk.pop();    //If ')' is encountered, a '(' is stacked to match it.
				}
				if(value.charAt(i)==','&&stk.size()==1){
					comma.add(i);   //Record the position of the commas that distinguish his child nodes.
				}
			}
			comma.add(0,0);  //Add left bracket position.
			comma.add((value.length()-1));    //Add right bracket position.
			for(int j=0;j<(comma.size()-1);j++){
				net.add(new Node());
				net.get(net.size()-1).value=value.substring(comma.get(j)+1,comma.get(j+1));	
				node.children.add(net.get(net.size()-1));
				net.get(net.size()-1).parents.add(node);
				mkNod(net.get(net.size()-1),net.get(net.size()-1).value);
				}
		}
		if(value.startsWith("(")&&value.contains(")")&&(!(value.endsWith(")")))){
			Stack<Character> stk = new Stack<Character>();
			ArrayList<Integer> comma=new ArrayList<Integer>();
			for(int i=0;i<value.length();i++){  
				if(value.charAt(i)=='('){
					stk.push(value.charAt(i));  //If the character is '(' then it goes on the stack.
				}
				if(value.charAt(i)==')'){
					if(stk.size()==1){comma.add(i);}
					stk.pop();    //If ')' is encountered, a '(' is stacked to match it
				}
				if(value.charAt(i)==','&&stk.size()==1){
					comma.add(i);   //Record the position of the commas that distinguish his child nodes.
				}
			}
			comma.add(0,0);  //Add left bracket position
				record.put(value.substring(comma.get(comma.size()-1)+1,value.length()), node);
				for(int j=0;j<(comma.size()-1);j++){
					net.add(new Node());
					net.get(net.size()-1).value=value.substring(comma.get(j)+1,comma.get(j+1));	
					node.children.add(net.get(net.size()-1));
					net.get(net.size()-1).parents.add(node);
					mkNod(net.get(net.size()-1),net.get(net.size()-1).value);
			}
				node.value=value.substring(0,comma.get(comma.size()-1)+1);
		}
    }
}
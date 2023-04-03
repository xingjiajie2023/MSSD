package Distance;

import java.util.ArrayList;
import java.util.Stack;

public class MkNetCollOne {
	public static ArrayList<ArrayList<Node>> mkNetCollOne(ArrayList<Node> net){
		ArrayList<ArrayList<Node>> result=null;
		result=new ArrayList<ArrayList<Node>>();
		for(int i=0;i<net.size();i++){
			if(!net.get(i).parents.isEmpty()){
				ArrayList<Node> bridge=new ArrayList<Node>();		
				Stack<Node> temp=new Stack<Node>();
				temp.add(net.get(i));
				Node node=null;
				while(!temp.isEmpty()){
					node=temp.get(temp.size()-1);
					if(node instanceof LeafNode&&simpleIsAbleAdd(bridge,node)){
						bridge.add(node);
					}
					temp.pop();
					if(!node.children.isEmpty()){
						temp.addAll(node.children);
					}
				}
				if(IsAbleAdd(result,bridge)){
					result.add(bridge);
				}
			}
		}
		return result;
	}
	public static boolean IsAbleAdd(ArrayList<ArrayList<Node>> list,ArrayList<Node> node){
		for(int i=0;i<list.size();i++){
			if(CompareSetOne.compareArrayOne(node, list.get(i))){
				return false;
			}
		}
		return true;
	}
	public static boolean simpleIsAbleAdd(ArrayList<Node> list, Node node){
		for(int i=0;i<list.size();i++){
			if(node.value.equals(list.get(i).value)){
				return false;
			}
		}
		return true;
	}
}

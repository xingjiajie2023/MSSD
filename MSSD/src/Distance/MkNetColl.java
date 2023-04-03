package Distance;
import java.util.ArrayList;
import java.util.Stack;
public class MkNetColl {
	public static ArrayList<int[]> mkNetColl(ArrayList<Node> net,ArrayList<Node> leaf,int leafNum){
		ArrayList<int[]> result=null;
		result=new ArrayList<int[]>();
		for(int a = 0; a < net.size(); a++){
			int[] record = new int[leafNum];
			for(int b = 0; b < leafNum; b++){
				record[b]=pathNum(net,leaf,a,b);
			}
			//The IsAbleAdd static method in the MkNetCollOne class is used to determine whether a cluster is already contained in the clusterSet collection.
			if(IsAbleAdd(result,record)){
				
				result.add(record);
			}
		}
		
		return result;
	}
	
	//求节点的簇
	public static int pathNum(ArrayList<Node> net,ArrayList<Node> leaf,int n,int l){
		int number=0;
		Stack<Node> temp=new Stack<Node>();
		//先将传进来的节点放入栈temp中
		temp.add(net.get(n));
		Node node=null;
		while(!temp.isEmpty()){
			node=temp.get(temp.size()-1); //第0个节点
			//如果与叶子集合中的第I个节点相同，那么num++
			if(node.value.equals(leaf.get(l).value)){
				number++;
			}
			//比较完出栈
			temp.pop();
			if(!node.children.isEmpty()){
				//再将刚出栈的节点的子节点入栈
				temp.addAll(node.children);
			}
		}
		//num==1的情况就是传进来的是叶子节点
		//如果大于1就返回1
		//如果不大于1就返回num，不是0就是1
		if(number>1){
			return 1;
		}
		return number;
	}
	public static boolean IsAbleAdd(ArrayList<int[]> list,int[] node){
		for(int i=0;i<list.size();i++){
			if(CompareSet.compareArray(list.get(i),node)){
				return false;
			}
		}
		return true;
	}
	public static boolean add(ArrayList<Node> list,Node node){
		for(int i=0;i<list.size();i++){
			if(node.value.equals(list.get(i).value)){
				return false;
			}
		}
		return true;

	}
}

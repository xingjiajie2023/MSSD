

package Distance;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Node implements Serializable{
	public ArrayList<Node> parents=new ArrayList<Node>();//Parent of a node
	public ArrayList<Node> children=new ArrayList<Node>();//Children of a node
	public String value=null;//Store the value of this node
	
	public boolean isRet = false;//Determine whether it is a network node.
	public int d;//Depth of nodes.
	public boolean visited = false;
	public String strflag;
	public int label;
	public boolean isNewRoot = false;
	public String newRootFlag = "";
}

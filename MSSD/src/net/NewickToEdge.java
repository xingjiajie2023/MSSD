package net;


import main.CloneUtils;

public class NewickToEdge {

	public static String NeToEd(Network network){
		String s = "";
		int rootNum = 0;
		Network net = CloneUtils.clone(network);
		for (int i = 0; i < network.net.size(); i++) {
			if(network.net.get(i).parents.size()==0){
				rootNum++;
			}
		}
		if (rootNum>1) {
			net = Network.createRootForClone(net);
		}
		for (int i = 0; i < net.net.size(); i++) {
			
			if(net.net.get(i).children.size()>0){
				for (int j = 0; j < net.net.get(i).children.size(); j++) {
					s+=Integer.toString(i)+" ";
					if(net.net.get(i).children.get(j).children.size()==0){
						s+=net.net.get(i).children.get(j).value+"\n";
					}else{
						int index = net.net.indexOf(net.net.get(i).children.get(j));
						s+=Integer.toString(index)+"\n";
					}
				}
			}
		}
		//System.out.println("edge set "+s);
		return s;
	}
	
	/*public static String DelBranchLengthForNet(String s){
		
	}*/
	
	public static String DelBranchLength(String s){
		//(((seq00002_pop1:0.000048,seq00001_pop1:0.000048):0.000009,seq00003_pop1:0.000057):0.003580,(((seq00008_pop2:0.000005,seq00004_pop2:0.000005):0.000090,(seq00010_pop2:0.000014,seq00005_pop2:0.000014):0.000081):0.000471,((seq00006_pop2:0.000003,seq00007_pop2:0.000003):0.000019,seq00009_pop2:0.000022):0.000544):0.003071,outgroup_pop0:0.100000);
		s = s.replaceAll(",outgroup_pop0:0.100000", "");//,outgroup_pop0:0.100000
		s = s.replaceAll(",outgroup_pop0", "");
		//s = s.substring(1,s.length());
		StringBuffer buffer = new StringBuffer(s);
		char start = ':';
		char end1 = ',';
		char end2 = ')';
		for (int i = 0; i < buffer.length(); i++) {
			if(buffer.charAt(i)==start){
				int index1 = i;
				int index2 = i;
				while(true){
					if(buffer.charAt(index2)==end1||buffer.charAt(index2)==end2){
						break;
					}
					index2++;
				}
				if(index2!=-1)
					buffer.delete(index1, index2);
				
				//i = index2 + 1;
			}
			
		}
		
		for (int i = 0; i < buffer.length();) {
			if(buffer.charAt(i)=='e'){
				buffer.deleteCharAt(i);
			}
			else if(buffer.charAt(i)=='q'){
				buffer.deleteCharAt(i);
			}
			else if(buffer.charAt(i)=='p'){
				buffer.deleteCharAt(i);
			}
			else if(buffer.charAt(i)=='o'){
				buffer.deleteCharAt(i);
			}
			else if(buffer.charAt(i)=='0'&&buffer.charAt(i-1)=='s'){
				buffer.delete(i, i+2);
				i++;
			}
			else{
				i++;
			}
		}
		if(s.indexOf("H")<0){
		buffer.deleteCharAt(buffer.length()-1);}
		return buffer.toString();
	}
	
}

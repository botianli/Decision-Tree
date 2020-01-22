//ID 260824476
//Name; Li Botian
import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree implements Serializable {

	DTNode rootDTNode;
	int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split
	//Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
	public static final long serialVersionUID = 343L;
	public DecisionTree(ArrayList<Datum> datalist , int min) {
		minSizeDatalist = min;
		rootDTNode = (new DTNode()).fillDTNode(datalist);
	}

	class DTNode implements Serializable{
		//Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
		public static final long serialVersionUID = 438L;
		boolean leaf;
		int label = -1;      // only defined if node is a leaf
		int attribute; // only defined if node is not a leaf
		double threshold;  // only defined if node is not a leaf

		DTNode left, right; //the left and right child of a particular node. (null if leaf)

		DTNode() {
			leaf = true;
			threshold = Double.MAX_VALUE;
		}
	



		// this method takes in a datalist (ArrayList of type datum) and a minSizeInClassification (int) and returns
		// the calling DTNode object as the root of a decision tree trained using the datapoints present in the
		// datalist variable
		// Also, KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
		DTNode fillDTNode(ArrayList<Datum> datalist) {

			//YOUR CODE HERE
			
			 if(datalist.size()>= minSizeDatalist) {
				
				boolean samelabel=true;
				
				int firstattribut= datalist.get(0).y;
				
				for(Datum a : datalist){
					
				if(a.y!=firstattribut) {//check if data point has all same label
						
					samelabel=false;//if samelabel is false for loop break
						
					break;
						
				 	}
				}
				
		
				
				if(samelabel==true) {//if it has same label, return it has a leaf

					DTNode newleaf=new DTNode();	
					newleaf.leaf=true;
				
					newleaf.label=datalist.get(0).y;
				
					
				return newleaf;
				
				}else {
					
					int best_attr=-1;
					double best_threshold=-1;
					double best_avg_entropy=Double.MAX_VALUE;
					
					ArrayList<Datum>  list1=new ArrayList<Datum>();//testing datalist
					ArrayList<Datum>  list2=new ArrayList<Datum>();
					
					
					for(int i=0;i<=1;i++) {//check all x[0] then x[1] to fint best split
						
						for(Datum a :datalist) {
						
							double split_attr=a.x[i];
							
							ArrayList<Datum>  datalist1=new ArrayList<Datum>(); //less than split test
							ArrayList<Datum>  datalist2=new ArrayList<Datum>();//grester thsan split test
						
							for(Datum b : datalist) {
							
							
							
							if(b.x[i]<split_attr) {
								
								datalist1.add(b);//less than datalist
								
							}else {
								datalist2.add(b);//add to more than datalist
								
							}
							
						}
						
						double w1=((double)datalist1.size())/((double)(datalist.size()));// w1/w1+2
						double w2=((double)datalist2.size())/((double)(datalist.size()));//w2/w1+w2
						
						double current_average_entropy= (w1*calcEntropy(datalist1)+ w2*calcEntropy(datalist2));//calculate average
						
						if(best_avg_entropy>current_average_entropy) {//find best average at this step
							
							best_avg_entropy=current_average_entropy;
							best_threshold=split_attr;
							best_attr=i;
						
						}
					
					}
						
				}
				
//-------------create new node step with best threshold and attribute-----------------------
					
					DTNode interNode=new DTNode();
					
					interNode.leaf=false;
					interNode.attribute=best_attr;
					interNode.threshold=best_threshold;
					
					for(Datum a:datalist) {
						
						if(a.x[best_attr]<best_threshold) {
							list1.add(a);
						}else {
							list2.add(a);
						}
					}
					interNode.left=fillDTNode(list1);
					interNode.right=fillDTNode(list2);
					
					return interNode;
					  
				}
			}
//-----------------if size smaller then min size, do below--------------------------------------------------------------------------------------------			 
			
			 else{
				
				DTNode leafNode=new DTNode();
				leafNode.leaf=true;
				leafNode.label=findMajority(datalist);
				
				return leafNode;
			
			}
			
		
		}
		
	
	



		//This is a helper method. Given a datalist, this method returns the label that has the most
		// occurences. In case of a tie it returns the label with the smallest value (numerically) involved in the tie.
		int findMajority(ArrayList<Datum> datalist)
		{
			int l = datalist.get(0).x.length;
			int [] votes = new int[l];

			//loop through the data and count the occurrences of datapoints of each label
			for (Datum data : datalist)
			{
				votes[data.y]+=1;
			}
			int max = -1;
			int max_index = -1;
			//find the label with the max occurrences
			for (int i = 0 ; i < l ;i++)
			{
				if (max<votes[i])
				{
					max = votes[i];
					max_index = i;
				}
			}
			return max_index;
		}




		// This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
		// returns its corresponding label, as determined by the decision tree
		int classifyAtNode(double[] xQuery) {
			//YOUR CODE HERE
			if(this.leaf==true) {
				return this.label;
			}else {
				if(xQuery[this.attribute]<this.threshold) {
					return this.left.classifyAtNode(xQuery);
				}else {
					return this.right.classifyAtNode(xQuery);
				}
				
			}

			 //dummy code.  Update while completing the assignment.
		}


		//given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
		//at DTNode object passed as the parameter
		public boolean equals(Object dt2)
		{	
			if(!(dt2 instanceof DTNode)) {//return false if its not a root
				System.out.println("a");
				return false;
		}
			
			DTNode compar=(DTNode) dt2; //make dt2 a node, easier for later
			
			int att1=this.attribute;
			int att2=compar.attribute;
			int label1=this.label;
			int label2= compar.label;
			
			boolean leaf1=this.leaf;
			boolean leaf2=compar.leaf;
			
			double thres1=this.threshold;
			double thres2=compar.threshold;
			
			DTNode childleft1=this.left;
			DTNode childright1=this.right;
			DTNode childleft2=compar.left;
			DTNode childright2=compar.right;
			//boolean variable, less condition to check this way
			
			boolean rootEqual=false;
			boolean leftEqual=false;
			boolean rightEqual=false;
			
			if(leaf1==true&&leaf2==true) {
				
				if(label1==label2) {
					rootEqual=true;
				}
			}
			if(leaf1!=true&&leaf2!=true) {
				
				if((att1==att2)&&(thres1==thres2)) {
					rootEqual=true;
				}
			}
				if(childleft1!=null&&childleft2!=null) {
					
					leftEqual=childleft1.equals(childleft2);
				}
				else if (childleft1==null&&childleft2==null) {
					
					leftEqual=true;
				}
				if(childright1!=null&&childright2!=null) {
					rightEqual=childright1.equals(childright2);
				}
				else if(childright1==null&&childright2==null) {
					rightEqual=true;
				}
				
			//System.out.print("a");
				return (rootEqual&&leftEqual&&rightEqual);
			}
}



	//Given a dataset, this retuns the entropy of the dataset
	double calcEntropy(ArrayList<Datum> datalist)
	{
		double entropy = 0;
		double px = 0;
		float [] counter= new float[2];
		if (datalist.size()==0)
			return 0;
		double num0 = 0.00000001,num1 = 0.000000001;

		//calculates the number of points belonging to each of the labels
		for (Datum d : datalist)
		{
			counter[d.y]+=1;
		}
		//calculates the entropy using the formula specified in the document
		for (int i = 0 ; i< counter.length ; i++)
		{
			if (counter[i]>0)
			{
				px = counter[i]/datalist.size();
				entropy -= (px*Math.log(px)/Math.log(2));
			}
		}

		return entropy;
	}


	// given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
	int classify(double[] xQuery ) {
		DTNode node = this.rootDTNode;
		return node.classifyAtNode( xQuery );
	}

    // Checks the performance of a DecisionTree on a dataset
    //  This method is provided in case you would like to compare your
    //results with the reference values provided in the PDF in the Data
    //section of the PDF

    String checkPerformance( ArrayList<Datum> datalist)
	{
		DecimalFormat df = new DecimalFormat("0.000");
		float total = datalist.size();
		float count = 0;

		for (int s = 0 ; s < datalist.size() ; s++) {
			double[] x = datalist.get(s).x;
			int result = datalist.get(s).y;
			if (classify(x) != result) {
				count = count + 1;
			}
		}

		return df.format((count/total));
	}

   

	//Given two DecisionTree objects, this method checks if both the trees are equal by
	//calling onto the DTNode.equals() method
	public static boolean equals(DecisionTree dt1,  DecisionTree dt2)
	{
		boolean flag = true;
		flag = dt1.rootDTNode.equals(dt2.rootDTNode);
		return flag;
	}
}
	
	

	



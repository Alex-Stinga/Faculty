import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;


public class Assocition_rules_Apriori {
	
	private final File file = new File("C:\\Users\\Alex\\Desktop\\test-db.txt"); 	
	private int minSup = 2;
	private ArrayList<String> tranzactions = readDB();
	
	
	private ArrayList<String> readDB() {
		
	  ArrayList<String> tranzactions = new ArrayList<>();
	  BufferedReader br = null;
	  String st = null; 
	  
	try {
		br = new BufferedReader(new FileReader(file));
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
	  
	 try {
		 
		while ((st = br.readLine()) != null) {
			tranzactions.add(st);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return tranzactions;
	}
	
	private ArrayList<String> getUnique(ArrayList<String> list) {
		
		ArrayList<String> itemsList = new ArrayList<>();	
		List<String> trans_items;
		Iterator<String> li = list.iterator();
		String tmpString, tmpVal;
		int i;
		
		while(li.hasNext()) {
			tmpString = li.next();
			trans_items =  Arrays.asList(tmpString.split(",[ ]*"));
					
			for(i=0;i <trans_items.size();i++) {
				tmpVal = trans_items.get(i);
				if(!itemsList.contains(tmpVal))
					itemsList.add(tmpVal);
				}
		}
//		System.out.println("Unique "+itemsList.toString());
		return itemsList;
	}

	private ArrayList<String> itemsetSize1(ArrayList<String> candidates){
		
		LinkedHashMap<String, Integer> newCandidates = new LinkedHashMap<>();
		ArrayList<String> itemsList = new ArrayList<>();	
		
		Iterator<String> li = tranzactions.iterator();
		String tmpString;
		List<String> trans_items;
		
		//init the candidates hashmap
		int count = 0;
		candidates.forEach(candidat -> newCandidates.put(candidat, count));
//		candidates.forEach(i -> System.out.println(i));
		
		while(li.hasNext()) {
			//for each transaction
			tmpString = li.next();
			trans_items =  Arrays.asList(tmpString.split(",[ ]*"));
			
			//update the counter of each item
			for(int i = 0; i<trans_items.size(); i++) {
				newCandidates.put(trans_items.get(i), newCandidates.get(trans_items.get(i)) + 1);
			}		
		}
		
		for (Entry<String, Integer> m: newCandidates.entrySet()) { 
			if(m.getValue() >= minSup )
				itemsList.add(m.getKey());
        } 
//		
//		for (Entry<String, Integer> m: newCandidates.entrySet()) { 
//			System.out.println(m.getKey()+" "+m.getValue()); 
//		} 
//		itemsList.forEach(i -> System.out.println(i));
		
		return itemsList;
	}
	
	private ArrayList<ArrayList<String>> itemsetSize2(ArrayList<String> candidates){
		
		ArrayList<String> tmpSet = new ArrayList<>();
		ArrayList<ArrayList<String>> items = new ArrayList<>();	 

		int i,j;
		for(i = 0; i < candidates.size(); i++) {
			for(j = i+1;j < candidates.size(); j++) {
			
				tmpSet.add(candidates.get(i));
				tmpSet.add(candidates.get(j));	
	
				items.add(tmpSet);
//				System.out.println(tmpSet.toString());
				tmpSet = new ArrayList<>();
			}
		}
//		System.out.println(items.toString());
		return items;
	}
	
	private void apriori() {
		
		//create 1-itemset
		ArrayList<String> uniqueItems = getUnique(tranzactions);
		ArrayList<String> candidates = itemsetSize1(uniqueItems);
//		System.out.println(candidates.toString());
		
		//generate 2-itemset
		ArrayList<ArrayList<String>> items2 = itemsetSize2(candidates);
		ArrayList<ArrayList<String>> countCandidates = countOccurences(items2);
		ArrayList<ArrayList<String>> finalArrayList = new ArrayList<>();
		ArrayList<ArrayList<String>> tmpCandidates = new ArrayList<>();
		
		int cicle = 3;
		do{
			tmpCandidates = generateNitems(countCandidates, cicle++);
			if(tmpCandidates.size() >=2 ) {
				countCandidates = countOccurences(tmpCandidates);	
				finalArrayList.addAll(countCandidates);
			}
			break;
		}while(!countCandidates.isEmpty()); 
		
//		System.out.println(finalArrayList );
		generateAssocRules(finalArrayList);
	}
	
	private ArrayList<ArrayList<String>> countOccurences(ArrayList<ArrayList<String>> items){
		
		ArrayList<ArrayList<String>> finalCandidates = new ArrayList<>();
		
		Iterator<String> tranzOp = tranzactions.iterator();
		String tmpString;
		List<String> trans_items;
		int i;
		ArrayList<String> tmpList = new ArrayList<>();
		LinkedHashMap<ArrayList<String>, Integer> counterHolder = new LinkedHashMap<>();	 
		
		//init hashmap
		for(i = 0;i<items.size();i++) {
			counterHolder.put(items.get(i),0);
		}
		
		while(tranzOp.hasNext()) {
			//for each transaction line
			tmpString = tranzOp.next();
			trans_items =  Arrays.asList(tmpString.split(",[ ]*"));
			
			//for each itemset
			for(i = 0;i<items.size();i++) {
				tmpList = items.get(i);
				
				//count occurences by updating the hashmap
				if(commonList(tmpList, trans_items)) {
//				if(trans_items.contains(tmpList))
					counterHolder.put(tmpList, counterHolder.get(tmpList) + 1);}
			}		
		}

		//pruning
		for (Entry<ArrayList<String>, Integer> m: counterHolder.entrySet()) { 
//			System.out.println(m.getKey().toString() + " "+ m.getValue()); 
			if( m.getValue() >= minSup)
				finalCandidates.add(m.getKey());
		} 
		
//		System.out.println("finalCandidates"+finalCandidates.toString());
		return finalCandidates;
	}
	
	private ArrayList<ArrayList<String>> generateNitems(ArrayList<ArrayList<String>> items, int cicle){
		
//		System.out.println("Cicle "+cicle);
		ArrayList<ArrayList<String>> newItems = new ArrayList<>();
		ArrayList<String> tmpSet1,tmpSet2, tmpItem = new ArrayList<>();
		
		int i,j;
		for(i = 0; i < items.size(); i++) {
			for(j = i+1;j < items.size(); j++) {
				
				/*get the 2 list*/
				tmpSet1 = items.get(i);
				tmpSet2 = items.get(j);
				
				/*det if are cicle-1 elem common*/
				if(canJoin(tmpSet1, tmpSet2,cicle)) {
					tmpItem = joinList(tmpSet1, tmpSet2);
					
					/*det if has frequent subsets*/
					if(hasFrequent(items,tmpItem,cicle))
						newItems.add(tmpItem);
				}
			}
		}
		
//		System.out.println("generateNitems "+newItems.toString());
		return newItems;
	}
	
	private boolean hasFrequent(ArrayList<ArrayList<String>> prevItemset ,ArrayList<String> currentIemset, int cicle) {
		
		ArrayList<ArrayList<String>> genSubsets = getSubsets(currentIemset,cicle-1);
//		genSubsets.forEach(i -> System.out.println(i));
		
		int index, count =0;
		for(index=0;index<genSubsets.size();index++) {
			for(int j=0;j<prevItemset.size();j++) {
				if(prevItemset.get(j).containsAll(genSubsets.get(index))) {
					count++;
//					System.out.println(genSubsets.get(index) +"  "+prevItemset.get(j).containsAll(genSubsets.get(index)));
					break;
				}
			}
		}
		return count == genSubsets.size();
	}
	
	private boolean canJoin(ArrayList<String> firstArray, ArrayList<String> secondArray, int cicle) {
		
		Collections.sort(firstArray);Collections.sort(secondArray);
		boolean sameItems = true;
		for(int i = 0; i < firstArray.size() - 1; i++) {
			if(firstArray.get(i).compareTo(secondArray.get(i)) != 0)
				 sameItems = false;
		}

		return ((firstArray.get(firstArray.size() - 1 ).compareTo(secondArray.get(secondArray.size() - 1 )) != 0)  && sameItems);
	}
	
	private boolean commonList(ArrayList<String> itemset, List<String> toFind) {
		
		int counter = 0;
		for(int i = 0; i < itemset.size(); i++) {
			if(toFind.contains(itemset.get(i)))
				counter++;
		}
		return (counter == itemset.size());
	}
	
	private ArrayList<String> joinList(ArrayList<String> firstArray, ArrayList<String> secondArray){

//		System.out.println("JOIN"+firstArray.toString() + " "+ secondArray.toString()); 
//		Collections.sort(firstArray);Collections.sort(secondArray);
		ArrayList<String> newList = firstArray;
		newList.add(secondArray.get(firstArray.size() - 1));
		
//		System.out.println("newList Join "+newList.toString()+"\n\n");
		return newList;
	}
	
	private void getSubsets(List<String> superSet, int nrItemset, int start, ArrayList<String> current,ArrayList<ArrayList<String>> solution) {
	    //successful stop clause
	    if (current.size() == nrItemset) {
	        solution.add(new ArrayList<>(current));
	        return;
	    }
	    //unseccessful stop clause
	    if (start == superSet.size()) return;
	    String x = superSet.get(start);
	    current.add(x);
	    
	    //"guess" x is in the subset
	    getSubsets(superSet, nrItemset, start+1, current, solution);
	    current.remove(x);
	    
	    //"guess" x is not in the subset
	    getSubsets(superSet, nrItemset, start+1, current, solution);
	}

	private ArrayList<ArrayList<String>> getSubsets(ArrayList<String> superSet, int nrItemset) {
		ArrayList<ArrayList<String>> res = new ArrayList<>();
	    getSubsets(superSet, nrItemset, 0, new ArrayList<String>(), res);
	    return res;
	}
	
	private void generateAssocRules(ArrayList<ArrayList<String>> assocCandidates) {
	
		System.out.println("Association rules");
		assocCandidates.forEach(i -> System.out.println(i));
		int pairCount;
		
//		//count each item appearence
		LinkedHashMap<String, Integer> onesHashMap = new LinkedHashMap<>();
		for(int i=0;i<assocCandidates.size();i++) {
			assocCandidates.get(i).forEach(candidat -> onesHashMap.put(candidat, 0));
		}
		countOne(onesHashMap);
//		System.out.println(onesHashMap);

		ArrayList<ArrayList<String>> genSubsets = new ArrayList<>(); 
		for(int item = 0; item < assocCandidates.size(); item++) {	
			genSubsets.addAll(getSubsets(assocCandidates.get(item),2));
		}
		
//		genSubsets.forEach(i -> System.out.println(i));
		
		//generate 1->1 associations
		for(int index = 0;index < genSubsets.size(); index++) {
			pairCount = countPair(genSubsets.get(index));
			System.out.print(genSubsets.get(index).toString() +" ");
			System.out.printf("%.2f",((float) pairCount/onesHashMap.get(genSubsets.get(index).get(0))));
			System.out.println();
		}
		System.out.println("\n----------------------------------------");
		
		//generate 2->1 associations
		//for each final itemset
		for(int index = 0;index < assocCandidates.size(); index++) {
			ArrayList<String> tmpItemset = assocCandidates.get(index);
			System.out.println(tmpItemset +" ");
			//for each 3 subitemsets of the itemset
			for (int index2 = 0; index2 < 3; index2++) {
				System.out.print(genSubsets.get(index2) + " -> ");
				//get the elements in reverse order
				String item = tmpItemset.get(2 - index2);
				System.out.print(item + " ");
				
				//[A,B] -> C
				pairCount = countPair(tmpItemset);
				System.out.printf("%.2f",((float) pairCount/countPair(genSubsets.get(index2))));
				System.out.println();
			}
			System.out.println("-----------");
		}
	}
	
	private LinkedHashMap<String, Integer> countOne(LinkedHashMap<String, Integer> onesHashMap){
		
		Iterator<String> li = tranzactions.iterator();
		String tmpString;
		List<String> trans_items;

		while(li.hasNext()) {
			//for each transaction
			tmpString = li.next();
			trans_items =  Arrays.asList(tmpString.split(",[ ]*"));
			
			//update the counter of each item
			for(int i = 0; i<trans_items.size(); i++) {
				if(onesHashMap.containsKey(trans_items.get(i)))
					onesHashMap.put(trans_items.get(i), onesHashMap.get(trans_items.get(i)) + 1);
			}		
		}	
		return onesHashMap;
	}
	
	
	private int countPair(ArrayList<String> set) {
		
		int count = 0;
		List<String> trans_items;
		Iterator<String> li = tranzactions.iterator();
		String tmpString;
		
		while(li.hasNext()) {
			tmpString = li.next();
			trans_items =  Arrays.asList(tmpString.split(",[ ]*"));
			
			if(trans_items.containsAll(set))
				count++;
		}	
//		System.out.println(count);
		return count;
	}

	
	public static void main(String[] args) {
		Assocition_rules_Apriori t = new Assocition_rules_Apriori();
		ArrayList<String> tranzactionsList = t.readDB();
		System.out.println("Database");
		tranzactionsList.forEach(i -> System.out.println(i));
		System.out.println();
		t.apriori();
	
	}
		
}
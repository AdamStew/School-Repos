package anagram;

import java.util.*;
import java.util.Map.Entry;

public class Anagram {
	public static void main(String [] args){
		TreeMap<String, ArrayList<String>> map = new TreeMap<String, 
				ArrayList<String>>();
		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()){
			String word = scan.nextLine();
			char[] wordArray = word.toCharArray();
			Arrays.sort(wordArray);
			String w = new String();
			for (int i=0; i<wordArray.length; i++){
				w += wordArray[i];
			}
			if(map.containsKey(w)){
				ArrayList<String> al = map.get(w);
				al.add(word);
				map.put(w, al);
			}else{
				ArrayList<String> al = new ArrayList<String>();
				al.add(word);
				map.put(w,al);
			}
		}
		Entry<String, ArrayList<String>> maxEntry = null;
		for(Entry<String, ArrayList<String>> entry : map.entrySet()){
			if(maxEntry == null || entry.getValue().size() > 
					maxEntry.getValue().size()){
				maxEntry = entry;
			}
		}
		System.out.print("Key: " + maxEntry.getKey() + ".  Value(s): " 
				+ maxEntry.getValue() + ".  ");
	}
}

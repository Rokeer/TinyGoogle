import java.io.Serializable;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;

public class InvertedIndex implements Serializable{
	Hashtable<String, LinkedList<IIItem>> list = new Hashtable<String, LinkedList<IIItem>>();
	
	public InvertedIndex() {
		
	}
	
	private InvertedIndex(Hashtable<String, LinkedList<IIItem>> list) {
		this.list = (Hashtable<String, LinkedList<IIItem>>) list.clone();
	}
	
	
	public boolean containsKey(String key) {
		return list.containsKey(key);
	}
	
	public InvertedIndex clone() {
		return new InvertedIndex(list);
	}
	
	public LinkedList<IIItem> get (String word) {
		return list.get(word);
	}
	
	
	public void remove (String id) {
		for (String word : list.keySet()) {
			remove(word, id);
		}
	}
	
	public void remove (String word, String id) {
		LinkedList<IIItem> tmpList = list.get(word);
		IIItem item = null;
		for (int i = 0; i < tmpList.size(); i++) {
			item = tmpList.get(i);
			if (item.getID().equals(id)) {
				tmpList.remove(i);
				break;
			}
		}
	}
	
	public void merge(InvertedIndex newII) {
		
		Hashtable<String, LinkedList<IIItem>> newList = newII.list;
		LinkedList<IIItem> tmpList = null;
		for (String word : newList.keySet()) {
			tmpList = newList.get(word);
			if (list.containsKey(word)) {
				for (int i = 0; i < tmpList.size(); i++) {
					put(word, tmpList.get(i));
				}
				
			} else {
				list.put(word, tmpList);
			}
		}
	}
	
	public void put(String word, IIItem item) {
		LinkedList<IIItem> tmpList = null;
		
		if (list.containsKey(word)){
			tmpList = list.get(word);
			boolean flag = true;
			IIItem tmpItem = null;
			
			
			for(int i = 0; i < tmpList.size(); i++) {
				tmpItem = tmpList.get(i);
				if (tmpItem.getID().equals(item.getID())){
					tmpItem.setCount(tmpItem.getCount() + item.getCount());
					flag = false;
					break;
				}
			}
			if (flag) {
				tmpList.add(item);
			}
			
			Collections.sort(tmpList, new ItemComparator());

		} else {
			tmpList = new LinkedList<IIItem>();
			tmpList.add(item);
			list.put(word, tmpList);
		}
	}
	
	public String toString() {
		String result = "";
		for (String key : list.keySet()) {
			LinkedList<IIItem> tmpList = list.get(key);
			result = result + key +": ";
			for (int i = 0; i < tmpList.size(); i++) {
				result = result + tmpList.get(i).getID() + " -> " + tmpList.get(i).getCount() + "; ";
			}
			result = result + "\n";
		}
		return result;
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		IIItem i1 = new IIItem("1", 1);
//		IIItem i2 = new IIItem("2", 2);
//		IIItem i3 = new IIItem("3", 3);
//		IIItem i4 = new IIItem("2", 3);
//		
//		InvertedIndex ii = new InvertedIndex();
//		ii.put("a", i2);
//		ii.put("a", i3);
//		ii.put("a", i1);
//		ii.put("a", i4);
//		
//		IIItem i21 = new IIItem("1", 1);
//		IIItem i22 = new IIItem("2", 2);
//		IIItem i23 = new IIItem("3", 3);
//		IIItem i24 = new IIItem("2", 3);
//		InvertedIndex ii2 = new InvertedIndex();
//		ii2.put("a", i22);
//		ii2.put("a", i23);
//		ii2.put("a", i21);
//		ii2.put("a", i24);
//		
//		ii.merge(ii2);
//		System.out.println(ii.get("a").get(0).getCount());
//		
//	}

}

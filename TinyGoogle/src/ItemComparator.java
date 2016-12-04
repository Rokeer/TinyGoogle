import java.util.Comparator;

public class ItemComparator implements Comparator<IIItem>{

	@Override
	public int compare(IIItem o1, IIItem o2) {
		return o2.getCount() - o1.getCount();
	}

}

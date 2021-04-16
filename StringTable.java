import java.util.ArrayList;

public class StringTable {
	
	public ArrayList<StringObject> table;
	public int[] hash;
	
	public StringTable() {
		table = new ArrayList<StringObject>();
		hash=new int[100];
		for (int i=0;i<100;i++)
		{
			hash[i]=-1;
		}
	}
	public int search(String name) {
		int r=hash[f(name)];
		if(r!=-1) {
			StringObject s=table.get(r);
			while(true) {
				if(s.string.equals(name)) {
					return r;
				}
				r=s.next;
				if(r==-1) {
					break;
				}
				s=table.get(r);
			}
		}
		return -1;
	}
	public int insert(String name) {
		int se=search(name);
		if (se == -1) {
			table.add(new StringObject(name));
			int h=f(name);
			if (hash[h]==-1) 
				hash[h]=table.size()-1;
			else {
				StringObject s=table.get(hash[h]);
				while(s.next!=-1)
					s=table.get(s.next);
				s.next=table.size()-1;
			}
			return table.size()-1;		
		}			
		return se;
	}
	public int f(String s) {
		char ch[];
	   	ch = s.toCharArray();
	   	int sum=0;
	   	for (int i=1; i < s.length()-1; i++)
		     sum += ch[i];
	   	return sum%100;
	}
	public void printTable() {
		for (StringObject a : table)
			System.out.println(a.string);
		for (int i=0;i<100;i++){
			if(hash[i]!=-1)
			{
				System.out.println("hash: "+i+" index: "+hash[i]);
			}
		}
	}
	
}

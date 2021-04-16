import java.util.ArrayList;

public class SymbolTable {
	
	public ArrayList<Symbol> table;
	public int[] hash;
	
	public SymbolTable() {
		table = new ArrayList<Symbol>();
		hash=new int[100];
		for (int i=0;i<100;i++)
		{
			hash[i]=-1;
		}
	}
	
	public int search(String name, int scope) {
		int r=hash[f((name+scope).toUpperCase())];
		if(r!=-1) {
			Symbol s=table.get(r);
			while(true) {
				if(s.name.equalsIgnoreCase(name) && s.scope == scope) {
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
	public int insert(String name, int scope) {
		int se=search(name,scope);
		if (se == -1) {
			Symbol symbol = new Symbol();
			symbol.name = name;
			symbol.scope = scope;
			table.add(symbol);
			int h=f((name+scope).toUpperCase());
			if (hash[h]==-1) 
				hash[h]=table.size()-1;
			else {
				Symbol s=table.get(hash[h]);
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
	   	for (int i=0; i < s.length(); i++)
		     sum += ch[i];
	   	return sum%100;
	}
	
	public void printTable() {
		for (Symbol a : table)
			System.out.println(a);
		for (int i=0;i<100;i++){
			if(hash[i]!=-1)
			{
				System.out.println("hash: "+i+" index: "+hash[i]);
			}
		}
	}
	
}


public class Symbol {

	public String name;
	public int type;
	public boolean declared;
	public int scope;
	public int next;
	
	public Symbol() {
		next=-1;
	}
	
	public Symbol(String name, int type, boolean declared, int scope) {
		this.name=name;
		this.type=type;
		this.declared=declared;
		this.scope = scope;
		next=-1;
	}
	
	public String toString() {
		return String.format("Name: %s, Type: %d, Delcared %b, Scope %d", name, type, declared, scope);
	}
	
}

package sms.model;

import java.util.*;

public class Trie<Edge, Data> implements java.io.Serializable {
	Data data;
	Map<Edge, Trie<Edge, Data> > children;
	
	protected Trie() {
	}
	
	public Trie(Data data) {
		this.data = data;
		this.children = new HashMap<Edge, Trie<Edge, Data> >();
	}
	
	public Data getData() {
		return data;
	}
	
	public void setChild(Edge key, Trie<Edge, Data> child) {
		children.put(key, child);
	}
	public Trie<Edge, Data> getChild(Edge key) {
		return children.get(key);
	}
	public boolean hasChild(Edge key) {
		return children.containsKey(key);
	}
	public String toStringWithSuffix(String suffix) {
		String result = "\"" + suffix + "\": " + data + "\n";
		for(Map.Entry<Edge, Trie<Edge, Data> > e: children.entrySet())
			result += e.getValue().toStringWithSuffix(e.getKey() + suffix);
		
		return result;
	}
	public String toString() {
		return toStringWithSuffix("");
	}
}

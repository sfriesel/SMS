package sms.codec;

import sms.model.*;
import java.io.*;
import java.util.zip.*;
import java.util.Enumeration;

public class TrieLoader {
	static String trieExtension = ".trie";
	static String trainingExtension = ".txt";
	int cutoff = 120;
	String basepath;
	public TrieLoader() {
		this("../data/training-iso");
	}
	public TrieLoader(String basepath) {
		this.basepath = basepath;
	}
	
	public Trie<Character, Dictionary> loadTrie() throws IOException {
		InputStream trieFile;
		try { //loading trie from file
			try { //to load the serialized trie
				trieFile = new FileInputStream(basepath + trieExtension);
			} catch(Exception exception) {
				//see if we can load the zipped version instead
				ZipFile zipfile = new ZipFile(basepath + trieExtension + ".zip");
				ZipEntry entry = zipfile.entries().nextElement();
				trieFile = zipfile.getInputStream(entry);
			}
			
			ObjectInputStream ois = new ObjectInputStream(trieFile);
			
			return (Trie<Character, Dictionary>) (ois.readObject());
		} catch(Exception e) {
			System.err.println("trie needs to be rebuild... this may take a while");
			FileInputStream training = new FileInputStream(basepath + trainingExtension);
			Trie<Character, Dictionary> temp = new sms.builder.TrieBuilder().build(training, cutoff);
			
			FileOutputStream fos = new FileOutputStream(basepath + trieExtension);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(temp);
			return temp;
		}
	}
}

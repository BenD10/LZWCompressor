package zlwcompressor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

public class Compress {

	public static void main(String[] args) {
		String INPUT_FILE = new String("test1.txt");
		String OUTPUT_FILE = new String("test1.bcr");
		
		// Create the initial dictionary
		HashMap<String,Integer> dictionary = new HashMap<String,Integer>();
		
		for(int x=0; x <= 78; x++){
			// Fill it alphabet, digits, and most symbols.
			dictionary.put(Character.toString((char)(x+48)), new Integer(x));
		}
		
		// Create the Reader object
		try (InputStream in = new FileInputStream(INPUT_FILE);
			Reader reader = new InputStreamReader(in);
			// buffer for efficiency
			Reader buffer = new BufferedReader(reader);
			DataOutputStream writer = new DataOutputStream(new FileOutputStream(OUTPUT_FILE));) {
				char c;
				String tmp = "";
				while(true){
					c = getCharacter(buffer);
					if(c != '\0'){
						tmp += c;
						if(isInDictionary(dictionary, tmp)){
							continue;
						}
						writer.writeInt(addToDictionary(dictionary,tmp));
						tmp = "" + c;
					}
					else {
						writer.writeInt(getDictionaryLocation(dictionary, tmp));
						break;
					}
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		printDictionary(dictionary);
	}
	
	private static void printDictionary(HashMap<String,Integer> dictionary){
		for (String name: dictionary.keySet()){
			System.out.println("[" + name + ": " + dictionary.get(name).toString() + "]");
		}
	}
	
	private static char getCharacter(Reader reader) throws IOException {
		int r = reader.read();
		if (r != -1 && r != 32){
			// We grabbed a valid character, return it
			return (char) r;
		}
		// Hit end of file, return a null character.
        return '\0';
	}
	
	private static int addToDictionary(HashMap<String,Integer> dictionary, String target){
		dictionary.put(target, new Integer(dictionary.size()));
		return dictionary.get(target.substring(0,target.length()-1));
	}
	
	private static boolean isInDictionary(HashMap<String,Integer> dictionary, String target){
		if(dictionary.containsKey(target))
			return true;
		return false;
	}
	private static int getDictionaryLocation(HashMap<String,Integer> dictionary, String target){
		return dictionary.get(target);
	}

}

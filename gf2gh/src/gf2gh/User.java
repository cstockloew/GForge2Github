package gf2gh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class User {

	private HashMap<String, Integer> unix2id = new HashMap<String, Integer>();
	public static HashMap<Integer, String> id2gh = new HashMap<Integer, String>();

	private void loadMapping1() throws IOException {
		File file = new File("data", "_user_mapping1.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			String[] sp = line.split("\t");
			if (sp.length != 2) {
				br.close();
				throw new RuntimeException("error while reading mapping 1");
			}
			unix2id.put(sp[1], Integer.parseInt(sp[0]));
			id2gh.put(Integer.parseInt(sp[0]), sp[1]);
		}
		br.close();

		System.out.println("loaded " + unix2id.keySet().size() + " users");
	}

	private void loadMapping2() throws IOException {
		File file = new File("data", "_user_mapping2.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			String[] sp = line.split("\t");
			if (sp.length != 3 && sp.length != 4) {
				br.close();
				throw new RuntimeException("error while reading mapping 1");
			}

			String unix = null;
			String gh = null;
			String uc = "\"unix_name\":\"";
			String gc = "\"github\":\"";
			for (int i = 0; i < sp.length; i++) {
				String tmp = sp[i];
				tmp = tmp.substring(0, tmp.length() - 1);
				if (tmp.startsWith(uc)) {
					unix = tmp.substring(uc.length());
				} else if (tmp.startsWith(gc)) {
					gh = tmp.substring(gc.length());
				}
			}
			// System.out.println(unix +" " + gh);
			if (gh == null)
				gh = unix;
			id2gh.put(unix2id.get(unix), gh);
		}
		br.close();

		System.out.println("loaded " + id2gh.keySet().size() + " gh users");
	}

	public User() throws IOException {
		loadMapping1();
		loadMapping2();
		// for (java.util.Map.Entry<Integer, String> e : id2gh.entrySet()) {
		// System.out.println(" -- " + e.getKey() + "\t -> " + e.getValue());
		// }
	}
}

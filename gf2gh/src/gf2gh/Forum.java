package gf2gh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Forum {
	private int postCount = 0;

	private HashMap<Integer, List<Post>> posts = new HashMap<Integer, List<Post>>();

	public int getPostCount() {
		return postCount;
	}

	public int getThreadCount() {
		return posts.keySet().size();
	}

	public boolean read(String repo) {
		File file = new File("data", repo + ".txt");
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			while (true) {
				Post p = new Post();
				if (!p.read(br))
					break;

				List<Post> lst = posts.get(p.threadID);
				if (lst == null) {
					lst = new ArrayList<Post>();
					posts.put(p.threadID, lst);
				}
				lst.add(p);

				postCount++;
			}
			// System.out.println("Threads: " + posts.keySet().size());
			// System.out.println("Posts: " + i);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

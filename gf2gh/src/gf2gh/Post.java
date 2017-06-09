package gf2gh;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post {
	public int forumID;
	public int threadID;
	public int msgID;
	public int parentID;
	public Date postDate;
	public int userID;
	public String subject;
	public String body;

	private static SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private void printLinks(int start) {
		int pos1 = body.indexOf("forge.universaal.org", start);
		if (pos1 == -1)
			return;
		int pos2 = Integer.MAX_VALUE;
		int tmp;
		tmp = body.indexOf("<", pos1);
		if (tmp != -1 && tmp < pos2)
			pos2 = tmp;
		tmp = body.indexOf("\"", pos1);
		if (tmp != -1 && tmp < pos2)
			pos2 = tmp;
		tmp = body.indexOf(" ", pos1);
		if (tmp != -1 && tmp < pos2)
			pos2 = tmp;
		tmp = body.indexOf(")", pos1);
		if (tmp != -1 && tmp < pos2)
			pos2 = tmp;
		if (pos2 == Integer.MAX_VALUE)
			pos2 = body.length();
		
		System.out.println("Link: " + body.substring(pos1, pos2));
		
		printLinks(pos2);
	}

	public boolean read(BufferedReader br) throws IOException, ParseException {
		String line = br.readLine();
		if (line == null)
			return false;
		String[] fields = line.split("\t");
		forumID = Integer.parseInt(fields[0]);
		threadID = Integer.parseInt(fields[1]);
		msgID = Integer.parseInt(fields[2]);
		parentID = Integer.parseInt(fields[3]);
		postDate = parser.parse(fields[4]);
		userID = Integer.parseInt(fields[5]);
		subject = fields[6];

		body = "";
		while ((line = br.readLine()) != null) {
			if ("--------------uhoatao484z5zq934zqzt84t".equals(line))
				break;
			body += line;
		}
		printLinks(0);
		return true;
	}
}

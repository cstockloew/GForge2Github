package gf2gh;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryIssue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class Main {

	String[] gforge = { "lddi", "middleware", "nativeandroid", "ontologies", "rinterop", "service", "support",
			"uaal_context", "uaalsecurity", "uaaltools", "uaal_ui" };

	String[] github = { "lddi", "middleware", "nativeandroid", "ontologies", "remote", "service", "platform", "context",
			"security", "tools.eclipse-plugins", "ui" };

	ArrayList<Forum> forums = new ArrayList<Forum>();
	User users;

	void loadForums() {
		for (int i = 0; i < gforge.length; i++) {
			System.out.println("Loading " + gforge[i]);
			Forum f = new Forum();
			f.read(gforge[i]);
			forums.add(f);
			System.out.println(
					"Loaded  " + gforge[i] + "\tThread: " + f.getThreadCount() + "\tPosts: " + f.getPostCount());
		}
	}

	void createMapping() {
		System.out.println("Create Mapping");
		for (int i = 0; i < gforge.length; i++) {
			Forum f = forums.get(i);
			System.out.println(" -- " + gforge[i] + "\t#threads: " + f.getThreadCount());
		}
	}

	void main() throws IOException {
		users = new User();
		loadForums();
		createMapping();
	}

	public static void main(String[] args) throws IOException {
		new Main().main();

		// GitHubClient client = new GitHubClient();
		// client.setOAuth2Token("yourToken");

		// RepositoryService service = new RepositoryService();
		// for (Repository repo : service.getRepositories("universAAL"))
		// System.out.println(repo.getName() + " Watchers: " +
		// repo.getWatchers());

		// IssueService iss = new IssueService();
		// Issue is = iss.getIssue("universAAL", "middleware", 1);
		// System.out.println(is.getBody());
	}
}

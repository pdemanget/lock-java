package pdemanget.lock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class LockManager {
	private static final String PWD = "~/etc/lock-java.pwd";
	public static final LockManager instance = new LockManager();
	private Argon2 argon2 = Argon2Factory.create();
	Map<String,String> users=new TreeMap<>();
	
	private String stored="$argon2i$v=19$m=65536,t=2,p=1$J47ePzoa/2zBi0V6cwKzrg$Ipl7r7vAi930ta49cfhwCXzydqoNGfq+2gbOGQAQMk8";

	private LockManager() {
		try {
			loadFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadFile() throws IOException {
		Path path = Paths.get(PWD);
		if(!Files.exists(path)) {
			return;
		}
		List<String> lines = Files.readAllLines(path);
		users.clear();
		for(String line:lines) {
			String[] cols = line.split(":",2);
			if (cols.length>1) {
				users.put(cols[0],cols[1]);
			}
		}
		String pass=users.get(System.getenv("USER"));
		if (pass != null) {
			stored=pass;
		}
	}
	
	public boolean check(String password) {
		return argon2.verify(stored, password);
	}
	
	public void save(String user, String password) throws IOException {
	    String hash = argon2.hash(2, 65536, 1, password);
	    users.put(user,hash);
	    Path path = Paths.get(PWD);
		Files.createDirectories(path.getParent());
	    Files.write(path,users.keySet().stream().map(key->key+":"+users.get(key)).collect(Collectors.toList()));
	}
	
	public void close() {
		//argon2.wipeArray(password);
	}
	
	public static void main(String[] args) {
		try {
			instance.save("fil","coucou");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

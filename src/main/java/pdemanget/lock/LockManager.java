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
	private static final String PWD = "etc/lock-java.pwd";
	public static final LockManager instance = new LockManager();
	private Argon2 argon2 = Argon2Factory.create();
	Map<String,String> users=new TreeMap<>();
	
    public static boolean isWindows() {
        return System.getProperty( "os.name" ).toLowerCase().indexOf( "win" ) >= 0;
    }
	
	private LockManager() {
		try {
			System.setProperty("line.separator", "\n");
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
	}

	public String currentUser() {
		if(isWindows()) {
			return new com.sun.security.auth.module.NTSystem().getName();
		}else {
			return System.getenv("USER");
		}
		//String userName = System.getProperty("user.name");
	}
	
	public boolean hasPassword(String user) {
		return  user==null?null:users.get(user)!=null;
	}
	
	public boolean check(String user,String password) {
		String pass = users.get(user);
		if( pass== null) {
			return false;
		}
		return argon2.verify(pass, password);
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
	
}

package pdemanget.lock.argon;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class ArgonTest {
	static String stored="$argon2i$v=19$m=65536,t=2,p=1$J47ePzoa/2zBi0V6cwKzrg$Ipl7r7vAi930ta49cfhwCXzydqoNGfq+2gbOGQAQMk8";

	public static void main(String[] args) {
		// Create instance
		Argon2 argon2 = Argon2Factory.create();

		// Read password from user
		char[] password = "coucou".toCharArray();

		try {
		    // Hash password
		    String hash = argon2.hash(2, 65536, 1, password);
		    System.out.println(hash);
		    System.out.println(stored);
		    // Verify password
		    if (argon2.verify(stored, password)) {
		        // Hash matches password
		    	System.out.println("OK");
		    } else {
		        // Hash doesn't match password
		    	System.out.println("KO");
		    }
		} finally {
		    // Wipe confidential data
		    argon2.wipeArray(password);
		}
	}
}

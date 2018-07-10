package pdemanget.filedb;

import java.util.List;

/**
 * It was the api definition, keep the mismatch.
 *
 * @param <T>
 * @param <K>
 */
public class DbPai<T,K> {
	
	public T getByLine(long lineNb) {
		return null;
	}
	
	public long save(T data) {
		return -1;
	}
	
	public T getByHash(K key) {
		return null;
	}
	
	public List<T> getAll() {
		return null;
	}
	
	public List<T> getSorted() {
		return null;
	}


	

}

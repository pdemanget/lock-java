package pdemanget.filedb;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import pdemanget.filedb.bean.Person;

public class CoreFileAccess<T extends Dic> {
	
	
	private final Class<T> clazz;
	RandomAccessFile raf;
	
	public CoreFileAccess(Class<T> clazz) {
		this.clazz=clazz;
		try {
			Path path = Paths.get("c:/opt/data/filedb.json");
			Files.createDirectories(path.getParent());
			raf = new RandomAccessFile(path.toFile(),"rws");
		} catch ( IOException e) {
			throw new IllegalArgumentException("not managed exception",e);
		}
		
	}
	
	public Class<T> getClazz() {
		return clazz;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		//I shouldn't have done this
		raf.close();
	}

	public T toObject(byte[] buffer) {
		ObjectMapper mapper = getMapper();
		T value;
		try {
			value = mapper.readValue(buffer,getClazz());
		} catch (IOException e) {
			throw new IllegalArgumentException("not managed exception",e);
		}
		return value;
	}
	
	public byte[] fromObject(T value) {
		ObjectMapper mapper = getMapper();
		try {
			byte[] buffer=mapper.writeValueAsBytes(value);
			return buffer;
		} catch (IOException e) {
			throw new IllegalArgumentException("not managed exception",e);
		}
	}


	private ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT,false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}
	
	public T read(long from, long to) {
		try {
			
			raf.seek(from);
			byte[] buffer = new byte[ (int) (to - from)];
			raf.read(buffer);
			
			return toObject(buffer);
			
			
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("not managed exception",e);
		} catch (IOException e) {
			throw new IllegalArgumentException("not managed exception",e);
		}
		
	}
	
	public String repeat(int n,String s) {
		//To bo replaced in java 11 with String::repeat
		return new String(new char[n]).replace("\0", s);
	}
	
	/**
	 * Writes between from and to if it fits, find a new place else.
	 * @param from
	 * @param to
	 * @param value
	 * @return
	 */
	public long write(long from, long to, T value) {
		
		
		try {
			byte[] buffer = fromObject(value);
			byte[] filler = (repeat(1023," ")+"\n").getBytes(UTF_8); 
			if(buffer.length>(to-from)) {
				from=raf.length();
				to=from+buffer.length+filler.length;
			} else {
				int size=((int)(to-from))-buffer.length-1;
				if(size>1) {
					filler = repeat(size-1," ").getBytes(UTF_8);
				}
			}
			raf.seek(from);

			raf.write(buffer);
			raf.write(filler);
			//raf.write('\n');
			
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("not managed exception",e);
		} catch (IOException e) {
			throw new IllegalArgumentException("not managed exception",e);
		}
		
		return from;
	}
	
	public static void main(String[] args) {
		CoreFileAccess cfa = new CoreFileAccess(Person.class);
		long a = cfa.write(0,0,new Person("Gates","Bill",LocalDate.of(1964,1,1),1.65f ));
		long b = cfa.write(0,0,new Person("Rambo","John",LocalDate.of(1965,2,2),1.85f ));
		Dic read = cfa.read(a,b);
		System.out.println(read);
	}

}

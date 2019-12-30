package java_nio_file;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Set;
import org.junit.Test;
public class _filesystem {
	private final FileSystem fs;
	public _filesystem() {
		this.fs = FileSystems.getDefault();
	}
	public void print(Object obj) {
		System.out.println(obj);
	}
	// (1) 获取path抽象的实例
	// (2) 实际效果等价于Paths.get函数,因为这个函数内部也是调用FileSystem.getPath函数;
	// 其内部语句: return FileSystems.getDefault().getPath(first, more);
	@Test
	public void getPath() {
		Path path = fs.getPath("files");
		print(path); // out: files
	}
	@Test
	public void getPathMatcher() {
		PathMatcher pm = fs.getPathMatcher("glob:*.java");
		print(pm.matches(Paths.get("files"))); // out: false
	}
	@Test
	public void getSeparator() {
		String separator = fs.getSeparator();
		print(separator); // out: /
	}
	@Test
	public void getFileStores() {
		Iterable<FileStore> container = fs.getFileStores();
		container.forEach(p -> print(p));
	}
	@Test
	public void getRootDirectories() {
		Iterable<Path> container = fs.getRootDirectories();
		container.forEach(p -> print(p)); // out: /
	}
	@Test
	public void getUserPrincipalLookupService() {
		try {
			UserPrincipalLookupService upls = fs.getUserPrincipalLookupService();
			print(upls.toString());
			UserPrincipal up = upls.lookupPrincipalByName("abc");
			print(up);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void newWatchService() {
		try {
			WatchService ws = fs.newWatchService();
			print(ws);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void provider() {
		FileSystemProvider fsp = fs.provider();
		print(fsp);
	}
	@Test
	public void supportedFileAttributeViews() {
		Set<String> sets = fs.supportedFileAttributeViews();
		print(sets);
		sets.forEach(p -> print(p));
	}
	/**
	 * 
	 */
	@Test
	public void f2() {
		boolean open = fs.isOpen();
		print(open);
		boolean readOnly = fs.isReadOnly();
		print(readOnly);
	}
	/*
	 * 
	 */
	@Test
	public void f3() throws IOException {
		fs.close();
	}
}

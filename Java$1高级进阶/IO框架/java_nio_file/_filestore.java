package java_nio_file;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.AttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import javax.print.attribute.standard.PrinterLocation;
import static java.lang.System.out;
import java.io.IOException;
@SuppressWarnings("all")
public class _filestore {
	public static void main(String[] args) throws IOException {
		String absolutePath = "files";
		FileStore fs = Files.getFileStore(Paths.get(absolutePath));
		long totalSpace = fs.getTotalSpace();
		out.println(fs.name());
		out.println(totalSpace / 1024 / 1024 / 1024);
		//
		print(fs.type());
		//
		print(fs.getTotalSpace());
		print(fs.getUnallocatedSpace());
		print(fs.getUsableSpace());
		//
		print(fs.isReadOnly());
		//
		print(fs.supportsFileAttributeView(BasicFileAttributeView.class));
		print(fs.supportsFileAttributeView(DosFileAttributeView.class));
		print(fs.supportsFileAttributeView(PosixFileAttributeView.class));
		print(fs.supportsFileAttributeView(FileOwnerAttributeView.class));
		print(fs.supportsFileAttributeView(AclFileAttributeView.class));
		//
		FileStoreAttributeView fsav = fs.getFileStoreAttributeView(FileStoreAttributeView.class);
		print(fsav);
		//
		print(fs.supportsFileAttributeView("basic"));
		print(fs.supportsFileAttributeView("dos"));
		print(fs.supportsFileAttributeView("posix"));
		print(fs.supportsFileAttributeView("ext4"));
		print(fs.supportsFileAttributeView("ntfs"));
		print(fs.supportsFileAttributeView("xfs"));
		print(fs.supportsFileAttributeView("acl"));
		print(fs.supportsFileAttributeView("unix"));
		print(fs.supportsFileAttributeView("owner"));
		//
		print(fs.getAttribute("totalSpace"));
		print(fs.getAttribute("unallocatedSpace"));
		print(fs.getAttribute("usableSpace"));
	}
	private static void print(Object obj) {
		out.println(obj);
	}
}

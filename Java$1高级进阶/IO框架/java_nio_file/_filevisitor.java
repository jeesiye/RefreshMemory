package java_nio_file;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
public class _filevisitor {
	public static void main(String[] args) throws IOException {
		Path path = Paths.get("/home/coder");
		Path walkFileTree = Files.walkFileTree(path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 3,
				new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						boolean bool = dir.toString().contains("Download");
						if (bool) {
							print(1);
							print("pre");
							print(attrs);
							print(dir.toString() +"----a");
						}
						return FileVisitResult.CONTINUE;
					}
					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						// TODO Auto-generated method stub
						boolean bool = dir.toString().contains("Download");
						if (bool)
							print("post");
						return super.postVisitDirectory(dir, exc);
					}
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//						boolean bool = file.toString().contains("Download");
//						if (bool)
//							print(file);
						return FileVisitResult.CONTINUE;
					}
					@Override
					public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
						// TODO Auto-generated method stub
						return super.visitFileFailed(file, exc);
					}
				});
		print(walkFileTree);
		print(Files.isDirectory(walkFileTree, LinkOption.NOFOLLOW_LINKS));
		print(Files.isRegularFile(walkFileTree, LinkOption.NOFOLLOW_LINKS));
	}
	public static void print(Object obj) {
		System.out.println(obj);
	}
}

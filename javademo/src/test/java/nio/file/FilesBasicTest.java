package nio.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;
import metrics.Temp;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class FilesBasicTest {

    @Test
    public void walkFileTree() throws IOException {
        /*
        D:\temp
        -- AA
            -- AAA
            -- a.txt
        -- BB
            -- BBB
                --- BBBB
                --- bbb.txt
        */
        /*
        preVisitDirectory :: file:///D:/temp/
        preVisitDirectory :: file:///D:/temp/AA/
        visitFile :: file:///D:/temp/AA/a.txt
        preVisitDirectory :: file:///D:/temp/AA/AAA/
        postVisitDirectory :: file:///D:/temp/AA/AAA/
        postVisitDirectory :: file:///D:/temp/AA/
        preVisitDirectory :: file:///D:/temp/BB/
        preVisitDirectory :: file:///D:/temp/BB/BBB/
        visitFile :: file:///D:/temp/BB/BBB/bbb.txt
        preVisitDirectory :: file:///D:/temp/BB/BBB/BBBB/
        postVisitDirectory :: file:///D:/temp/BB/BBB/BBBB/
        postVisitDirectory :: file:///D:/temp/BB/BBB/
        postVisitDirectory :: file:///D:/temp/BB/
        postVisitDirectory :: file:///D:/temp/
         */
        Path root = new File("D:\\temp").toPath();
        Files.walkFileTree(root, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("visitFile :: " + file.toUri().toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("visitFileFailed :: " + file.toUri().toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("postVisitDirectory :: " + dir.toUri().toString());
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void findGitDirectory() throws IOException {
        Path start = Paths.get("C:\\git\\zaccoding");
        Consumer<Path> gitDirectoryConsumer = path -> System.out.println("Found git dir : " + path.toString());
        GitDirectoryVisitor visitor = new GitDirectoryVisitor(start, gitDirectoryConsumer);
        Files.walkFileTree(start, visitor);
    }


    private static class GitDirectoryVisitor implements FileVisitor<Path> {

        private Path root;
        private Consumer<Path> gitDirectoryConsumer;

        public GitDirectoryVisitor(Path root, Consumer<Path> gitDirectoryConsumer) {
            this.root = root;
            this.gitDirectoryConsumer = gitDirectoryConsumer;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (root == dir) {
                return FileVisitResult.CONTINUE;
            }

            try (Git git = Git.open(dir.toFile())) {
                gitDirectoryConsumer.accept(dir);
                return FileVisitResult.SKIP_SUBTREE;
            } catch (RepositoryNotFoundException e) {
                return FileVisitResult.CONTINUE;
            } catch (Exception e) {
                return FileVisitResult.CONTINUE;
            }
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}

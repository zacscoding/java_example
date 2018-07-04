import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-07-04
 * @GitHub : https://github.com/zacscoding
 */
public class GitTest {

    String gitURI = "https://github.com/zacscoding/jgit-test.git";
    String destDir = "E:\\jgit-test\\jgit-tests";

    Path dirPath = Paths.get(destDir);
    Path node1 = Paths.get(destDir, "host1/node1", "node1.config");
    Path node2 = Paths.get(destDir, "host2/node2", "node2.config");

    @Test
    public void stepByStep() throws Exception {
        // del dir & clone
        step("Start to clone", () -> cloneRepository());

        // pull
        step("Start to pull", () -> pull());

        // write 3 times & commit
        step("Start to write & commit 3", () -> writeAndCommitRepeat(3));

        // read content at commits
        step("Start to read content at commits", () -> displayFiles());

        // push to upstream
        step("Start to push", () -> push());
    }

    // Step1 : clone repository
    private void cloneRepository() throws Exception {
        File dest = new File(destDir);
        FileUtils.deleteDirectory(dest);
        dest.mkdirs();

        try (Git git = Git.cloneRepository().setURI(gitURI).setDirectory(new File(destDir)).call()) {
        }
    }

    // Step2 : pull repository
    private void pull() throws Exception {
        try (Git git = Git.open(new File(destDir))) {
            git.pull().call();
        }
    }

    // Step3 : Write & Commit
    private void writeAndCommitRepeat(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            writeFileAndCommit(i + 3);
        }
    }

    // Step4 : Push
    private void push() throws Exception {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try (Repository repository = builder.setGitDir(new File(destDir + "\\.git")).readEnvironment() // scan environment GIT_* variables
                                            .findGitDir() // scan up the file system tree
                                            .build()) {
            try (Git git = new Git(repository)) {
                git.push().call();
            }

            System.out.println("Pushed from repository: " + repository.getDirectory());
        }
    }

    private void displayFiles() throws Exception {
        try (Git git = Git.open(new File(destDir)); Repository repository = git.getRepository()) {
            Collection<Ref> allRefs = repository.getAllRefs().values();

            // a RevWalk allows to walk over commits based on some filtering that is defined
            try (RevWalk revWalk = new RevWalk(repository)) {
                for (Ref ref : allRefs) {
                    revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
                }

                for (RevCommit commit : revWalk) {
                    System.out.println("## Current commit : " + commit.toString());

                    for (int i = 0; i <= 1; i++) {
                        Path node = (i == 0) ? node1 : node2;
                        // TODO :: checking another way about replace
                        String filter = dirPath.relativize(node).toString().replace("\\", "/");

                        SimpleLogger.println("file name : {} || filter : {}", node.getFileName().toString(), filter);

                        RevTree tree = commit.getTree();

                        try (TreeWalk treeWalk = new TreeWalk(repository)) {

                            treeWalk.addTree(tree);
                            treeWalk.setRecursive(true);
                            treeWalk.setFilter(PathFilter.create(filter));
                            if (!treeWalk.next()) {
                                System.out.println("## not found");
                            } else {
                                ObjectId objectId = treeWalk.getObjectId(0);
                                ObjectLoader loader = repository.open(objectId);
                                byte[] bytes = loader.getBytes();
                                String content = new String(bytes, StandardCharsets.UTF_8);
                                System.out.println(content);
                            }
                        }
                    }
                    System.out.println();
                }

                revWalk.dispose();
            }
        }
    }

    private void writeFileAndCommit(int version) throws Exception {
        try (Git git = Git.open(new File(destDir))) {
            if (version == 0) {
                FileUtils.forceMkdir(node1.getParent().toFile());
                FileUtils.forceMkdir(node2.getParent().toFile());
                Files.createFile(node1);
                Files.createFile(node2);
            }

            Files.write(node1, ("node1`s version" + version).getBytes());
            Files.write(node2, ("node2`s version" + version).getBytes());

            git.add().addFilepattern(".").call();
            git.commit().setMessage("commit v" + version).call();
        }
    }

    private void step(String title, DoTask task) throws Exception {
        System.out.println("## ======================================================== ");
        System.out.println(title);
        task.run();
        System.out.println("## ======================================================== //\n\n");
    }


    @FunctionalInterface
    public interface DoTask {

        void run() throws Exception;
    }
}

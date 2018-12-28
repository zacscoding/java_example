package git;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.junit.Test;
import task.DoTask;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-12-29
 * @GitHub : https://github.com/zacscoding
 */
public class GitCompareTest {

    File gitDir = new File("C:\\git\\zaccoding\\git-temp");

    @Test
    public void compareLocalWithRemotes() throws Exception {
        DoTask.step("Compare local repository with remote repository", () -> {
            try (Git git = Git.open(gitDir)) {
                // local branchs map
                Map<String, String> localRefsMap = git.branchList().call().stream().collect(
                    Collectors.toMap(ref -> ref.getName(), ref -> ref.getObjectId().name())
                );

                // get remote configs
                List<RemoteConfig> remoteConfigs = git.remoteList().call();
                for (RemoteConfig remoteConfig : remoteConfigs) {
                    SimpleLogger.println("Compare local with {}", remoteConfig.getName());

                    for (URIish url : remoteConfig.getURIs()) {
                        SimpleLogger.println("> Check {}` url :  {}", remoteConfig.getName(), url.toString());

                        Collection<Ref> refs = Git.lsRemoteRepository()
                            .setHeads(true)
                            .setTags(true)
                            .setRemote(url.toString())
                            .call();

                        for (Ref ref : refs) {
                            String sha = localRefsMap.get(ref.getName());
                            SimpleLogger.println(">> Check ref : {} | sha : {} | local sha : {}"
                                , ref.getName(), ref.getObjectId().name(), sha);

                            if (sha == null) {
                                System.out.println(">>> There is no local branch : " + ref.getName());
                                continue;
                            }

                            if (sha.equals(ref.getObjectId().name())) {
                                SimpleLogger.println(">>> Same sha");
                                continue;
                            }

                            SimpleLogger.println(">>> Find different sha");
                        }
                    }
                }
            }
        });

/*      Output

        ## ========================================================
        Compare local repository with remote repository
        SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
        SLF4J: Defaulting to no-operation (NOP) logger implementation
        SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
        Compare local with origin
        > Check origin` url :  https://github.com/zacscoding/git-temp.git
        >> Check ref : refs/heads/branch1 | sha : 7be2369eea92cec0cf035bfd486aaf1729b6fea5 | local sha : 070e197a30efa7db6dd0ed1c7d20a843ab0e0bfc
        >>> Find different sha
        >> Check ref : refs/heads/master | sha : 6f4576480708f73ed2f5cfdd1729a56e1379f454 | local sha : 1b3ce2a22ccc969c541869e75fbab9bb01f1de5b
        >>> Find different sha
        ## ======================================================== //
 */
    }
}

package org.jboss.pnc.yet;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class GitClient implements Closeable {

    private String repository;
    private File tempDir;
    private Git git;

    public GitClient(String repository) throws Exception {
        this.repository = repository;
        tempDir = Files.createTempDirectory("yet-git-client").toFile();
        ;

        git = Git.cloneRepository()
                .setURI(repository)
                .setDirectory(tempDir)
                .setCloneAllBranches(true)
                .call();
    }

    public String getParentOfCommit(String commitToFind) throws Exception {

        ObjectId commitId = git.getRepository().resolve(commitToFind);
        RevWalk revWalk = new RevWalk(git.getRepository());
        RevCommit commit = revWalk.parseCommit(commitId);

        return commit.getParent(0).getId().getName();
    }

    public String getCommiterOfCommit(String commitToFind) throws Exception {
        ObjectId commitId = git.getRepository().resolve(commitToFind);
        RevWalk revWalk = new RevWalk(git.getRepository());
        RevCommit commit = revWalk.parseCommit(commitId);

        return commit.getAuthorIdent().getName();
    }

    public String getMessageOfCommit(String commitToFind) throws Exception {
        ObjectId commitId = git.getRepository().resolve(commitToFind);
        RevWalk revWalk = new RevWalk(git.getRepository());
        RevCommit commit = revWalk.parseCommit(commitId);

        return commit.getShortMessage();
    }

    public boolean isCommitPresent(String commitToFind) throws Exception {
        ObjectId commitId = git.getRepository().resolve(commitToFind);
        return commitId != null;
    }

    @Override
    public void close() throws IOException {
        git.close();

        // delete the temp dir
        try (Stream<Path> pathStream = Files.walk(tempDir.toPath())) {
            pathStream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}

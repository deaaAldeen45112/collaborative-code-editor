package org.test.editor.infra.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitService {

    public void initRepo(String projectPath) throws GitAPIException {
        Git.init().setDirectory(new File(projectPath)).call();
    }

    public void addFiles(String projectPath, String filePattern) throws GitAPIException, IOException {
        try (Git git = Git.open(new File(projectPath))) {
            git.add().addFilepattern(filePattern).call();
        }
    }

    public void commit(String projectPath, String message) throws GitAPIException, IOException {
        try (Git git = Git.open(new File(projectPath))) {
            git.commit().setMessage(message).call();
        }
    }

    public String getFileContent(String projectPath, String commitHash, String filePath) throws IOException, GitAPIException {
        try (Git git = Git.open(new File(projectPath));
             Repository repository = git.getRepository()) {

            ObjectId commitId = repository.resolve(commitHash);
            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit commit = revWalk.parseCommit(commitId);
                ObjectId treeId = commit.getTree().getId();
                ObjectId fileObjectId = repository.resolve(treeId.getName() + ":" + filePath);

                if (fileObjectId == null) {
                    throw new IOException("File not found in the specified commit: " + filePath);
                }

                var loader = repository.open(fileObjectId);
                return new String(loader.getBytes());
            }
        }
    }
}

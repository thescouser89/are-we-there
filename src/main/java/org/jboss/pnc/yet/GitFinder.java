package org.jboss.pnc.yet;

import lombok.Data;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class GitFinder {

    @Data
    public static class InternalResult {
        String internalRepository;
        String upstreamRepository;

        String ref;
        String parentCommitId;

        String upstreamCommitId;
        boolean isRefInUpstreamRepository;
    }

    private static ConcurrentHashMap<String, GitClient> cache = new ConcurrentHashMap<>();

    public static InternalResult find(String repository, String upstreamRepository, String ref) throws Exception {
        InternalResult result = new InternalResult();
        result.internalRepository = repository;
        result.upstreamRepository = upstreamRepository;
        result.ref = ref;

        GitClient gitInternalRepository = cache.computeIfAbsent(repository, repositorySource -> {
            try {
                return new GitClient(repositorySource);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        if (gitInternalRepository.isCommitPresent(ref)) {
            result.parentCommitId = gitInternalRepository.getParentOfCommit(ref);

            // TODO: all the other logic for upstreamCommitId and isRef. Maybe we also need to consume the upstream ref from the upstream repository?
        }

        return result;

    }

    public static void cleanup() {
        cache.forEach((key, value) -> {
            try {
                value.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

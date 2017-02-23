package de.swirtz.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

/**
 * This class is used to walk a Path recursively. It collects every Path which matches a user-defined {@link PathMatcher}.
 * This class is immutable.
 * thread-safety depends on the provided {@link PathMatcher}.
 *
 * @author Simon.Wirtz
 */
public class GlobbingFileCollector {

    private final PathMatcher pm;
    private final Path path;
    private Object matcherLock;

    /**
     * Creates new instance of this class.
     *
     * @param pm   {@link PathMatcher} to be used when {{@link #collectMatches()}} is called.
     * @param path {@link Path} where to start searching when {{@link #collectMatches()}} is called.
     */
    public GlobbingFileCollector(PathMatcher pm, Path path) {
        this.pm = Objects.requireNonNull(pm, "PathMatcher must not be null.");
        this.path = Objects.requireNonNull(path, "Path must not be null.");
    }

    /**
     * Walks the path using the provided {@link PathMatcher}.
     *
     * @return All {@link Path}s matching.
     */
    public Stream<Path> collectMatches() {
        GlobbingFileVisitor visitor = new GlobbingFileVisitor(pm);

        try {
            Files.walkFileTree(path, visitor);
        } catch (IOException e) {
            throw new IllegalStateException("walking the file tree failed.", e);
        }
        return visitor.getMatches().stream();
    }

    private class GlobbingFileVisitor extends SimpleFileVisitor<Path> {
        private Set<Path> matches;

        GlobbingFileVisitor(PathMatcher pm) {
            matches = new HashSet<>();
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (pm.matches(file)) {
                matches.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.out.println("File cannot be visited: " + file);
            return FileVisitResult.CONTINUE;
        }

        List<Path> getMatches() {
            return new ArrayList<>(this.matches);
        }
    }

}

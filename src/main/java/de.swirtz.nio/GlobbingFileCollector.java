package de.swirtz.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

/**
 * created on 22.02.2017.
 */
public class GlobbingFileCollector {

    private final PathMatcher pm;
    private final Path path;


    public GlobbingFileCollector(PathMatcher pm, Path path) {
        this.pm = Objects.requireNonNull(pm, "PathMatcher must not be null.");
        this.path = Objects.requireNonNull(path, "Path must not be null.");
    }

   public Stream<Path> collectMatches() throws IOException {
        GlobbingFileVisitor visitor = new GlobbingFileVisitor(pm);
        Files.walkFileTree(path, visitor);
        return visitor.getMatches().stream();
    }

    class GlobbingFileVisitor extends SimpleFileVisitor<Path> {
        private Set<Path> matches;

        GlobbingFileVisitor(PathMatcher pm) {
            matches = new HashSet<>();
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
            if (pm.matches(file)) {
                matches.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            System.out.println("File cannot be visited: "+file);
            return FileVisitResult.CONTINUE;
        }

        List<Path> getMatches() {
            return new ArrayList<>(this.matches);
        }
    }

}

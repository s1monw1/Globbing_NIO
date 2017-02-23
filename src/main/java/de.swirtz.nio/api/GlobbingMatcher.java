package de.swirtz.nio.api;

import de.swirtz.nio.GlobbingFileCollector;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides static methods which can be used for searching files recursively starting at an defined path. As filters you can provide glob patterns.
 *
 * @author Simon.Wirtz
 */
public class GlobbingMatcher {

    /**
     * Recursively searches for Files (not directories) which match one of the given Glob expressions. A PathMatcher created with {@link FileSystems#getDefault()} is used.
     *
     * @param path    where to start from
     * @param globExp expressions to be used as filters, can be one or many
     * @return Stream of every File represented as a {@link Path}
     */
    public static Stream<Path> getMatches(Path path, String... globExp) {
        Objects.requireNonNull(path, "Path must not be null.");
        Objects.requireNonNull(globExp, "globExp must not be null.");
        String glob = String.format("glob:%s", createGlobExpression(globExp));
        PathMatcher pm = FileSystems.getDefault().getPathMatcher(glob);
        return new GlobbingFileCollector(pm, path).collectMatches();
    }

    private static String createGlobExpression(String... globExp) {
        String s = globExp.length == 1 ? globExp[0] : Arrays.stream(globExp).collect(Collectors.joining(",", "{", "}"));
        System.out.println("Glob expression: " + s);
        return s;
    }
}

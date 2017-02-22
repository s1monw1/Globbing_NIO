package de.swirtz.nio.api;

import de.swirtz.nio.GlobbingFileCollector;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * created on 22.02.2017.
 */
public class GlobbingMatcher {

    /**
     *
     * @param p
     * @param globExp
     * @return
     * @throws IOException
     */
    public static Stream<Path> getMatches(Path p, String... globExp) throws IOException {
        String glob = String.format("glob:%s", createGlobExpression(globExp));
        PathMatcher pm = FileSystems.getDefault().getPathMatcher(glob);
        return new GlobbingFileCollector(pm, p).collectMatches();
    }

    private static String createGlobExpression(String... globExp) {
        String s = globExp.length == 1 ? globExp[0] : Arrays.stream(globExp).collect(Collectors.joining(",", "{", "}"));
        System.out.println("Glob expression: "+ s);
        return s;
    }
}

package de.swirtz.nio;

import de.swirtz.nio.api.GlobbingMatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: Simon
 * created on 22.02.2017.
 */
public class ConsoleAccesor {

    public static void main(String[] args) {
        int argsLen = args.length;
        if (argsLen < 2) {
            throw new IllegalArgumentException("You need to enter a path and at least one glob expression.");
        }
        Path path = Paths.get(args[0]);
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("The entered path is not existent: " + path.toString());

        }
        System.out.println("Looking in path: " + path.toAbsolutePath().toString());
        String[] globs = new String[argsLen - 1];
        System.arraycopy(args, 1, globs, 0, args.length - 1);
        try {
            GlobbingMatcher.getMatches(path, globs).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error while matching. " + e.getLocalizedMessage());
        }
    }
}

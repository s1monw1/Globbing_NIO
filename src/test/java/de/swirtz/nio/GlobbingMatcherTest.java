package de.swirtz.nio;

import de.swirtz.nio.api.GlobbingMatcher;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author: Simon
 * created on 22.02.2017.
 */
public class GlobbingMatcherTest {
    private static final Path RES_BASE = Paths.get("src/main/resources");
    private static final Path ENV = RES_BASE.resolve("./globbing").normalize();

    @BeforeClass
    public static void init() throws IOException {
        Files.walkFileTree(ENV, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        setupEnvironment();
    }

    @Test
    public void getMatchesTest() throws IOException {
        assertEquals(5, GlobbingMatcher.getMatches(ENV, "**").count());
        assertEquals(1, GlobbingMatcher.getMatches(ENV, "**/.*").count());
        assertEquals(2, GlobbingMatcher.getMatches(ENV, "**/folder1/*").count());
        assertEquals(2, GlobbingMatcher.getMatches(ENV, "**/folder2/*").count());
    }

    @Test
    public void getMatchesMultiGlobTest() throws IOException {
        List<String> expected = new ArrayList<>(Arrays.asList(".gitignore", "pic.img", "pic.png"));
        Stream<Path> matches = GlobbingMatcher.getMatches(ENV, "**/.*", "**/folder1/pic.*");
        matches.map(p->p.getFileName().toString()).forEach(expected::remove);
        assertEquals(0, expected.size());
    }

    /**
     * Creates this structure:
     * ./globbing
     * |
     * .gitignore
     * folder1
     * |
     * pic.img
     * pic.png
     * folder2
     * |
     * file1.xml
     * file2.xml
     */
    private static void setupEnvironment() throws IOException {
        //Only reference, not created yet

        Path testDir1 = Files.createDirectories(ENV.resolve("folder1"));
        Path testDir2 = Files.createDirectories(ENV.resolve("folder2"));
        Files.createFile(ENV.resolve(".gitignore"));
        Files.createFile(testDir1.resolve("pic.img"));
        Files.createFile(testDir1.resolve("pic.png"));
        Files.createFile(testDir2.resolve("file1.xml"));
        Files.createFile(testDir2.resolve("file2.xml"));
    }
}

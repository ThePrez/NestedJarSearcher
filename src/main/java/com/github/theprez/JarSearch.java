package com.github.theprez;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class JarSearch {

    public static void main(final String[] _args) {
        LinkedList<String> args = new LinkedList<>(Arrays.asList(_args));
        boolean isQuiet = args.remove("-q");
        if (args.size() < 2) {
            System.out.println("Usage: jarsearch <jar_to_search> <file_to_search_for>");
            System.exit(-5);
        }
        final String fileName = args.removeFirst();
        final String searchFile = args.removeFirst();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileName))) {
            final JarSearcher searcher = new JarSearcher(zis, new File(fileName).toURI().toString(), searchFile);
            System.out.printf("\n\n%d hits were found\n\n", searcher.search());
        } catch (final Exception e) {
            e.printStackTrace(System.err);
        }
    }

}

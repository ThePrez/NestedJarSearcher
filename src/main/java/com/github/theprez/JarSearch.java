package com.github.theprez;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;

public class JarSearch {

    public static void main(final String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: jarsearch <jar_to_search> <file_to_search_for>");
            System.exit(-5);
        }
        final String fileName = args[0];
        final String searchFile = args[1];

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileName))) {
            final JarSearcher searcher = new JarSearcher(zis, new File(fileName).toURI().toString(), searchFile);
            System.out.printf("\n\n%d hits were found\n\n", searcher.search());
        } catch (final Exception e) {
            e.printStackTrace(System.err);
        }
    }

}

package com.github.theprez;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class JarSearcher {

    private final ZipInputStream m_zis;
    private final String m_searchFile;
    private final String m_fileEyeCatcher;
    private static boolean s_verbose = false;
    private final static String[] s_zipExtensions = new String[] { "zip", "ear", "war", "jar" };

    public JarSearcher(final ZipInputStream _zis, final String _fileEyeCatcher, final String _searchFile) {
        m_zis = _zis;
        m_fileEyeCatcher = _fileEyeCatcher;
        m_searchFile = _searchFile.toLowerCase();
    }

    public int search() throws IOException {
        int numberOfHits = 0;
        boolean scanned = false;
        for (ZipEntry ze = m_zis.getNextEntry(); null != ze; ze = m_zis.getNextEntry()) {
            scanned = true;
            if (isMatch(ze)) {
                ++numberOfHits;
                System.out.printf("***Found file '%s' in %s\n", ze.getName(), m_fileEyeCatcher);
            } else {
                final String eyeCatcher = m_fileEyeCatcher + " --> " + ze.getName();
                try {
                    final JarSearcher nestedSearcher = new JarSearcher(new ZipInputStream(m_zis), eyeCatcher, m_searchFile);
                    numberOfHits += nestedSearcher.search();
                } catch (final ZipException e) {
                    System.err.println("Zip format error: " + eyeCatcher);
                }
            }
        }
        if (!scanned && s_verbose) {
            System.err.println("not a zip: " + m_fileEyeCatcher);
        }
        return numberOfHits;

    }

    private boolean isMatch(final ZipEntry ze) {
        return ze.getName().toLowerCase().contains(m_searchFile);
    }

    private boolean isNestedZip(final ZipEntry ze) {
        for (final String ext : s_zipExtensions) {
            if (ze.getName().toLowerCase().endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }
}

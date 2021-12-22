package com.github.theprez;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class JarSearcher {

    // private static final Object LOG4J_VERSION_CLASS = "org/apache/logging/log4j/core/Version.class";
    final String META_INF = "meta-inf/manifest.mf";
    private final ZipInputStream m_zis;
    private final String m_searchFile;
    private final String m_fileEyeCatcher;
    private static boolean s_verbose = false;

    public JarSearcher(final ZipInputStream _zis, final String _fileEyeCatcher, final String _searchFile) {
        m_zis = _zis;
        m_fileEyeCatcher = _fileEyeCatcher;
        m_searchFile = _searchFile.toLowerCase();
    }

    public int search() throws IOException {
        int numberOfHits = 0;
        boolean scanned = false;
        List<String> processedManifestInfo = new LinkedList<String>();
        for (ZipEntry ze = m_zis.getNextEntry(); null != ze; ze = m_zis.getNextEntry()) {
            scanned = true;
            if (isMatch(ze)) {
                ++numberOfHits;
                System.out.printf("*** Found file '%s' in %s\n", ze.getName(), m_fileEyeCatcher);
            } else if (/* m_searchFile.contains("jndilookup") && */ze.getName().toLowerCase().equals(META_INF)) {
                final String eyeCatcher = m_fileEyeCatcher + " --> " + ze.getName();
                List<String> manifestInfo = processMetaInf("Log4jReleaseV", "Implementation-", "Specification-");
                if (!manifestInfo.isEmpty()) {
                    processedManifestInfo = manifestInfo;
                }
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
        if (scanned && 0 < numberOfHits && !processedManifestInfo.isEmpty()) {
            System.out.println("***     Version information found in " + m_fileEyeCatcher + ":");
            for (String manifestEntry : processedManifestInfo) {
                System.out.println("***         " + manifestEntry);
            }
        }
        if (!scanned && s_verbose) {
            System.err.println("not a zip: " + m_fileEyeCatcher);
        }
        return numberOfHits;

    }

    private List<String> processMetaInf(final String... _startsWiths) throws IOException {
        InputStreamReader reader = new InputStreamReader(m_zis, "UTF-8");
        List<String> lines = new LinkedList<String>();
        String currentLine = "";
        for (int curInt = 0; curInt != -1; curInt = reader.read()) {
            if (-1 == curInt) {
                break;
            }
            char cur = (char) curInt;
            if ('\n' == cur) {
                lines.add(currentLine.trim());
                currentLine = "";
            } else {
                currentLine += cur;
            }
        }
        lines.add(currentLine.trim());
        if (0 == _startsWiths.length) {
            return lines;
        }
        List<String> ret = new LinkedList<String>();
        for (String line : lines) {
            for (String startsWith : _startsWiths) {
                if (line.toLowerCase().startsWith(startsWith.toLowerCase())) {
                    ret.add(line);
                }
            }
        }
        return ret;
    }

    private boolean isMatch(final ZipEntry ze) {
        return ze.getName().toLowerCase().contains(m_searchFile);
    }
}

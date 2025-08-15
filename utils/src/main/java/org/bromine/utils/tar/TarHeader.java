package org.bromine.utils.tar;

public class TarHeader {
    private final String name;
    private final long size;

    // Constructor to parse the header
    public TarHeader(byte[] header) {
        this.name = extractName(header);
        this.size = extractSize(header);
    }

    // Extract the file name (first 100 bytes)
    private String extractName(byte[] header) {
        return new String(header, 0, 100).trim();
    }

    // Extract the file size (12 bytes starting at offset 124, in octal format)
    private long extractSize(byte[] header) {
        String sizeOctal = new String(header, 124, 12).trim();
        return sizeOctal.isEmpty() ? 0 : Long.parseLong(sizeOctal, 8);
    }

    // Getters for the fields
    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}

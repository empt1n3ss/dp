package org.example.dto;

public class FileMetadataDto {
    private String filename;
    private long size;

    public FileMetadataDto(String filename, long size) {
        this.filename = filename;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }
}

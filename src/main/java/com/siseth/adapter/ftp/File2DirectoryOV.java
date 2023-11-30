package com.siseth.adapter.ftp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.net.ftp.FTPFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
public class File2DirectoryOV {

    String directory;

    FTPFile[] files;

    String directoryRegex;

    String dateNameRegex;

    public File2DirectoryOV(FTPFile[] files, String directory, String directoryRegex, String dateNameRegex) {
        this.directory = directory;
        this.files = files;
        this.directoryRegex = directoryRegex;
        this.dateNameRegex = dateNameRegex;
    }

    public FTPFile getFile() {
        return getNewestFile();
    }

    public FTPFile getNewestFile() {
        return files != null ?
                Stream.of(files).max(Comparator.comparing(FTPFile::getTimestamp)).orElse(null) :
                null;
    }

    public String getFullPath() {
        return this.directory + "/" + getNewestFile().getName();
    }
    public List<FileOV> getFiles() {
        return this.files != null ?
                Arrays.stream(this.files)
                        .filter(x -> x.getName().endsWith(".jpg") || x.getName().endsWith(".png"))
                        .map(x -> new FileOV(x.getName(), this.directory, x.getTimestamp(), this.directoryRegex, this.dateNameRegex))
                        .collect(Collectors.toList()) :
                new ArrayList<>();
    }

}

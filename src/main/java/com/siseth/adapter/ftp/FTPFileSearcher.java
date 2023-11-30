package com.siseth.adapter.ftp;

import com.siseth.adapter.entity.source.ImageSource;
import com.siseth.adapter.entity.source.SourceConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@NoArgsConstructor
@Getter
@Setter
public class FTPFileSearcher {

    private String user;

    private String password;

    private String address;

    private Integer port;

    private String directory;

    private String directoryRegex;

    private String dateNameRegex;

    private String serialNumber;

    //___________________________________________________
    private FTPClient client;

    //___________________________________________________

    private final String temporaryString = System.getProperty("java.io.tmpdir");

    //___________________________________________________
    private LocalDate from;

    private LocalDate to;

    public FTPFileSearcher(ImageSource source) {
        SourceConfig config = source.getFtpConfig();
        this.user = config.getUser();
        this.password = config.getPassword();
        this.address = config.getIp();
        this.port = config.getPort();
        this.directory = source.getDirectory();
        this.directoryRegex = source.getDirectoryRegex();
        this.dateNameRegex = source.getDateNameRegex();
        this.serialNumber = source.getSerialNumber();
    }

    @SneakyThrows
    public FTPFileSearcher init() {
        this.client  = new FTPClient();
        client.connect(address, port);
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }
        client.login(user, password);
        return this;
    }

    @SneakyThrows
    public void close() {
        if(this.client != null && this.client.isConnected()) {
            client.logout();
            client.disconnect();
        }
    }

    public FTPFileSearcher initDate(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
        return this;
    }

    public List<FileOV> getAllFiles() throws IOException {
        List<String> directories = getAllDirectories();
        List<File2DirectoryOV> ov =
                directories.stream()
                        .map(x -> {
                            try {
                                return new File2DirectoryOV(client.listFiles(x), x, this.directoryRegex, this.dateNameRegex );
                            } catch (IOException e) {
                                System.out.println(e.getLocalizedMessage());
                                return new File2DirectoryOV();
                            }
                        })
                        .filter(x -> x.getFiles() != null)
                        .collect(Collectors.toList());

        List<FileOV> fileOVS = new ArrayList<>();

        for (File2DirectoryOV file2DirectoryOV : ov) {
            fileOVS.addAll(file2DirectoryOV.getFiles());
        }

        return fileOVS;


    }

    @SneakyThrows
    public File createFile(String name, String directory) {
        String[] paths = name.split("\\.");
        File file = File.createTempFile(name, "." + paths[paths.length - 1]);
        file.deleteOnExit();
        FileOutputStream os = new FileOutputStream(file);
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.retrieveFile(directory + "/" + name  , os);
        os.close();
        return file;
    }

    @SneakyThrows
    public File getNewestFile() {
        List<String> directories = getAllDirectories();
        File2DirectoryOV ov =
                directories.stream()
                        .map(x -> {
                            try {
                                return new File2DirectoryOV(client.listFiles(x), x, this.directoryRegex, this.dateNameRegex );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .filter(x -> x.getNewestFile() != null)
                        .max(Comparator.comparing(x -> x.getNewestFile().getTimestamp())).orElse(null);

        if(ov == null)
            throw new RuntimeException("File not found!");

        String[] fileSplit = ov.getNewestFile().getName().split("\\.");

        File file = File.createTempFile(fileSplit[0], "." + fileSplit[1]);
        file.deleteOnExit();
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        client.retrieveFile(ov.getFullPath(), os);
        os.close();

        return file;
    }

    @SneakyThrows
    private List<String> getAllDirectories() {
        List<String> directories = new ArrayList<>();

        for (String path : this.directoryRegex.split("/")) {
            directories = cross(directories, getPath(path));
        }
        return directories;
    }

    private List<String> getPath(String type) {
        switch (type) {
            case "MAIN":
                return List.of(this.directory);
            case "DEVICE_TYPE":
                return List.of(this.serialNumber);
            case "DATE":
                return Stream.iterate(this.from, d -> d.plusDays(1))
                        .limit((int) this.from.until(this.to, ChronoUnit.DAYS))
                        .map(LocalDate::toString)
                        .collect(Collectors.toList());
            case "ZOOM":
                return List.of("Zoom");
            case "CHANNEL":
                return List.of("001");
            case "IMAGE_TYPE":
                return List.of("jpg");
            case "HOUR":
                return IntStream.range(0, 24)
                        .mapToObj(x -> (x < 9) ? "0" + x : String.valueOf(x))
                        .collect(Collectors.toList());
        }
        return List.of("");
    }

    private List<String> cross(List<String> list1, List<String> list2){
       if(list1.size() == 0)
           return list2;

        return list1
                .stream()
                .map( l1 ->
                        list2.stream().map(l2 -> l1 + "/" + l2).collect(Collectors.toList())
                ).flatMap(List::stream)
                .collect(Collectors.toList());
    }


}

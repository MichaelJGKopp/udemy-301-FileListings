package dev.lpa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

public class Main {

  public static void main(String[] args) {

    Path path = Path.of("");  // current dir
    System.out.println("cwd = " + path.toAbsolutePath());

    try (Stream<Path> paths = Files.list(path)) {
      paths
        .map(Main::listDir)
        .forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("----------------------------------");

    try (Stream<Path> paths = Files.walk(path, 2)) {  // recursive for depth > 1
      paths
        .filter(Files::isRegularFile)
        .map(Main::listDir)
        .forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("----------------------------------");

    try (Stream<Path> paths = Files.find(path, Integer.MAX_VALUE,
      (p, attr) -> attr.isRegularFile() && attr.size() > 300
    )) {  // recursive for depth > 1
      paths
        .map(Main::listDir)
        .forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String listDir(Path path) {

    try {
      boolean isDir = Files.isDirectory(path);
      FileTime dateField = Files.getLastModifiedTime(path);
      LocalDateTime modDT = LocalDateTime.ofInstant(dateField.toInstant(), ZoneId.systemDefault());
      return "%tD %tT %-5s %12s %s"
        .formatted(modDT, modDT, (isDir ? "<DIR>" : ""), (isDir ? "" : Files.size(path)), path);
    } catch (IOException e) {
      System.out.println("Whoops! Something went wrong with " + path);
      return path.toString();
    }
  }
}

package com.puvn;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by puvN on 07.08.2017.
 * <p>
 * Actual Implementation of ComparingService.
 * <p>
 * Two directories will be compared by calculating symmetric difference of two sets
 * of files and folders from both directories. If any of directory has folders - this service
 * implementation will visit files in that folders by using SimpleFileVisitor<Path>.
 * Two files in any folders are equals if they have got same name and size - @see PathComparator
 * For example, if file is located in /dir1/someSubFolder/file.txt
 * and the same file is located in /dir2/anotherSubFolder/folder/file.txt - the files are equals.
 * If dir1 and dir2 contain same files, there will be no files in compareResult folder.
 * If dir1 and dir2 are the one dir (first and second parameters are equals), there will be no files
 * in compareResult folder
 */
public class ComparingServiceImpl implements ComparingService {

    @Override
    public void compareDirs(Path dir1, Path dir2, Path compareResult) {
        Set<Path> filesFromFirstDir = new TreeSet<>(new PathComparator());
        Set<Path> filesFromSecondDir = new TreeSet<>(new PathComparator());

        try {
            //  Delete result dir if it exists.
            deleteDirIfExists(compareResult);

            Files.createDirectory(compareResult);

            // Get file sets from directories
            filesFromFirstDir.addAll(getFileSet(dir1));
            filesFromSecondDir.addAll(getFileSet(dir2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculating intersection of two sets of files.
        Set<Path> intersection = new TreeSet<>(new PathComparator());
        intersection.addAll(filesFromFirstDir);
        intersection.retainAll(filesFromSecondDir);

        // Creating result set which represents next bool logic: (A v B) \ intersection
        Set<Path> resultSet = new TreeSet<>(new PathComparator());
        resultSet.addAll(filesFromFirstDir);
        resultSet.addAll(filesFromSecondDir);
        resultSet.removeAll(intersection);

        // Write the result set to result directory
        for (Path path : resultSet)
            try {
                Files.copy(path, compareResult.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * @param dir is a directory to visit
     * @return set of Paths in directory with it's subfolders if the directory exists. If another
     * shut down the program because source directory is missing.
     * @throws IOException may be thrown while visiting file in folder
     */
    private Set<Path> getFileSet(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Set<Path> fileSet = new HashSet<>();
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory()) {
                        fileSet.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            return fileSet;
        } else {
            System.out.println("No such directory: " + dir.toAbsolutePath().normalize());
            System.exit(-1);
        }
        return null;
    }

    /**
     * Removes directory with all files and subfolders. There will java.nio.file.FileAlreadyExistsException
     * if the result directory exists.
     *
     * @param dir to be removed
     * @throws IOException may be thrown while visiting file in folder
     */
    private void deleteDirIfExists(Path dir) throws IOException {
        if (Files.exists(dir))
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
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
    }
}

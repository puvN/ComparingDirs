package com.puvn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * Created by puvN on 07.08.2017.
 * <p>
 * Compares two java.nio.file.Path by it's actual file name and file size.
 */
public class PathComparator implements Comparator<Path> {
    @Override
    public int compare(Path o1, Path o2) {
        try {
            if (Files.size(o1) != Files.size(o2)) return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o1.getFileName().toString().compareTo(o2.getFileName().toString());
    }
}

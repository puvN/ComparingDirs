package com.puvn;

import java.nio.file.Path;

/**
 * Created by puvN on 07.08.2017.
 * <p>
 * This interface represents service which will compare two directories on disk.
 */
public interface ComparingService {

    /**
     * @param dir1          is a first directory to be compared
     * @param dir2          is a second directory to be compared
     * @param compareResult is also a directory, where the difference of two directories is located.
     */
    void compareDirs(Path dir1, Path dir2, Path compareResult);
}

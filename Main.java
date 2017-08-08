package com.puvn;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: only with [args]" +
                    " \n java filename [dir1] [dir2] [dirOfResult]" +
                    "\n java -jar filename [dir1] [dir2] [dirOfResult]");
            System.exit(-1);
        }

        Path dir1 = Paths.get(args[0]);
        Path dir2 = Paths.get(args[1]);
        Path compareResult = Paths.get(args[2]);

        ComparingService service = new ComparingServiceImpl();
        service.compareDirs(dir1.toAbsolutePath().normalize(),
                dir2.toAbsolutePath().normalize(),
                compareResult);

        System.out.println("Successfully");
    }
}

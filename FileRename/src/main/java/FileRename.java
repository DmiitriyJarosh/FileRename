import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileRename {

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Input path to dir where files should be renamed:");
            String dir = in.readLine();
            List<FileRecord> fileStatuses = new ArrayList<>();
            renameFilesInDir(dir, fileStatuses, Paths.get(dir));
            for (FileRecord fileRecord : fileStatuses) {
                System.out.println(fileRecord.print());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void renameFilesInDir(String dir, List<FileRecord> fileStatuses, Path rootDir) {
        File directory = new File(dir);
        File[] fileList = directory.listFiles();
        if (fileList == null) {
            System.out.println("Can't open '" + dir + "'");
            System.out.println("Maybe this path doesn't exist!");
            return;
        }

        for (File file : fileList) {
            if (file.isDirectory()) {
                renameFilesInDir(file.getAbsolutePath(), fileStatuses, rootDir);
            } else if (file.getName().endsWith(".java") || file.getName().endsWith(".kt")) {
                Path fileForRename = Paths.get(file.getAbsolutePath());
                if (file.canWrite()) {
                    if (!new File(file.getAbsolutePath() + ".2019").isFile()) {
                        try {
                            Files.move(fileForRename, fileForRename.resolveSibling(fileForRename.getFileName() + ".2019"));
                            fileStatuses.add(new FileRecord(rootDir.relativize(fileForRename), FileStatus.SUCCESS));
                        } catch(IOException e) {
                            fileStatuses.add(new FileRecord(rootDir.relativize(fileForRename), FileStatus.RENAMEFAIL));
                        }
                    } else {
                        fileStatuses.add(new FileRecord(rootDir.relativize(fileForRename), FileStatus.FILEEXISTS));
                    }
                } else {
                    fileStatuses.add(new FileRecord(rootDir.relativize(fileForRename), FileStatus.NOWRITEACCESS));
                }
            }
        }
    }
}

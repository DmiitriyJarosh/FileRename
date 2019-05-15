import java.nio.file.Path;

public class FileRecord {

    private Path file;
    private FileStatus status;

    public FileRecord(Path file, FileStatus status) {
        this.status = status;
        this.file = file;
    }

    public String print() {
        String fileStatus;
        switch(status) {
            case SUCCESS:
                fileStatus = "SUCCESS!";
                break;
            case FILEEXISTS:
                fileStatus = "Such file already exists. Renaming failed!";
                break;
            case NOWRITEACCESS:
                fileStatus = "File is blocked for writing!";
                break;
            case RENAMEFAIL:
                fileStatus = "File renaming failed. Unknown error!";
                break;
            default:
                fileStatus = "No status.";
        }
        return file + " --> " + fileStatus;
    }

}

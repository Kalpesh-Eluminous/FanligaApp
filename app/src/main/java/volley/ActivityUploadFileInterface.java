package volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface ActivityUploadFileInterface {
    void getUploadFile(File file) throws FileNotFoundException;
    void getUploadFiles(ArrayList<File> fileLists);
}

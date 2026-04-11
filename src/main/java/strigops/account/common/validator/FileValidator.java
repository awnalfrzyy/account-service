package strigops.account.common.validator;

import java.util.List;
import java.util.Arrays;

public class FileValidator {
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final List<String> ALLOWED_FORMATS = Arrays.asList(
            "image/png",
            "image/jpg",
            "image/jpeg"
    );

    public static boolean isFileSizeValid(byte[] fileBytes) {
        return fileBytes != null && fileBytes.length <= MAX_FILE_SIZE;
    }

    public static boolean isFormatValid(String contentType){
        return contentType != null && ALLOWED_FORMATS.contains(contentType.toLowerCase());
    }

    public static boolean isImageSignatureValid(byte[] fileBytes){
        if (fileBytes == null || fileBytes.length < 4){
            return false;
        }

        if (fileBytes[0] == (byte) 0xFF &&
                fileBytes[1] == (byte) 0xD8 &&
                    fileBytes[2] == (byte) 0xFF){
            return true;
        }

        return fileBytes[0] == (byte) 0x89 &&
                fileBytes[1] == (byte) 0x50 &&
                fileBytes[2] == (byte) 0x4E &&
                fileBytes[3] == (byte) 0x47;
    }
}

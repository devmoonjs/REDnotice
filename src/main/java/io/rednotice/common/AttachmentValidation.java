package io.rednotice.common;

import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class AttachmentValidation {

    private static final long MAX_FILE_SIZE = 524880;
    private static final List<String> SUPPORTED_FILE_TYPE = Arrays.asList("image/jpeg", "image/png", "application/png/", "text/csv");

    public static void validationFile(MultipartFile file) {
        if(file.getSize() > MAX_FILE_SIZE) {
            throw new ApiException(ErrorStatus._FILE_SIZE_EXCEEDED);
        }

        String fileType = file.getContentType();
        if(!SUPPORTED_FILE_TYPE.contains(fileType)) {
            throw new ApiException(ErrorStatus._UNSUPPORTED_FILE_TYPE);
        }
    }
}

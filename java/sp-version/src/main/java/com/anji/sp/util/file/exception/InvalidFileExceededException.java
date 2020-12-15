package com.anji.sp.util.file.exception;

/**
 * 文件名大小限制异常类
 *
 * @author ruoyi
 */
public class InvalidFileExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public InvalidFileExceededException(String extension) {
        super("upload.file.invalid", new Object[]{extension});
    }
}

package io.github.jokoframework.wilson.exceptions;

import io.github.jokoframework.common.errors.BusinessException;

/**
 * 
 * @author bsandoval
 *
 */
public class ReadOperationException extends BusinessException {
	private static final long serialVersionUID = 1L;
    public static final String READ_OPERATION_ERROR = "read.operation.error";
    public static final String READ_OPERATION_NOT_FOUND = READ_OPERATION_ERROR + ".notFound";

    public ReadOperationException(String errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Builds a Read Operation Not Found Exception
     *
     * @param uri read operation uri
     * @return ReadOperationException
     */
    public static ReadOperationException notFound(String uri) {
        return new ReadOperationException(READ_OPERATION_NOT_FOUND, String.format("Read Operation with URI %s not found" , uri));
    }
}

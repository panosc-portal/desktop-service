package eu.panosc.portal.cloud.http;

public class HttpException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -2245966033979824019L;

    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}


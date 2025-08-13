package server;

public class FileTooLargeException extends Exception {
    /**
     * Exception to handle too large ontology files
     * @param message with the exception
     */
    public FileTooLargeException(String message) {
        super(message);
    }
}

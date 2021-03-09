package theaterseating;

/** A general-purpose error-reporting exception for this package.  All
 *  anticipated user or I/O errors should be reported through this
 *  exception, with the message being the error message to be printed.
 *  @author Lisa Sam Wang
 */
class SeatingException extends RuntimeException {

    /** An exception whose getMessage() value is MSG. */
    SeatingException(String msg) {
        super(msg);
    }

    /** A utility method that returns a new exception with a message
     *  formed from MSGFORMAT and ARGUMENTS, interpreted as for the
     *  String.format method or the standard printf methods.
     *
     *  The use is thus 'throw error(...)', which tells the compiler that
     *  execution will terminate at that point, and avoid insistance on
     *  an explicit return in a value-returning function.)  */
    static SeatingException error(String msgFormat, Object... arguments) {
        return new SeatingException(String.format(msgFormat, arguments));
    }

}
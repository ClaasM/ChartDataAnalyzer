package exceptions;

/**
 * Exception, die geworfen wird, wenn bei der Bearbeitung der Tickdaten ein ungültiger Zeitraum angegeben wird
 * 
 * @author Claas
 *
 */
public class InvalidIntervalException extends Exception {

	public InvalidIntervalException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidIntervalException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public InvalidIntervalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public InvalidIntervalException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidIntervalException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}

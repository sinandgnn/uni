import java.time.Duration;
import java.time.LocalDateTime;

public class SmartCamera extends Device {
	private final double mbPerMinute;
	private LocalDateTime onTime;
	private double millis = 0.0;

	/**
	 * @param name        The name of the smart camera.
	 * @param mbPerMinute Information about how much storage the smart camera.
	 */
	public SmartCamera(String name, double mbPerMinute) {
		this.name = name;
		this.mbPerMinute = mbPerMinute;
	}

	/**
	 * @param name        The name of the smart camera.
	 * @param mbPerMinute Information about how much storage the smart camera.
	 * @param status      The status of the smart camera.
	 */
	public SmartCamera(String name, double mbPerMinute, boolean status) {
		this.name = name;
		this.mbPerMinute = mbPerMinute;
		this.status = status;
		if (status) {
			onTime = Timer.getTime();
		}
	}

	/**
	 * Calculates how much storage the smart camera is used.
	 *
	 * @return Storage the smart camera is used.
	 */
	private double mb() {
		return mbPerMinute * millis / 60000;
	}

	/**
	 * Checks that the string is positive value.
	 *
	 * @param mbStr String of megabyte value.
	 * @return True if the string is positive value.
	 * @throws Exception An error is thrown if mbStr is not a positive value.
	 */
	public static boolean checkMB(String mbStr) throws Exception {
		try {
			double mb = Double.parseDouble(mbStr);
			if (mb > 0) {
				return true;
			} else {
				throw new Exception("ERROR: Megabyte value must be a positive number!");
			}
		} catch (NumberFormatException e) {
			throw new Exception("ERROR: Megabyte value has to be a positive number!");
		}
	}

	@Override
	public void setStatus(boolean status) {
		if (status) {
			onTime = Timer.getTime();
		} else {
			if (time != null) {
				millis += (double) Duration.between(onTime, time).toMillis();
			} else {
				millis += (double) Duration.between(onTime, Timer.getTime()).toMillis();
			}
		}
		this.status = status;
	}

	@Override
	public String toString() {
		String statusStr = status ? "on" : "off";
		return String.format("Smart Camera %s is %s and used %.2f MB of storage so far (excluding current status), and its time to switch its status is %s.", name, statusStr, mb(), Timer.toStr(time));
	}
}

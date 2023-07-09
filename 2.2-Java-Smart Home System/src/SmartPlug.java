import java.time.Duration;
import java.time.LocalDateTime;

public class SmartPlug extends Device {
	private double ampere;
	private boolean plugged = false;
	private LocalDateTime onTime;
	private LocalDateTime offTime;
	private double millis = 0.0d;


	/**
	 * @param name The name of the smart plug.
	 */
	public SmartPlug(String name) {
		this.name = name;
	}

	/**
	 * @param name   The name of the smart plug.
	 * @param status The status of the smart plug.
	 */
	public SmartPlug(String name, boolean status) {
		this.name = name;
		this.status = status;
		if (status) {
			onTime = Timer.getTime();
		}
	}

	/**
	 * @param name   The name of the smart plug.
	 * @param status The status of the smart plug.
	 * @param ampere The ampere of the smart plug.
	 */
	public SmartPlug(String name, boolean status, float ampere) {
		this.name = name;
		this.status = status;
		this.ampere = ampere;
		plugged = true;
		if (status) {
			onTime = Timer.getTime();
		}
	}

	/**
	 * Sets the ampere value of the smart plug.
	 *
	 * @param ampere The ampere value of the smart plug.
	 */
	public void setAmpere(double ampere) {
		this.ampere = ampere;
	}

	/**
	 * Checks that the ampere value is correct.
	 *
	 * @param ampereStr String of ampere value.
	 * @return True if ampereStr is a true value for ampere.
	 * @throws Exception An error is thrown if the value is not integer.
	 */
	public static boolean checkAmpere(String ampereStr) throws Exception {
		try {
			float ampere = Float.parseFloat(ampereStr);
			return ampere > 0;
		} catch (NumberFormatException e) {
			throw new Exception("ERROR: Erroneous command!");
		}
	}

	/**
	 * Calculates the consumption of the smart plug.
	 *
	 * @return Consumption of the smart plug.
	 */
	private double watt() {
		return 220.0 * this.ampere * (millis / 3600000);
	}

	/**
	 * Plugs the device and keeps the time values.
	 *
	 * @param plugged Boolean value of whether the device is plugged in
	 */
	public void setPlugged(boolean plugged) {
		if (plugged) {
			onTime = Timer.getTime();
		}
		if (!plugged && offTime == null) {
			offTime = Timer.getTime();
		}
		if (!plugged && !(ampere == 0.0)) {
			if (Duration.between(onTime, offTime).toMillis() > 0) {
				millis += (double) Duration.between(offTime, offTime).toMillis();
				offTime = time;
			}
		}
		this.plugged = plugged;
	}

	/**
	 * Returns boolean value of whether the device is plugged in
	 *
	 * @return Boolean value of whether the device is plugged in
	 */
	public boolean getPlugged() {
		return plugged;
	}

	@Override
	public void setStatus(boolean status) {
		if (status) {
			onTime = Timer.getTime();
		} else {
			if (time != null) {
				offTime = time;
			} else {
				offTime = Timer.getTime();
			}
			millis += (double) Duration.between(onTime, offTime).toMillis();
		}
		this.status = status;
	}

	@Override
	public String toString() {
		String statusStr = status ? "on" : "off";
		return String.format("Smart Plug %s is %s and consumed %.2fW so far (excluding current device), and its time to switch its status is %s.", name, statusStr, watt(), Timer.toStr(time));
	}
}

public class SmartLamp extends Device {
	protected int kelvin;
	protected int brightness;

	/**
	 * The status of the lamp is set to false and the kelvin value and brightness level are set to 4000 and 100 respectively by default.
	 *
	 * @param name The name of the lamp
	 */
	public SmartLamp(String name) {
		this(name, false, 4000, 100);
	}

	/**
	 * The kelvin value and brightness level are set to 4000 and 100 respectively by default.
	 *
	 * @param name   The name of the smart lamp
	 * @param status The status of the smart lamp.
	 */
	public SmartLamp(String name, boolean status) {
		this(name, status, 4000, 100);
	}

	/**
	 * @param name       The name of the smart lamp
	 * @param status     The status of the smart lamp.
	 * @param kelvin     The kelvin value of the smart lamp.
	 * @param brightness The brightness level of the smart lamp.
	 */
	public SmartLamp(String name, boolean status, int kelvin, int brightness) {
		this.name = name;
		this.status = status;
		this.kelvin = kelvin;
		this.brightness = brightness;
	}

	/**
	 * Checks that the kelvin value is correct.
	 *
	 * @param kelvinStr String of kelvin value.
	 * @return True if kelvinStr is a true value for kelvin.
	 * @throws Exception An error is thrown if the value is wrong.
	 */
	public static boolean checkKelvin(String kelvinStr) throws Exception {
		try {
			int kelvin = Integer.parseInt(kelvinStr);
			return (2000 <= kelvin && 6500 >= kelvin);
		} catch (NumberFormatException e) {
			throw new Exception("ERROR: Erroneous command!");
		}
	}

	/**
	 * Checks that the brightness level is correct.
	 *
	 * @param brightnessStr String of brightness level.
	 * @return True if brightnessStr is a true value for brightness.
	 * @throws Exception An error is thrown if the value is wrong.
	 */
	public static boolean checkBrightness(String brightnessStr) throws Exception {
		try {
			int brightness = Integer.parseInt(brightnessStr);
			return ((0 <= brightness) && (100 >= brightness));
		} catch (NumberFormatException e) {
			throw new Exception("ERROR: Erroneous command!");
		}
	}

	/**
	 * Sets the kelvin value of the smart lamp.
	 *
	 * @param kelvin The kelvin value.
	 */
	public void setKelvin(int kelvin) {
		this.kelvin = kelvin;
	}

	/**
	 * Sets the brightness level of the smart lamp.
	 *
	 * @param brightness The brightness value.
	 */
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	@Override
	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		String statusStr = status ? "on" : "off";
		return String.format("Smart Lamp %s is %s and its kelvin value is %dK with %s%% brightness, and its time to switch its status is %s.", name, statusStr, kelvin, brightness, Timer.toStr(time));
	}

}

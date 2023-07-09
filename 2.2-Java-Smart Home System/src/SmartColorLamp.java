public class SmartColorLamp extends SmartLamp {
	private String color;

	/**
	 * @param name The name of the smart lamp.
	 */
	public SmartColorLamp(String name) {
		super(name);
	}

	/**
	 * @param name   The name of the smart color lamp.
	 * @param status The status of the smart color lamp.
	 */
	public SmartColorLamp(String name, boolean status) {
		super(name, status);
	}

	/**
	 * @param name       The name of the smart color lamp.
	 * @param status     The status of the smart color lamp.
	 * @param kelvin     The kelvin value of the smart color lamp.
	 * @param brightness The brightness level of the smart color lamp.
	 */
	public SmartColorLamp(String name, boolean status, int kelvin, int brightness) {
		super(name, status, kelvin, brightness);
	}

	/**
	 * @param name       The name of the smart color lamp.
	 * @param status     The status of the smart color lamp.
	 * @param hex        The color code of the smart color lamp.
	 * @param brightness The brightness level of the smart color lamp.
	 */
	public SmartColorLamp(String name, boolean status, String hex, int brightness) {
		super(name, status);
		this.color = hex;
		this.brightness = brightness;
	}

	/**
	 * Checks that the string is hexadecimal.
	 *
	 * @param hexStr String of hexadecimal code.
	 * @return True if hexStr is a true value for color code.
	 * @throws Exception An error is thrown if hexStr cannot be converted to hexadecimal code, if the range is incorrect.
	 */
	public static boolean checkHex(String hexStr) throws Exception {
		if (!hexStr.startsWith("0x")) {
			return false;
		}

		String hex = hexStr.substring(2);
		for (int i = 0; i < hex.length(); i++) {
			char letter = hex.charAt(i);
			if ((!Character.isDigit(letter) && (letter > 'F' || letter < 'A'))) {
				throw new Exception("ERROR: Erroneous command!");
			}
			if (hex.length() > 6) {
				throw new Exception("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
			}
		}
		return true;
	}

	/**
	 * Sets the color of the smart color lamp to the hex value.
	 *
	 * @param hex The hexadecimal value.
	 */
	public void setColor(String hex) {
		this.color = hex;
	}

	@Override
	public String toString() {
		String statusStr = status ? "on" : "off";
		if (color == null) {
			return String.format("Smart Color Lamp %s is %s and its color value is %dK with %s%% brightness, and its time to switch its status is %s.", name, statusStr, kelvin, brightness, Timer.toStr(time));
		} else {
			return String.format("Smart Color Lamp %s is %s and its color value is %s with %s%% brightness, and its time to switch its status is %s.", name, statusStr, color, brightness, Timer.toStr(time));
		}
	}
}

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	public static final List<Device> devices = new ArrayList<>();
	public static String outputFile;
	public static boolean isRun = true;

	/**
	 * Reads the input file line by line and calls the required methods.
	 *
	 * @param outputFile Path for the file content is going to be written.
	 * @param commands   An array containing the commands from the input file.
	 */
	public static void run(String[] commands, String outputFile) {
		CommandManager.outputFile = outputFile;
		run:
		for (String command : commands) {
			try {
				FileOutput.writeToFile(outputFile, "COMMAND: " + command, true, true); //Prints COMMAND: <...> line.
				String[] words = command.split("\t"); //Splits the command line by "\t".
				switch (words[0]) {
					case "SetInitialTime":
						try {
							Timer.setInitialTime(words);
						} catch (ArrayIndexOutOfBoundsException e) {
							FileOutput.writeToFile(outputFile, "ERROR: A time must be entered in the format \"<YEAR>-<MONTH>-<DAY>_<HOUR>:<MINUTE>:<SECOND>\".", true, true);
							isRun = false;
							break run;
						} catch (DateTimeParseException e) {
							FileOutput.writeToFile(outputFile, "ERROR: Format of the initial date is wrong! Program is going to terminate!", true, true);
							isRun = false;
							break run;
						}
						break;
					case "Add":
						switch (words[1]) {
							case "SmartLamp":
								addLamp(words);
								break;
							case "SmartColorLamp":
								addColorLamp(words);
								break;
							case "SmartCamera":
								addCamera(words);
								break;
							case "SmartPlug":
								addPlug(words);
								break;
						}
						break;
					case "PlugIn":
						plugIn(words);
						break;
					case "PlugOut":
						plugOut(words);
						break;
					case "SetTime":
						Timer.setTimeCommand(words);
						break;
					case "SkipMinutes":
						Timer.skipMinutes(words);
						break;
					case "SetKelvin":
						setKelvin(words);
						break;
					case "SetBrightness":
						setBrightness(words);
						break;
					case "SetColorCode":
						setColorCode(words);
						break;
					case "SetWhite":
						setWhite(words);
						break;
					case "SetColor":
						setColorCommand(words);
						break;
					case "Remove":
						removeCommand(words);
						break;
					case "ChangeName":
						changeName(words);
						break;
					case "Switch":
						switchCommand(words);
						break;
					case "SetSwitchTime":
						Timer.setSwitchTime(words);
						break;
					case "Nop":
						Timer.nop();
						break;
					case "ZReport":
						zReport();
						break;
					default:
						FileOutput.writeToFile(outputFile, "ERROR: Erroneous command!", true, true);
						break;
				}
			} catch (Exception e) {
				FileOutput.writeToFile(outputFile, e.getMessage(), true, true);
			}
		}
	}

	/**
	 * Adds a new plug to the devices array.
	 *
	 * @param words An array that should contain the device name and can contain the initial status of the device and ampere.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is a device with the same name, if the ampere value is not positive.
	 */
	private static void addPlug(String[] words) throws Exception {
		if (words.length != 5 && words.length != 4 && words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}

		String name = words[2];
		if (!checkName(name)) {
			boolean status;
			switch (words.length) {
				case 3:
					devices.add(new SmartPlug(name));
					break;
				case 4:
					status = checkStatus(words[3]);
					devices.add(new SmartPlug(name, status));
					break;
				case 5:
					status = checkStatus(words[3]);
					if (SmartPlug.checkAmpere(words[4])) {
						devices.add(new SmartPlug(name, status, Float.parseFloat(words[4])));
					} else {
						throw new Exception("ERROR: Ampere value must be a positive number!");
					}
					break;
				default:
					break;
			}
		} else {
			throw new Exception("ERROR: There is already a smart device with same name!");
		}
	}

	/**
	 * Adds a new camera to the devices array.
	 *
	 * @param words An array that should contain the device name and the megabytes consumed per record of camera, and can contain the initial status of the device.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is a device with the same name,
	 */
	private static void addCamera(String[] words) throws Exception {
		if (words.length != 4 && words.length != 5) {
			throw new Exception("ERROR: Erroneous command!");
		}

		String name = words[2];
		if (!checkName(name)) {
			boolean status;
			if (SmartCamera.checkMB(words[3])) {
				switch (words.length) {
					case 4:
						devices.add(new SmartCamera(name, Double.parseDouble(words[3])));
						break;
					case 5:
						status = checkStatus(words[4]);
						devices.add(new SmartCamera(name, Double.parseDouble(words[3]), status));
						break;
					default:
						break;
				}
			}
		} else {
			throw new Exception("ERROR: There is already a smart device with same name!");
		}
	}

	/**
	 * Adds a new lamp to the devices array.
	 *
	 * @param words An array that should contain the device name, and can contain the initial status of the device and kelvin & brightness values of the device.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is a device with the same name, if the kelvin value or the brightness level is entered incorrectly.
	 */
	private static void addLamp(String[] words) throws Exception {
		if (words.length != 6 && words.length != 4 && words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}

		String name = words[2];
		boolean status;
		if (!checkName(name)) {
			switch (words.length) {
				case 3:
					devices.add(new SmartLamp(name));
					break;
				case 4:
					status = checkStatus(words[3]);
					devices.add(new SmartLamp(name, status));
					break;
				case 6:
					status = checkStatus(words[3]);
					if (SmartLamp.checkKelvin(words[4])) {
						if (SmartLamp.checkBrightness(words[5])) {
							devices.add(new SmartLamp(name, status, Integer.parseInt(words[4]), Integer.parseInt(words[5])));
						} else {
							throw new Exception("ERROR: Brightness must be in range of 0%-100%!");
						}
					} else {
						throw new Exception("ERROR: Kelvin value must be in range of 2000K-6500K!");
					}
					break;
			}
		} else {
			throw new Exception("ERROR: There is already a smart device with same name!");
		}
	}

	/**
	 * Adds a new color lamp to the devices array.
	 *
	 * @param words An array that should contain the device name, and can contain the initial status of the device and kelvin & brightness values of the device
	 *              or can contain the color code and brightness values of the device.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is a device with the same name, if the kelvin value or the color code or the brightness level is entered incorrectly.
	 */
	private static void addColorLamp(String[] words) throws Exception {
		if (words.length != 6 && words.length != 4 && words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}

		String name = words[2];
		boolean status;
		if (!checkName(name)) {
			switch (words.length) {
				case 3:
					devices.add(new SmartColorLamp(name));
					break;
				case 4:
					status = checkStatus(words[3]);
					devices.add(new SmartColorLamp(name, status));
					break;
				case 6:
					status = checkStatus(words[3]);
					if (SmartColorLamp.checkHex(words[4]) && SmartLamp.checkBrightness(words[5])) {
						devices.add(new SmartColorLamp(name, status, words[4], Integer.parseInt(words[5])));
					} else if (SmartLamp.checkKelvin(words[4]) && SmartLamp.checkBrightness(words[5])) {
						devices.add(new SmartColorLamp(name, status, Integer.parseInt(words[4]), Integer.parseInt(words[5])));
					} else {
						throw new Exception("ERROR: Erroneous command!");
					}
					break;
			}
		} else {
			throw new Exception("ERROR: There is already a smart device with same name!");
		}
	}

	/**
	 * Removes a device from the devices array.
	 *
	 * @param words An array that should contain the device name.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name.
	 */
	private static void removeCommand(String[] words) throws Exception {
		if (words.length != 2) {
			throw new Exception("ERROR: Erroneous command!");
		}

		if (checkName(words[1])) {
			Device removeDevice = findDevice(words[1]);
			FileOutput.writeToFile(outputFile, "SUCCESS: Information about removed smart device is as follows:", true, true);
			removeDevice.setStatus(false); //To calculate the total consumption (MB or watt)
			FileOutput.writeToFile(outputFile, removeDevice.toString(), true, true);
			devices.remove(findDevice(words[1]));
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Turns the device on or off.
	 *
	 * @param words An array that should contain the device name and new initial status.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the initial status is the same as the new status.
	 */
	private static void switchCommand(String[] words) throws Exception {
		if (words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}

		if (checkName(words[1])) {
			Device device = findDevice(words[1]);
			boolean status = checkStatus(words[2]);

			if (device.getStatus() == status) {
				throw new Exception("ERROR: This device is already switched " + words[2].toLowerCase() + "!");
			} else {
				device.setStatus(status);
			}

		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Changes the name of a device.
	 *
	 * @param words An array that should contain the old name of device and new name.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if the new name is the same as the old name, if there is no device with old name, if there is already a device with same name.
	 */
	private static void changeName(String[] words) throws Exception {
		if (words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}

		if (!words[1].equals(words[2])) { //Checks if two names are equal.
			if (checkName(words[1])) {
				if (!checkName(words[2])) {
					Device device = findDevice(words[1]);
					device.setName(words[2]);
				} else {
					throw new Exception("ERROR: There is already a smart device with same name!");
				}
			} else {
				throw new Exception("ERROR: There is not such a device!");
			}
		} else {
			throw new Exception("ERROR: Both of the names are the same, nothing changed!");
		}
	}

	/**
	 * Plugs in the device from the smart plug.
	 *
	 * @param words An array that should contain the device name and ampere value.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the device is not a smart plug, if there is already an item plugged in to the plug, if the ampere value is not a positive number!.
	 */
	private static void plugIn(String[] words) throws Exception {
		if (words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}

		if (checkName(words[1])) {
			if (findDevice(words[1]) instanceof SmartPlug) {
				SmartPlug plug = (SmartPlug) findDevice(words[1]);
				if (!plug.getPlugged()) {
					if (SmartPlug.checkAmpere(words[2])) {
						plug.setAmpere(Double.parseDouble(words[2]));
						plug.setPlugged(true);
					} else {
						throw new Exception("ERROR: Ampere value must be a positive number!");
					}
				} else {
					throw new Exception("ERROR: There is already an item plugged in to that plug!");
				}
			} else {
				throw new Exception("ERROR: This device is not a smart plug!");
			}
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Plugs out the device from the smart plug.
	 *
	 * @param words An array that should contain the device name.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the device is not a smart plug, if there is no item to plug out from the plug.
	 */
	private static void plugOut(String[] words) throws Exception {
		if (words.length != 2) {
			throw new Exception("ERROR: Erroneous command!");
		}

		if (checkName(words[1])) {
			if (findDevice(words[1]) instanceof SmartPlug) {
				SmartPlug plug = (SmartPlug) findDevice(words[1]);
				if (plug.getPlugged()) {
					plug.setPlugged(false);
				} else {
					throw new Exception("ERROR: This plug has no item to plug out from that plug!");
				}
			} else {
				throw new Exception("ERROR: This device is not a smart plug!");
			}
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Sets the tune of a smart lamp's kelvin.
	 *
	 * @param words An array that should contain the device name and new kelvin value.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the device is not a smart lamp.
	 */
	private static void setKelvin(String[] words) throws Exception {
		if (words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}
		if (checkName(words[1])) {
			Device device = findDevice(words[1]);
			if (device instanceof SmartLamp) {
				if (SmartLamp.checkKelvin(words[2])) {
					((SmartLamp) device).setKelvin(Integer.parseInt(words[2]));
					if (device instanceof SmartColorLamp) {
						((SmartColorLamp) device).setColor(null);
					}
				} else {
					throw new Exception("ERROR: Kelvin value must be in range of 2000K-6500K!");
				}
			} else {
				throw new Exception("ERROR: This device is not a smart lamp!");
			}
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Sets the brightness level of a smart lamp.
	 *
	 * @param words An array that should contain the device name and new brightness level.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the device is not a smart lamp.
	 */
	private static void setBrightness(String[] words) throws Exception {
		if (words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}
		if (checkName(words[1])) {
			Device device = findDevice(words[1]);
			if (device instanceof SmartLamp) {
				if (SmartLamp.checkBrightness(words[2])) {
					((SmartLamp) device).setBrightness(Integer.parseInt(words[2]));
				} else {
					throw new Exception("ERROR: Brightness must be in range of 0%-100%!");
				}
			} else {
				throw new Exception("ERROR: This device is not a smart lamp!");
			}
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Sets the color of a smart color lamp using a hexadecimal color code.
	 *
	 * @param words An array that should contain the device name and new hexadecimal color code.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the device is not a smart color lamp.
	 */
	private static void setColorCode(String[] words) throws Exception {
		if (words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}
		if (checkName(words[1])) {
			Device device = findDevice(words[1]);
			if (device instanceof SmartColorLamp) {
				if (SmartColorLamp.checkHex(words[2])) {
					((SmartColorLamp) device).setColor(words[2]);
				} else {
					throw new Exception("ERROR: Erroneous command!");
				}
			} else {
				throw new Exception("ERROR: This device is not a smart color lamp!");
			}
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Sets the white color of a smart lamp.
	 *
	 * @param words An array that should contain the device name, new kelvin value and new brightness level.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the device is not a smart lamp.
	 */
	private static void setWhite(String[] words) throws Exception {
		if (words.length != 4) {
			throw new Exception("ERROR: Erroneous command!");
		}
		if (checkName(words[1])) {
			Device device = findDevice(words[1]);
			if (device instanceof SmartLamp) {
				if (SmartLamp.checkKelvin(words[2])) {
					if (SmartLamp.checkBrightness(words[3])) {
						((SmartLamp) device).setKelvin(Integer.parseInt(words[2]));
						((SmartLamp) device).setBrightness(Integer.parseInt(words[3]));
						if (device instanceof SmartColorLamp) {
							((SmartColorLamp) device).setColor(null);
						}
					} else {
						throw new Exception("ERROR: Brightness must be in range of 0%-100%!");
					}
				} else {
					throw new Exception("ERROR: Kelvin value must be in range of 2000K-6500K!");
				}
			} else {
				throw new Exception("ERROR: This device is not a smart lamp!");
			}
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Sets the color of a smart color lamp.
	 *
	 * @param words An array that should contain the device name, new hexadecimal color code and new brightness level.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name, if the device is not a smart color lamp.
	 */
	private static void setColorCommand(String[] words) throws Exception {
		if (words.length != 4) {
			throw new Exception("ERROR: Erroneous command!");
		}
		if (checkName(words[1])) {
			Device device = findDevice(words[1]);
			if (device instanceof SmartColorLamp) {
				if (SmartColorLamp.checkHex(words[2])) {
					if (SmartLamp.checkBrightness(words[3])) {
						((SmartColorLamp) device).setColor(words[2]);
						((SmartColorLamp) device).setBrightness(Integer.parseInt(words[3]));
					} else {
						throw new Exception("ERROR: Brightness must be in range of 0%-100%!");
					}
				} else {
					throw new Exception("ERROR: Erroneous command!");
				}
			} else {
				throw new Exception("ERROR: This device is not a smart color lamp!");
			}
		} else {
			throw new Exception("ERROR: There is not such a device!");
		}
	}

	/**
	 * Writes the information of the existing devices in the desired order.
	 */
	public static void zReport() {
		FileOutput.writeToFile(outputFile, "Time is:\t" + Timer.toStr(Timer.getTime()), true, true);
		for (Device device : devices) {
			if (device.getTime() != null && device.getTime().isBefore(Timer.getTime())) {
				device.setTime(null);
				device.setStatus(!device.getStatus());
			}
		}
		Timer.getSwitchTimes().removeIf(time -> time.isBefore(Timer.getTime()) || time.isEqual(Timer.getTime()));
		Timer.sortDevices(devices);
		for (Device device : devices) {
			FileOutput.writeToFile(outputFile, device.toString(), true, true);
		}

	}

	/**
	 * Converts string text to true or false.
	 *
	 * @param word The string to be converted.
	 * @return True if word is "on", false if word is "off".
	 * @throws Exception The word is not "on" or "off", an error is thrown.
	 */
	private static boolean checkStatus(String word) throws Exception {
		if (word.equals("On")) {
			return true;
		} else if (word.equals("Off")) {
			return false;
		}
		throw new Exception("ERROR: Erroneous command!");
	}

	/**
	 * Checks if there is a device with the name.
	 *
	 * @param name The name of the checked device.
	 * @return True if the device exists, false if there is no such a device.
	 */
	public static boolean checkName(String name) {
		if (devices.size() > 0) {
			for (Device device : devices) {
				if (device.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Finds the searched device.
	 *
	 * @param name The name of the device being searched for.
	 * @return The device with the searched name, returns null if there is no such a device.
	 * @throws Exception An error is thrown if the device is not found.
	 */
	public static Device findDevice(String name) throws Exception {
		for (Device device : devices) {
			if (device.getName().equals(name)) {
				return device;
			}
		}
		throw new Exception("ERROR: Erroneous command!");
	}
}

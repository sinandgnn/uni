import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Timer {
	public static List<LocalDateTime> switchTimes = new ArrayList<>();
	private static LocalDateTime time;

	/**
	 * Gets the list of switch times.
	 *
	 * @return The list with switch times.
	 */
	public static List<LocalDateTime> getSwitchTimes() {
		return switchTimes;
	}

	/**
	 * Adds a new time to the list of switch times.
	 *
	 * @param addTime Time to add.
	 */
	public static void addSwitchTimes(LocalDateTime addTime) {
		Timer.switchTimes.add(addTime);
	}

	/**
	 * Sorts the given list by time.
	 *
	 * @param deviceList Arraylist to be sorted.
	 */
	public static void sortDevices(List<Device> deviceList) {
		Comparator<Device> comparator = (o1, o2) -> {
			if (o1.getTime() == null && o2.getTime() == null) {
				return 0;
			} else if (o1.getTime() == null) {
				return 1;
			} else if (o2.getTime() == null) {
				return -1;
			} else {
				return o1.getTime().compareTo(o2.getTime());
			}
		};
		deviceList.sort(comparator);
	}

	/**
	 * Sorts the given list in reverse.
	 *
	 * @param deviceList Arraylist to be sorted.
	 */
	public static void sortReverse(List<Device> deviceList) {
		Comparator<Device> reverse = (o1, o2) -> {
			if (o1.getTime().isEqual(o2.getTime())) {
				return -1;
			} else {
				return o1.getTime().compareTo(o2.getTime());
			}
		};
		deviceList.sort(reverse);
	}

	/**
	 * Returns the current time.
	 *
	 * @return LocalDateTime object.
	 */
	public static LocalDateTime getTime() {
		return time;
	}

	/**
	 * Returns a date of the given String object in the format "yyyy-MM-dd_HH:mm:ss".
	 *
	 * @param time The String object to be converted to a LocalDateTime.
	 * @return LocalDateTime object of the given date in the format "yyyy-MM-dd_HH:mm:ss".
	 */
	public static LocalDateTime toDate(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
		return LocalDateTime.parse(time, formatter);
	}

	/**
	 * Returns a string of the given LocalDateTime object in the format "yyyy-MM-dd_HH:mm:ss".
	 *
	 * @param time The LocalDateTime object to be converted to a String.
	 * @return String of the given date in the format "yyyy-MM-dd_HH:mm:ss".
	 */
	public static String toStr(LocalDateTime time) {
		if (time == null) {
			return "null";
		} else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
			return time.format(formatter);
		}
	}

	/**
	 * Sets the first time of program.
	 *
	 * @param words An array that should contain the first time.
	 * @throws Exception An error is thrown if the initial time has already been set.
	 */
	static void setInitialTime(String[] words) throws Exception {
		if (getTime() == null) {
			time = toDate(words[1]);
			FileOutput.writeToFile(CommandManager.outputFile, "SUCCESS: Time has been set to " + toStr(getTime()) + "!", true, true);
		} else {
			throw new Exception("ERROR: Erroneous command!");
		}
	}

	/**
	 * Sets the time with respect to given time and sorts the devices arraylist.
	 *
	 * @param words An array that should contain the new time.
	 * @throws Exception An error is thrown if the time format is not correct or the time is in the past.
	 */
	static void setTimeCommand(String[] words) throws Exception {
		if (words.length != 2) {
			throw new Exception("ERROR: Erroneous command!");
		}

		try {
			LocalDateTime newTime = toDate(words[1]);
			if (newTime.isAfter(getTime())) {
				time = newTime;
				List<Device> reverse = new ArrayList<>();
				for (Device device : CommandManager.devices) {
					if (device.getTime() != null && device.getTime().isBefore(newTime)) {
						reverse.add(device); //Keeps time lapses in the list.
					}
				}
				if (reverse.size() != 0) {
					sortReverse(reverse);
					for (Device dvc : reverse) { //To simulate a simultaneous shutdown
						Device realDvc = CommandManager.findDevice(dvc.getName());
						realDvc.setStatus(!dvc.getStatus());
						realDvc.setTime(null);
						sortDevices(CommandManager.devices);
					}
					reverse.clear();
				}
				getSwitchTimes().removeIf(time -> time.isBefore(newTime) || time.isEqual(newTime));
			} else if (newTime.isEqual(getTime())) {
				throw new Exception("ERROR: There is nothing to change!");
			} else {
				throw new Exception("ERROR: Time cannot be reversed!");
			}
		} catch (DateTimeParseException e) {
			throw new Exception("ERROR: Time format is not correct!");
		}
	}

	/**
	 * Skips given amount of minutes.
	 *
	 * @param words An array that should contain the given amount of minutes.
	 * @throws Exception An error is thrown if the value given is not a positive integer.
	 */
	static void skipMinutes(String[] words) throws Exception {
		if (words.length != 2) {
			throw new Exception("ERROR: Erroneous command!");
		}
		try {
			int minute = Integer.parseInt(words[1]);
			if (minute > 0) {
				time = getTime().plusMinutes(minute);
			} else if (minute == 0) {
				throw new Exception("ERROR: There is nothing to skip!");
			} else {
				throw new Exception("ERROR: Time cannot be reversed!");
			}
		} catch (NumberFormatException e) {
			throw new Exception("ERROR: Erroneous command!");
		}
	}

	/**
	 * Skips forwards the time to the first switch event and does the necessary switch operations.
	 */
	static void nop() {
		Timer.getSwitchTimes().removeIf(time -> time.isBefore(Timer.getTime()));
		if (getSwitchTimes().size() != 0) {
			Collections.sort(getSwitchTimes());
			LocalDateTime nopTime = getSwitchTimes().get(0);
			getSwitchTimes().removeIf(time -> time.isBefore(nopTime) || time.isEqual(nopTime));
			for (Device device : CommandManager.devices) {
				if ((device.getTime() != null) && (device.getTime().isBefore(nopTime) || device.getTime().isEqual(nopTime))) {
					device.setStatus(!device.getStatus());
					device.setTime(null);
					time = nopTime;
				}
			}
		} else {
			FileOutput.writeToFile(CommandManager.outputFile, "ERROR: There is nothing to switch!", true, true);
		}
	}

	/**
	 * Timely, turns off or on the device's status.
	 *
	 * @param words An array that should contain the device name and the switch time.
	 * @throws Exception An error is thrown if the number of arguments entered is wrong, if there is no device with this name.
	 */
	static void setSwitchTime(String[] words) throws Exception {
		if (words.length != 3) {
			throw new Exception("ERROR: Erroneous command!");
		}
		try {
			if (CommandManager.checkName(words[1])) {
				Device device = CommandManager.findDevice(words[1]);
				LocalDateTime switchTime = toDate(words[2]);
				if (getTime().isAfter(switchTime)) {
					throw new Exception("ERROR: Switch time cannot be in the past!");
				} else if (getTime().isEqual(switchTime)){
					device.setTime(switchTime);
					device.setStatus(!device.getStatus());
					Timer.getSwitchTimes().remove(switchTime);
					sortDevices(CommandManager.devices);
					device.setTime(null);
					sortDevices(CommandManager.devices);
				} else {
					addSwitchTimes(switchTime);
					device.setTime(switchTime);
				}
			} else {
				FileOutput.writeToFile(CommandManager.outputFile, "ERROR: There is not such a device!", true, true);
			}
		} catch (DateTimeParseException e) {
			throw new Exception("ERROR: Time format is not correct!");
		}

	}
}

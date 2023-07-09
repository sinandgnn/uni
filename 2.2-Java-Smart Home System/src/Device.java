import java.time.LocalDateTime;

public abstract class Device {
	protected String name;
	protected boolean status;
	protected LocalDateTime time;

	/**
	 * Sets the name of the smart device.
	 *
	 * @param name New name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the smart device.
	 *
	 * @return Name of the smart device.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the status of the smart device.
	 *
	 * @param status Status of smart device.
	 */
	abstract void setStatus(boolean status);

	/**
	 * Gets the status of the smart device.
	 *
	 * @return Status of the smart device.
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * Sets the switch time of the smart device.
	 *
	 * @param time Switch time of the smart device.
	 */
	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	/**
	 * Gets the switch time of the smart device.
	 *
	 * @return Switch time of the smart device
	 */
	public LocalDateTime getTime() {
		return time;
	}
}

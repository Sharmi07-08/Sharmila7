import java.util.ArrayList;
import java.util.List;

// Device.java
interface Device {
    void turnOn();
    void turnOff();
    String getStatus();
    int getId();
}

// Light.java
class Light implements Device {
    private int id;
    private boolean isOn;

    public Light(int id) {
        this.id = id;
        this.isOn = false;
    }

    @Override
    public void turnOn() {
        isOn = true;
        System.out.println("Light " + id + " is turned on.");
    }

    @Override
    public void turnOff() {
        isOn = false;
        System.out.println("Light " + id + " is turned off.");
    }

    @Override
    public String getStatus() {
        return "Light " + id + " is " + (isOn ? "On" : "Off");
    }

    @Override
    public int getId() {
        return id;
    }
}

// Thermostat.java
class Thermostat implements Device {
    private int id;
    private int temperature;

    public Thermostat(int id, int temperature) {
        this.id = id;
        this.temperature = temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        System.out.println("Thermostat " + id + " set to " + temperature + " degrees.");
    }

    @Override
    public void turnOn() {
        System.out.println("Thermostat " + id + " is turned on.");
    }

    @Override
    public void turnOff() {
        System.out.println("Thermostat " + id + " is turned off.");
    }

    @Override
    public String getStatus() {
        return "Thermostat " + id + " is set to " + temperature + " degrees";
    }

    @Override
    public int getId() {
        return id;
    }
}

// Door.java
class Door implements Device {
    private int id;
    private boolean isLocked;

    public Door(int id) {
        this.id = id;
        this.isLocked = true;
    }

    public void lock() {
        isLocked = true;
        System.out.println("Door " + id + " is locked.");
    }

    public void unlock() {
        isLocked = false;
        System.out.println("Door " + id + " is unlocked.");
    }

    @Override
    public void turnOn() {
        unlock();
    }

    @Override
    public void turnOff() {
        lock();
    }

    @Override
    public String getStatus() {
        return "Door " + id + " is " + (isLocked ? "Locked" : "Unlocked");
    }

    @Override
    public int getId() {
        return id;
    }
}

// DeviceFactory.java
class DeviceFactory {
    public static Device createDevice(int id, String type) {
        switch (type.toLowerCase()) {
            case "light":
                return new Light(id);
            case "thermostat":
                return new Thermostat(id, 70);
            case "door":
                return new Door(id);
            default:
                throw new IllegalArgumentException("Unknown device type");
        }
    }
}

// DeviceProxy.java
class DeviceProxy implements Device {
    private Device device;

    public DeviceProxy(Device device) {
        this.device = device;
    }

    @Override
    public void turnOn() {
        System.out.println("Proxy: Checking access before turning on the device...");
        device.turnOn();
    }

    @Override
    public void turnOff() {
        System.out.println("Proxy: Checking access before turning off the device...");
        device.turnOff();
    }

    @Override
    public String getStatus() {
        return device.getStatus();
    }

    @Override
    public int getId() {
        return device.getId();
    }
}

// SmartHomeSystem.java
class SmartHomeSystem {
    private List<Device> devices = new ArrayList<>();
    private List<Schedule> schedules = new ArrayList<>();

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void removeDevice(int deviceId) {
        devices.removeIf(device -> device.getId() == deviceId);
    }

    public void turnOnDevice(int deviceId) {
        devices.stream()
                .filter(device -> device.getId() == deviceId)
                .forEach(Device::turnOn);
    }

    public void turnOffDevice(int deviceId) {
        devices.stream()
                .filter(device -> device.getId() == deviceId)
                .forEach(Device::turnOff);
    }

    public void setSchedule(int deviceId, String time, String action) {
        Schedule schedule = new Schedule(deviceId, time, action);
        schedules.add(schedule);
        System.out.println("Schedule added: " + schedule);
    }

    public void showStatus() {
        devices.forEach(device -> System.out.println(device.getStatus()));
    }

    public void automateTask(String condition, String action) {
        System.out.println("Automated task: If " + condition + " then " + action);
    }
}

// Schedule.java
class Schedule {
    private int deviceId;
    private String time;
    private String action;

    public Schedule(int deviceId, String time, String action) {
        this.deviceId = deviceId;
        this.time = time;
        this.action = action;
    }

    @Override
    public String toString() {
        return "Schedule: Device " + deviceId + " at " + time + " with action " + action;
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        SmartHomeSystem system = new SmartHomeSystem();

        Device light = DeviceFactory.createDevice(1, "light");
        Device thermostat = DeviceFactory.createDevice(2, "thermostat");
        Device door = DeviceFactory.createDevice(3, "door");

        system.addDevice(new DeviceProxy(light));
        system.addDevice(new DeviceProxy(thermostat));
        system.addDevice(new DeviceProxy(door));

        system.turnOnDevice(1);
        system.turnOnDevice(2);
        system.turnOnDevice(3);

        system.showStatus();

        system.setSchedule(1, "06:00", "Turn On");
        system.automateTask("temperature > 75", "turnOff(1)");
    }
}

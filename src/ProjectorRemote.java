/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/**
 * Điều khiển máy chiếu.
 * Sẵn sàng khi Available và pin >= 20%. Điểm ưu tiên = mức pin.
 */
public class ProjectorRemote extends TeachingDevice {
    private static final int MIN_BATTERY = 20;

    private final int batteryLevel;

    public ProjectorRemote(String id, String name, String storageLocation, int batteryLevel) {
        super(id, name, storageLocation);
        this.batteryLevel = requirePercent(batteryLevel, "Mức pin");
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.Available && batteryLevel >= MIN_BATTERY;
    }

    @Override
    public double getDefaultPriorityScore() {
        return batteryLevel;
    }
}

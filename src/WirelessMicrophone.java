/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/**
 * Micro không dây.
 * Sẵn sàng khi Available, pin >= 30% và tín hiệu >= 40%.
 * Điểm ưu tiên = trung bình(pin, chất lượng tín hiệu).
 */
public class WirelessMicrophone extends TeachingDevice {
    private static final int MIN_BATTERY = 30;
    private static final int MIN_SIGNAL = 40;

    private final int batteryLevel;
    private final int signalQuality;

    public WirelessMicrophone(String id, String name, String storageLocation, int batteryLevel, int signalQuality) {
        super(id, name, storageLocation);
        this.batteryLevel = requirePercent(batteryLevel, "Mức pin");
        this.signalQuality = requirePercent(signalQuality, "Chất lượng tín hiệu");
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public int getSignalQuality() {
        return signalQuality;
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.Available
                && batteryLevel >= MIN_BATTERY
                && signalQuality >= MIN_SIGNAL;
    }

    @Override
    public double getDefaultPriorityScore() {
        return (batteryLevel + signalQuality) / 2.0;
    }
}

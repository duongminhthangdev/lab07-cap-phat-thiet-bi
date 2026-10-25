/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/**
 * Điều khiển màn hình tương tác - loại thiết bị BỔ SUNG để minh họa
 * khả năng mở rộng: thêm lớp mới mà không sửa AllocationService/Repository.
 * Sẵn sàng khi Available và pin >= 50%. Điểm ưu tiên = mức pin.
 */
public class InteractiveDisplayRemote extends TeachingDevice {
    private static final int MIN_BATTERY = 50;

    private final int batteryLevel;

    public InteractiveDisplayRemote(String id, String name, String storageLocation, int batteryLevel) {
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

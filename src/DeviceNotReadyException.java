/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/** Ném ra khi mượn thiết bị chưa sẵn sàng. */
public class DeviceNotReadyException extends RuntimeException {
    private final String deviceId;
    private final DeviceStatus currentStatus;

    public DeviceNotReadyException(String deviceId, DeviceStatus currentStatus) {
        super("Thiết bị " + deviceId + " chưa sẵn sàng (trạng thái: " + currentStatus + ").");
        this.deviceId = deviceId;
        this.currentStatus = currentStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceStatus getCurrentStatus() {
        return currentStatus;
    }
}

/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/**
 * Lớp trừu tượng cho mọi thiết bị dạy học.
 * - Phần chung (mã, tên, vị trí, trạng thái, mượn/trả) nằm ở lớp cơ sở.
 * - Phần thay đổi theo loại thiết bị (kiểm tra sẵn sàng, tính điểm ưu tiên)
 *   là phương thức trừu tượng, được ghi đè ở lớp dẫn xuất (đa hình).
 */
public abstract class TeachingDevice implements Identifiable {
    private final String id;
    private final String name;
    private final String storageLocation;
    private DeviceStatus status;

    protected TeachingDevice(String id, String name, String storageLocation) {
        this.id = requireText(id, "Mã thiết bị");
        this.name = requireText(name, "Tên thiết bị");
        this.storageLocation = requireText(storageLocation, "Vị trí lưu trữ");
        this.status = DeviceStatus.Available; // trạng thái ban đầu
    }

    /** Kiểm tra chuỗi không null/rỗng, trả về chuỗi đã cắt khoảng trắng. */
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        return value.trim();
    }

    /** Kiểm tra giá trị phần trăm nằm trong khoảng 0..100. */
    protected static int requirePercent(int value, String fieldName) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException(fieldName + " phải nằm trong khoảng 0..100.");
        }
        return value;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    /** Chỉ lớp con được đổi trạng thái trực tiếp. */
    protected void setStatus(DeviceStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Trạng thái không được null.");
        }
        this.status = status;
    }

    /** Đưa thiết bị vào bảo trì (dùng khi tạo dữ liệu kiểm thử). */
    public void sendToMaintenance() {
        setStatus(DeviceStatus.UnderMaintenance);
    }

    /** Thiết bị có sẵn sàng cấp phát không - mỗi loại có quy tắc riêng. */
    public abstract boolean isReadyForUse();

    /** Điểm ưu tiên mặc định - mỗi loại có cách tính riêng. */
    public abstract double getDefaultPriorityScore();

    /** Mượn thiết bị: chỉ thành công khi isReadyForUse() trả về true. */
    public void borrow() {
        if (!isReadyForUse()) {
            throw new DeviceNotReadyException(id, status);
        }
        status = DeviceStatus.Borrowed;
    }

    /** Trả thiết bị: chỉ thành công khi đang ở trạng thái Borrowed. */
    public void returnDevice() {
        if (status != DeviceStatus.Borrowed) {
            throw new IllegalStateException("Thiết bị " + id + " không ở trạng thái Borrowed, không thể trả.");
        }
        status = DeviceStatus.Available;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%s, name=%s, location=%s, status=%s, score=%.1f]",
                getClass().getSimpleName(), id, name, storageLocation, status, getDefaultPriorityScore());
    }
}

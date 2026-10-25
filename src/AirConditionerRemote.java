/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Điều khiển điều hòa.
 * Có tập phòng tương thích. Sẵn sàng khi Available.
 * Điểm ưu tiên mặc định: 100 nếu có phòng tương thích, ngược lại 0.
 * Khi chọn cho một phòng cụ thể, dùng isCompatibleWith(roomId) làm điều kiện lọc
 * hoặc getPriorityScoreFor(roomId) làm hàm tính điểm.
 */
public class AirConditionerRemote extends TeachingDevice {
    private final Set<String> compatibleRooms;

    public AirConditionerRemote(String id, String name, String storageLocation, Set<String> compatibleRooms) {
        super(id, name, storageLocation);
        if (compatibleRooms == null) {
            throw new IllegalArgumentException("Tập phòng tương thích không được null.");
        }
        // Sao chép phòng thủ + bọc chỉ đọc để bên ngoài không sửa được
        Set<String> copy = new LinkedHashSet<>();
        for (String room : compatibleRooms) {
            copy.add(requireText(room, "Mã phòng"));
        }
        this.compatibleRooms = Collections.unmodifiableSet(copy);
    }

    public Set<String> getCompatibleRooms() {
        return compatibleRooms;
    }

    public boolean isCompatibleWith(String roomId) {
        return roomId != null && compatibleRooms.contains(roomId.trim());
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.Available;
    }

    @Override
    public double getDefaultPriorityScore() {
        return compatibleRooms.isEmpty() ? 0 : 100;
    }

    /** Điểm ưu tiên khi đã biết phòng cần dùng: 100 nếu tương thích, ngược lại 0. */
    public double getPriorityScoreFor(String roomId) {
        return isCompatibleWith(roomId) ? 100 : 0;
    }
}

/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.List;
import java.util.function.Predicate;

/**
 * Dịch vụ cấp phát tổng quát.
 * Chỉ phụ thuộc vào abstraction: Repository<T> và SelectionStrategy<T>.
 * Ràng buộc T extends TeachingDevice cho phép gọi isReadyForUse()/borrow()
 * mà KHÔNG cần if/else theo loại thiết bị -> thêm loại mới không phải sửa lớp này.
 */
public class AllocationService<T extends TeachingDevice> {
    private final Repository<T> repository;
    private SelectionStrategy<T> strategy;

    public AllocationService(Repository<T> repository, SelectionStrategy<T> strategy) {
        if (repository == null || strategy == null) {
            throw new IllegalArgumentException("Repository và strategy không được null.");
        }
        this.repository = repository;
        this.strategy = strategy;
    }

    /** Đổi chiến lược lựa chọn trong thời gian chạy. */
    public void changeStrategy(SelectionStrategy<T> newStrategy) {
        if (newStrategy == null) {
            throw new IllegalArgumentException("Chiến lược mới không được null.");
        }
        this.strategy = newStrategy;
    }

    /**
     * Cấp phát một thiết bị.
     * @param additionalCondition điều kiện bổ sung, có thể null (bỏ qua)
     * @return thiết bị đã chuyển sang Borrowed
     * @throws NoSuitableItemException nếu không có ứng viên phù hợp
     */
    public T allocate(Predicate<T> additionalCondition) {
        // B1-B3: lấy từ repository, giữ thiết bị sẵn sàng, áp dụng điều kiện bổ sung
        List<T> candidates = repository.findAll(device ->
                device.isReadyForUse()
                        && (additionalCondition == null || additionalCondition.test(device)));
        // B4: chọn bằng chiến lược hiện tại (đa hình)
        T selected = strategy.select(candidates);
        // B5: mượn thiết bị
        selected.borrow();
        // B6: trả về thiết bị đã cấp phát
        return selected;
    }

    /** Trả thiết bị về kho theo mã. */
    public T returnDevice(String id) {
        T device = repository.findById(id);
        if (device == null) {
            throw new NoSuitableItemException("Không tìm thấy thiết bị có mã " + id + ".");
        }
        device.returnDevice();
        return device;
    }
}

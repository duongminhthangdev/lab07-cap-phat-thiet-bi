/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.List;

/**
 * Chọn phần tử đầu tiên trong danh sách ứng viên.
 */
public class FirstAvailableStrategy<T> implements SelectionStrategy<T> {
    @Override
    public T select(List<T> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new NoSuitableItemException("Không có ứng viên đầu tiên để chọn.");
        }
        return candidates.get(0);
    }
}

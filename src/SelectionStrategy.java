/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.List;

/**
 * Chiến lược lựa chọn một phần tử từ danh sách ứng viên (ISelectionStrategy<T> trong đề).
 * Đa hình: mỗi cài đặt là một thuật toán chọn khác nhau, có thể đổi lúc chạy.
 */
public interface SelectionStrategy<T> {
    /**
     * Chọn một phần tử. Không được sửa danh sách đầu vào.
     * @throws NoSuitableItemException nếu danh sách rỗng
     */
    T select(List<T> candidates);
}

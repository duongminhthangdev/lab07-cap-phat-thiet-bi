/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.List;
import java.util.function.Predicate;

/**
 * Kho lưu trữ tổng quát (tương ứng IRepository<T> trong đề).
 * Ràng buộc T extends Identifiable bảo đảm mọi phần tử đều có mã,
 * nhờ đó findById/remove/kiểm tra trùng mã làm việc được với mọi kiểu.
 */
public interface Repository<T extends Identifiable> {
    void add(T item);

    /** Trả về false nếu không tìm thấy mã. */
    boolean remove(String id);

    /** Trả về null nếu không tìm thấy. */
    T findById(String id);

    /** Danh sách chỉ đọc. */
    List<T> getAll();

    /** Lọc theo điều kiện bất kỳ, không phụ thuộc loại thiết bị cụ thể. */
    List<T> findAll(Predicate<T> condition);
}

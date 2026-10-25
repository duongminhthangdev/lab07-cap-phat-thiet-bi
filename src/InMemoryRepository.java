/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Cài đặt Repository trong bộ nhớ bằng LinkedHashMap<String, T>:
 * - Tìm/xóa/kiểm tra trùng theo mã O(1).
 * - Giữ thứ tự thêm vào (cần cho FirstAvailableStrategy).
 */
public class InMemoryRepository<T extends Identifiable> implements Repository<T> {
    private final Map<String, T> items = new LinkedHashMap<>();

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Không chấp nhận phần tử null.");
        }
        if (items.containsKey(item.getId())) {
            throw new DuplicateIdException(item.getId());
        }
        items.put(item.getId(), item);
    }

    @Override
    public boolean remove(String id) {
        return id != null && items.remove(id) != null;
    }

    @Override
    public T findById(String id) {
        return id == null ? null : items.get(id);
    }

    @Override
    public List<T> getAll() {
        // Trả bản sao chỉ đọc để bên ngoài không sửa được collection nội bộ
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    @Override
    public List<T> findAll(Predicate<T> condition) {
        if (condition == null) {
            throw new IllegalArgumentException("Điều kiện lọc không được null.");
        }
        List<T> result = new ArrayList<>();
        for (T item : items.values()) {
            if (condition.test(item)) {
                result.add(item);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public int size() {
        return items.size();
    }
}

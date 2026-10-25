/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Chọn phần tử có điểm cao nhất theo hàm tính điểm truyền từ bên ngoài.
 * Nếu bằng điểm, giữ phần tử xuất hiện trước.
 */
public class HighestPriorityStrategy<T> implements SelectionStrategy<T> {
    private final ToDoubleFunction<T> scoreSelector;

    public HighestPriorityStrategy(ToDoubleFunction<T> scoreSelector) {
        if (scoreSelector == null) {
            throw new IllegalArgumentException("Hàm tính điểm không được null.");
        }
        this.scoreSelector = scoreSelector;
    }

    @Override
    public T select(List<T> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            throw new NoSuitableItemException("Không có ứng viên để chọn theo điểm ưu tiên.");
        }
        // Duyệt tuyến tính, chỉ đọc danh sách -> không thay đổi đầu vào
        T best = candidates.get(0);
        double bestScore = scoreSelector.applyAsDouble(best);
        for (int i = 1; i < candidates.size(); i++) {
            T current = candidates.get(i);
            double score = scoreSelector.applyAsDouble(current);
            if (score > bestScore) {
                best = current;
                bestScore = score;
            }
        }
        return best;
    }
}

/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/** Ném ra khi không có ứng viên phù hợp để chọn/cấp phát. */
public class NoSuitableItemException extends RuntimeException {
    public NoSuitableItemException(String message) {
        super(message);
    }
}

/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/**
 * Interface nhận diện đối tượng theo mã.
 * Cho phép các cấu trúc tổng quát (repository) tìm đối tượng theo mã
 * mà không phụ thuộc vào lớp cụ thể.
 */
public interface Identifiable {
    String getId();
}

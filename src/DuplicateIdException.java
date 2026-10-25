/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

/** Ném ra khi thêm phần tử có mã đã tồn tại trong kho. */
public class DuplicateIdException extends RuntimeException {
    private final String duplicateId;

    public DuplicateIdException(String duplicateId) {
        super("Mã " + duplicateId + " đã tồn tại.");
        this.duplicateId = duplicateId;
    }

    public String getDuplicateId() {
        return duplicateId;
    }
}

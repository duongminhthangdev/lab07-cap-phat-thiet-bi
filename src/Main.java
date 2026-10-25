/****************
 * Mã sinh viên: 20227067
 * Họ tên: Dương Minh Thắng
 ****************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Chương trình kiểm thử các yêu cầu của Lab W07.
 * Mỗi kịch bản in ra [PASS]/[FAIL]; cuối cùng in tổng kết.
 */
public class Main {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        // ===== Dữ liệu kiểm thử =====
        // 3 điều khiển máy chiếu với mức pin khác nhau (PR03 pin thấp -> không sẵn sàng)
        ProjectorRemote pr01 = new ProjectorRemote("PR01", "ĐK máy chiếu Epson", "Tủ A - Tầng 2", 45);
        ProjectorRemote pr02 = new ProjectorRemote("PR02", "ĐK máy chiếu Sony", "Tủ A - Tầng 2", 90);
        ProjectorRemote pr03 = new ProjectorRemote("PR03", "ĐK máy chiếu Panasonic", "Tủ B - Tầng 3", 15);

        // 2 điều khiển điều hòa với tập phòng tương thích khác nhau
        AirConditionerRemote ac01 = new AirConditionerRemote("AC01", "ĐK điều hòa Daikin", "Phòng thiết bị A",
                new HashSet<>(Arrays.asList("A201", "A202", "A203")));
        AirConditionerRemote ac02 = new AirConditionerRemote("AC02", "ĐK điều hòa Panasonic", "Phòng thiết bị B",
                new HashSet<>(Arrays.asList("B101", "B102")));

        // 3 micro với pin và tín hiệu khác nhau (MIC02 pin 25% -> không sẵn sàng)
        WirelessMicrophone mic01 = new WirelessMicrophone("MIC01", "Micro Shure", "Tủ C", 80, 70);   // 75.0
        WirelessMicrophone mic02 = new WirelessMicrophone("MIC02", "Micro Sennheiser", "Tủ C", 25, 90); // không sẵn sàng
        WirelessMicrophone mic03 = new WirelessMicrophone("MIC03", "Micro Rode", "Tủ D", 60, 95);    // 77.5

        // Cùng MỘT lớp generic InMemoryRepository dùng cho ba loại thiết bị
        InMemoryRepository<ProjectorRemote> projectorRepo = new InMemoryRepository<>();
        InMemoryRepository<AirConditionerRemote> acRepo = new InMemoryRepository<>();
        InMemoryRepository<WirelessMicrophone> micRepo = new InMemoryRepository<>();

        // ===== TC1: Thêm thiết bị vào repository =====
        section("TC1. Thêm thiết bị vào repository");
        projectorRepo.add(pr01);
        projectorRepo.add(pr02);
        projectorRepo.add(pr03);
        acRepo.add(ac01);
        acRepo.add(ac02);
        micRepo.add(mic01);
        micRepo.add(mic02);
        micRepo.add(mic03);
        printList("Máy chiếu", projectorRepo.getAll());
        printList("Điều hòa", acRepo.getAll());
        printList("Micro", micRepo.getAll());
        check("Kho máy chiếu có 3 phần tử", projectorRepo.size() == 3);
        check("Kho điều hòa có 2 phần tử", acRepo.size() == 2);
        check("Kho micro có 3 phần tử", micRepo.size() == 3);
        check("Thêm null bị từ chối (IllegalArgumentException)",
                throwsType(() -> projectorRepo.add(null), IllegalArgumentException.class));

        // ===== TC2: Thêm hai thiết bị trùng mã =====
        section("TC2. Thử thêm thiết bị trùng mã");
        try {
            projectorRepo.add(new ProjectorRemote("PR01", "Bản sao", "Tủ X", 50));
            check("Phải ném DuplicateIdException", false);
        } catch (DuplicateIdException ex) {
            System.out.println("  Bắt được: " + ex.getMessage() + " (duplicateId=" + ex.getDuplicateId() + ")");
            check("Ném DuplicateIdException với duplicateId = PR01", "PR01".equals(ex.getDuplicateId()));
        }
        check("Số phần tử không đổi sau khi thêm trùng", projectorRepo.size() == 3);

        // ===== TC3: Tìm thiết bị theo mã =====
        section("TC3. Tìm thiết bị theo mã");
        AirConditionerRemote foundAc = acRepo.findById("AC02");
        System.out.println("  findById(\"AC02\") -> " + foundAc);
        check("Tìm thấy AC02", foundAc == ac02);
        check("Không tìm thấy mã XYZ -> null", micRepo.findById("XYZ") == null);
        check("remove mã không tồn tại -> false", !micRepo.remove("XYZ"));

        // ===== TC4: Lọc các thiết bị sẵn sàng =====
        section("TC4. Lọc các thiết bị sẵn sàng");
        List<ProjectorRemote> readyProjectors = projectorRepo.findAll(TeachingDevice::isReadyForUse);
        List<WirelessMicrophone> readyMics = micRepo.findAll(TeachingDevice::isReadyForUse);
        printList("Máy chiếu sẵn sàng", readyProjectors);
        printList("Micro sẵn sàng", readyMics);
        check("Máy chiếu sẵn sàng = [PR01, PR02] (PR03 pin 15% < 20%)",
                ids(readyProjectors).equals(Arrays.asList("PR01", "PR02")));
        check("Micro sẵn sàng = [MIC01, MIC03] (MIC02 pin 25% < 30%)",
                ids(readyMics).equals(Arrays.asList("MIC01", "MIC03")));
        check("getAll() trả danh sách chỉ đọc",
                throwsType(() -> projectorRepo.getAll().clear(), UnsupportedOperationException.class));

        // ===== TC5: Chọn điều khiển máy chiếu có điểm cao nhất =====
        section("TC5. Chọn điều khiển máy chiếu có điểm cao nhất");
        HighestPriorityStrategy<ProjectorRemote> projectorHighest =
                new HighestPriorityStrategy<>(TeachingDevice::getDefaultPriorityScore);
        AllocationService<ProjectorRemote> projectorService =
                new AllocationService<>(projectorRepo, projectorHighest);
        ProjectorRemote selectedProjector = projectorService.allocate(device -> true);
        System.out.println("  Được cấp: " + selectedProjector);
        check("Chọn PR02 (pin 90%)", selectedProjector == pr02);

        // ===== TC6: Chọn điều khiển điều hòa tương thích với phòng =====
        section("TC6. Chọn điều khiển điều hòa tương thích với phòng A203");
        AllocationService<AirConditionerRemote> acService = new AllocationService<>(acRepo,
                new HighestPriorityStrategy<>(ac -> ac.getPriorityScoreFor("A203")));
        AirConditionerRemote selectedAc = acService.allocate(device -> device.isCompatibleWith("A203"));
        System.out.println("  Được cấp: " + selectedAc);
        check("Chọn AC01 (tương thích A203)", selectedAc == ac01);

        // ===== TC7: Chọn micro đầu tiên sẵn sàng =====
        section("TC7. Chọn micro đầu tiên sẵn sàng (FirstAvailableStrategy)");
        AllocationService<WirelessMicrophone> micService =
                new AllocationService<>(micRepo, new FirstAvailableStrategy<>());
        WirelessMicrophone firstMic = micService.allocate(null);
        System.out.println("  Được cấp: " + firstMic);
        check("Chọn MIC01 (micro sẵn sàng đầu tiên)", firstMic == mic01);

        // ===== TC8: Đổi chiến lược trong thời gian chạy =====
        section("TC8. Đổi chiến lược trong thời gian chạy");
        micService.returnDevice("MIC01"); // trả lại để so sánh hai chiến lược trên cùng dữ liệu
        System.out.println("  Đã trả MIC01, trạng thái: " + mic01.getStatus());
        micService.changeStrategy(new HighestPriorityStrategy<>(TeachingDevice::getDefaultPriorityScore));
        WirelessMicrophone bestMic = micService.allocate(null);
        System.out.println("  Sau khi đổi sang HighestPriority, được cấp: " + bestMic);
        check("Cùng dữ liệu: First -> MIC01, Highest -> MIC03 (77.5 > 75.0)", bestMic == mic03);

        projectorService.changeStrategy(new FirstAvailableStrategy<>());
        ProjectorRemote firstProjector = projectorService.allocate(null);
        System.out.println("  Máy chiếu sau khi đổi sang FirstAvailable, được cấp: " + firstProjector);
        check("Máy chiếu: FirstAvailable chọn PR01", firstProjector == pr01);

        // ===== TC9: Cấp phát khi không có ứng viên phù hợp =====
        section("TC9. Cấp phát khi không có ứng viên phù hợp");
        try {
            projectorService.allocate(null); // PR01, PR02 đã mượn; PR03 pin thấp
            check("Phải ném NoSuitableItemException (máy chiếu)", false);
        } catch (NoSuitableItemException ex) {
            System.out.println("  Bắt được: " + ex.getMessage());
            check("Máy chiếu hết ứng viên -> NoSuitableItemException", true);
        }
        check("Điều hòa cho phòng C999 -> NoSuitableItemException",
                throwsType(() -> acService.allocate(d -> d.isCompatibleWith("C999")), NoSuitableItemException.class));
        check("Mượn trực tiếp PR03 (pin thấp) -> DeviceNotReadyException",
                throwsType(pr03::borrow, DeviceNotReadyException.class));
        check("Trả thiết bị chưa mượn (MIC02) -> IllegalStateException",
                throwsType(mic02::returnDevice, IllegalStateException.class));

        // ===== TC10: Thiết bị được chọn chuyển sang Borrowed =====
        section("TC10. Thiết bị được chọn chuyển sang Borrowed");
        System.out.println("  PR02: " + pr02.getStatus() + ", AC01: " + ac01.getStatus()
                + ", MIC03: " + mic03.getStatus() + ", AC02: " + ac02.getStatus());
        check("PR02 Borrowed", pr02.getStatus() == DeviceStatus.Borrowed);
        check("AC01 Borrowed", ac01.getStatus() == DeviceStatus.Borrowed);
        check("MIC03 Borrowed", mic03.getStatus() == DeviceStatus.Borrowed);
        check("AC02 (không được chọn) vẫn Available", ac02.getStatus() == DeviceStatus.Available);
        acService.returnDevice("AC01");
        check("Sau khi trả, AC01 về Available", ac01.getStatus() == DeviceStatus.Available);

        // ===== TC11: Repository tổng quát dùng cho nhiều loại thiết bị =====
        section("TC11. Repository/Service tổng quát dùng cho nhiều loại thiết bị");
        check("Cùng lớp InMemoryRepository cho ProjectorRemote/AirConditionerRemote/WirelessMicrophone",
                projectorRepo.getClass() == acRepo.getClass() && acRepo.getClass() == micRepo.getClass());

        // Kho chung kiểu cơ sở: chứa lẫn nhiều loại, lọc/chọn nhờ đa hình
        InMemoryRepository<TeachingDevice> allRepo = new InMemoryRepository<>();
        for (TeachingDevice d : Arrays.asList(pr01, pr02, pr03, ac01, ac02, mic01, mic02, mic03)) {
            allRepo.add(d);
        }
        printList("Tất cả thiết bị sẵn sàng", allRepo.findAll(TeachingDevice::isReadyForUse));
        check("Kho chung chứa 8 thiết bị thuộc 3 loại", allRepo.size() == 8);

        // Bổ sung loại mới InteractiveDisplayRemote: KHÔNG sửa AllocationService/Repository
        InMemoryRepository<InteractiveDisplayRemote> displayRepo = new InMemoryRepository<>();
        displayRepo.add(new InteractiveDisplayRemote("ID01", "ĐK màn hình Samsung Flip", "Tủ E", 40));
        displayRepo.add(new InteractiveDisplayRemote("ID02", "ĐK màn hình LG CreateBoard", "Tủ E", 70));
        AllocationService<InteractiveDisplayRemote> displayService = new AllocationService<>(displayRepo,
                new HighestPriorityStrategy<>(TeachingDevice::getDefaultPriorityScore));
        InteractiveDisplayRemote display = displayService.allocate(null);
        System.out.println("  Loại mới được cấp: " + display);
        check("Loại mới InteractiveDisplayRemote cấp phát được ID02", "ID02".equals(display.getId()));

        // Chiến lược không thay đổi danh sách đầu vào
        List<WirelessMicrophone> input = new ArrayList<>(micRepo.getAll());
        List<WirelessMicrophone> snapshot = new ArrayList<>(input);
        new HighestPriorityStrategy<WirelessMicrophone>(TeachingDevice::getDefaultPriorityScore).select(input);
        new FirstAvailableStrategy<WirelessMicrophone>().select(input);
        check("Chiến lược không thay đổi danh sách đầu vào", input.equals(snapshot));
        check("Danh sách rỗng -> NoSuitableItemException",
                throwsType(() -> new FirstAvailableStrategy<String>().select(new ArrayList<>()),
                        NoSuitableItemException.class));

        // ===== Tổng kết =====
        System.out.println();
        System.out.println("==================================================");
        System.out.printf("KẾT QUẢ: %d PASS, %d FAIL%n", passed, failed);
        System.out.println("==================================================");
    }

    // ---------- Các hàm tiện ích kiểm thử ----------

    private static void section(String title) {
        System.out.println();
        System.out.println("---- " + title + " ----");
    }

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + description);
        } else {
            failed++;
            System.out.println("  [FAIL] " + description);
        }
    }

    /** Trả về true nếu action ném đúng loại ngoại lệ mong đợi. */
    private static boolean throwsType(Runnable action, Class<? extends Throwable> expected) {
        try {
            action.run();
            return false;
        } catch (Throwable t) {
            return expected.isInstance(t);
        }
    }

    private static List<String> ids(List<? extends Identifiable> items) {
        List<String> result = new ArrayList<>();
        for (Identifiable item : items) {
            result.add(item.getId());
        }
        return result;
    }

    private static void printList(String label, List<? extends TeachingDevice> devices) {
        System.out.println("  " + label + ":");
        for (TeachingDevice d : devices) {
            System.out.println("    - " + d);
        }
    }
}

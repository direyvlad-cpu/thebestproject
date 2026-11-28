package Project.test;

public final class TestSupport {
    private static int passed;
    private static int failed;

    private TestSupport() { }

    public static void test(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (Throwable e) {
            failed++;
            System.out.println("[FAIL] " + name + " — " + e.getMessage());
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + ". Ожидалось: " + expected + ", получено: " + actual);
        }
    }

    public static void assertDoubleEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(message + ". Ожидалось: " + expected + ", получено: " + actual);
        }
    }

    public static void assertThrows(Class<? extends Throwable> expectedType, Runnable action, String message) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expectedType.isInstance(actual)) return;
            throw new AssertionError(message + ". Ожидалось исключение " + expectedType.getSimpleName()
                    + ", получено " + actual.getClass().getSimpleName(), actual);
        }
        throw new AssertionError(message + ". Исключение " + expectedType.getSimpleName() + " не возникло.");
    }

    public static void printSummary() {
        System.out.println("\nИТОГ: успешно — " + passed + ", ошибок — " + failed
                + ", всего — " + (passed + failed));
        if (failed > 0) throw new AssertionError("Есть неуспешные тесты: " + failed);
    }
}

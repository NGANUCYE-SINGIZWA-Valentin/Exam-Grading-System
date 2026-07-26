import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GradingServiceTest {

    private GradingService service;

    @BeforeClass
    public static void beforeAllTests() {
        System.out.println("Starting GradingService tests");
    }

    @Before
    public void setUp() {
        service = new GradingService();
    }

    @After
    public void afterEachTest() {
        service = null;
    }

    @AfterClass
    public static void afterAllTests() {
        System.out.println("GradingService tests finished");
    }

    // --- Boundary tests: F partition ---

    @Test
    public void grade_shouldReturnF_whenMarkIsZero() {
        assertEquals("F", service.grade(0));
    }

    @Test
    public void grade_shouldReturnF_whenMarkIsFiftyNine() {
        assertEquals("F", service.grade(59));
    }

    // --- Boundary tests: D partition ---

    @Test
    public void grade_shouldReturnD_whenMarkIsSixty() {
        assertEquals("D", service.grade(60));
    }

    @Test
    public void grade_shouldReturnD_whenMarkIsSixtyNine() {
        assertEquals("D", service.grade(69));
    }

    // --- Boundary tests: C partition ---

    @Test
    public void grade_shouldReturnC_whenMarkIsSeventy() {
        assertEquals("C", service.grade(70));
    }

    @Test
    public void grade_shouldReturnC_whenMarkIsSeventyNine() {
        assertEquals("C", service.grade(79));
    }

    // --- Boundary tests: B partition ---

    @Test
    public void grade_shouldReturnB_whenMarkIsEighty() {
        assertEquals("B", service.grade(80));
    }

    @Test
    public void grade_shouldReturnB_whenMarkIsEightyNine() {
        assertEquals("B", service.grade(89));
    }

    // --- Boundary tests: A partition ---

    @Test
    public void grade_shouldReturnA_whenMarkIsNinety() {
        assertEquals("A", service.grade(90));
    }

    @Test
    public void grade_shouldReturnA_whenMarkIsOneHundred() {
        assertEquals("A", service.grade(100));
    }

    // --- Boundary tests: invalid inputs ---

    @Test(expected = IllegalArgumentException.class)
    public void grade_shouldThrowException_whenMarkIsMinusOne() {
        service.grade(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void grade_shouldThrowException_whenMarkIsOneHundredOne() {
        service.grade(101);
    }

    // --- gradeAll ---

    @Test
    public void gradeAll_shouldReturnGradesInTheSameOrder() {
        // assertArrayEquals compares element by element.
        // assertEquals would compare array references and fail even with identical contents.
        assertArrayEquals(new String[]{"A", "F", "C"}, service.gradeAll(new int[]{95, 40, 71}));
    }

    // --- classAverage ---

    @Test
    public void classAverage_shouldReturnCorrectAverage() {
        // delta 0.001 tolerates harmless floating-point rounding differences
        assertEquals(75.0, service.classAverage(new int[]{60, 70, 80, 90}), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void classAverage_shouldThrowException_whenMarksAreEmpty() {
        service.classAverage(new int[]{});
    }
}

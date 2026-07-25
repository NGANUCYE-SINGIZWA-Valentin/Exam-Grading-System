package service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.Mark;
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

    @Test
    public void grade_shouldReturnF_whenMarkIsZero() {
        assertEquals("F", service.grade(0));
    }

    @Test
    public void grade_shouldReturnF_whenMarkIsFiftyNine() {
        assertEquals("F", service.grade(59));
    }

    @Test
    public void grade_shouldReturnD_whenMarkIsSixty() {
        assertEquals("D", service.grade(60));
    }

    @Test
    public void grade_shouldReturnD_whenMarkIsSixtyNine() {
        assertEquals("D", service.grade(69));
    }

    @Test
    public void grade_shouldReturnC_whenMarkIsSeventy() {
        assertEquals("C", service.grade(70));
    }

    @Test
    public void grade_shouldReturnC_whenMarkIsSeventyNine() {
        assertEquals("C", service.grade(79));
    }

    @Test
    public void grade_shouldReturnB_whenMarkIsEighty() {
        assertEquals("B", service.grade(80));
    }

    @Test
    public void grade_shouldReturnB_whenMarkIsEightyNine() {
        assertEquals("B", service.grade(89));
    }

    @Test
    public void grade_shouldReturnA_whenMarkIsNinety() {
        assertEquals("A", service.grade(90));
    }

    @Test
    public void grade_shouldReturnA_whenMarkIsOneHundred() {
        assertEquals("A", service.grade(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void grade_shouldThrowException_whenMarkIsMinusOne() {
        service.grade(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void grade_shouldThrowException_whenMarkIsOneHundredOne() {
        service.grade(101);
    }

    @Test
    public void gradeAll_shouldReturnGradesInTheSameOrder() {
        String[] expected = {"A", "F", "C"};
        String[] actual = service.gradeAll(new int[]{95, 40, 71});

        // assertEquals compares array references, not the values inside the arrays.
        assertArrayEquals(expected, actual);
    }

    @Test
    public void classAverage_shouldReturnAverageForValidMarks() {
        // The delta allows a very small difference caused by decimal representation.
        assertEquals(75.0, service.classAverage(new int[]{60, 70, 80, 90}), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void classAverage_shouldThrowException_whenMarksAreEmpty() {
        service.classAverage(new int[]{});
    }

    @Test
    public void gradeFromMark_shouldReturnCorrectGrade_forDomainObject() {
        assertEquals("A", service.gradeFromMark(new Mark(95)));
    }
}

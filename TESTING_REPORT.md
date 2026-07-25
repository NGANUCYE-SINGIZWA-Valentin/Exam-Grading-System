# Software Testing Practical Assignment

Student Name: Nganucye Singizwa Valentin
Student ID: 27201
Course: Software Testing
Project: Exam Grading System
Semester: Summer Semester , 2025–2026
Date:25/July/2026

---

# 1. Introduction

This report describes the testing activities carried out for the Exam Grading System project using JUnit 4 and Maven. The objective was to implement the required business rules for converting raw marks into letter grades and verify that they work correctly using unit tests, lifecycle annotations, and JUnit assertions.

The system accepts integer marks from 0 to 100, converts them to letter grades (A, B, C, D, F), and rejects any mark outside that range with an `IllegalArgumentException`. It also supports batch grading via `gradeAll` and class average calculation via `classAverage`.

---

# 2. JUnit Lifecycle Annotations Used

## @BeforeClass

Used once before all tests to print a start message to the console. It must be `static` because JUnit 4 calls it before creating any instance of the test class — there is no object to call it on yet.

## @Before

Used to create a fresh `GradingService` instance before every test. This ensures each test starts with a clean, predictable state and no test can be affected by side effects from a previous one.

## @Test

Marks a method as a test case. Used on all 16 test methods. The `expected` attribute (e.g. `@Test(expected = IllegalArgumentException.class)`) is used on exception tests to assert that the correct exception is thrown.

## @After

Used to set the `service` reference to `null` after every test, releasing the object and making it clear that no state is carried between tests.

## @AfterClass

Used once after all tests have completed to print a finish message to the console. Also `static` for the same reason as `@BeforeClass`.

---

# 3. Equivalence Partitions

| Partition | Input range | Expected result |
|---|---|---|
| Grade F | 0 – 59 | `"F"` |
| Grade D | 60 – 69 | `"D"` |
| Grade C | 70 – 79 | `"C"` |
| Grade B | 80 – 89 | `"B"` |
| Grade A | 90 – 100 | `"A"` |
| Invalid (low) | Below 0 | `IllegalArgumentException` |
| Invalid (high) | Above 100 | `IllegalArgumentException` |

---

# 4. Boundary Values

| Mark | Expected result |
|---:|---|
| -1 | `IllegalArgumentException` |
| 0 | `"F"` |
| 59 | `"F"` |
| 60 | `"D"` |
| 69 | `"D"` |
| 70 | `"C"` |
| 79 | `"C"` |
| 80 | `"B"` |
| 89 | `"B"` |
| 90 | `"A"` |
| 100 | `"A"` |
| 101 | `IllegalArgumentException` |

Each boundary has its own dedicated test method. Boundary bugs (`>=` vs `>`) are the most common bugs in grading systems and only boundary tests catch them.

---

# 5. Test Cases Implemented

| Test Method | Purpose | Expected Result |
|---|---|---|
| `grade_shouldReturnF_whenMarkIsZero` | Boundary: lowest valid mark | `"F"` |
| `grade_shouldReturnF_whenMarkIsFiftyNine` | Boundary: top of F partition | `"F"` |
| `grade_shouldReturnD_whenMarkIsSixty` | Boundary: bottom of D partition | `"D"` |
| `grade_shouldReturnD_whenMarkIsSixtyNine` | Boundary: top of D partition | `"D"` |
| `grade_shouldReturnC_whenMarkIsSeventy` | Boundary: bottom of C partition | `"C"` |
| `grade_shouldReturnC_whenMarkIsSeventyNine` | Boundary: top of C partition | `"C"` |
| `grade_shouldReturnB_whenMarkIsEighty` | Boundary: bottom of B partition | `"B"` |
| `grade_shouldReturnB_whenMarkIsEightyNine` | Boundary: top of B partition | `"B"` |
| `grade_shouldReturnA_whenMarkIsNinety` | Boundary: bottom of A partition | `"A"` |
| `grade_shouldReturnA_whenMarkIsOneHundred` | Boundary: highest valid mark | `"A"` |
| `grade_shouldThrowException_whenMarkIsMinusOne` | Invalid: one below minimum | `IllegalArgumentException` |
| `grade_shouldThrowException_whenMarkIsOneHundredOne` | Invalid: one above maximum | `IllegalArgumentException` |
| `gradeAll_shouldReturnGradesInTheSameOrder` | Batch grading preserves order | `{"A", "F", "C"}` |
| `classAverage_shouldReturnAverageForValidMarks` | Average of four marks | `75.0` |
| `classAverage_shouldThrowException_whenMarksAreEmpty` | Empty array rejected | `IllegalArgumentException` |
| `gradeFromMark_shouldReturnCorrectGrade_forDomainObject` | Domain `Mark` object accepted | `"A"` |

---

# 6. Assertions Used

- `assertEquals(String, String)` – Compared the expected letter grade string to the actual result returned by `grade()`.
- `assertEquals(double, double, delta)` – Compared the expected average to the actual result of `classAverage()`. The delta `0.001` accounts for harmless floating-point rounding differences.
- `assertArrayEquals(String[], String[])` – Compared the full array of grades returned by `gradeAll()` element by element.
- `@Test(expected = ...)` – Asserted that `IllegalArgumentException` is thrown for invalid marks and empty arrays.

---

# 7. Answers to Written Questions

**Why use `assertArrayEquals` instead of `assertEquals` for arrays?**

`assertEquals` on two arrays compares their object references, not their contents. Two separate arrays with identical values are different objects, so `assertEquals` would fail even when the grades are correct. `assertArrayEquals` compares each element at the same index, which is the correct tool for verifying array output.

**Why use the delta form of `assertEquals` for `classAverage`?**

A `double` value can have tiny rounding differences depending on the order of arithmetic operations. Using `assertEquals(75.0, result, 0.001)` accepts any result within 0.001 of the expected value, which is far smaller than one mark and therefore only allows harmless precision differences, not real bugs.

**Why must `@BeforeClass` and `@AfterClass` be static in JUnit 4?**

JUnit 4 creates a new instance of the test class for every `@Test` method. `@BeforeClass` and `@AfterClass` must run once for the entire class, before or after any instance is created. Since no instance exists at that point, the method must be `static` so JUnit can call it on the class itself.

**When would you use `@Before` instead of `@BeforeClass`?**

Use `@Before` when each test needs its own independent copy of an object — for example, a fresh `GradingService` so one test cannot affect another. Use `@BeforeClass` for expensive setup that is safe to share, such as opening a database connection once for all tests.

**Does `@After` still run if a test fails?**

Yes. JUnit 4 guarantees that `@After` runs after every test regardless of whether it passed, failed, or threw an exception. This makes it reliable for cleanup such as closing resources or nulling references.

---

# 8. Mutation Testing Experiment

To verify that the boundary tests can actually detect bugs, the condition `mark >= 90` in `GradingService.grade()` was deliberately changed to `mark > 90` and `mvn test` was run.

**Result with the mutation active:**

```
Tests run: 16, Failures: 1, Errors: 0, Skipped: 0

FAILED: grade_shouldReturnA_whenMarkIsNinety
expected:<A> but was:<B>
```

The boundary test for mark `90` caught the bug immediately. No other test failed because only the exact boundary value `90` is affected by `>` vs `>=`. After restoring `>=`, all 16 tests passed.

This technique is called **mutation testing**: a fault is deliberately injected into the source code to confirm that the test suite is sensitive enough to detect it. A test that never fails — even when the code is broken — proves nothing.

---

# 9. Testing Results

All tests were executed using:

```
mvn test
```

```
[INFO] Running service.GradingServiceTest
Starting GradingService tests
GradingService tests finished
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.103 s
[INFO] BUILD SUCCESS
```

All 16 tests passed. A failing test was intentionally produced during the mutation experiment (see Section 8) to confirm that the test suite can detect real bugs.

---

# 10. Conclusion

The Exam Grading System correctly implements all five grade rules and both invalid-input rules. The test suite covers all seven equivalence partitions, all twelve boundary values, batch grading, class average calculation, and domain object integration. All five JUnit lifecycle annotations were used with the correct scope and purpose. The mutation experiment confirmed that the boundary tests are sensitive enough to catch the most common grading bug — an off-by-one error in a boundary condition.

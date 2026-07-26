# Software Testing Practical Assignment

**Student Name:** Nganucye Singizwa Valentin
**Student ID:** 27201
**Course:** Software Testing
**Project:** Exam Grading System
**Semester:** Semester Two, 2025–2026
**Date:** 25 July 2026

---

# 1. Introduction

This report is about the testing I did for the Exam Grading System project. I used JUnit 4 and Maven. The goal was to write a service that gives letter grades based on marks, and then write tests to check that it works correctly.

The system takes a mark between 0 and 100 and returns a grade like A, B, C, D, or F. If the mark is less than 0 or more than 100, the system throws an error. It can also grade many marks at once using `gradeAll`, and calculate the class average using `classAverage`.

---

# 2. JUnit Lifecycle Annotations Used

## @BeforeClass

I used this to print a message before all tests start. It only runs once. It has to be `static` because JUnit runs it before creating any test object.

## @Before

I used this to create a new `GradingService` before each test. This way every test starts fresh and does not use results from the previous test.

## @Test

This marks a method as a test. I used it on all 15 tests. When I add `expected = IllegalArgumentException.class` inside it, JUnit checks that the method throws that error.

## @After

I used this to set `service` to null after each test. This cleans up after every test.

## @AfterClass

I used this to print a message after all tests finish. It also has to be `static` for the same reason as `@BeforeClass`.

---

# 3. Equivalence Partitions

| Partition | Input range | Expected result |
|---|---|---|
| Grade F | 0 – 59 | `"F"` |
| Grade D | 60 – 69 | `"D"` |
| Grade C | 70 – 79 | `"C"` |
| Grade B | 80 – 89 | `"B"` |
| Grade A | 90 – 100 | `"A"` |
| Invalid (too low) | Below 0 | Error |
| Invalid (too high) | Above 100 | Error |

---

# 4. Boundary Values

| Input | Expected |
|---:|---|
| -1 | Error |
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
| 101 | Error |

I wrote a separate test for each of these values because mistakes usually happen at the edges, for example using `>` instead of `>=`.

---

# 5. Test Cases Implemented

| Test Method | Purpose | Expected Result |
|---|---|---|
| `grade_shouldReturnF_whenMarkIsZero` | Check mark 0 gives F | `"F"` |
| `grade_shouldReturnF_whenMarkIsFiftyNine` | Check mark 59 gives F | `"F"` |
| `grade_shouldReturnD_whenMarkIsSixty` | Check mark 60 gives D | `"D"` |
| `grade_shouldReturnD_whenMarkIsSixtyNine` | Check mark 69 gives D | `"D"` |
| `grade_shouldReturnC_whenMarkIsSeventy` | Check mark 70 gives C | `"C"` |
| `grade_shouldReturnC_whenMarkIsSeventyNine` | Check mark 79 gives C | `"C"` |
| `grade_shouldReturnB_whenMarkIsEighty` | Check mark 80 gives B | `"B"` |
| `grade_shouldReturnB_whenMarkIsEightyNine` | Check mark 89 gives B | `"B"` |
| `grade_shouldReturnA_whenMarkIsNinety` | Check mark 90 gives A | `"A"` |
| `grade_shouldReturnA_whenMarkIsOneHundred` | Check mark 100 gives A | `"A"` |
| `grade_shouldThrowException_whenMarkIsMinusOne` | Check -1 gives error | Error |
| `grade_shouldThrowException_whenMarkIsOneHundredOne` | Check 101 gives error | Error |
| `gradeAll_shouldReturnGradesInTheSameOrder` | Check gradeAll returns grades in order | `{"A", "F", "C"}` |
| `classAverage_shouldReturnCorrectAverage` | Check average of 4 marks | `75.0` |
| `classAverage_shouldThrowException_whenMarksAreEmpty` | Check empty array gives error | Error |

---

# 6. Assertions Used

- `assertEquals(String, String)` – I used this to check that the grade returned matches what I expected.
- `assertEquals(double, double, delta)` – I used this for `classAverage` because double numbers can have very small differences. The delta `0.001` means the result just needs to be very close to the expected value.
- `assertArrayEquals(String[], String[])` – I used this to check that `gradeAll` returns the right grades in the right order.
- `@Test(expected = ...)` – I used this to check that the system throws an error when the mark is invalid or the array is empty.

---

# 7. Answers to Written Questions

**Why use `assertArrayEquals()` instead of `assertEquals()` for arrays?**

When you use `assertEquals` on two arrays, Java checks if they are the same object in memory, not if they have the same values. So even if both arrays have `{"A", "F", "C"}`, `assertEquals` will say they are not equal because they are two different arrays. `assertArrayEquals` checks each value one by one, which is what we actually want.

**Which boundary tests failed after changing `>=` to `>`, and why?**

Only one test failed: `grade_shouldReturnA_whenMarkIsNinety`. When I changed `mark >= 90` to `mark > 90`, mark 90 no longer went into the A block. Instead it went into the B block and returned `"B"` instead of `"A"`. The other tests for marks 91 to 100 still passed because those marks are still greater than 90. The tests for B (80 to 89) were not affected at all. This shows why we need a test for exactly 90 — without it, this bug would not be caught.

**Why must `@BeforeClass` and `@AfterClass` be static in JUnit 4?**

JUnit creates a new object of the test class for each test method. `@BeforeClass` runs before any object is created, so there is no object to call the method on. That is why it must be `static` — so JUnit can call it directly on the class without needing an object.

**When would you use `@Before` instead of `@BeforeClass`?**

I use `@Before` when I want each test to have its own fresh object. For example, I create a new `GradingService` before each test so that one test does not affect another. I would use `@BeforeClass` for something that only needs to be set up once, like connecting to a database.

**Does `@After` still run if a test fails?**

Yes. Even if a test fails, `@After` still runs. JUnit always runs it after every test no matter what happened. This is useful for cleaning up, like setting `service` to null.

---

# 8. Intentional Bug Experiment

To check that my tests can actually catch bugs, I changed `mark >= 90` to `mark > 90` in `GradingService.java` and ran `mvn test`.

**Result:**

```
Tests run: 15, Failures: 1, Errors: 0, Skipped: 0

FAILED: grade_shouldReturnA_whenMarkIsNinety
expected:<A> but was:<B>
```

![Failing test after introducing bug](Testing_images/failing_mvn_test_when_marks_was_greater_than_to_90.png)

The test for mark 90 failed straight away. After I changed `>` back to `>=`, all 15 tests passed again. This experiment is called mutation testing. It means you break the code on purpose to see if your tests notice. If no test fails, the tests are not good enough.

---

# 9. Testing Results

I ran all tests using:

```
mvn test
```

```
[INFO] Running GradingServiceTest
Starting GradingService tests
GradingService tests finished
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.132 s
[INFO] BUILD SUCCESS
```

![All 15 tests passing](Testing_images/build_mvn_test_when_marks_was_equla_to_90.png)

All 15 tests passed.

---

# 10. Conclusion

I built the Exam Grading System and tested all the grading rules. I wrote tests for every boundary value and every group of marks. I used all five JUnit annotations and the right assertions for each situation. I also broke the code on purpose to prove that my tests can catch real bugs. Everything passed in the end.

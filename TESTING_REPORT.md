# Exam Grading System - Testing Report

## JUnit annotations used

- `@BeforeClass` runs once before all tests. I used it to print the start of the test suite.
- `@Before` runs before every test. It creates a new `GradingService` so each test starts independently.
- `@Test` identifies a test method.
- `@After` runs after every test. It clears the service reference after each test.
- `@AfterClass` runs once after all tests. I used it to print the end of the test suite.

## Equivalence partitions

| Input marks | Expected result |
|---|---|
| 0 to 59 | F |
| 60 to 69 | D |
| 70 to 79 | C |
| 80 to 89 | B |
| 90 to 100 | A |
| Less than 0 | `IllegalArgumentException` |
| Greater than 100 | `IllegalArgumentException` |

## Boundary values

| Mark | Expected result |
|---:|---|
| -1 | `IllegalArgumentException` |
| 0 | F |
| 59 | F |
| 60 | D |
| 69 | D |
| 70 | C |
| 79 | C |
| 80 | B |
| 89 | B |
| 90 | A |
| 100 | A |
| 101 | `IllegalArgumentException` |

## Written answers

`assertArrayEquals` compares the values at each position in two arrays. Plain `assertEquals` compares the array objects, so two different arrays with the same contents would fail. It is the wrong assertion for `gradeAll`.

`classAverage` uses `assertEquals(expected, actual, delta)` because a double can have a small precision difference when it represents a decimal value. The delta `0.001` is much smaller than one mark, so it accepts only harmless rounding differences.

For the mutation experiment, change one condition such as `mark >= 90` to `mark > 90`. The test for mark `90` fails because it incorrectly becomes grade B. After observing the failure, restore `>=` and run the tests again.

## Mutation experiment record

Changed `mark >= 90` to `mark > 90` in `GradingService.grade()`. Running `mvn test` produced:

```
Tests run: 16, Failures: 1, Errors: 0, Skipped: 0

FAILED: grade_shouldReturnA_whenMarkIsNinety
expected:<A> but was:<B>
```

The boundary test for mark `90` caught the bug immediately. No other test failed because only the exact boundary value `90` is affected by `>` vs `>=`. After restoring `>=`, all 16 tests pass. This is mutation testing: deliberately injecting a fault to verify that the test suite detects it.

## Test command and result

```text
[INFO] Running service.GradingServiceTest
Starting GradingService tests
GradingService tests finished
[INFO] Tests run: 16, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.103 s
[INFO] BUILD SUCCESS
```

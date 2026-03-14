package com.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParameterizedTests {

    // ==================== ТЕСТЫ ДЛЯ FELINE ====================

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 3, 5, 10})
    void testFelineGetKittensWithDifferentValues(int kittensCount) {
        Feline feline = new Feline();
        assertEquals(kittensCount, feline.getKittens(kittensCount));
    }

    @ParameterizedTest
    @MethodSource("provideFelineGetKittensScenarios")
    void testFelineGetKittensWithSpy(int expectedKittens, int getKittensParam) {
        Feline felineSpy = spy(new Feline());

        doReturn(expectedKittens).when(felineSpy).getKittens(anyInt());

        int result = felineSpy.getKittens();

        verify(felineSpy).getKittens(getKittensParam);
        assertEquals(expectedKittens, result);
    }

    private static Stream<Arguments> provideFelineGetKittensScenarios() {
        return Stream.of(
                Arguments.of(5, 1),
                Arguments.of(0, 1),
                Arguments.of(10, 1),
                Arguments.of(3, 1)
        );
    }

    // ==================== ТЕСТЫ ДЛЯ CAT ====================

    @ParameterizedTest
    @MethodSource("provideCatFoodScenarios")
    void testCatGetFoodWithDifferentFoodLists(List<String> expectedFood) throws Exception {
        Feline felineMock = mock(Feline.class);
        Cat cat = new Cat(felineMock);

        when(felineMock.eatMeat()).thenReturn(expectedFood);

        List<String> actualFood = cat.getFood();

        assertEquals(expectedFood, actualFood);
        assertEquals(expectedFood.size(), actualFood.size());
        verify(felineMock).eatMeat();
    }

    private static Stream<Arguments> provideCatFoodScenarios() {
        return Stream.of(
                Arguments.of(List.of("Мясо", "Рыба")),
                Arguments.of(List.of("Птицы", "Мыши", "Кролики")),
                Arguments.of(List.of("Только мясо")),
                Arguments.of(List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("provideCatSoundTestData")
    void testCatSound(Cat cat, String expectedSound) {
        assertEquals(expectedSound, cat.getSound());
    }

    private static Stream<Arguments> provideCatSoundTestData() {
        return Stream.of(
                Arguments.of(new Cat(new Feline()), "Мяу"),
                Arguments.of(new Cat(mock(Feline.class)), "Мяу")
        );
    }

    // ==================== ТЕСТЫ ДЛЯ LION ====================

    @ParameterizedTest
    @CsvSource({
            "Самец, true",
            "Самка, false"
    })
    void testLionDoesHaveManeWithDifferentSexValues(String sex, boolean expectedHasMane) throws Exception {
        Predator localMock = mock(Predator.class);
        Lion lion = new Lion(sex, localMock);

        assertEquals(expectedHasMane, lion.doesHaveMane());
        verifyNoInteractions(localMock);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Самец", "Самка"})
    void testLionConstructorWithValidSex(String sex) {
        Predator localMock = mock(Predator.class);
        assertDoesNotThrow(() -> {
            new Lion(sex, localMock);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"самец", "самка", "Самей", "самей", "Неизвестно", "Мужчина", "Женщина", "", "САМЕЦ", "САМКА"})
    void testLionConstructorWithInvalidSex(String invalidSex) {
        Predator localMock = mock(Predator.class);
        Exception exception = assertThrows(Exception.class, () -> new Lion(invalidSex, localMock));

        String expectedMessage = "Используйте допустимые значения пола животного - самец или самка";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideLionGetKittensScenarios")
    void testLionGetKittensWithDifferentValues(int expectedKittens) throws Exception {
        Predator localMock = mock(Predator.class);
        Lion lion = new Lion("Самец", localMock);

        when(localMock.getKittens()).thenReturn(expectedKittens);

        int result = lion.getKittens();

        assertEquals(expectedKittens, result);
        verify(localMock, times(1)).getKittens();
    }

    private static Stream<Arguments> provideLionGetKittensScenarios() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(3),
                Arguments.of(5),
                Arguments.of(10)
        );
    }

    @ParameterizedTest
    @MethodSource("provideLionFoodScenarios")
    void testLionGetFoodWithDifferentLists(List<String> expectedFood) throws Exception {
        Predator localMock = mock(Predator.class);
        Lion lion = new Lion("Самец", localMock);

        when(localMock.eatMeat()).thenReturn(expectedFood);

        List<String> actualFood = lion.getFood();

        assertEquals(expectedFood, actualFood);
        assertEquals(expectedFood.size(), actualFood.size());
        verify(localMock).eatMeat();
    }

    private static Stream<Arguments> provideLionFoodScenarios() {
        return Stream.of(
                Arguments.of(List.of("Зебры", "Антилопы")),
                Arguments.of(List.of("Буйволы", "Газели", "Бородавочники")),
                Arguments.of(List.of("Мясо")),
                Arguments.of(List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("provideSexCaseSensitivityScenarios")
    void testLionSexCaseSensitivity(String sex, boolean isValid) {
        Predator localMock = mock(Predator.class);

        if (isValid) {
            assertDoesNotThrow(() -> new Lion(sex, localMock));
        } else {
            Exception exception = assertThrows(Exception.class, () -> new Lion(sex, localMock));
            assertEquals("Используйте допустимые значения пола животного - самец или самка",
                    exception.getMessage());
        }
    }

    private static Stream<Arguments> provideSexCaseSensitivityScenarios() {
        return Stream.of(
                Arguments.of("Самец", true),
                Arguments.of("самец", false),
                Arguments.of("САМЕЦ", false),
                Arguments.of("Самка", true),
                Arguments.of("самка", false),
                Arguments.of("САМКА", false)
        );
    }
}
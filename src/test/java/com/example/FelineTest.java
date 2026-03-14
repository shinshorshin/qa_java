package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FelineTest {

    @Spy
    private Feline felineSpy = new Feline();

    // 1. Тестирование метода eatMeat()
    @Test
    void testEatMeat() throws Exception {
        Feline feline = new Feline();

        List<String> food = feline.eatMeat();
        assertNotNull(food);
        assertFalse(food.isEmpty());
    }

    // 2. Тестирование метода getFamily()
    @Test
    void testGetFamily() {
        Feline feline = new Feline();
        assertEquals("Кошачьи", feline.getFamily());
    }

    // 3. Тестирование метода getKittens() без параметров
    @Test
    void testGetKittensWithoutParams() {
        Feline feline = new Feline();
        assertEquals(1, feline.getKittens());
    }
    // 4. Тестирование метода getKittens() с параметрами
    @Test
    void testGetKittensWithParam() {
        Feline feline = new Feline();

        assertEquals(3, feline.getKittens(3));
        assertEquals(5, feline.getKittens(5));
        assertEquals(0, feline.getKittens(0));
    }

    // 5. Тестирование обработки исключений
    @Test
    void testEatMeatException() throws Exception {
        Feline felineSpy = spy(new Feline());

        // Настраиваем spy, чтобы он выбрасывал исключение при вызове getFood
        doThrow(new Exception("Ошибка получения еды")).when(felineSpy).getFood(anyString());

        // Проверяем, что исключение пробрасывается
        Exception exception = assertThrows(Exception.class, () -> felineSpy.eatMeat());
        assertEquals("Ошибка получения еды", exception.getMessage());

        // Проверяем, что метод getFood был вызван
        verify(felineSpy).getFood("Хищник");
    }

    // 6. Тест с использованием мока Animal
    @Test
    void testEatMeatWithAnimalMock() throws Exception {
        Feline feline = new Feline();
        Feline felineSpy = spy(feline);
        List<String> expectedFood = List.of("Мясо", "Рыба");

        doReturn(expectedFood).when(felineSpy).getFood("Хищник");

        List<String> actualFood = felineSpy.eatMeat();

        assertEquals(expectedFood, actualFood);
        verify(felineSpy).getFood("Хищник");
        verify(felineSpy).eatMeat();
    }

    // 7. Дополнительный тест для проверки реального поведения getKittens
    @Test
    void testGetKittensRealBehavior() {
        Feline feline = new Feline();
        assertEquals(1, feline.getKittens());
    }
}
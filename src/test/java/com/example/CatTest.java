package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatTest {

    @Mock
    private Feline felineMock;  // Мок для Feline, который имплементирует Predator

    @InjectMocks
    private Cat cat;  // Создаст Cat с felineMock через конструктор

    // 1. Тест метода getSound()
    @Test
    void testGetSound() {
        Cat cat = new Cat(new Feline());  // создаем с реальным объектом
        assertEquals("Мяу", cat.getSound());
    }

    // 2. Тест метода getSound() с проверкой, что мок не используется
    @Test
    void testGetSoundWithMockNotUsed() {
        String sound = cat.getSound();

        assertEquals("Мяу", sound);

        // Проверяем, что мок не вызывался
        verifyNoInteractions(felineMock);
    }

    // 3. Тест getFood() с обработкой исключения
    @Test
    void testGetFoodThrowsException() throws Exception {
        // Настраиваем мок на выброс исключения
        when(felineMock.eatMeat()).thenThrow(new Exception("Ошибка получения еды"));

        // Проверяем, что исключение пробрасывается
        Exception exception = assertThrows(Exception.class, cat::getFood);

        assertEquals("Ошибка получения еды", exception.getMessage());
        verify(felineMock).eatMeat();
    }

    // 4. Тест с использованием verify для проверки порядка вызовов
    @Test
    void testGetFoodVerifyOrder() throws Exception {
        List<String> food = List.of("Еда");
        when(felineMock.eatMeat()).thenReturn(food);

        cat.getFood();

        // Проверяем, что метод был вызван
        verify(felineMock, atLeastOnce()).eatMeat();
        verify(felineMock, atMost(1)).eatMeat();
        verify(felineMock, never()).getFamily(); // проверяем, что getFamily не вызывался
    }

    // 5. Тест с созданием мока внутри метода (без @Mock)
    @Test
    void testWithLocalMock() throws Exception {
        // Создаем мок локально
        Feline localFelineMock = mock(Feline.class);
        Cat localCat = new Cat(localFelineMock);

        List<String> expectedFood = List.of("Тестовая еда");
        when(localFelineMock.eatMeat()).thenReturn(expectedFood);

        List<String> result = localCat.getFood();

        assertEquals(expectedFood, result);
        verify(localFelineMock).eatMeat();
    }

    // 6. Тест для проверки, что predator это именно Feline
    @Test
    void testPredatorIsFeline() {
        assertInstanceOf(Feline.class, cat.predator);
        assertNotNull(cat.predator);
    }

    // 7. Тест с несколькими вызовами
    @Test
    void testMultipleGetFoodCalls() throws Exception {
        List<String> food = List.of("Еда");
        when(felineMock.eatMeat()).thenReturn(food);

        // Вызываем метод 3 раза
        cat.getFood();
        cat.getFood();
        cat.getFood();

        // Проверяем, что метод был вызван 3 раза
        verify(felineMock, times(3)).eatMeat();
    }
}
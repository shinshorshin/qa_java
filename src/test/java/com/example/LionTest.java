package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LionTest {

    @Mock
    private Predator predatorMock;  // Мок для интерфейса Predator

    // Тесты для самца
    @Test
    void testLionMaleConstructor() throws Exception {
        Lion lion = new Lion("Самец", predatorMock);

        assertTrue(lion.doesHaveMane());
        assertNotNull(lion);
    }

    // Тесты для самки
    @Test
    void testLionFemaleConstructor() throws Exception {
        Lion lion = new Lion("Самка", predatorMock);

        assertFalse(lion.doesHaveMane());
        assertNotNull(lion);
    }

    // Тест на исключение при неверном поле
    @Test
    void testLionConstructorWithInvalidSex() {
        Exception exception = assertThrows(Exception.class, () -> new Lion("Неизвестно", predatorMock));

        assertEquals("Используйте допустимые значения пола животного - самец или самка",
                exception.getMessage());
    }

    // Тест метода getKittens()
    @Test
    void testGetKittens() throws Exception {
        Lion lion = new Lion("Самец", predatorMock);

        when(predatorMock.getKittens()).thenReturn(3);

        int kittens = lion.getKittens();

        assertEquals(3, kittens);
        verify(predatorMock, times(1)).getKittens();
    }

    // Тест метода doesHaveMane() для самца
    @Test
    void testDoesHaveManeForMale() throws Exception {
        Lion lion = new Lion("Самец", predatorMock);

        assertTrue(lion.doesHaveMane());
        verifyNoInteractions(predatorMock);
    }

    // Тест метода doesHaveMane() для самки
    @Test
    void testDoesHaveManeForFemale() throws Exception {
        Lion lion = new Lion("Самка", predatorMock);

        assertFalse(lion.doesHaveMane());
        verifyNoInteractions(predatorMock);
    }

    // Тест getFood() с исключением
    @Test
    void testGetFoodThrowsException() throws Exception {
        Lion lion = new Lion("Самец", predatorMock);

        when(predatorMock.eatMeat()).thenThrow(new Exception("Ошибка получения еды"));

        Exception exception = assertThrows(Exception.class, lion::getFood);

        assertEquals("Ошибка получения еды", exception.getMessage());
        verify(predatorMock).eatMeat();
    }

    // Тест с использованием реального Feline (не мок)
    @Test
    void testWithRealFeline() throws Exception {
        Feline realFeline = new Feline();
        Lion lion = new Lion("Самец", realFeline);

        assertTrue(lion.doesHaveMane());

        List<String> food = lion.getFood();
        assertNotNull(food);
        assertFalse(food.isEmpty());

        int kittens = lion.getKittens();
        assertEquals(1, kittens);
    }

    // Комплексный тест с проверкой всех методов
    @Test
    void testAllLionMethods() throws Exception {
        Lion lion = new Lion("Самец", predatorMock);

        List<String> expectedFood = List.of("Мясо");
        when(predatorMock.eatMeat()).thenReturn(expectedFood);
        when(predatorMock.getKittens()).thenReturn(2);

        assertTrue(lion.doesHaveMane());
        assertEquals(expectedFood, lion.getFood());
        assertEquals(2, lion.getKittens());

        verify(predatorMock).eatMeat();
        verify(predatorMock).getKittens();
    }

}
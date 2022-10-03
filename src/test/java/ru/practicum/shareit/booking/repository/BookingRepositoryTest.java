package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookingRepositoryTest {
    //todo: Реализовать тесты для слоя репозиториев вашего приложения с использованием аннотации @DataJpaTest.
    // Есть смысл написать тесты для тех репозиториев, которые содержат кастомные запросы.
    // Работа с аннотацией @DataJpaTest не рассматривалась подробно в уроке,
    // поэтому вам предстоит изучить пример самостоятельно, перейдя по ссылке.
    // Ещё больше деталей вы сможете найти в приложенном файле с советами ментора.

    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void setBookingRepository(){

    }

    @Test
    void findAllByBooker_IdAndStatusInOrderByEndDesc() {
    }

    @Test
    void testFindAllByBooker_IdAndStatusInOrderByEndDesc() {
    }

    @Test
    void findAllByBooker_IdOrderByEndDesc() {
    }

    @Test
    void findAllByItem_IdInOrderByEndDesc() {
    }

    @Test
    void findAllByItem_IdInAndStatusInOrderByEndDesc() {
    }

    @Test
    void testFindAllByItem_IdInAndStatusInOrderByEndDesc() {
    }
}
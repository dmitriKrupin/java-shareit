package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookingRepositoryTest {

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
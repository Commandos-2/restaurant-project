package ru.restaurants.util.exсeption;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException implements Supplier<NotFoundException> {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public NotFoundException get() {
        return new NotFoundException("Not found entity with");
    }
}
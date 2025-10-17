package pl.rezerveo.notification.event;

public record BookingEvent(String targetEmail, String title, String message) {}
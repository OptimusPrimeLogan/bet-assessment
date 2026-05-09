package com.sporty.settlement.exception;

import java.time.Instant;
import java.util.List;

public record ErrorMessage(int statusCode, Instant timestamp, List<String> message, String description) {}

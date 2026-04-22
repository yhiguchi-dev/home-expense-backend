package dev.yhiguchi.home_expense.presentation.validation.payload;

import jakarta.validation.Payload;

/** Bean Validation違反をHTTP 428 Precondition Requiredとして扱うためのマーカー */
public final class PreconditionRequired implements Payload {}

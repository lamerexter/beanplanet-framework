package org.beanplanet.core.lang.conversion;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Conversions declare what they do to a value: widening (without loss), narrowing, or unspecified. */
class ConversionKindTest {
    private final SystemTypeConverter converters = (SystemTypeConverter) SystemTypeConverter.getInstance();

    @Test
    void numbers_asJavaWidensAndNarrows_withoutLoss() {
        assertEquals(Optional.of(ConversionKind.WIDENING), converters.kindOf(int.class, Long.class));
        assertEquals(Optional.of(ConversionKind.WIDENING), converters.kindOf(Integer.class, Long.class));
        assertEquals(Optional.of(ConversionKind.WIDENING), converters.kindOf(int.class, Integer.class), "boxing");
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(Integer.class, int.class), "unboxing fails on null");
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(Long.class, Integer.class));
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(int.class, Float.class), "loses precision");
        assertEquals(Optional.of(ConversionKind.WIDENING), converters.kindOf(int.class, BigDecimal.class));
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(BigDecimal.class, Long.class));
    }

    @Test
    void text_andTimes() {
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(String.class, Integer.class), "parsing may fail");
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(String.class, Instant.class));
        assertEquals(Optional.of(ConversionKind.WIDENING), converters.kindOf(ZonedDateTime.class, Instant.class));
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(LocalDateTime.class, Instant.class), "assumes a time zone");
        assertEquals(Optional.of(ConversionKind.NARROWING), converters.kindOf(Instant.class, java.util.Date.class), "loses nanoseconds");
    }

    @Test
    void noConverter_noKind() {
        assertEquals(Optional.empty(), converters.kindOf(Thread.class, Instant.class));
    }

    @Test
    void convert_narrowingOnlyWhenAllowed() {
        assertEquals(5L, converters.convert(5, Long.class, false));
        assertEquals(5, converters.convert(5L, Integer.class, true));
        assertThrows(UnsupportedTypeConversionException.class, () -> converters.convert(5L, Integer.class, false));
        assertThrows(UnsupportedTypeConversionException.class, () -> converters.convert("42", Integer.class, false));
        assertEquals(42, converters.convert("42", Integer.class, true));
        assertEquals("same", converters.convert("same", String.class, false), "a value of the type is never converted");
        assertEquals(Instant.EPOCH, converters.convert(ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC), Instant.class, false));
    }

    @Test
    void anUnspecifiedConverter_isNeverTreatedAsNarrowing() {
        final AbstractTypeConverterRegistry registry = new AbstractTypeConverterRegistry() { };
        final TypeConverter legacy = new TypeConverter() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> T convert(final Object value, final Class<T> targetType) {
                return (T) Integer.valueOf(((String) value).length());
            }
        };
        registry.addConverter(String.class, Integer.class, legacy);
        assertEquals(Optional.of(ConversionKind.UNSPECIFIED), registry.kindOf(String.class, Integer.class));
        assertEquals(3, registry.lookup(String.class, Integer.class).get().convert("abc", Integer.class, false));
    }
}

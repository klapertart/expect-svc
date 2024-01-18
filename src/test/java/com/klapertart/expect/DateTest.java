package com.klapertart.expect;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author tritr
 * @since 7/25/2023
 */

@Slf4j
public class DateTest {

    @Test
    void testZoneDateTime(){
        String date = "2023-07-25T09:50:12.324+07:00";

        ZonedDateTime parse = ZonedDateTime.parse(date);
        log.info("Parsing Date: {}", parse);

        log.info("Local Date Time: {}", parse.toLocalDateTime());
        log.info("Local Date : {}", parse.toLocalDate());
        log.info("Local Time : {}", parse.toLocalTime());

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String format = parse.toLocalDate().format(dateTimeFormat);
        log.info("Format Date : {}", format);
    }
}

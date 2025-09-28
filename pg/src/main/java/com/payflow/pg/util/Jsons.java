package com.payflow.pg.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Jsons {
    private static final ObjectMapper om = new ObjectMapper();
    public static String toJson(Object o) {
        try { return om.writeValueAsString(o); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
}

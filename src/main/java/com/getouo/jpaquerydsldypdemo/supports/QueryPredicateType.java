package com.getouo.jpaquerydsldypdemo.supports;

import java.util.Arrays;
import java.util.Optional;

public enum QueryPredicateType {
        DEFAULT_EQ(""),

        LIKE("(LIKE)"),
        NULL("(NULL)"),
        BETWEEN("(BETWEEN)"),
        BIT_HAS("(BIT.HAS)"),
        BIT_NOTHAS("(BIT.NOTHAS)"),
        ;
        public final String type;

        QueryPredicateType(String type) {
            this.type = type;
        }

        public static Optional<QueryPredicateType> byTypeFirst(String type) {
            return Arrays.asList(values()).stream().filter(t -> t.type.equals(type)).findFirst();
        }
    }
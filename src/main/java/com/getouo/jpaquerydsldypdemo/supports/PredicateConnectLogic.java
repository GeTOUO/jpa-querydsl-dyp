package com.getouo.jpaquerydsldypdemo.supports;

import java.util.Arrays;
import java.util.Optional;

public enum PredicateConnectLogic {
        DEFAULT_AND("[AND]"),

        OR("[OR]"),
        ;
        public final String type;

        PredicateConnectLogic(String type) {
            this.type = type;
        }

        public static Optional<PredicateConnectLogic> byLogicFirst(String type) {
            return Arrays.asList(values()).stream().filter(t -> t.type.equals(type)).findFirst();
        }
    }
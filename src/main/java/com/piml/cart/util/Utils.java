package com.piml.cart.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Utils {

    public static String makeURIWithIds(String apiUri, String resource, List<Long> ids){
        String idsString = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        return apiUri.concat(resource).concat(idsString);
    }
}

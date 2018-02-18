package courage.library.authserver.controller;


import courage.library.authserver.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;

public class PageValidator {

    public static Map<String, Integer> validatePageAndSize(Integer page, Integer size) {
        if( page == null || size == null ) {
            page = 1;
            size = 20;
        } else if ( page <= 0 || size <= 0 ) {
            throw BadRequestException.create("Bad Request: Invalid page number: {0} or page size: {1} value", page, size);
        }
        Map<String, Integer> pageAttributes = new HashMap<>();
        pageAttributes.put("page", page);
        pageAttributes.put("size", size);
        return pageAttributes;
    }

}

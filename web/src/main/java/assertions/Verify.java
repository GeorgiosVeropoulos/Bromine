package assertions;

import exceptions.ElementNotFoundException;

public class Verify {




    public static <T> T nonNull(T obj, String message) {
        if (obj == null)
            throw new ElementNotFoundException(message);
        return obj;
    }

}

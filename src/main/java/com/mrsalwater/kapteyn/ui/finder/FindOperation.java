package com.mrsalwater.kapteyn.ui.finder;

public enum FindOperation {

    ALL("All"),
    LDC("LDC"),
    LOCAL_VARIABLE("Local Variable"),
    METHOD_REFERENCE("Method Reference"),
    FIELD_REFERENCE("Field Reference");

    private final String name;

    FindOperation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FindOperation match(String name) {
        for (FindOperation findOperation : values()) {
            if (findOperation.getName().equals(name)) {
                return findOperation;
            }
        }

        throw new NullPointerException();
    }

}

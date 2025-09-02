package models;

public enum Position {
    MANAGER,
    ENGINEER,
    MANAGER_OF_CLEANING;

    public static String list() {
        StringBuilder sb = new StringBuilder();
        for (Position position : Position.values()) {
            sb.append(position).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}

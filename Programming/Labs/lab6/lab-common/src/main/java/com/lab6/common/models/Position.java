package com.lab6.common.models;

/**
 * Перечисление, представляющее музыкальные жанры.
 */
public enum Position {
    MANAGER,
    ENGINEER,
    MANAGER_OF_CLEANING;

    /**
     * Возвращает строку, содержащую все позиции.
     * @return строка с перечислением всех позиций
     */
    public static String list() {
        StringBuilder list = new StringBuilder();
        for (Position genre : Position.values()) {
            list.append(genre).append(", ");
        }
        return list.substring(0, list.length() - 2);
    }
}

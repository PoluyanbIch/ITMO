package com.lab6.client.utility;

import com.lab6.client.managers.Asker;
import com.lab6.common.models.Worker;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Pair;

/**
 * Валидатор для проверки корректности элемента коллекции.
 */
public class ElementValidator {
    /**
     * Проверяет корректность введенного элемента коллекции.
     *
     * @param console Консоль для ввода/вывода.
     * @param id Идентификатор элемента коллекции.
     * @return Пара, содержащая статус выполнения проверки и элемент коллекции.
     */
    public Pair<ExecutionStatus, Worker> validateAsking(Console console, Long id) {
        try {
            Worker band = Asker.askBand(console, id);
            return validating(band);
        } catch (Asker.Breaker e) {
            return new Pair<>(new ExecutionStatus(false, "Ввод был прерван пользователем!"), null);
        } catch (Asker.IllegalInputException e) {
            return new Pair<>(new ExecutionStatus(false, e.getMessage()), null);
        }
    }

    public Pair<ExecutionStatus, Worker> validating(Worker band) {
        if (band != null && band.validate()) {
            return new Pair<>(new ExecutionStatus(true, "Элемент введён корректно!"), band);
        }
        return new Pair<>(new ExecutionStatus(false, "Введены некорректные данные!"), null);
    }
}

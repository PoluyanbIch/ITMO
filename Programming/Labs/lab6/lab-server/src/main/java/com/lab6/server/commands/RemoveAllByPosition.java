package com.lab6.server.commands;

import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.GenreValidator;
import com.lab6.common.models.Position;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс команды для удаления всех элементов из коллекции по заданному жанру.
 */
public class RemoveAllByPosition extends Command<GenreValidator> {

    /**
     * Конструктор команды removeAllByPosition.
     */
    public RemoveAllByPosition() {
        super(CommandNames.REMOVE_ALL_BY_POSITION.getName() + " position", CommandNames.REMOVE_ALL_BY_POSITION.getDescription(), new GenreValidator());
    }

    /**
     * Выполняет команду удаления всех элементов коллекции по заданному жанру.
     * @param argument Аргумент команды, содержащий жанр.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus runInternal(String argument) {
        Position position = Position.valueOf(argument);
        int count = collectionManager.removeAllByPosition(position);
        return new ExecutionStatus(true, "Удалено " + count + " элементов!");
    }
}

package com.lab6.server.commands.askingCommands;

import com.lab6.server.utility.AskingCommand;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.models.Worker;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для добавления нового элемента в коллекцию.
 */
public class Add extends AskingCommand<EmptyValidator> {
    /**
     * Конструктор команды add.
     */
    public Add() {
        super(CommandNames.ADD.getName() + " {element}", CommandNames.ADD.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет внутреннюю часть команды добавления.
     * @param band  которую нужно добавить.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(Worker band) {
        CollectionManager.getInstance().add(band);
        return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
    }
}

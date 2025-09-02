package commands;

import models.Ask;
import utility.Console;
import managers.UDPManager; // Изменено с TCPManager на UDPManager

import java.util.HashMap;

import models.Worker;

/**
 * Базовая реализация команды
 * @author dim0n4eg
 */
public class DefaultCommand extends Command {
    private interface ArgExecuter {
        boolean execute(String s, Console cons, UDPManager udpm); // Изменен тип параметра
    }

    private interface AskExecuter {
        Object execute(Console cons);
    }

    private final Console console;
    private final UDPManager udpManager; // Изменено с TCPManager на UDPManager
    private final String args;
    private static final HashMap<String, ArgExecuter> argMap = new HashMap<>();
    private static final HashMap<String, AskExecuter> askMap = new HashMap<>();

    static {
        argMap.put("index", DefaultCommand::argIndex);
        argMap.put("N", DefaultCommand::argN);
        argMap.put("ID", DefaultCommand::argID);

        askMap.put("{element}", DefaultCommand::askWorker);
        askMap.put("{character}", DefaultCommand::askPosition);
    }

    public DefaultCommand(String[] command, Console console, UDPManager udpManager) { // Изменен тип параметра
        super(command[1], command[3]);
        this.args = command[2];
        this.console = console;
        this.udpManager = udpManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды
     */
    @Override
    public boolean apply(String[] arguments) {
        boolean argCommandNotUsed = !arguments[1].isEmpty();
        Object sendObj = null;

        if (!args.isEmpty()) {
            for (var arg : args.split(",")) {
                if (argMap.get(arg) != null) {
                    if (!argMap.get(arg).execute(arguments[1].trim(), console, udpManager)) {
                        return false;
                    }
                    argCommandNotUsed = false;
                } else if (askMap.get(arg) != null) {
                    if (argCommandNotUsed) break;
                    sendObj = askMap.get(arg).execute(console);
                } else {
                    console.printErr("Неизвестный тип аргумента: " + arg);
                }
            }
        }

        if (argCommandNotUsed) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        var res = udpManager.send(arguments[0] + " " + arguments[1], sendObj);
        console.println(res.getMessage()); // Исправлена опечатка (massage -> message)
        return res.getExitCode() < 300;
    }

    private static boolean argIndex(String s, Console cons, UDPManager udpm) {
        try {
            int index = Integer.parseInt(s);
            if (index < 0) {
                cons.println("index < 0");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            cons.println("index не распознан");
            return false;
        }
    }

    private static boolean argN(String s, Console cons, UDPManager udpm) {
        try {
            int N = Integer.parseInt(s);
            if (N < 1) {
                cons.println("N < 1");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            cons.println("N не распознан");
            return false;
        }
    }

    private static boolean argID(String s, Console cons, UDPManager udpm) {
        try {
            long id = Long.parseLong(s);
            if (!udpm.sendAndGetMessage("is_id_exist " + s).equals("EXIST")) {
                cons.println("не существует дракона с таким ID");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            cons.println("ID не распознан");
            return false;
        }
    }

    public static Object askWorker(Console cons) {
        try {
            cons.println("* Создание работника:");
            Worker d = Ask.askWorker(cons);

            if (d != null && d.validate()) {
                return d;
            } else {
                cons.println("Поля работника не валидны! Дракон не создан!");
                return null;
            }
        } catch (Ask.AskBreakException e) {
            cons.println("Отмена...");
            return null;
        }
    }

    public static Object askPosition(Console cons) {
        try {
            return Ask.askPosition(cons);
        } catch (Ask.AskBreakException e) {
            cons.println("Отмена...");
            return null;
        }
    }
}
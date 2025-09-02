package managers;

import commands.Command;

import java.util.LinkedHashMap;
import java.util.ArrayList;

public class CommandManager {
    private final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    private final ArrayList<String> commandHistory = new ArrayList<>();

    public void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    public LinkedHashMap<String, Command> getCommands() {
        return commands;
    }

    public ArrayList<String> getCommandHistory() {
        return commandHistory;
    }

    public void addToHistory(String command) {
        commandHistory.add(command);
    }

    public void clear() {
        for (var e:commands.keySet().toArray(new String[0]))
            if (e.charAt(0) == '$')
                commands.remove(e);
    }
}

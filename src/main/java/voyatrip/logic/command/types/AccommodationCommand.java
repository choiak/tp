package voyatrip.logic.command.types;

import java.util.ArrayList;
import java.util.Arrays;

import voyatrip.logic.command.exceptions.ExtraArgument;
import voyatrip.logic.command.exceptions.InvalidArgumentKeyword;
import voyatrip.logic.command.exceptions.InvalidArgumentValue;
import voyatrip.logic.command.exceptions.InvalidBudget;
import voyatrip.logic.command.exceptions.InvalidDay;
import voyatrip.logic.command.exceptions.InvalidName;
import voyatrip.logic.command.exceptions.InvalidNumberFormat;
import voyatrip.logic.command.exceptions.InvalidTimeFormat;
import voyatrip.logic.command.exceptions.MissingArgument;

public class AccommodationCommand extends Command {
    static final String[] INVALID_NAMES = {"all"};

    private String trip;
    private String name;
    private Integer budget;
    private Integer index;
    private Integer startDay;
    private Integer endDay;
    private ArrayList<Integer> days;

    public AccommodationCommand(CommandAction commandAction,
                                CommandTarget commandTarget,
                                String trip,
                                ArrayList<String> arguments)
            throws InvalidArgumentKeyword,
            InvalidArgumentValue,
            InvalidNumberFormat,
            MissingArgument, InvalidTimeFormat {
        super(commandAction, commandTarget);
        this.trip = trip;
        name = null;
        budget = null;
        index = null;
        startDay = null;
        endDay = null;
        days = null;

        processRawArgument(arguments);
        if (startDay != null && endDay != null) {
            storeDaysInList();
        }
    }

    @Override
    protected void processRawArgument(ArrayList<String> arguments)
            throws InvalidArgumentKeyword,
            InvalidArgumentValue,
            InvalidNumberFormat,
            MissingArgument, InvalidTimeFormat {
        super.processRawArgument(arguments);

        processCommandVariation();
    }

    private void processCommandVariation() {
        if (commandAction == CommandAction.DELETE_BY_INDEX && name != null) {
            super.setCommandAction(CommandAction.DELETE_BY_NAME);
        } else if (commandAction == CommandAction.LIST) {
            if (name != null) {
                super.setCommandAction(CommandAction.LIST_BY_NAME);
            } else if (index != null) {
                super.setCommandAction(CommandAction.LIST_BY_INDEX);
            }
        }
    }

    @Override
    protected void matchArgument(String argument)
            throws InvalidArgumentKeyword, InvalidArgumentValue {
        String argumentKeyword = argument.split("\\s+")[0];
        String argumentValue = argument.replaceFirst(argumentKeyword, "").strip();
        argumentKeyword = argumentKeyword.toLowerCase();

        if (argumentKeyword.isEmpty()) {
            throw new InvalidArgumentKeyword();
        }
        if (!argumentKeyword.equals("all") && argumentValue.isEmpty()) {
            throw new InvalidArgumentValue();
        }

        try {
            switch (argumentKeyword) {
            case "name", "n" -> name = argumentValue;
            case "budget", "b" -> budget = Integer.parseInt(argumentValue);
            case "index", "i" -> index = Integer.parseInt(argumentValue);
            case "start", "s" -> startDay = Integer.parseInt(argumentValue);
            case "end", "e" -> endDay = Integer.parseInt(argumentValue);
            case "all" -> name = "all";
            default -> throw new InvalidArgumentKeyword();
            }
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormat();
        }
    }

    @Override
    protected void validateArgument() throws InvalidArgumentValue, MissingArgument {
        boolean isAdd = commandAction == CommandAction.ADD;
        boolean isDelete = commandAction == CommandAction.DELETE_BY_INDEX ||
                commandAction == CommandAction.DELETE_BY_NAME;
        boolean isModify = commandAction == CommandAction.MODIFY;
        boolean isList = commandAction == CommandAction.LIST;
        boolean isChangeDirectory = commandAction == CommandAction.CHANGE_DIRECTORY;

        boolean isInvalidChangeDirectoryArgument = index != null || startDay != null
                || endDay != null || budget != null || name != null;

        if (isInvalidChangeDirectoryArgument && isChangeDirectory) {
            throw new ExtraArgument();
        }

        boolean isMissingAddArgument = name == null || budget == null || startDay == null || endDay == null;
        boolean isMissingDeleteArgument = name == null && index == null;
        boolean isMissingModifyArgument = index == null ||
                (name == null && budget == null && (startDay == null || endDay == null));
        boolean isMissingListArgument = name == null && index == null;

        if (isAdd && isMissingAddArgument ||
                isDelete && isMissingDeleteArgument ||
                isModify && isMissingModifyArgument ||
                isList && isMissingListArgument) {
            throw new MissingArgument();
        }

        boolean isInvalidBudget = budget != null && budget < 0;
        boolean isInvalidName = !isList && !isDelete && name != null && Arrays.asList(INVALID_NAMES).contains(name);
        boolean hasStartEndDay = startDay != null && endDay != null;
        boolean isInvalidStartEndDay = hasStartEndDay && (startDay < 0 || endDay < 0 || startDay >= endDay);

        if (isInvalidBudget) {
            throw new InvalidBudget();
        }
        if (isInvalidName) {
            throw new InvalidName();
        }
        if (isInvalidStartEndDay) {
            throw new InvalidDay();
        }
    }

    private void storeDaysInList() {
        days = new ArrayList<>();
        for (int i = startDay; i <= endDay; i++) {
            days.add(i);
        }
    }

    public String getTrip() {
        return trip;
    }

    public String getName() {
        return name;
    }

    public Integer getBudget() {
        return budget;
    }

    public Integer getIndex() {
        return index;
    }

    public ArrayList<Integer> getDays() {
        return days;
    }
}

package seedu.task.logic.parser;

import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.FindCommand;
import seedu.task.logic.commands.IncorrectCommand;
import seedu.task.logic.commands.SortCommand;

public class SortCommandParser {
	public static final String byTime = "time";
	public static final String byName = "name";

	public Command parse(String arguments) {
		arguments = arguments.toLowerCase();
		arguments = arguments.trim();
		switch(arguments) {
		case byTime:
			return new SortCommand(arguments);
		case byName:
			return new SortCommand(arguments);
		default:
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
		}
	}

}

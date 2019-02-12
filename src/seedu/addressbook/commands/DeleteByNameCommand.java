package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;

import java.util.*;


/**
 * Deletes all people identified using with the same name from the address book.
 */
public class DeleteByNameCommand extends Command {

    public static final String COMMAND_WORD = "deletebyname";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes all persons whose names contain any of the specified keywords (case-sensitive)\n"
            + "Parameters: NAME\n"
            + "Example: " + COMMAND_WORD + " John";

    private static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person(s): \n%1$s";

    private final Set<String> keywords;

    public DeleteByNameCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns a copy of keywords in this command.
     */
    public Set<String> getKeywords() {
        return new HashSet<>(keywords);
    }

    @Override
    public CommandResult execute() {
        final List<ReadOnlyPerson> personsFound = getPersonsWithNameContainingAnyKeyword(keywords);
        Iterator<ReadOnlyPerson> it = personsFound.iterator();
        String deletedPersonsList = "";
        try{
            for(personsFound.listIterator(); it.hasNext();){
                ReadOnlyPerson target = it.next();
                addressBook.removePerson(target);
                deletedPersonsList += " " + target + "\n";
            }
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPersonsList));
        } catch (PersonNotFoundException e) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        }
    }

    /**
     * Retrieves all persons in the address book whose names contain some of the specified keywords.
     *
     * @param keywords for searching
     * @return list of persons found
     */
    private List<ReadOnlyPerson> getPersonsWithNameContainingAnyKeyword(Set<String> keywords) {
        final List<ReadOnlyPerson> matchedPersons = new ArrayList<>();
        for (ReadOnlyPerson person : addressBook.getAllPersons()) {
            final Set<String> wordsInName = new HashSet<>(person.getName().getWordsInName());
            if (!Collections.disjoint(wordsInName, keywords)) {
                matchedPersons.add(person);
            }
        }
        return matchedPersons;
    }

}
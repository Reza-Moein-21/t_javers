package org.example.letter_of_credit;

import java.util.Set;

public record IssueMasterSearchParam(Set<String> inputBranch, String reference) {
}

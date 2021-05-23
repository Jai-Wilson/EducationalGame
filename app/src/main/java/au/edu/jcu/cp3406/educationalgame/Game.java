package au.edu.jcu.cp3406.educationalgame;

public class Game {

    public String[] questions =
            {"Chemical formula for Sodium Chloride?",
                    "How many oxygen atoms in a molecule of water?",
                    "Atomic number of Titanium?",
                    "Chemical responsible 'spiciness' of a chilli?",
                    "What element has the symbol Hg?",
                    "Which atom has the highest atomic number?",
                    "Only letter that does not appear in the periodic table?",
                    "What is the lightest element?",
                    "What colour are copper crystals?",
                    "What colour does Tungsten burn?"};

    public String[] answers =
            {"NaCl",
                    "1",
                    "22",
                    "Capsaicin",
                    "Mercury",
                    "Oganesson",
                    "j",
                    "Hydrogen",
                    "Blue",
                    "Green"};

    public String getQuestion(int questionCounter) {
        return questions[questionCounter];
    }

    public String getAnswer(int questionCounter) {
        return answers[questionCounter];
    }

}
